package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.YearMonth;

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
        // invalid join dates
        assertFalse(JoinDate.isValidJoinDate("")); // empty string
        assertFalse(JoinDate.isValidJoinDate(" ")); // spaces only
        assertFalse(JoinDate.isValidJoinDate("01/01/2024")); // wrong separator
        assertFalse(JoinDate.isValidJoinDate("1-1-2024")); // missing leading zeros
        assertFalse(JoinDate.isValidJoinDate("32-01-2024")); // invalid day
        assertFalse(JoinDate.isValidJoinDate("01-13-2024")); // invalid month
        assertFalse(JoinDate.isValidJoinDate("2024-01-01")); // wrong format
        assertFalse(JoinDate.isValidJoinDate("01-01-24")); // 2-digit year
        assertFalse(JoinDate.isValidJoinDate("29-02-2023")); // invalid leap year date

        // valid join dates
        assertTrue(JoinDate.isValidJoinDate("01-01-2024"));
        assertTrue(JoinDate.isValidJoinDate("15-06-2023"));
        assertTrue(JoinDate.isValidJoinDate("31-12-2022"));
        assertTrue(JoinDate.isValidJoinDate("29-02-2024")); // valid leap year date
    }

    @Test
    public void constructor_withLocalDate_success() {
        LocalDate date = LocalDate.of(2024, 1, 15);
        JoinDate joinDate = new JoinDate(date);
        assertTrue(joinDate.equals(date));
    }

    @Test
    public void constructor_withString_success() {
        String dateString = "15-01-2024";
        JoinDate joinDate = new JoinDate(dateString);
        assertTrue(joinDate.equals(LocalDate.of(2024, 1, 15)));
    }

    @Test
    public void toString_returnsCorrectFormat() {
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 15));
        assertEquals("15-01-2024", joinDate.toString());
    }

    @Test
    public void equals() {
        JoinDate joinDate = new JoinDate("01-01-2024");

        // same values -> returns true
        assertTrue(joinDate.equals(new JoinDate("01-01-2024")));
        assertTrue(joinDate.equals(new JoinDate(LocalDate.of(2024, 1, 1))));

        // same object -> returns true
        assertTrue(joinDate.equals(joinDate));

        // null -> returns false
        assertFalse(joinDate.equals(null));

        // different types -> returns false
        assertFalse(joinDate.equals(5.0f));

        // different values -> returns false
        assertFalse(joinDate.equals(new JoinDate("02-01-2024")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        JoinDate joinDate1 = new JoinDate("01-01-2024");
        JoinDate joinDate2 = new JoinDate("01-01-2024");
        assertEquals(joinDate1.hashCode(), joinDate2.hashCode());
    }

    // Does have dependency on WeeklyAttendance equals method, but unavoidable and
    // better than using getters.
    @Test
    public void toWeeklyAttendanceFormat_variousDates() {
        // First week of month
        JoinDate joinDate1 = new JoinDate("01-01-2024");
        WeeklyAttendance weekly1 = joinDate1.toWeeklyAttendanceFormat();
        WeeklyAttendance expected1 = new WeeklyAttendance(1, YearMonth.of(2024, 1));
        assertEquals(expected1, weekly1);

        // Second week of month
        JoinDate joinDate2 = new JoinDate("10-02-2024");
        WeeklyAttendance weekly2 = joinDate2.toWeeklyAttendanceFormat();
        WeeklyAttendance expected2 = new WeeklyAttendance(2, YearMonth.of(2024, 2));
        assertEquals(expected2, weekly2);

        // Last day of third week
        JoinDate joinDate3 = new JoinDate("21-01-2024");
        WeeklyAttendance weekly3 = joinDate3.toWeeklyAttendanceFormat();
        WeeklyAttendance expected3 = new WeeklyAttendance(3, YearMonth.of(2024, 1));
        assertEquals(expected3, weekly3);
    }
}
