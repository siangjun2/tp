package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Class in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidClass(String)}
 */
public class Class {

    public static final String MESSAGE_CONSTRAINTS =
            "Class should follow the format: s[1-5][day][time]\n"
            + "Where:\n"
            + "  - Level: s1 to s5\n"
            + "  - Day: mon, tue, wed, thu, fri, sat, sun\n"
            + "  - Time: 4 digits in 24-hour format (0000-2359)\n"
            + "Example: s4mon1600";

    /*
     * Class format: s[1-5](mon|tue|wed|thu|fri|sat|sun)[0-2][0-9][0-5][0-9]
     * Examples: s4mon1600, s1fri0900, s3wed1430
     */
    public static final String VALIDATION_REGEX =
            "s[1-5](mon|tue|wed|thu|fri|sat|sun)([01][0-9]|2[0-3])[0-5][0-9]";

    public final String value;

    /**
     * Constructs a {@code Class}.
     *
     * @param className A valid class.
     */
    public Class(String className) {
        requireNonNull(className);
        String trimmedClass = className.trim().toLowerCase();
        checkArgument(isValidClass(trimmedClass), MESSAGE_CONSTRAINTS);
        value = trimmedClass;
    }

    /**
     * Returns true if a given string is a valid class format.
     */
    public static boolean isValidClass(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Class)) {
            return false;
        }

        Class otherClass = (Class) other;
        return value.equals(otherClass.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
