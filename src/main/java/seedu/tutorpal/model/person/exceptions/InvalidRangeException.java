package seedu.tutorpal.model.person.exceptions;

/**
 * Specific Command Exception where the date range being indicated is outside the valid time range.
 * Allows for more specific exception handling path.
 */
public class InvalidRangeException extends IllegalArgumentException {
    public InvalidRangeException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code InvalidRangeException} with the specified detail {@code message} and {@code cause}.
     */
    public InvalidRangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
