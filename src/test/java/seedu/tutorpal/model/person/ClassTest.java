package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ClassTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Class(null));
    }

    @Test
    public void constructor_invalidClass_throwsIllegalArgumentException() {
        String invalidClass = "";
        assertThrows(IllegalArgumentException.class, () -> new Class(invalidClass));
    }

    @Test
    public void constructor_validClass_success() {
        // Valid format: s4mon1600
        Class validClass = new Class("s4mon1600");
        assertEquals("s4mon1600", validClass.value);
    }

    @Test
    public void constructor_validClassWithWhitespace_trimsAndConvertsToLowercase() {
        Class validClass = new Class("  S4MON1600  ");
        assertEquals("s4mon1600", validClass.value);
    }

    @Test
    public void isValidClass_validFormats_returnsTrue() {
        // Valid formats
        assertTrue(Class.isValidClass("s1mon0000")); // Boundary: earliest time
        assertTrue(Class.isValidClass("s5sun2359")); // Boundary: latest time
        assertTrue(Class.isValidClass("s2tue1030"));
        assertTrue(Class.isValidClass("s3wed1500"));
        assertTrue(Class.isValidClass("s4thu0945"));
        assertTrue(Class.isValidClass("s5fri1800"));
        assertTrue(Class.isValidClass("s1sat1200"));
    }

    @Test
    public void isValidClass_invalidLevel_returnsFalse() {
        assertFalse(Class.isValidClass("s0mon1600")); // Level too low
        assertFalse(Class.isValidClass("s6mon1600")); // Level too high
        assertFalse(Class.isValidClass("s10mon1600")); // Multiple digits
    }

    @Test
    public void isValidClass_invalidDay_returnsFalse() {
        assertFalse(Class.isValidClass("s4monday1600")); // Full day name
        assertFalse(Class.isValidClass("s4abc1600")); // Invalid day
        assertFalse(Class.isValidClass("s41600")); // Missing day
    }

    @Test
    public void isValidClass_invalidTime_returnsFalse() {
        assertFalse(Class.isValidClass("s4mon2400")); // Hour out of range
        assertFalse(Class.isValidClass("s4mon1660")); // Minute out of range
        assertFalse(Class.isValidClass("s4mon160")); // Too few digits
        assertFalse(Class.isValidClass("s4mon16000")); // Too many digits
        assertFalse(Class.isValidClass("s4mon")); // Missing time
    }

    @Test
    public void isValidClass_emptyString_returnsFalse() {
        assertFalse(Class.isValidClass(""));
    }

    @Test
    public void isValidClass_missingPrefix_returnsFalse() {
        assertFalse(Class.isValidClass("4mon1600")); // Missing 's'
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Class classObj = new Class("s4mon1600");
        assertTrue(classObj.equals(classObj));
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Class class1 = new Class("s4mon1600");
        Class class2 = new Class("s4mon1600");
        assertTrue(class1.equals(class2));
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Class class1 = new Class("s4mon1600");
        Class class2 = new Class("s4tue1600");
        assertFalse(class1.equals(class2));
    }

    @Test
    public void equals_null_returnsFalse() {
        Class classObj = new Class("s4mon1600");
        assertFalse(classObj.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Class classObj = new Class("s4mon1600");
        assertFalse(classObj.equals("s4mon1600")); //String vs Class
    }

    @Test
    public void hashCode_sameValue_returnsSameHashCode() {
        Class class1 = new Class("s4mon1600");
        Class class2 = new Class("s4mon1600");
        assertEquals(class1.hashCode(), class2.hashCode());
    }

    @Test
    public void toString_returnsCorrectFormat() {
        Class classObj = new Class("s4mon1600");
        assertEquals("s4mon1600", classObj.toString());
    }
}
