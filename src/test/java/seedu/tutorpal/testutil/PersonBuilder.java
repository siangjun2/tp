package seedu.tutorpal.testutil;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import seedu.tutorpal.model.person.Address;
import seedu.tutorpal.model.person.AttendanceHistory;
import seedu.tutorpal.model.person.Class;
import seedu.tutorpal.model.person.Email;
import seedu.tutorpal.model.person.JoinMonth;
import seedu.tutorpal.model.person.Name;
import seedu.tutorpal.model.person.PaymentHistory;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Phone;
import seedu.tutorpal.model.person.Role;
import seedu.tutorpal.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ROLE = "student";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_CLASS = "s4mon1600";
    public static final String DEFAULT_PAYMENT_STATUS = "unpaid"; // kept for backward compatibility; unused now
    public static final String DEFAULT_JOIN_MONTH = "11-2024";

    private Name name;
    private Phone phone;
    private Email email;
    private Role role;
    private Address address;
    private Set<Class> classes;
    private JoinMonth joinMonth;
    private AttendanceHistory attendanceHistory;
    // Payment is now derived from PaymentHistory; no explicit field needed

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        role = new Role(DEFAULT_ROLE);
        address = new Address(DEFAULT_ADDRESS);
        classes = new HashSet<>();
        classes.add(new Class(DEFAULT_CLASS));
        joinMonth = new JoinMonth(DEFAULT_JOIN_MONTH);
        if (Role.isStudent(role)) {
            attendanceHistory = new AttendanceHistory(joinMonth);
        } else {
            attendanceHistory = null;
        }
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        role = personToCopy.getRole();
        address = personToCopy.getAddress();
        classes = new HashSet<>(personToCopy.getClasses());
        joinMonth = personToCopy.getJoinMonth();
        // ensure attendanceHistory matches role: students keep or get initialized;
        // tutors get null
        if (Role.isStudent(role)) {
            attendanceHistory = personToCopy.getAttendanceHistory() != null
                    ? personToCopy.getAttendanceHistory()
                    : new AttendanceHistory(joinMonth);
        } else {
            attendanceHistory = null;
        }
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the
     * {@code Person} that we are building.
     */
    public PersonBuilder withTags(String... tags) {
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Role} of the {@code Person} that we are building.
     */
    public PersonBuilder withRole(String role) {
        this.role = new Role(role);
        // keep attendanceHistory consistent with role
        if (Role.isStudent(this.role)) {
            if (this.attendanceHistory == null) {
                this.attendanceHistory = new AttendanceHistory(joinMonth);
            }
        } else {
            this.attendanceHistory = null;
        }
        return this;
    }

    /**
     * Parses the {@code classes} into a {@code Set<Class>} and set it to the
     * {@code Person} that we are building.
     */
    public PersonBuilder withClasses(String... classes) {
        this.classes = SampleDataUtil.getClassSet(classes);
        return this;
    }

    /**
     * Sets the {@code JoinMonth} of the {@code Person} that we are building.
     */
    public PersonBuilder withJoinMonth(String joinMonth) {
        this.joinMonth = new JoinMonth(joinMonth);
        // NOTE : also update attendance history to use the new join month
        // This resets the attendance history
        this.attendanceHistory = new AttendanceHistory(this.joinMonth);
        return this;
    }

    /**
     * Sets the {@code AttendanceHistory} of the {@code Person} that we are
     * building.
     */
    public PersonBuilder withAttendanceHistory(AttendanceHistory attendanceHistory) {
        this.attendanceHistory = attendanceHistory;
        return this;
    }

    // Backward-compatibility no-op: tests that call withPayment() can remain
    // unchanged
    public PersonBuilder withPayment(String paymentStatus) {
        return this;
    }

    /**
     * Builds the person using edited fields.
     * 
     * @return
     */
    public Person build() {
        return new Person(name, phone, email, role, address, classes, joinMonth,
                attendanceHistory, new PaymentHistory(LocalDate.now())); // NOT SURE WHAT YOU
        // MEAN BY // Payment is
        // now derived from
        // PaymentHistory; no
        // explicit field needed
    }

}
