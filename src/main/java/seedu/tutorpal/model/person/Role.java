package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's role in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidRole(String)}
 */
public class Role {

    public static final String MESSAGE_CONSTRAINTS = "Only student or tutor roles available"
            + "The comparison is case-insensitive, so variations like STUDENT, Student, or student are accepted.";

    /*
     * Role must be exactly "student" or "tutor" (case-insensitive)
     */
    public static final String STUDENT_ROLE = "student";
    public static final String TUTOR_ROLE = "tutor";
    public static final String VALIDATION_REGEX = "(?i)(" + STUDENT_ROLE + "|" + TUTOR_ROLE + ")";

    public final String value;

    /**
     * Constructs a {@code Role}.
     *
     * @param role A valid role.
     */
    public Role(String role) {
        requireNonNull(role);
        String trimmedRole = role.trim().toLowerCase();
        checkArgument(isValidRole(trimmedRole), MESSAGE_CONSTRAINTS);
        value = trimmedRole;
    }

    /**
     * Returns true if a given string is a valid role.
     */
    public static boolean isValidRole(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the role is "student".
     */
    public static boolean isStudent(Role role) {
        requireNonNull(role);
        return role.value.equals(STUDENT_ROLE);
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
        if (!(other instanceof Role)) {
            return false;
        }

        Role otherRole = (Role) other;
        return value.equals(otherRole.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
