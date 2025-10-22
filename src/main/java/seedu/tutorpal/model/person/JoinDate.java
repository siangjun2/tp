package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Person's join date in the address book.
 * Guarantees: immutable; is valid as declared in
 * {@link #isValidJoinDate(String)}
 */
public class JoinDate {

    public static final String MESSAGE_CONSTRAINTS = "Join dates should be in the format DD-MM-YYYY, "
            + "and it should be a valid date";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final LocalDate value;

    /**
     * Constructs a {@code JoinDate}.
     *
     * @param joinDate A valid LocalDate object.
     */
    public JoinDate(LocalDate joinDate) {
        requireNonNull(joinDate);
        value = joinDate;
    }

    /**
     * Constructs a {@code JoinDate} from a string.
     *
     * @param joinDate A valid join date string in DD/MM/YYYY format.
     */
    public JoinDate(String joinDate) {
        requireNonNull(joinDate);
        checkArgument(isValidJoinDate(joinDate), MESSAGE_CONSTRAINTS);
        this.value = LocalDate.parse(joinDate, DATE_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid join date.
     */
    public static boolean isValidJoinDate(String test) {
        try {
            LocalDate.parse(test, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Convert to weekly attendance format.
     */
    public WeeklyAttendance toWeeklyAttendanceFormat() {
        int day = value.getDayOfMonth();
        int weekIndex = (day - 1) / 7 + 1;
        YearMonth yearMonth = YearMonth.from(value);
        return new WeeklyAttendance(weekIndex, yearMonth);
    }

    @Override
    public String toString() {
        assert value != null : "JoinDate value should not be null";
        return value.format(DATE_FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        assert value != null : "JoinDate value should not be null";
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JoinDate)) {
            return false;
        }

        JoinDate otherJoinDate = (JoinDate) other;
        return value.equals(otherJoinDate.value);
    }

    public boolean equals(LocalDate otherDate) {
        assert value != null : "JoinDate value should not be null";
        return value.equals(otherDate);
    }

    @Override
    public int hashCode() {
        assert value != null : "JoinDate value should not be null";
        return value.hashCode();
    }
}
