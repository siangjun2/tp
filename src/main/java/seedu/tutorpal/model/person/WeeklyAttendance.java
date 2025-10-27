package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.AppUtil.checkArgument;
import static seedu.tutorpal.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Clock;
import java.time.YearMonth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a weekly attendance period for a specific week in a month.
 * Guarantees: immutable; week index is between 1-4.
 */
public class WeeklyAttendance {

    public static final String MESSAGE_CONSTRAINTS = "Weekly attendance must be in the format W[1-4]-MM-YYYY, "
            + "where week index is between 1 and 4, MM is the month (01-12), "
            + "and YYYY is a 4-digit year.";
    public static final int LAST_WEEK_INDEX = 4;

    public static final String WEEKLY_ATTENDANCE_REGEX = "W([1-4])-(0[1-9]|1[0-2])-(\\d{4})";

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
     * Constructs a {@code WeeklyAttendance} from a string.
     *
     * @param weeklyAttendanceString A valid weekly attendance string in
     *                               W[1-4]-MM-YYYY format.
     */
    public WeeklyAttendance(String weeklyAttendanceString) {
        requireNonNull(weeklyAttendanceString);
        checkArgument(isValidWeeklyAttendance(weeklyAttendanceString), MESSAGE_CONSTRAINTS);

        Pattern pattern = Pattern.compile(WEEKLY_ATTENDANCE_REGEX);
        Matcher matcher = pattern.matcher(weeklyAttendanceString);

        // checkArgument should have checked this already
        matcher.matches();
        this.weekIndex = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int year = Integer.parseInt(matcher.group(3));
        this.yearMonth = YearMonth.of(year, month);
    }

    /**
     * Returns true if a given string is a valid weekly attendance format.
     */
    public static boolean isValidWeeklyAttendance(String test) {
        return test.matches(WEEKLY_ATTENDANCE_REGEX);
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
     * Retrieve latest accessible weekly attendance (Clock's month, last week).
     *
     * @param clock Clock to represent date.
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
