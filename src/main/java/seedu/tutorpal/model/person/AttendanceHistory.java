package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;

import java.time.Clock;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import seedu.tutorpal.commons.util.ToStringBuilder;

/**
 * Tracks attendance of students.
 */
public class AttendanceHistory {

    public static final String MESSAGE_INVALID_WEEK_RANGE = "Weekly attendance period is out of valid range!\n"
            + " It should be between the first week of join month and the last week of current month inclusive.";

    private final JoinMonth joinMonth;
    private final Set<WeeklyAttendance> weeklyAttendances;
    private final Clock nowClock; // Represents current date for validation
    /**
     * Constructs an {@code AttendanceHistory} with the given join month.
     *
     * @param joinMonth The month when the person joined the system.
     */
    public AttendanceHistory(JoinMonth joinMonth) {
        this.joinMonth = joinMonth;
        this.weeklyAttendances = new HashSet<WeeklyAttendance>();
        this.nowClock = Clock.systemDefaultZone();
    }

    /**
     * Constructs an {@code AttendanceHistory} with a clock for testing.
     * @param joinMonth The month when the person joined the system.
     * @param nowClock  The clock to use for getting the current week.
     */
    public AttendanceHistory(JoinMonth joinMonth, Clock nowClock) {
        this.joinMonth = joinMonth;
        this.weeklyAttendances = new HashSet<WeeklyAttendance>();
        this.nowClock = nowClock;
    }

    /**
     * Constructs an {@code AttendanceHistory} with another attendanceHistory.
     * @param attendanceHistory
     */
    public AttendanceHistory(AttendanceHistory attendanceHistory) {
        requireNonNull(attendanceHistory);
        this.joinMonth = attendanceHistory.joinMonth;
        this.weeklyAttendances = new HashSet<>(attendanceHistory.weeklyAttendances);
        this.nowClock = attendanceHistory.nowClock;
    }

    /**
     * Checks if the person attended on the given weekly attendance period.
     */
    public boolean hasAttended(WeeklyAttendance weeklyAttendance) {
        requireNonNull(weeklyAttendance);
        if (!isWithinValidRange(weeklyAttendance)) {
            throw new IllegalArgumentException(MESSAGE_INVALID_WEEK_RANGE);
        }
        return weeklyAttendances.contains(weeklyAttendance);
    }

    /**
     * Marks attendance for the given weekly attendance period.
     */
    public AttendanceHistory markAttendance(WeeklyAttendance weeklyAttendance) {
        requireNonNull(weeklyAttendance);
        if (!isWithinValidRange(weeklyAttendance)) {
            throw new IllegalArgumentException(MESSAGE_INVALID_WEEK_RANGE);
        } else if (hasAttended(weeklyAttendance)) {
            throw new IllegalArgumentException("Attendance for the given week is already marked.");
        }
        weeklyAttendances.add(weeklyAttendance);
        return this;
    }

    /**
     * Unmarks attendance for the given weekly attendance period.
     */
    public AttendanceHistory unmarkAttendance(WeeklyAttendance weeklyAttendance) {
        requireNonNull(weeklyAttendance);
        if (!isWithinValidRange(weeklyAttendance)) {
            throw new IllegalArgumentException(MESSAGE_INVALID_WEEK_RANGE);
        } else if (!hasAttended(weeklyAttendance)) {
            throw new IllegalArgumentException("Attendance for the given week is not marked yet.");
        }
        weeklyAttendances.remove(weeklyAttendance);
        return this;
    }

    /**
     * Returns an immutable set of weekly attendances.
     */
    public Set<WeeklyAttendance> getWeeklyAttendances() {
        return Collections.unmodifiableSet(weeklyAttendances);
    }

    /**
     * Returns the join month of this attendance history.
     */
    public JoinMonth getJoinMonth() {
        return joinMonth;
    }

    /**
     * Check if given week attendance is:
     * - not before join date
     * - not after current week attendance
     * ie join date week inclusive to current week inclusive
     */
    private boolean isWithinValidRange(WeeklyAttendance weeklyAttendance) {
        WeeklyAttendance joinWeek = joinMonth.toFirstWeeklyAttendance();
        WeeklyAttendance currentWeek = WeeklyAttendance.getLatestAccessibleWeeklyAttendance(this.nowClock);
        return !weeklyAttendance.isBefore(joinWeek) && !weeklyAttendance.isAfter(currentWeek);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AttendanceHistory)) {
            return false;
        }
        AttendanceHistory otherHistory = (AttendanceHistory) other;
        assert joinMonth != null : "JoinMonth should not be null";
        assert weeklyAttendances != null : "WeeklyAttendances should not be null";
        return joinMonth.equals(otherHistory.joinMonth)
                && weeklyAttendances.equals(otherHistory.weeklyAttendances);
    }

    @Override
    public int hashCode() {
        assert joinMonth != null : "JoinMonth should not be null";
        assert weeklyAttendances != null : "WeeklyAttendances should not be null";
        return joinMonth.hashCode() + weeklyAttendances.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("joinMonth", joinMonth)
                .add("weeklyAttendances", weeklyAttendances)
                .toString();
    }
}
