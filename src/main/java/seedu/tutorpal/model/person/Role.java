package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's role.
 * Guarantees: immutable and type-safe.
 */
public enum Role {
    STUDENT("student"),
    TUTOR("tutor");

    public static final String MESSAGE_CONSTRAINTS = "Only student or tutor roles available. "
            + "The comparison is case-insensitive, so variations like STUDENT, Student, or student are accepted.";

    private final String value;

    Role(String value) {
        this.value = value;
    }

    /** * Returns true if a given string is a valid role. */
    public static boolean isValidRole(String test) {
        requireNonNull(test);
        try {
            Role r = Role.fromString(test);
            assert r != null;
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Parses a string into a {@code Role}, ignoring case and surrounding spaces.
     *
     * @throws IllegalArgumentException if the string is not a valid role.
     */
    public static Role fromString(String role) {
        requireNonNull(role);
        String trimmed = role.trim().toLowerCase();
        for (Role r : values()) {
            if (r.value.equals(trimmed)) {
                return r;
            }
        }
        throw new IllegalArgumentException(Role.MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns the string value of the role (e.g., "Student" or "Tutor").
     * Formats it such that it capitalises first letter.
     */
    @Override
    public String toString() {
        return Character.toUpperCase(value.charAt(0)) + value.substring(1);
    }
}
