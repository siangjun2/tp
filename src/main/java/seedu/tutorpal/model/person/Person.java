package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import seedu.tutorpal.commons.util.ToStringBuilder;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {
    public static final String MESSAGE_STUDENT_INVALID_CLASS_SIZE = "Student must have a single class only! "
            + "Currently, initialised with %1$s class(es).";
    public static final String MESSAGE_TUTOR_NO_ATTENDANCE_HISTORY = "Tutors must not have attendance history.";
    public static final String MESSAGE_OUT_OF_SYNC = "%1$s.joinDate must match Person.joinDate.";
    public static final String MESSAGE_INVALID_ATTENDANCE_RETRIEVAL = "Tutors do not have attendance history";

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final Email email;
    private final Role role;
    private final Address address;
    private final Set<Class> classes;
    private final JoinDate joinDate;
    private final AttendanceHistory attendanceHistory; // null for tutors
    private final PaymentHistory paymentHistory;

    /**
     * Public constructor (default "now").
     */
    public Person(Name name, Phone phone, Email email, Role role, Address address, Set<Class> classes) {
        this(name, phone, email, role, address, classes, JoinDate.now(),
                null, new PaymentHistory(LocalDate.now()));
    }

    /**
     * Public constructor with explicit joinDate (uses system clock for defaults).
     */
    public Person(Name name, Phone phone, Email email, Role role, Address address, Set<Class> classes,
                  JoinDate joinDate) {
        this(name, phone, email, role, address, classes, joinDate, null,
                new PaymentHistory(LocalDate.now()));
    }

    /**
     * Package-private constructor for tests: inject a Clock to control "now".
     * Does not store the clock; only used to compute defaults/AttendanceHistory.
     */
    Person(Name name, Phone phone, Email email, Role role, Address address, Set<Class> classes,
           JoinDate joinDate, AttendanceHistory attendanceHistory, Clock nowClock) {
        this(name, phone, email, role, address, classes, joinDate,
                attendanceHistory, new PaymentHistory(LocalDate.now(nowClock)), nowClock);
    }

    /**
     * Private constructor with all fields (prod path, uses system clock).
     */
    private Person(Name name, Phone phone, Email email, Role role, Address address,
                   Set<Class> classes, JoinDate joinDate, AttendanceHistory attendanceHistory,
                   PaymentHistory paymentHistory) {
        this(name, phone, email, role, address, classes, joinDate, attendanceHistory, paymentHistory,
                Clock.systemDefaultZone());
    }

    /**
     * Private constructor with all fields and an injected Clock for normalization.
     * Guarantees that all students initialised will have valid AttendanceHistory in sync with joinDate,
     * while all tutors initialised will not have AttendanceHistory.
     * @param name              name of the person.
     * @param phone             phone number of the person.
     * @param email             email of the person.
     * @param role              role of the person, either student or tutor.
     * @param address           address of the person.
     * @param classes           class the student belongs to, or the tutor is teaching.
     * @param joinDate          join date of the person.
     * @param attendanceHistory attendance history of student. null for tutors.
     * @param paymentHistory    payment history
     */
    private Person(Name name, Phone phone, Email email, Role role, Address address,
                   Set<Class> classes, JoinDate joinDate, AttendanceHistory attendanceHistory,
                   PaymentHistory paymentHistory, Clock nowClock) {
        requireAllNonNull(name, phone, email, role, address, classes, joinDate, paymentHistory, nowClock);

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.address = address;
        this.joinDate = joinDate;
        this.classes = Set.copyOf(classes);
        this.paymentHistory = paymentHistory;

        final boolean isStudent = Role.isStudent(this.role);
        // Validate first, then normalize
        validateAttendanceHistoryRules(isStudent, attendanceHistory); // Validate raw input
        this.attendanceHistory = normalizeAttendanceHistory(isStudent, attendanceHistory, nowClock);
        validateClassSize(isStudent, this.classes);
        validateJoinDateConsistency(isStudent, this.attendanceHistory, this.joinDate);
    }

    /**
     * Validates attendance history rules based on role.
     */
    private static void validateAttendanceHistoryRules(boolean isStudent, AttendanceHistory attendanceHistory) {
        // Tutors must not have attendance history
        if (!isStudent && attendanceHistory != null) {
            throw new IllegalArgumentException(MESSAGE_TUTOR_NO_ATTENDANCE_HISTORY);
        }
    }

    /**
     * Normalizes attendance history based on role and provided value.
     */
    private AttendanceHistory normalizeAttendanceHistory(
            boolean isStudent, AttendanceHistory attendanceHistory, Clock nowClock) {

        if (!isStudent) {
            assert attendanceHistory == null;
            return null; // Tutors never have attendance history
        }

        // Students: use provided history or create new one if null
        return attendanceHistory != null
                ? attendanceHistory
                : new AttendanceHistory(this.joinDate, nowClock);
    }

    /**
     * Validates class size based on role.
     */
    private static void validateClassSize(boolean isStudent, Set<Class> classes) {
        if (isStudent && classes.size() > 1) {
            throw new IllegalArgumentException(String.format(
                    Person.MESSAGE_STUDENT_INVALID_CLASS_SIZE, classes.size()));
        }
    }


    /**
     * Validates join date consistency with attendance history.
     */
    private static void validateJoinDateConsistency(boolean isStudent, AttendanceHistory normalizedAttendanceHistory,
                                                    JoinDate joinDate) {
        if (isStudent) {
            requireNonNull(normalizedAttendanceHistory);
            if (!normalizedAttendanceHistory.getJoinDate().equals(joinDate)) {
                throw new IllegalArgumentException(String.format(MESSAGE_OUT_OF_SYNC, "AttendanceHistory"));
            }
        } else {
            assert normalizedAttendanceHistory == null;
        }
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable class set, which throws
     * {@code UnsupportedOperationException} if modification is attempted.
     */
    public Set<Class> getClasses() {
        return classes;
    }

    /**
     * Returns the join date of this person.
     */
    public JoinDate getJoinDate() {
        return joinDate;
    }

    /**
     * Returns whether this person has attendance history.
     * Only students have attendance history.
     */
    public boolean hasAttendanceHistory() {
        return Role.isStudent(this.role);
    }

    /**
     * Returns the attendance history for students.
     *
     * @return the attendance history
     * @throws IllegalStateException if called on a tutor
     */
    public AttendanceHistory getAttendanceHistory() {
        if (!hasAttendanceHistory()) {
            throw new IllegalStateException(MESSAGE_INVALID_ATTENDANCE_RETRIEVAL);
        }
        return attendanceHistory;
    }

    /**
     * Returns the payment status of this person.
     */
    public Payment getPaymentStatus() {
        return new Payment(paymentHistory);
    }

    /**
     * Returns the payment history of this person.
     */
    public PaymentHistory getPaymentHistory() {
        return paymentHistory;
    }

    /**
     * Returns true if both persons have the same name and phone number.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && otherPerson.getPhone().equals(getPhone());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && role.equals(otherPerson.role)
                && address.equals(otherPerson.address)
                && classes.equals(otherPerson.classes)
                && joinDate.equals(otherPerson.joinDate)
                && Objects.equals(attendanceHistory, otherPerson.attendanceHistory)
                && paymentHistory.equals(otherPerson.paymentHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, role, address, classes,
                joinDate, attendanceHistory, paymentHistory);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("role", role)
                .add("address", address)
                .add("classes", classes)
                .add("joinDate", joinDate)
                .add("attendanceHistory", attendanceHistory)
                .add("paymentHistory", paymentHistory)
                .toString();
    }
}
