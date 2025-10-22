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
