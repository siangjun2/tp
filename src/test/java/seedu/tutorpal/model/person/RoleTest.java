package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void isValidRole_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Role.isValidRole(null));
    }

    @Test
    public void isValidRole_validInputs_returnsTrue() {
        assertTrue(Role.isValidRole("student"));
        assertTrue(Role.isValidRole("tutor"));
        assertTrue(Role.isValidRole("STUDENT"));
        assertTrue(Role.isValidRole("TUTOR"));
        assertTrue(Role.isValidRole(" Student "));
        assertTrue(Role.isValidRole("  tutor  "));
    }

    @Test
    public void isValidRole_invalidInputs_returnsFalse() {
        assertFalse(Role.isValidRole(""));
        assertFalse(Role.isValidRole(" "));
        assertFalse(Role.isValidRole("students"));
        assertFalse(Role.isValidRole("teacher"));
        assertFalse(Role.isValidRole("123"));
        assertFalse(Role.isValidRole("stu dent"));
    }

    @Test
    public void fromString_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Role.fromString(null));
    }

    @Test
    public void fromString_invalid_throwsIllegalArgumentExceptionWithMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> Role.fromString("invalid"));
        assertEquals(Role.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    public void fromString_valid_caseInsensitiveAndTrimmed() {
        assertSame(Role.STUDENT, Role.fromString("student"));
        assertSame(Role.STUDENT, Role.fromString("STUDENT"));
        assertSame(Role.STUDENT, Role.fromString(" Student "));
        assertSame(Role.TUTOR, Role.fromString("tutor"));
        assertSame(Role.TUTOR, Role.fromString("TUTOR"));
        assertSame(Role.TUTOR, Role.fromString("  tutor  "));
    }

    @Test
    public void toString_returnsCapitalizedValue() {
        assertEquals("Student", Role.STUDENT.toString());
        assertEquals("Tutor", Role.TUTOR.toString());
        // round-trip
        assertEquals("Student", Role.fromString("StuDent").toString());
        assertEquals("Tutor", Role.fromString("TuToR").toString());
    }
}
