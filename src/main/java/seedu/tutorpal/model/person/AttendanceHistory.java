package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.tutorpal.commons.util.ToStringBuilder;
import seedu.tutorpal.model.person.exceptions.InvalidRangeException;

/**
 * Tracks attendance of students in a set of WeeklyAttendance.
 * Guarantees : immutable
 */
public final class AttendanceHistory {

    public static final String MESSAGE_INVALID_WEEK_RANGE =
            "Command causes Attendance Week %1$s to be out of valid range!\n"
            + "It should be between the week of joining and the current week inclusive.\n"
            + "Join week : %2$s\t\t" + "Current week : %3$s";
    public static final String MESSAGE_JOIN_DATE_IN_FUTURE = "Cannot set join date %1$s after current date %2$s!";
    public static final String MESSAGE_ALREADY_MARKED = "Attendance for %1$s is already marked for %2$s.";
    public static final String MESSAGE_CANNOT_UNMARK = "Attendance for the %1$s is not marked yet for %2$s.";

    //JoinDate is immutable.
    private final JoinDate joinDate;
    //If WeeklyAttendance is inside, means attended that week
    //Immutable set stored.
    private final Set<WeeklyAttendance> weeklyAttendances;
    //Clock is immutable.
    private final Clock nowClock; // Represents current date for testability

    /**
     * Constructs an {@code AttendanceHistory} with the given join date.
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

        // Validate invariant: joinDate cannot be after current date based on nowClock
        LocalDate today = LocalDate.now(nowClock);
        if (joinDate.isAfter(today)) {
            throw new InvalidRangeException(String.format(MESSAGE_JOIN_DATE_IN_FUTURE, joinDate, today));
        }
        // Validate invariant: all provided attendances must be within [joinWeek, currentWeek]
        // else throw InvalidRangeException
        for (WeeklyAttendance wa : attendances) {
            ensureWithinValidRange(wa);
        }

        // Use Set.copyOf() so code is more defended
        this.weeklyAttendances = Set.copyOf(attendances);
    }

    public List<WeeklyAttendance> getLatestAttendance() {
        List<WeeklyAttendance> sorted = weeklyAttendances.stream()
            .sorted((a, b) -> b.compareTo(a))
            .limit(10)
            .toList();

        return sorted;
    }

    /**
     * Checks if the weekly attendance is marked.
     * If outside valid range, return false.
     */
    public boolean hasBeenMarked(WeeklyAttendance weeklyAttendance) {
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
            throw new IllegalStateException(
                    String.format(AttendanceHistory.MESSAGE_ALREADY_MARKED, weeklyAttendance, "%1$s"));
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
            throw new IllegalStateException(
                    String.format(AttendanceHistory.MESSAGE_CANNOT_UNMARK, weeklyAttendance, "%1$s"));
        }

        // Return a new immutable AttendanceHistory with the updated set.
        return new AttendanceHistory(this.joinDate, newSet, this.nowClock);
    }

    /**
     * Check if given week attendance is:
     * - not before join week
     * - not after current week
     * i.e. join date week inclusive to current week inclusive
     * else Throw InvalidRangeException
     */
    private void ensureWithinValidRange(WeeklyAttendance weeklyAttendance) {
        WeeklyAttendance joinWeek = this.joinDate.getJoinWeek();
        WeeklyAttendance currentWeek = WeeklyAttendance.getCurrentWeek(this.nowClock);

        if (weeklyAttendance.isBefore(joinWeek) || weeklyAttendance.isAfter(currentWeek)) {
            throw new InvalidRangeException(String.format(MESSAGE_INVALID_WEEK_RANGE,
                    weeklyAttendance, joinWeek, currentWeek));
        }
    }

    /**
     * Allows edit command to change Join Date of Person.
     * Built in validation of WeeklyAttendance in constructor.
     * @param joinDate New JoinDate
     * @return AttendanceHistory with new JoinDate
     */
    public AttendanceHistory changeJoinDate(JoinDate joinDate) {
        return new AttendanceHistory(joinDate, this.weeklyAttendances, this.nowClock);
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
        // Constructor already verifies these invariants.
        assert joinDate != null : "JoinDate should not be null";
        assert weeklyAttendances != null : "WeeklyAttendances should not be null";
        return joinDate.equals(otherHistory.joinDate)
                && weeklyAttendances.equals(otherHistory.weeklyAttendances);
    }

    @Override
    public int hashCode() {
        // Constructor already verifies these invariants.
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
