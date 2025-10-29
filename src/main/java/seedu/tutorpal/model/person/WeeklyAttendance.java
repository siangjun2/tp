package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.AppUtil.checkArgument;
import static seedu.tutorpal.commons.util.CollectionUtil.requireAllNonNull;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.IsoFields;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a weekly attendance period for a specific ISO week in a year.
 * Guarantees: immutable
 *
 * <p>
 * Uses ISO-8601 week definitions:
 * <ul>
 * <li>Weeks start on Monday.</li>
 * <li>Week 1 is the week containing January 4.</li>
 * <li>Each year has either 52 or 53 weeks.</li>
 * </ul>
 */
public final class WeeklyAttendance {

    public static final String MESSAGE_CONSTRAINTS = "Weekly attendance must be in the format W[XX]-YYYY, "
            + "where:\n"
            + "1) W is case insensitive,\n"
            + "2) week number [XX] is between 01 and 52 (or 53 if that year has 53 weeks), and\n"
            + "3) YYYY is a 4-digit year between 0001 and 9999. "
            + "This week format is following the international standard of ISO-8601 week numbering.\n"
            + "Example: W04-2025 represents the fourth ISO week of 2025.";

    public static final int FIRST_WEEK_NUMBER = 1;

    // Allow up to 53 weeks (some ISO years have 53)
    // Does not allow Year 0000 because of Year.of() limitations.
    public static final String WEEKLY_ATTENDANCE_REGEX =
            "(?i)^W(0[1-9]|[1-4][0-9]|5[0-3])-(?!0000)(\\d{4})$";

    private final int weekIndex; // 01 to 52 or 53 depending on year
    private final Year year; //immutable

    /**
     * Constructs a {@code WeeklyAttendance} from week index and Year.
     *
     * @param weekIndex Week index (1–52 or 53 depending on year).
     * @param year      Year object.
     */
    public WeeklyAttendance(int weekIndex, Year year) {
        requireAllNonNull(weekIndex, year);
        checkArgument(isValidWeekIndex(weekIndex, year), MESSAGE_CONSTRAINTS);
        this.weekIndex = weekIndex;
        this.year = year;
    }

    /**
     * Constructs a {@code WeeklyAttendance} from a string.
     *
     * @param weeklyAttendanceString A valid weekly attendance string in
     *                               W[01–53]-YYYY format.
     */
    public WeeklyAttendance(String weeklyAttendanceString) {
        requireNonNull(weeklyAttendanceString);
        checkArgument(isValidWeeklyAttendance(weeklyAttendanceString), MESSAGE_CONSTRAINTS);

        Pattern pattern = Pattern.compile(WEEKLY_ATTENDANCE_REGEX);
        Matcher matcher = pattern.matcher(weeklyAttendanceString);
        if (!matcher.matches()) {
            // Should be checked by checkArgument already.
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }

        int weekIndex = Integer.parseInt(matcher.group(1));
        int yearValue = Integer.parseInt(matcher.group(2));
        Year year = Year.of(yearValue);
        assert isValidWeekIndex(weekIndex, year);

        this.weekIndex = weekIndex;
        this.year = year;
    }

    /**
     * Returns true if a given string matches the weekly attendance format.
     * Checks :
     * 1) Matches regex pattern.
     * 2) Week number is within max week number of that year.
     */
    public static boolean isValidWeeklyAttendance(String test) {
        requireNonNull(test);
        Pattern pattern = Pattern.compile(WEEKLY_ATTENDANCE_REGEX);
        Matcher matcher = pattern.matcher(test);
        if (matcher.matches()) {
            int weekIndex = Integer.parseInt(matcher.group(1));
            int yearValue = Integer.parseInt(matcher.group(2));
            Year year = Year.of(yearValue);

            return isValidWeekIndex(weekIndex, year);
        }
        return false;
    }

    /**
     * Returns true if a given week index is valid for the given year (1–52 or 53 if
     * applicable).
     */
    private static boolean isValidWeekIndex(int weekIndex, Year year) {
        requireNonNull(year);
        return WeeklyAttendance.FIRST_WEEK_NUMBER <= weekIndex
                && weekIndex <= getNumberOfWeeksInIsoYear(year.getValue());
    }

    /**
     * Returns the number of ISO weeks in the given year (52 or 53).
     * Using December 28, as it ALWAYS belongs to the last ISO week of its
     * week-based year.
     * Using other days such as December 30, will cause wrong logic.
     */
    public static int getNumberOfWeeksInIsoYear(int year) {
        LocalDate date = LocalDate.of(year, 12, 28);
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    /**
     * Check if this week is before another week.
     */
    public boolean isBefore(WeeklyAttendance other) {
        requireNonNull(other);
        if (this.year.isBefore(other.year)) {
            return true;
        } else if (this.year.equals(other.year)) {
            return this.weekIndex < other.weekIndex;
        } else {
            return false;
        }
    }

    /**
     * Check if this week is after another week.
     */
    public boolean isAfter(WeeklyAttendance other) {
        requireNonNull(other);
        return !this.isBefore(other) && !this.equals(other);
    }

    /**
     * Retrieve the current ISO weekly attendance based on the current date.
     *
     * @param clock Clock to represent date/time source.
     * @return Current weekly attendance (ISO-8601 week-year).
     */
    public static WeeklyAttendance getCurrentWeek(Clock clock) {
        requireNonNull(clock);
        LocalDate currentDate = LocalDate.now(clock);
        return WeeklyAttendance.at(currentDate);
    }

    /**
     * Converts LocalDate to WeeklyAttendance object.
     * Week returned is ISO compliant.
     * @param date Date to be converted
     * @return  WeeklyAttendance that contains given date
     */
    public static WeeklyAttendance at(LocalDate date) {
        int weekIndex = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int weekYear = date.get(IsoFields.WEEK_BASED_YEAR);
        return new WeeklyAttendance(weekIndex, Year.of(weekYear));
    }

    @Override
    public String toString() {
        return String.format("W%02d-%04d", weekIndex, year.getValue());
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
                && year.equals(otherWeeklyAttendance.year);
    }

    @Override
    public int hashCode() {
        return 31 * weekIndex + year.hashCode();
    }
}
