package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a Person's join date in the address book.
 * Guarantees: immutable; is valid as declared in
 * {@link #isValidJoinDate(String)}
 */
public class JoinDate {

    public static final String MESSAGE_CONSTRAINTS = "Join dates should be in the format dd-MM-yyyy, "
            + "and it should be a valid date!";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final LocalDate value; //immutable

    /**
     * Constructs a {@code JoinDate}.
     *
     * @param joinDate A valid LocalDate object.
     */
    public JoinDate(LocalDate joinDate) {
        requireNonNull(joinDate);
        this.value = joinDate;
    }

    /**
     * Constructs a {@code JoinDate} from a string. For editing.
     *
     * @param joinDate A valid join date string in dd-MM-yyyy format.
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
     * Converts joinDate to join week.
     * i.e. week containing join date.
     * @return WeeklyAttendance containing joinDate
     */
    public WeeklyAttendance getJoinWeek() {
        return WeeklyAttendance.at(this.value);
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

    @Override
    public int hashCode() {
        assert value != null : "JoinDate value should not be null";
        return value.hashCode();
    }
}
