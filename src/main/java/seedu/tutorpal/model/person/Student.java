package seedu.tutorpal.model.person;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import seedu.tutorpal.commons.util.ToStringBuilder;

/**
 * Subtype of Person, representing a Student
 */
public class Student extends Person {
    public static final String PERSON_WORD = "Student";

    private final AttendanceHistory attendanceHistory;

    /**
     * Public constructor (default "now").
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Class> classes) {
        this(name, phone, email, address, classes, JoinDate.now(), null, Clock.systemDefaultZone(),
                new PaymentHistory(LocalDate.now()));
    }

    /**
     * Public constructor with explicit joinDate (uses system clock for defaults).
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Class> classes,
            JoinDate joinDate) {
        this(name, phone, email, address, classes, joinDate, null, Clock.systemDefaultZone(),
                new PaymentHistory(LocalDate.now()));
    }

    /**
     * Testing version of public constructor. Allows injecting of clock to control
     * "now".
     * ONLY MEANT FOR TESTS
     */
    protected Student(Name name, Phone phone, Email email, Address address, Set<Class> classes,
            JoinDate joinDate, Clock nowClock) {
        this(name, phone, email, address, classes, joinDate, null, nowClock,
                new PaymentHistory(LocalDate.now(nowClock)));
    }

    /**
     * Private constructor: inject a Clock to control "now".
     * If attendanceHistory is null, it will be constructed from joinDate and
     * nowClock.
     */
    private Student(Name name, Phone phone, Email email, Address address, Set<Class> classes,
            JoinDate joinDate, AttendanceHistory attendanceHistory, Clock nowClock,
            PaymentHistory paymentHistory) {
        super(name, phone, email, address, classes, joinDate, paymentHistory);
        validateClassSize(classes);
        AttendanceHistory normalized = attendanceHistory != null
                ? attendanceHistory
                : new AttendanceHistory(joinDate, nowClock);
        validateJoinDateSync(normalized, this.getJoinDate());
        this.attendanceHistory = normalized;
    }

    @Override
    public boolean hasAttendanceHistory() {
        return true;
    }

    @Override
    public AttendanceHistory getAttendanceHistory() {
        return attendanceHistory;
    }

    private static void validateClassSize(Set<Class> classes) {
        if (classes.size() > 1) {
            throw new IllegalArgumentException(String.format(
                    Person.MESSAGE_INVALID_CLASS_SIZE, Student.PERSON_WORD, classes.size()));
        }
    }

    private static void validateJoinDateSync(AttendanceHistory normalized, JoinDate joinDate) {
        if (!normalized.getJoinDate().equals(joinDate)) {
            throw new IllegalArgumentException(
                    String.format(Person.MESSAGE_OUT_OF_SYNC, Student.PERSON_WORD, Student.PERSON_WORD));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Student)) {
            return false;
        }
        if (other == this) {
            return true;
        }
        Student o = (Student) other;
        return super.equals(o) && Objects.equals(attendanceHistory, o.attendanceHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), attendanceHistory);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("person", super.toString())
                .add("attendanceHistory", attendanceHistory)
                .toString();
    }
}
