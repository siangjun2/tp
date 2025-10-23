package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.AppUtil.checkArgument;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Person's join month in the address book.
 * Guarantees: immutable; is valid as declared in
 * {@link #isValidJoinMonth(String)}
 */
public class JoinMonth {

    public static final String MESSAGE_CONSTRAINTS = "Join months should be in the format MM-YYYY, "
            + "and it should be a valid month!";

    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM-yyyy");

    private final YearMonth value;

    /**
     * Constructs a {@code JoinMonth}.
     *
     * @param joinMonth A valid YearMonth object.
     */
    public JoinMonth(YearMonth joinMonth) {
        requireNonNull(joinMonth);
        this.value = joinMonth;
    }

    /**
     * Constructs a {@code JoinMonth} from a string. For editing.
     *
     * @param joinMonth A valid join month string in MM-YYYY format.
     */
    public JoinMonth(String joinMonth) {
        requireNonNull(joinMonth);
        checkArgument(isValidJoinMonth(joinMonth), MESSAGE_CONSTRAINTS);
        this.value = YearMonth.parse(joinMonth, MONTH_FORMATTER);
    }

    /**
     * Returns true if a given string is a valid join month.
     */
    public static boolean isValidJoinMonth(String test) {
        try {
            YearMonth.parse(test, MONTH_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Get first WeeklyAttendance of this join month.
     */
    public WeeklyAttendance toFirstWeeklyAttendance() {
        return new WeeklyAttendance(1, value);
    }

    @Override
    public String toString() {
        assert value != null : "JoinMonth value should not be null";
        return value.format(MONTH_FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        assert value != null : "JoinMonth value should not be null";
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JoinMonth)) {
            return false;
        }

        JoinMonth otherJoinMonth = (JoinMonth) other;
        return value.equals(otherJoinMonth.value);
    }

    @Override
    public int hashCode() {
        assert value != null : "JoinMonth value should not be null";
        return value.hashCode();
    }
}
