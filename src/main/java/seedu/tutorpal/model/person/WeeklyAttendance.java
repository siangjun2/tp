package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.AppUtil.checkArgument;
import static seedu.tutorpal.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Clock;
import java.time.YearMonth;

/**
 * Represents a weekly attendance period for a specific week in a month.
 * Guarantees: immutable; week index is between 1-4.
 */
public class WeeklyAttendance {

    public static final String MESSAGE_CONSTRAINTS = "Week index must be between 1 and 4.";
    public static final int LAST_WEEK_INDEX = 4;

    private final int weekIndex; // 1-4
    private final YearMonth yearMonth;

    /**
     * Constructs a {@code WeeklyAttendance} from week index and YearMonth.
     *
     * @param weekIndex Week index (1-4).
     * @param yearMonth YearMonth object.
     */
    public WeeklyAttendance(int weekIndex, YearMonth yearMonth) {
        requireAllNonNull(weekIndex, yearMonth);
        checkArgument(isValidWeekIndex(weekIndex), MESSAGE_CONSTRAINTS);
        this.weekIndex = weekIndex;
        this.yearMonth = yearMonth;
    }

    /**
     * Returns true if a given week index is valid (1-4).
     */
    private static boolean isValidWeekIndex(int weekIndex) {
        return weekIndex >= 1 && weekIndex <= 4;
    }

    /**
     * Check if given week is before this week.
     */
    public boolean isBefore(WeeklyAttendance other) {
        requireNonNull(other);
        if (this.yearMonth.isBefore(other.yearMonth)) {
            return true;
        } else if (this.yearMonth.equals(other.yearMonth)) {
            return this.weekIndex < other.weekIndex;
        } else {
            return false;
        }
    }

    /**
     * Check if given week is after this week.
     */
    public boolean isAfter(WeeklyAttendance other) {
        requireNonNull(other);
        // Reuse isBefore logic and equality check
        return !this.isBefore(other) && !this.equals(other);
    }

    /**
     * Retrieve latest accessible weekly attendance (Current month, last week).
     */
    public static WeeklyAttendance getLatestAccessibleWeeklyAttendance() {
        YearMonth currentYearMonth = YearMonth.now();
        int currentWeekIndex = LAST_WEEK_INDEX;
        return new WeeklyAttendance(currentWeekIndex, currentYearMonth);
    }

    /**
     * Retrieve latest accessible weekly attendance (Clock month, last week).
     * For enhanced testability.
     * 
     * @param clock
     * @return
     */
    public static WeeklyAttendance getLatestAccessibleWeeklyAttendance(Clock clock) {
        YearMonth currentYearMonth = YearMonth.now(clock);
        int currentWeekIndex = LAST_WEEK_INDEX;
        return new WeeklyAttendance(currentWeekIndex, currentYearMonth);
    }

    @Override
    public String toString() {
        assert yearMonth != null : "YearMonth should not be null";
        int mm = yearMonth.getMonthValue();
        int yyyy = yearMonth.getYear();
        return String.format("W%d-%02d-%04d", weekIndex, mm, yyyy);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof WeeklyAttendance)) {
            return false;
        }

        WeeklyAttendance otherWeeklyAttendance = (WeeklyAttendance) other;
        return weekIndex == otherWeeklyAttendance.weekIndex
                && yearMonth.equals(otherWeeklyAttendance.yearMonth);
    }

    @Override
    public int hashCode() {
        assert yearMonth != null : "YearMonth should not be null";
        return 31 * weekIndex + yearMonth.hashCode();
    }
}
