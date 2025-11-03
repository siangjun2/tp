package seedu.tutorpal.model.person.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InvalidRangeExceptionTest {

    @Test
    void constructor_message_setsMessageAndIsIllegalArgumentException() {
        String message = "Range is invalid";
        InvalidRangeException ex = new InvalidRangeException(message);

        assertEquals(message, ex.getMessage());
        assertNull(ex.getCause());
        assertTrue(ex instanceof IllegalArgumentException);
    }

    @Test
    void constructor_messageAndCause_setsMessageAndCause() {
        String message = "Invalid range with cause";
        Throwable cause = new RuntimeException("root cause");
        InvalidRangeException ex = new InvalidRangeException(message, cause);

        assertEquals(message, ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    void assertThrows_capturesExceptionAndMessage() {
        String message = "Throwing invalid range";
        InvalidRangeException ex = assertThrows(
                InvalidRangeException.class, () -> {
                    throw new InvalidRangeException(message);
                });

        assertEquals(message, ex.getMessage());
    }
}
