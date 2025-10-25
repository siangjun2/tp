package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Clock;
import java.util.HashSet;
import java.util.Set;

import seedu.tutorpal.commons.util.ToStringBuilder;

/**
 * Tracks attendance of students in a set of WeeklyAttendance.
 * Guarantees : immutable
 */
public class AttendanceHistory {

    public static final String MESSAGE_INVALID_WEEK_RANGE = "Weekly attendance period is out of valid range!\n"
            + " It should be between the week of joining and the current week inclusive.\n"
            + "Join week : %1$s\n"
            + "Current week : %2$s";
    public static final String MESSAGE_ALREADY_MARKED = "Attendance for %1$s week is already marked.";
    public static final String MESSAGE_CANNOT_UNMARK = "Attendance for the %1$s week is not marked yet.";

    //JoinDate is immutable.
    private final JoinDate joinDate;
    //If WeeklyAttendance is inside, means attended that week
    //Immutable set stored.
    private final Set<WeeklyAttendance> weeklyAttendances;
    //Clock is immutable.
    private final Clock nowClock; // Represents current date for testability

    /**
     * Constructs an {@code AttendanceHistory} with the given join month.
     *
     * @param joinDate The date when the person joined the system.
     */
    public AttendanceHistory(JoinDate joinDate) {
        this(joinDate, Set.of(), Clock.systemDefaultZone());
    }

    /**
     * Constructs an {@code AttendanceHistory} with a clock for testing.
     * @param joinDate The date when the person joined the system.
     * @param nowClock The clock to use for getting the current week.
     */
    public AttendanceHistory(JoinDate joinDate, Clock nowClock) {
        this(joinDate, Set.of(), nowClock);
    }

    /**
     * Constructs an {@code AttendanceHistory} with another attendanceHistory fields.
     * Main constructor that actually initialises object fields.
     * @param joinDate The date when the person joined the system.
     * @param attendances Immutable set holding WeeklyAttendances representing student attended that week.
     * @param nowClock The clock to use for getting the current week.
     */
    private AttendanceHistory(JoinDate joinDate, Set<WeeklyAttendance> attendances, Clock nowClock) {
        requireAllNonNull(joinDate, attendances, nowClock);
        this.joinDate = joinDate;
        this.nowClock = nowClock;

        // Validate invariant: all provided attendances must be within [joinWeek, currentWeek]
        for (WeeklyAttendance wa : attendances) {
            ensureWithinValidRange(wa);
        }

        // Use Set.copyOf() so code is more defended
        this.weeklyAttendances = Set.copyOf(attendances);
    }

    /**
     * Checks if the person attended on the given weekly attendance period.
     * If outside valid range, return false.
     */
    public boolean hasAttended(WeeklyAttendance weeklyAttendance) {
        requireNonNull(weeklyAttendance);
        try {
            ensureWithinValidRange(weeklyAttendance);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return weeklyAttendances.contains(weeklyAttendance);
    }

    /**
     * Marks attendance for the given weekly attendance period.
     * Throws illegal argument exception if outside valid range.
     */
    public AttendanceHistory markAttendance(WeeklyAttendance weeklyAttendance) {
        requireNonNull(weeklyAttendance);
        ensureWithinValidRange(weeklyAttendance);

        // This operation requires a new mutable set to avoid modifying the current instance.
        Set<WeeklyAttendance> newSet = new HashSet<>(this.weeklyAttendances);
        if (!newSet.add(weeklyAttendance)) {
            throw new IllegalArgumentException(
                    String.format(AttendanceHistory.MESSAGE_ALREADY_MARKED, weeklyAttendance));
        }

        // Return a new immutable AttendanceHistory with the updated set.
        return new AttendanceHistory(this.joinDate, newSet, this.nowClock);
    }

    /**
     * Unmarks attendance for the given weekly attendance period.
     * Throws illegal argument exception if outside valid range.
     */
    public AttendanceHistory unmarkAttendance(WeeklyAttendance weeklyAttendance) {
        requireNonNull(weeklyAttendance);
        ensureWithinValidRange(weeklyAttendance);

        // This operation requires a new mutable set to avoid modifying the current instance.
        Set<WeeklyAttendance> newSet = new HashSet<>(this.weeklyAttendances);
        if (!newSet.remove(weeklyAttendance)) {
            throw new IllegalArgumentException(
                    String.format(AttendanceHistory.MESSAGE_CANNOT_UNMARK, weeklyAttendance));
        }

        // Return a new immutable AttendanceHistory with the updated set.
        return new AttendanceHistory(this.joinDate, newSet, this.nowClock);
    }

    /**
     * Check if given week attendance is:
     * - not before join week
     * - not after current week
     * i.e. join date week inclusive to current week inclusive
     * else Throw IllegalArgumentException
     */
    private void ensureWithinValidRange(WeeklyAttendance weeklyAttendance) throws IllegalArgumentException {
        WeeklyAttendance joinWeek = this.joinDate.getJoinWeek();
        WeeklyAttendance currentWeek = WeeklyAttendance.getCurrentWeek(this.nowClock);

        if (weeklyAttendance.isBefore(joinWeek) || weeklyAttendance.isAfter(currentWeek)) {
            throw new IllegalArgumentException(String.format(MESSAGE_INVALID_WEEK_RANGE, joinWeek, currentWeek));
        }
    }

    /**
     * Returns the immutable join date for Storage and UI.
     */
    public JoinDate getJoinDate() {
        return joinDate;
    }

    /**
     * Returns an unmodifiable view of the marked weekly attendances for Storage and UI.
     */
    public Set<WeeklyAttendance> getWeeklyAttendances() {
        return weeklyAttendances; // already unmodifiable
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
        assert joinDate != null : "JoinDate should not be null";
        assert weeklyAttendances != null : "WeeklyAttendances should not be null";
        return joinDate.equals(otherHistory.joinDate)
                && weeklyAttendances.equals(otherHistory.weeklyAttendances);
    }

    @Override
    public int hashCode() {
        assert joinDate != null : "JoinDate should not be null";
        assert weeklyAttendances != null : "WeeklyAttendances should not be null";
        return joinDate.hashCode() + weeklyAttendances.hashCode();
    }

    //Clock should be invisible to users, therefore not printed.
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("joinDate", joinDate)
                .add("weeklyAttendances", weeklyAttendances)
                .toString();
    }
}
