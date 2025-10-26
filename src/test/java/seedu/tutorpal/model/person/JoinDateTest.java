package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tutorpal.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class JoinDateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JoinDate((String) null));
        assertThrows(NullPointerException.class, () -> new JoinDate((LocalDate) null));
    }

    @Test
    public void constructor_invalidJoinDate_throwsIllegalArgumentException() {
        String invalidJoinDate = "";
        assertThrows(IllegalArgumentException.class, () -> new JoinDate(invalidJoinDate));
    }

    @Test
    public void isValidJoinDate() {
        // null join date
        assertThrows(NullPointerException.class, () -> JoinDate.isValidJoinDate(null));

        // invalid join dates
        assertFalse(JoinDate.isValidJoinDate("")); // empty string
        assertFalse(JoinDate.isValidJoinDate(" ")); // spaces only
        assertFalse(JoinDate.isValidJoinDate("01-2024")); // missing day
        assertFalse(JoinDate.isValidJoinDate("2024-01-15")); // wrong format
        assertFalse(JoinDate.isValidJoinDate("15/01/2024")); // wrong separator
        assertFalse(JoinDate.isValidJoinDate("32-01-2024")); // invalid day
        assertFalse(JoinDate.isValidJoinDate("01-13-2024")); // invalid month
        assertFalse(JoinDate.isValidJoinDate("29-02-2023")); // invalid leap year date
        assertFalse(JoinDate.isValidJoinDate("31-04-2024")); // April has only 30 days
        assertFalse(JoinDate.isValidJoinDate("abc-01-2024")); // non-numeric day
        assertFalse(JoinDate.isValidJoinDate("01-abc-2024")); // non-numeric month
        assertFalse(JoinDate.isValidJoinDate("01-01-abcd")); // non-numeric year
        assertFalse(JoinDate.isValidJoinDate("1-1-2024")); // single digit day and month

        // valid join dates
        assertTrue(JoinDate.isValidJoinDate("01-01-2024")); // valid date
        assertTrue(JoinDate.isValidJoinDate("15-06-2023")); // valid date
        assertTrue(JoinDate.isValidJoinDate("31-12-2024")); // end of year
        assertTrue(JoinDate.isValidJoinDate("29-02-2024")); // valid leap year date
        assertTrue(JoinDate.isValidJoinDate("28-02-2023")); // valid non-leap year date
        assertTrue(JoinDate.isValidJoinDate("31-01-2024")); // January has 31 days
        assertTrue(JoinDate.isValidJoinDate("30-04-2024")); // April has 30 days
    }

    @Test
    public void constructor_validLocalDate_success() {
        LocalDate localDate = LocalDate.of(2024, 1, 15);
        JoinDate joinDate = new JoinDate(localDate);
        assertEquals("15-01-2024", joinDate.toString());
    }

    @Test
    public void constructor_validString_success() {
        JoinDate joinDate = new JoinDate("15-01-2024");
        assertEquals("15-01-2024", joinDate.toString());
    }

    @Test
    public void toString_validDate_correctFormat() {
        JoinDate joinDate = new JoinDate("01-06-2023");
        assertEquals("01-06-2023", joinDate.toString());
    }

    @Test
    public void equals() {
        JoinDate joinDate = new JoinDate("15-01-2024");

        // same values -> returns true
        assertEquals(new JoinDate("15-01-2024"), joinDate);
        assertEquals(joinDate, new JoinDate(LocalDate.of(2024, 1, 15)));

        // same object -> returns true
        assertEquals(joinDate, joinDate);

        // null -> returns false
        assertNotEquals(null, joinDate);

        // different types -> returns false
        assertNotEquals(0.5f, joinDate);

        // different values -> returns false
        assertNotEquals(new JoinDate("16-01-2024"), joinDate);
        assertNotEquals(new JoinDate("15-02-2024"), joinDate);
        assertNotEquals(new JoinDate("15-01-2023"), joinDate);
    }

    @Test
    public void hashCode_sameDate_sameHashCode() {
        JoinDate joinDate1 = new JoinDate("15-01-2024");
        JoinDate joinDate2 = new JoinDate("15-01-2024");
        assertEquals(joinDate1.hashCode(), joinDate2.hashCode());
    }

    @Test
    public void hashCode_differentDate_differentHashCode() {
        JoinDate joinDate1 = new JoinDate("15-01-2024");
        JoinDate joinDate2 = new JoinDate("16-01-2024");
        assertNotEquals(joinDate1.hashCode(), joinDate2.hashCode());
    }

    @Test
    void isAfter_null_throwsNullPointerException() {
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 5, 10));
        assertThrows(NullPointerException.class, () -> joinDate.isAfter(null));
    }

    @Test
    void isAfter_joinDateAfterGiven_returnsTrue() {
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 5, 10));
        LocalDate given = LocalDate.of(2024, 5, 9);
        assertTrue(joinDate.isAfter(given));
    }

    @Test
    void isAfter_joinDateEqualGiven_returnsFalse() {
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 5, 10));
        LocalDate given = LocalDate.of(2024, 5, 10);
        assertFalse(joinDate.isAfter(given));
    }

    @Test
    void isAfter_joinDateBeforeGiven_returnsFalse() {
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 5, 10));
        LocalDate given = LocalDate.of(2024, 5, 11);
        assertFalse(joinDate.isAfter(given));
    }
}
