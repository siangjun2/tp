package seedu.tutorpal.model.person;

import static seedu.tutorpal.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Set;

import seedu.tutorpal.commons.util.ToStringBuilder;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public abstract class Person {
    public static final String MESSAGE_INVALID_CLASS_SIZE = "%1$s must have a single class only! "
            + "Currently, initialised with %2$s class(es).";
    public static final String MESSAGE_NO_ATTENDANCE_HISTORY = "%1$s must not have attendance history.";
    public static final String MESSAGE_OUT_OF_SYNC = "%1$s.joinDate must match %2$s.joinDate.";
    public static final String MESSAGE_INVALID_ATTENDANCE_RETRIEVAL = "%1$s does not have attendance history.";

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final Email email;
    private final Address address;
    private final Set<Class> classes;
    private final JoinDate joinDate;
    // attendanceHistory moved to Student subclass
    private final PaymentHistory paymentHistory;

    /**
     * Core constructor with all common fields.
     */
    protected Person(Name name, Phone phone, Email email, Address address,
                     Set<Class> classes, JoinDate joinDate, PaymentHistory paymentHistory) {
        requireAllNonNull(name, phone, email, address, classes, joinDate, paymentHistory);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.joinDate = joinDate;
        this.classes = Set.copyOf(classes);
        this.paymentHistory = paymentHistory;
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

    public Address getAddress() {
        return address;
    }

    /**
     * Returns a String to represent role of person
     */
    public abstract Role getRole();

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
     * Default: false. Students override to true.
     */
    public abstract boolean hasAttendanceHistory();

    /**
     * Returns the attendance history for students.
     * Default: throws for non-students.
     */
    public abstract AttendanceHistory getAttendanceHistory();

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
                && address.equals(otherPerson.address)
                && classes.equals(otherPerson.classes)
                && joinDate.equals(otherPerson.joinDate)
                && paymentHistory.equals(otherPerson.paymentHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone, email, address, classes,
                joinDate, paymentHistory);
    }

    /**
     * Returns a String representation of the detailed information of the Person.
     */
    public String displayInfo() {
        return name.fullName + "\n"
            + phone.value + "\n"
            + email.value + "\n"
            + address.value + "\n";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("classes", classes)
                .add("joinDate", joinDate)
                .add("paymentHistory", paymentHistory)
                .toString();
    }
}
