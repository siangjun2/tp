package seedu.tutorpal.model.person;

import static seedu.tutorpal.model.person.Role.STUDENT;

import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.tutorpal.commons.util.ToStringBuilder;

/**
 * Subtype of Person, representing a Student
 */
public class Student extends Person {
    public static final String PERSON_WORD = STUDENT.toString();

    private final AttendanceHistory attendanceHistory;

    /**
     * For Add Command
     * Public constructor with explicit joinDate (uses system clock for defaults).
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Class> classes,
            JoinDate joinDate) {
        this(name, phone, email, address, classes, joinDate, null, Clock.systemDefaultZone(),
                new PaymentHistory(LocalDate.now()));
    }

    /**
     * Public constructor with explicit joinDate and paymentHistory.
     * Used by Storage when restoring from JSON.
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Class> classes,
                   JoinDate joinDate, PaymentHistory paymentHistory) {
        this(name, phone, email, address, classes, joinDate, null, Clock.systemDefaultZone(), paymentHistory);
    }

    /**
     * For Add Command
     *  Allows injecting of clock to control
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Class> classes,
            JoinDate joinDate, Clock nowClock) {
        this(name, phone, email, address, classes, joinDate, null, nowClock,
                new PaymentHistory(LocalDate.now(nowClock)));
    }

    /**
     * For Edit Command
     * Public constructor with attendance history
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Class> classes,
                   JoinDate joinDate, AttendanceHistory attendanceHistory) {
        this(name, phone, email, address, classes, joinDate, attendanceHistory, Clock.systemDefaultZone(),
                new PaymentHistory(LocalDate.now()));
    }

    /**
     * For Edit Command
     * Allows injecting of clock to control
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Class> classes,
                   JoinDate joinDate, AttendanceHistory attendanceHistory, Clock nowClock) {
        this(name, phone, email, address, classes, joinDate, attendanceHistory, nowClock,
                new PaymentHistory(LocalDate.now(nowClock)));
    }

    /**
     * With all fields except clock
     */
    public Student(Name name, Phone phone, Email email, Address address, Set<Class> classes,
                   JoinDate joinDate, AttendanceHistory attendanceHistory, PaymentHistory paymentHistory) {
        this(name, phone, email, address, classes, joinDate, attendanceHistory, Clock.systemDefaultZone(),
                paymentHistory);
    }

    /**
     * Private constructor: inject a Clock to control "now".
     * If attendanceHistory is null, it will be constructed from joinDate and
     * nowClock.
     */
    private Student(Name name, Phone phone, Email email, Address address, Set<Class> classes,
            JoinDate joinDate, AttendanceHistory attendanceHistory, Clock nowClock,
            PaymentHistory paymentHistory) {
        super(name, phone, email, address, classes, joinDate, paymentHistory, nowClock);
        validateClassSize(classes);
        AttendanceHistory normalized = attendanceHistory != null
                ? attendanceHistory
                : new AttendanceHistory(joinDate, nowClock);
        validateJoinDateSync(normalized, this.getJoinDate());
        this.attendanceHistory = normalized;
    }

    @Override
    public Role getRole() {
        return Role.STUDENT;
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
        if (classes.size() != 1) {
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

    /**
     * Generates a formatted string summarizing the student's attendance
     * over the most recent ten ISO weeks.
     *
     * <p>The method retrieves the student's latest weekly attendance records
     * from {@code attendanceHistory.getLatestAttendance()}, determines which of the
     * past ten ISO weeks (including the current week) the student attended, and
     * constructs a textual summary marking each week as either "present" or "absent".</p>
     *
     * <p>Weeks are labeled using their ISO week numbers (as defined by
     * {@link java.time.temporal.IsoFields#WEEK_OF_WEEK_BASED_YEAR}).</p>
     *
     * <p>Example output:</p>
     * <pre>{@code
     * W35:absent W36:present W37:present W38:absent W39:present
     * W40:present W41:present W42:absent W43:present W44:present
     * }</pre>
     *
     * @return a formatted {@link String} listing attendance status for each of the last ten ISO weeks
     * @see java.time.temporal.IsoFields#WEEK_OF_WEEK_BASED_YEAR
     * @see WeeklyAttendance
     */
    public String printAttendanceHistory() {
        HashMap<WeeklyAttendance, Boolean> attendanceWeeks = new HashMap<>();
        LocalDate today = LocalDate.now();
        WeeklyAttendance todayWeeklyAttendance = WeeklyAttendance.of(today);
        for (int i = 0; i < 10; i++) {
            attendanceWeeks.put(todayWeeklyAttendance.minusWeeks(i), false);
        }

        List<WeeklyAttendance> attendanceList = attendanceHistory.getLatestAttendance();
        for (WeeklyAttendance weeklyAttendance : attendanceList) {
            //if (weeklyAttendance.isPaid()) {
                attendanceWeeks.replace(weeklyAttendance, true);
            //}
        }

        String output = "";
        int iterator = 0;
        LocalDate joinDate = super.getJoinDate().toLocalDate();
        WeeklyAttendance joinWeeklyAttendance = WeeklyAttendance.of(joinDate);
        if (joinWeeklyAttendance.isAfter(todayWeeklyAttendance.minusWeeks(10 - 1))) {
            iterator = todayWeeklyAttendance.subtractWeeklyAttendance(joinWeeklyAttendance);
        }
        for (int i = iterator; i < 10; i++) {
            WeeklyAttendance currWeeklyAttendance = todayWeeklyAttendance.minusWeeks(10 - 1 - i);
            if (attendanceWeeks.get(currWeeklyAttendance)) {
                output += String.format("W%d:present ", currWeeklyAttendance.getWeekIndex());
            } else {
                output += String.format("W%d:absent ", currWeeklyAttendance.getWeekIndex());
            }
        }

        return output;
        /*List<WeeklyAttendance> attendanceList = attendanceHistory.getLatestAttendance();
        boolean[] latestTenWeeks = new boolean[10];
        LocalDate today = LocalDate.now();
        int nowWeek = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);

        for (WeeklyAttendance weeklyAttendance: attendanceList) {
            int attendanceWeek = weeklyAttendance.getWeekIndex();
            if (attendanceWeek <= nowWeek && attendanceWeek >= nowWeek - 9) {
                latestTenWeeks[attendanceWeek - nowWeek + 9] = true;
            }
        }

        String output = "";

        for (int i = 0; i < latestTenWeeks.length; i++) {
            if (latestTenWeeks[i]) {
                output += String.format("W%d:present ", i + nowWeek - 9);
            } else {
                output += String.format("W%d:absent ", i + nowWeek - 9);
            }
        }

        return output;*/
    }

    @Override
    public String displayInfo() {
        return super.displayInfo() + "\n" + printAttendanceHistory();
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
                .add("name", getName())
                .add("phone", getPhone())
                .add("email", getEmail())
                .add("address", getAddress())
                .add("classes", getClasses())
                .add("joinDate", getJoinDate())
                .add("paymentHistory", getPaymentHistory())
                .add("attendanceHistory", attendanceHistory)
                .toString();
    }
}
