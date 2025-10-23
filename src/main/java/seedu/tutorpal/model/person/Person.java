package seedu.tutorpal.model.person;

import static seedu.tutorpal.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.tutorpal.commons.util.ToStringBuilder;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final Email email;
    private final Role role;
    private final Address address;
    private final Set<Class> classes = new HashSet<>();
    private final JoinMonth joinMonth;
    private final AttendanceHistory attendanceHistory;
    private final PaymentHistory paymentHistory;

    // TEMPORARY UNTIL PAYMENT UPDATED
    private final LocalDate joinDate;

    /**
     * Constructor for Person (for creating new persons).
     * @param name      name of the person.
     * @param phone     phone number of the person.
     * @param email     email of the person.
     * @param role      role of the person, either student or tutor.
     * @param address   address of the person.
     * @param classes   class the student belong to, or the tutor is teaching.
     * @param joinMonth the month when the person joined.
     */
    public Person(Name name, Phone phone, Email email, Role role, Address address,
            Set<Class> classes) {
        requireAllNonNull(name, phone, email, role, address, classes);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.address = address;
        this.classes.addAll(classes);
        this.joinMonth = new JoinMonth(YearMonth.now());
        this.joinDate = LocalDate.now(); // TEMPORARY TEMPORARY TODO TODO
        // Initialize new, empty AttendanceHistory for new Person
        if (Role.isStudent(role)) {
            this.attendanceHistory = new AttendanceHistory(joinMonth);
        } else {
            this.attendanceHistory = null; // Tutors do not have attendance history
        }
        this.paymentHistory = new PaymentHistory(LocalDate.now());
    }

    /**
     * Constructor with all fields (for editing existing persons).
     */
    public Person(Name name, Phone phone, Email email, Role role, Address address,
            Set<Class> classes, JoinMonth joinMonth, AttendanceHistory attendanceHistory,
            PaymentHistory paymentHistory) {
        requireAllNonNull(name, phone, email, role, address, classes, joinMonth, paymentHistory);
        // attendanceHistory can be null for tutors
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.address = address;
        this.classes.addAll(classes);
        this.joinMonth = joinMonth;
        this.joinDate = LocalDate.now(); // TEMPORARY TEMPORARY TODO TODO
        this.attendanceHistory = attendanceHistory; // May be null, for tutors
        this.paymentHistory = paymentHistory;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    } // TEMPORARY TODO

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
     * {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Class> getClasses() {
        return Collections.unmodifiableSet(classes);
    }

    /**
     * Returns the join month of this person.
     * @return
     */
    public JoinMonth getJoinMonth() {
        return joinMonth;
    }

    /**
     * Returns the attendance history of this person.
     */
    public AttendanceHistory getAttendanceHistory() {
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
        boolean attendanceEquals = (attendanceHistory == null && otherPerson.attendanceHistory == null)
                || (attendanceHistory != null && attendanceHistory.equals(otherPerson.attendanceHistory));

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && role.equals(otherPerson.role)
                && address.equals(otherPerson.address)
                && classes.equals(otherPerson.classes)
                && joinMonth.equals(otherPerson.joinMonth)
                && attendanceEquals
                && paymentHistory.equals(otherPerson.paymentHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, role, address, classes,
                joinMonth, attendanceHistory, paymentHistory);
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
                .add("joinMonth", joinMonth)
                .add("attendanceHistory", attendanceHistory)
                .add("paymentHistory", paymentHistory)
                .toString();
    }
}
