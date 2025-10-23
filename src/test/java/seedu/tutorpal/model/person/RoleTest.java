package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tutorpal.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Role(null));
    }

    @Test
    public void constructor_invalidRole_throwsIllegalArgumentException() {
        String invalidRole = "";
        assertThrows(IllegalArgumentException.class, () -> new Role(invalidRole));
    }

    @Test
    public void constructor_validRole_success() {
        Role studentRole = new Role("student");
        assertEquals("student", studentRole.value);

        Role tutorRole = new Role("tutor");
        assertEquals("tutor", tutorRole.value);
    }

    @Test
    public void constructor_validRoleWithWhitespace_trimsAndConvertsToLowercase() {
        Role role = new Role("  STUDENT  ");
        assertEquals("student", role.value);
    }

    @Test
    public void constructor_validRoleMixedCase_convertsToLowercase() {
        Role role1 = new Role("STUDENT");
        assertEquals("student", role1.value);

        Role role2 = new Role("Student");
        assertEquals("student", role2.value);

        Role role3 = new Role("TuToR");
        assertEquals("tutor", role3.value);
    }

    @Test
    public void isValidRole_validRoles_returnsTrue() {
        // Valid roles - case insensitive
        assertTrue(Role.isValidRole("student"));
        assertTrue(Role.isValidRole("tutor"));
        assertTrue(Role.isValidRole("STUDENT"));
        assertTrue(Role.isValidRole("TUTOR"));
        assertTrue(Role.isValidRole("Student"));
        assertTrue(Role.isValidRole("Tutor"));
        assertTrue(Role.isValidRole("StUdEnT"));
        assertTrue(Role.isValidRole("tUtOr"));
    }

    @Test
    public void isValidRole_invalidRoles_returnsFalse() {
        // Empty string
        assertFalse(Role.isValidRole(""));

        // Invalid role names
        assertFalse(Role.isValidRole("teacher"));
        assertFalse(Role.isValidRole("admin"));
        assertFalse(Role.isValidRole("students")); // extra s
        assertFalse(Role.isValidRole("tutors")); // extra s
        assertFalse(Role.isValidRole("student tutor"));
        assertFalse(Role.isValidRole("stu dent"));

        // Partial matches
        assertFalse(Role.isValidRole("stud"));
        assertFalse(Role.isValidRole("tut"));
        assertFalse(Role.isValidRole("studentt"));
        assertFalse(Role.isValidRole("tutorr"));

        // With spaces
        assertFalse(Role.isValidRole(" student ")); // Note: constructor trims, but isValidRole doesn't
        assertFalse(Role.isValidRole("student "));
        assertFalse(Role.isValidRole(" tutor"));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Role role = new Role("student");
        assertTrue(role.equals(role));
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Role role1 = new Role("student");
        Role role2 = new Role("student");
        assertTrue(role1.equals(role2));
    }

    @Test
    public void equals_sameValueDifferentCase_returnsTrue() {
        Role role1 = new Role("STUDENT");
        Role role2 = new Role("student");
        assertTrue(role1.equals(role2)); // Both normalized to lowercase
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Role role1 = new Role("student");
        Role role2 = new Role("tutor");
        assertFalse(role1.equals(role2));
    }

    @Test
    public void equals_null_returnsFalse() {
        Role role = new Role("student");
        assertFalse(role.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Role role = new Role("student");
        assertFalse(role.equals("student"));
    }

    @Test
    public void hashCode_sameValue_returnsSameHashCode() {
        Role role1 = new Role("student");
        Role role2 = new Role("STUDENT");
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    public void toString_returnsCorrectFormat() {
        Role studentRole = new Role("student");
        assertEquals("student", studentRole.toString());

        Role tutorRole = new Role("TUTOR");
        assertEquals("tutor", tutorRole.toString());
    }

    @Test
    public void isStudent_studentRole_returnsTrue() {
        Role student = new Role("student");
        assertTrue(Role.isStudent(student));
        assertTrue(Role.isStudent(new Role("Student"))); // case-sensitive
        assertTrue(Role.isStudent(new Role("STUDENT")));
    }

    @Test
    public void isStudent_nonStudentRoles_returnsFalse() {
        assertFalse(Role.isStudent(new Role("tutor")));
    }

    @Test
    public void isStudent_nullRole_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Role.isStudent(null));
    }
}
