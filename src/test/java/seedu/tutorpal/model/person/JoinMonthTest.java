package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

public class JoinMonthTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new JoinMonth((String) null));
        assertThrows(NullPointerException.class, () -> new JoinMonth((YearMonth) null));
    }

    @Test
    public void constructor_invalidJoinMonth_throwsIllegalArgumentException() {
        String invalidJoinMonth = "";
        assertThrows(IllegalArgumentException.class, () -> new JoinMonth(invalidJoinMonth));
    }

    @Test
    public void isValidJoinMonth() {
        // invalid join months
        assertFalse(JoinMonth.isValidJoinMonth("")); // empty string
        assertFalse(JoinMonth.isValidJoinMonth(" ")); // spaces only
        assertFalse(JoinMonth.isValidJoinMonth("01/2024")); // wrong separator
        assertFalse(JoinMonth.isValidJoinMonth("1-2024")); // missing leading zero
        assertFalse(JoinMonth.isValidJoinMonth("13-2024")); // invalid month
        assertFalse(JoinMonth.isValidJoinMonth("00-2024")); // invalid month
        assertFalse(JoinMonth.isValidJoinMonth("2024-01")); // wrong format
        assertFalse(JoinMonth.isValidJoinMonth("01-24")); // 2-digit year
        assertFalse(JoinMonth.isValidJoinMonth("01-01-2024")); // includes day

        // valid join months
        assertTrue(JoinMonth.isValidJoinMonth("01-2024"));
        assertTrue(JoinMonth.isValidJoinMonth("06-2023"));
        assertTrue(JoinMonth.isValidJoinMonth("12-2022"));
        assertTrue(JoinMonth.isValidJoinMonth("02-2024"));
    }

    @Test
    public void toString_returnsCorrectFormat() {
        JoinMonth joinMonth = new JoinMonth(YearMonth.of(2024, 1));
        assertEquals("01-2024", joinMonth.toString());

        JoinMonth joinMonth2 = new JoinMonth(YearMonth.of(2023, 12));
        assertEquals("12-2023", joinMonth2.toString());
    }

    @Test
    public void equals() {
        JoinMonth joinMonth = new JoinMonth("01-2024");

        // same values -> returns true
        assertTrue(joinMonth.equals(new JoinMonth("01-2024")));
        assertTrue(joinMonth.equals(new JoinMonth(YearMonth.of(2024, 1))));

        // same object -> returns true
        assertTrue(joinMonth.equals(joinMonth));

        // null -> returns false
        assertFalse(joinMonth.equals(null));

        // different types -> returns false
        assertFalse(joinMonth.equals(5.0f));

        // different values -> returns false
        assertFalse(joinMonth.equals(new JoinMonth("02-2024")));
        assertFalse(joinMonth.equals(new JoinMonth("01-2023")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        JoinMonth joinMonth1 = new JoinMonth("01-2024");
        JoinMonth joinMonth2 = new JoinMonth("01-2024");
        assertEquals(joinMonth1.hashCode(), joinMonth2.hashCode());
    }

    @Test
    public void toFirstWeeklyAttendance_returnsFirstWeek() {
        YearMonth ym = YearMonth.of(2024, 1);
        JoinMonth joinMonth1 = new JoinMonth(ym);
        WeeklyAttendance expected1 = new WeeklyAttendance(1, ym);
        assertEquals(expected1, joinMonth1.toFirstWeeklyAttendance());

        YearMonth ym2 = YearMonth.of(2023, 12);
        JoinMonth joinMonth2 = new JoinMonth(ym2);
        WeeklyAttendance expected2 = new WeeklyAttendance(1, ym2);
        assertEquals(expected2, joinMonth2.toFirstWeeklyAttendance());
    }
}
