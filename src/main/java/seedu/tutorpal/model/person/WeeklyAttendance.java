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
public final class WeeklyAttendance implements Comparable<WeeklyAttendance> {

    /**
     * User-facing validation constraints for the {@link WeeklyAttendance} format.
     * <p>Expected format: {@code W[XX]-YYYY}, where:
     * <ul>
     *   <li>{@code W} is case-insensitive,</li>
     *   <li>week number {@code [XX]} is between 01 and 52 (or 53 if that ISO year has 53 weeks), and</li>
     *   <li>{@code YYYY} is a 4-digit year between 0001 and 9999.</li>
     * </ul>
     * Example: {@code W04-2025} represents the fourth ISO week of 2025.
     */
    public static final String MESSAGE_CONSTRAINTS = "Weekly attendance must be in the format W[XX]-YYYY, "
            + "where:\n"
            + "1) W is case insensitive,\n"
            + "2) week number [XX] is between 01 and 52 (or 53 if that year has 53 weeks), and\n"
            + "3) YYYY is a 4-digit year between 2000 and 9999 inclusive. "
            + "This week format is following the international standard of ISO-8601 week numbering.\n"
            + "Example: W04-2025 represents the fourth ISO week of 2025.";

    /** The first valid ISO week number in any week-based year. */
    public static final int FIRST_WEEK_NUMBER = 1;

    // Allow up to 53 weeks (some ISO years have 53)
    public static final String WEEKLY_ATTENDANCE_REGEX =
            "(?i)^W(0[1-9]|[1-4][0-9]|5[0-3])-([2-9]\\d{3})$";

    /** ISO week index within the ISO week-based year (01–52 or 53 where applicable). */
    private final int weekIndex; // 01 to 52 or 53 depending on year
    /** ISO week-based year. {@link Year} is immutable. */
    private final Year year; //immutable

    /**
     * Constructs a {@code WeeklyAttendance} from week index and Year.
     *
     * @param weekIndex Week index (1–52 or 53 depending on year).
     * @param year      Year object.
     * @throws IllegalArgumentException if {@code weekIndex} is not valid for the given {@code year}.
     */
    public WeeklyAttendance(int weekIndex, Year year) {
        requireAllNonNull(weekIndex, year);
        checkArgument(isValidWeekIndex(weekIndex, year), MESSAGE_CONSTRAINTS);
        checkArgument(isValidYear(year), MESSAGE_CONSTRAINTS);
        this.weekIndex = weekIndex;
        this.year = year;
    }

    /**
     * Constructs a {@code WeeklyAttendance} from a string.
     *
     * @param weeklyAttendanceString A valid weekly attendance string in
     *                               {@code W[01–53]-YYYY} format.
     * @throws IllegalArgumentException if the string does not conform to the expected format
     *                                  or encodes an invalid week for the given year.
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
        assert isValidYear(year);

        this.weekIndex = weekIndex;
        this.year = year;
    }

    /**
     * Returns true if a given string matches the weekly attendance format.
     * Checks :
     * <ol>
     *   <li>Matches regex pattern {@link #WEEKLY_ATTENDANCE_REGEX}.</li>
     *   <li>Week number is within the maximum number of ISO weeks for that week-based year.</li>
     * </ol>
     *
     * @param test String to validate.
     * @return {@code true} if the string is a valid ISO week reference; {@code false} otherwise.
     */
    public static boolean isValidWeeklyAttendance(String test) {
        requireNonNull(test);
        Pattern pattern = Pattern.compile(WEEKLY_ATTENDANCE_REGEX);
        Matcher matcher = pattern.matcher(test);
        if (matcher.matches()) {
            int weekIndex = Integer.parseInt(matcher.group(1));
            int yearValue = Integer.parseInt(matcher.group(2));
            Year year = Year.of(yearValue);

            return isValidWeekIndex(weekIndex, year) && isValidYear(year);
        }
        return false;
    }

    /**
     * Returns true if a given week index is valid for the given year (1–52 or 53 if
     * applicable).
     *
     * @param weekIndex ISO week number candidate.
     * @param year      ISO week-based year.
     * @return {@code true} if the index is within the year's ISO week count; {@code false} otherwise.
     */
    private static boolean isValidWeekIndex(int weekIndex, Year year) {
        requireNonNull(year);
        return WeeklyAttendance.FIRST_WEEK_NUMBER <= weekIndex
            && weekIndex <= getNumberOfWeeksInIsoYear(year.getValue());
    }

    /**
     * Returns true if a given year is valid. ie 2000 <= year <= 9999
     */
    private static boolean isValidYear(Year year) {
        // Enforce year 2000–9999 and valid ISO week for that year
        int yearValue = year.getValue();
        return yearValue >= 2000 && yearValue <= 9999;
    }

    /**
     * Returns the number of ISO weeks in the given year (52 or 53).
     * <p>Uses {@code 28 December}, which always belongs to the last ISO week of its
     * week-based year. Using other dates (e.g. 30 December) may give incorrect results.
     *
     * @param year Calendar year (four-digit, positive).
     * @return {@code 52} or {@code 53} depending on the ISO calendar for that year.
     * @throws java.time.DateTimeException if the provided year is invalid.
     */
    public static int getNumberOfWeeksInIsoYear(int year) {
        LocalDate date = LocalDate.of(year, 12, 28);
        return date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    /**
     * Creates a {@code WeeklyAttendance} from a {@link LocalDate}, using the date's
     * ISO week and its calendar year.
     *
     * @param localDate the date to convert.
     * @return a {@code WeeklyAttendance} corresponding to the date's ISO week.
     * @throws NullPointerException if {@code localDate} is {@code null}.
     */
    public static WeeklyAttendance of(LocalDate localDate) {
        int weekIndex = localDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        Year year = Year.of(localDate.getYear());
        return new WeeklyAttendance(weekIndex, year);
    }

    /**
     * Returns the ISO week index (1–52/53) within the ISO week-based year.
     *
     * @return the week number.
     */
    public int getWeekIndex() {
        return this.weekIndex;
    }

    /**
     * Returns {@code true} if this ISO week is strictly before {@code other}.
     *
     * @param other the week to compare against.
     * @return {@code true} if earlier; {@code false} otherwise.
     * @throws NullPointerException if {@code other} is {@code null}.
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
     * Returns {@code true} if this ISO week is strictly after {@code other}.
     *
     * @param other the week to compare against.
     * @return {@code true} if later; {@code false} otherwise.
     * @throws NullPointerException if {@code other} is {@code null}.
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
     * @throws NullPointerException if {@code clock} is {@code null}.
     */
    public static WeeklyAttendance getCurrentWeek(Clock clock) {
        requireNonNull(clock);
        LocalDate currentDate = LocalDate.now(clock);
        return WeeklyAttendance.at(currentDate);
    }

    /**
     * Converts a {@link LocalDate} to a {@code WeeklyAttendance} using ISO week fields.
     *
     * @param date Date to be converted.
     * @return WeeklyAttendance that contains the given date.
     * @throws NullPointerException if {@code date} is {@code null}.
     */
    public static WeeklyAttendance at(LocalDate date) {
        int weekIndex = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int weekYear = date.get(IsoFields.WEEK_BASED_YEAR);
        return new WeeklyAttendance(weekIndex, Year.of(weekYear));
    }

    /**
     * Returns a new {@code WeeklyAttendance} representing {@code this} minus the given number of weeks.
     * <p>If subtraction crosses the ISO week-year boundary, the result is carried into the previous week-based year.
     *
     * @param weeksToSubtract number of weeks to subtract (non-negative expected).
     * @return a new {@code WeeklyAttendance} shifted backwards by the given number of weeks.
     */
    public WeeklyAttendance minusWeeks(int weeksToSubtract) {
        int temp = this.weekIndex - weeksToSubtract;
        if (temp > 0) {
            return new WeeklyAttendance(temp, this.year);
        } else {
            Year previousYear = this.year.minusYears(1);
            temp += getNumberOfWeeksInIsoYear(previousYear.getValue());
            return new WeeklyAttendance(temp, previousYear);
        }
    }

    /**
     * Computes the number of whole weeks between {@code other} (inclusive) and {@code this} (exclusive).
     * <p>Formally, returns {@code this - other} in weeks, assuming {@code other} is not after {@code this}.
     *
     * @param other the earlier or equal {@code WeeklyAttendance}.
     * @return the non-negative number of weeks between the two.
     * @throws IllegalArgumentException if {@code other} is after {@code this}.
     */
    public int subtractWeeklyAttendance(WeeklyAttendance other) {
        assert !(other.isAfter(this));

        if (other.isAfter(this)) {
            throw new IllegalArgumentException();
        }

        if (this.year.equals(other.year)) {
            return this.weekIndex - other.weekIndex;
        }

        int thisYearInt = this.year.getValue();
        int otherYearInt = other.year.getValue();
        int thisWeek = this.weekIndex;
        int otherWeek = other.weekIndex;
        while (thisYearInt > otherYearInt) {
            thisYearInt--;
            thisWeek += getNumberOfWeeksInIsoYear(thisYearInt);
        }

        return thisWeek - otherWeek;
    }

    /**
     * Natural ordering: chronological order by ISO week-based year then week index.
     *
     * @param other the object to be compared.
     * @return {@code -1}, {@code 0}, or {@code 1} according to standard {@link Comparable} contract.
     */
    @Override
    public int compareTo(WeeklyAttendance other) {
        if (this.isBefore(other)) {
            return -1;
        } else if (this.isAfter(other)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Returns a canonical ISO week string in the form {@code W[XX]-YYYY}.
     *
     * @return a string representation of this ISO week.
     */
    @Override
    public String toString() {
        return String.format("W%02d-%04d", weekIndex, year.getValue());
    }

    /**
     * Equality based on both ISO week index and ISO week-based year.
     *
     * @param other the other object.
     * @return {@code true} if both represent the same ISO week; {@code false} otherwise.
     */
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

    /**
     * Hash code consistent with {@link #equals(Object)}, combining week index and year.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return 31 * weekIndex + year.hashCode();
    }
}
