package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

public class WeeklyAttendanceTest {

    @Test
    public void constructor_nullYearMonth_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WeeklyAttendance(1, null));
    }

    @Test
    public void constructor_invalidWeekIndex_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance(0, YearMonth.of(2024, 1)));
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance(5, YearMonth.of(2024, 1)));
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance(50, YearMonth.of(2024, 1)));
    }

    @Test
    public void equals_and_hashCode() {
        WeeklyAttendance a = new WeeklyAttendance(2, YearMonth.of(2024, 6));
        WeeklyAttendance b = new WeeklyAttendance(2, YearMonth.of(2024, 6));
        WeeklyAttendance c = new WeeklyAttendance(3, YearMonth.of(2024, 6));
        WeeklyAttendance d = new WeeklyAttendance(2, YearMonth.of(2024, 7));

        assertTrue(a.equals(b));
        assertEquals(a.hashCode(), b.hashCode());

        assertFalse(a.equals(c));
        assertFalse(a.equals(d));
        assertFalse(a.equals(null));
        assertFalse(a.equals("not-weekly-attendance"));
    }

    @Test
    public void toString_format() {
        WeeklyAttendance w1 = new WeeklyAttendance(3, YearMonth.of(2024, 1));
        assertEquals("W3-01-2024", w1.toString());

        WeeklyAttendance w2 = new WeeklyAttendance(1, YearMonth.of(1999, 12));
        assertEquals("W1-12-1999", w2.toString());
    }

    @Test
    public void ordering_withinSameMonth() {
        WeeklyAttendance w1 = new WeeklyAttendance(1, YearMonth.of(2024, 3));
        WeeklyAttendance w2 = new WeeklyAttendance(2, YearMonth.of(2024, 3));
        WeeklyAttendance w3 = new WeeklyAttendance(3, YearMonth.of(2024, 3));

        assertTrue(w1.isBefore(w2));
        assertTrue(w2.isBefore(w3));
        assertTrue(w3.isAfter(w2));
        assertFalse(w2.isAfter(w2)); // equal -> not after
        assertFalse(w2.isBefore(w2)); // equal -> not before
    }

    @Test
    public void ordering_acrossMonths() {
        WeeklyAttendance jan4 = new WeeklyAttendance(4, YearMonth.of(2024, 1));
        WeeklyAttendance feb1 = new WeeklyAttendance(1, YearMonth.of(2024, 2));
        WeeklyAttendance dec4 = new WeeklyAttendance(4, YearMonth.of(2023, 12));

        assertTrue(jan4.isBefore(feb1));
        assertTrue(feb1.isAfter(jan4));
        assertTrue(dec4.isBefore(jan4));
        assertFalse(feb1.isBefore(jan4));
    }

    @Test
    public void constructor_stringVsParameters_equivalent() {
        // Test that string constructor produces same result as parameter constructor
        WeeklyAttendance fromString = new WeeklyAttendance("W1-01-2024");
        WeeklyAttendance fromParams = new WeeklyAttendance(1, YearMonth.of(2024, 1));
        assertEquals(fromString, fromParams);
        assertEquals(fromString.toString(), fromParams.toString());

        WeeklyAttendance fromString2 = new WeeklyAttendance("W4-12-2023");
        WeeklyAttendance fromParams2 = new WeeklyAttendance(4, YearMonth.of(2023, 12));
        assertEquals(fromString2, fromParams2);
        assertEquals(fromString2.toString(), fromParams2.toString());

        WeeklyAttendance fromString3 = new WeeklyAttendance("W2-06-2024");
        WeeklyAttendance fromParams3 = new WeeklyAttendance(2, YearMonth.of(2024, 6));
        assertEquals(fromString3, fromParams3);
        assertEquals(fromString3.toString(), fromParams3.toString());
    }

    @Test
    public void constructor_stringInvalid_throwsIllegalArgumentException() {
        // Invalid week index
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W0-01-2024"));
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W5-01-2024"));

        // Invalid month
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W1-00-2024"));
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W1-13-2024"));

        // Invalid format
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("1-01-2024"));
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W1-1-2024"));
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W1-01-24"));
    }

    @Test
    public void constructor_stringNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WeeklyAttendance(null));
    }

    @Test
    public void isValidWeeklyAttendance() {
        // Valid formats
        assertTrue(WeeklyAttendance.isValidWeeklyAttendance("W1-01-2024"));
        assertTrue(WeeklyAttendance.isValidWeeklyAttendance("W2-06-2024"));
        assertTrue(WeeklyAttendance.isValidWeeklyAttendance("W3-09-2023"));
        assertTrue(WeeklyAttendance.isValidWeeklyAttendance("W4-12-1999"));

        // Invalid week index
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W0-01-2024"));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W5-01-2024"));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W9-01-2024"));

        // Invalid month
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W1-00-2024"));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W1-13-2024"));

        // Invalid format - missing W prefix
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("1-01-2024"));

        // Invalid format - wrong separator
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W1/01/2024"));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W1.01.2024"));

        // Invalid format - single digit month without leading zero
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W1-1-2024"));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W1-9-2024"));

        // Invalid format - wrong year length
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W1-01-24"));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W1-01-024"));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W1-01-20240"));

        // Invalid format - empty or null-like
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance(""));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("   "));
    }

    @Test
    public void getLatestAccessibleWeeklyAttendance() {
        Clock fixedClock = Clock.fixed(
                LocalDate.of(2024, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault());

        WeeklyAttendance latest = WeeklyAttendance.getLatestAccessibleWeeklyAttendance(fixedClock);
        YearMonth expectedMonth = YearMonth.of(2024, 1);
        int expectedWeekIndex = WeeklyAttendance.LAST_WEEK_INDEX;
        WeeklyAttendance expected = new WeeklyAttendance(expectedWeekIndex, expectedMonth);

        assertEquals(expected, latest);
    }
}
