package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

public class AttendanceHistoryTest {

    // Fixed clock for March 2024, week 2
    private static final Clock FIXED_CLOCK_MARCH_2024_WEEK2 = Clock.fixed(
            Instant.parse("2024-03-10T10:00:00Z"), ZoneId.systemDefault());

    @Test
    public void hasAttended_noAttendanceMarked_returnsFalse() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week = new WeeklyAttendance(2, YearMonth.of(2024, 2));

        assertFalse(history.hasAttended(week));
    }

    @Test
    public void markAttendance_validWeek_success() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week = new WeeklyAttendance(2, YearMonth.of(2024, 2));

        history.markAttendance(week);
        assertTrue(history.hasAttended(week));
    }

    @Test
    public void markAttendance_alreadyMarked_throwsException() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week = new WeeklyAttendance(2, YearMonth.of(2024, 2));

        history.markAttendance(week);
        assertThrows(IllegalArgumentException.class, () -> history.markAttendance(week));
    }

    @Test
    public void unmarkAttendance_markedWeek_success() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week = new WeeklyAttendance(2, YearMonth.of(2024, 2));

        history.markAttendance(week);
        history.unmarkAttendance(week);
        assertFalse(history.hasAttended(week));
    }

    @Test
    public void unmarkAttendance_notMarked_throwsException() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week = new WeeklyAttendance(2, YearMonth.of(2024, 2));

        assertThrows(IllegalArgumentException.class, () -> history.unmarkAttendance(week));
    }

    @Test
    public void markAttendance_beforeJoinMonth_throwsException() {
        JoinMonth joinMonth = new JoinMonth("02-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week = new WeeklyAttendance(4, YearMonth.of(2024, 1)); // Before join month

        assertThrows(IllegalArgumentException.class, () -> history.markAttendance(week));
    }

    @Test
    public void markAttendance_atJoinWeek_success() {
        JoinMonth joinMonth = new JoinMonth("02-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance joinWeek = new WeeklyAttendance(1, YearMonth.of(2024, 2)); // First week of join month

        history.markAttendance(joinWeek);
        assertTrue(history.hasAttended(joinWeek));
    }

    @Test
    public void markAttendance_atCurrentWeek_success() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance currentWeek = new WeeklyAttendance(2, YearMonth.of(2024, 3)); // W2 March 2024

        history.markAttendance(currentWeek);
        assertTrue(history.hasAttended(currentWeek));
    }

    @Test
    public void markAttendance_atLastAccessibleWeek_success() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance lastAccessibleWeek = new WeeklyAttendance(4, YearMonth.of(2024, 3)); // W4 March 2024

        history.markAttendance(lastAccessibleWeek);
        assertTrue(history.hasAttended(lastAccessibleWeek));
    }

    @Test
    public void hasAttended_weekOutOfRange_throwsException() {
        JoinMonth joinMonth = new JoinMonth("02-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week = new WeeklyAttendance(4, YearMonth.of(2024, 4)); // Future month

        assertThrows(IllegalArgumentException.class, () -> history.hasAttended(week));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);

        assertTrue(history.equals(history));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);

        assertFalse(history.equals("not an attendance history"));
        assertFalse(history.equals(null));
    }

    @Test
    public void equals_sameJoinMonthNoAttendance_returnsTrue() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history1 = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        AttendanceHistory history2 = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);

        assertTrue(history1.equals(history2));
    }

    @Test
    public void equals_sameJoinMonthSameAttendance_returnsTrue() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history1 = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        AttendanceHistory history2 = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week = new WeeklyAttendance(2, YearMonth.of(2024, 2));

        history1.markAttendance(week);
        history2.markAttendance(week);

        assertTrue(history1.equals(history2));
    }

    @Test
    public void equals_differentJoinMonth_returnsFalse() {
        JoinMonth joinMonth1 = new JoinMonth("01-2024");
        JoinMonth joinMonth2 = new JoinMonth("02-2024");
        AttendanceHistory history1 = new AttendanceHistory(joinMonth1, FIXED_CLOCK_MARCH_2024_WEEK2);
        AttendanceHistory history2 = new AttendanceHistory(joinMonth2, FIXED_CLOCK_MARCH_2024_WEEK2);

        assertFalse(history1.equals(history2));
    }

    @Test
    public void equals_differentAttendance_returnsFalse() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history1 = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        AttendanceHistory history2 = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week = new WeeklyAttendance(2, YearMonth.of(2024, 2));

        history1.markAttendance(week);

        assertFalse(history1.equals(history2));
    }

    @Test
    public void hashCode_sameJoinMonthSameAttendance_sameHashCode() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history1 = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        AttendanceHistory history2 = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week = new WeeklyAttendance(2, YearMonth.of(2024, 2));

        history1.markAttendance(week);
        history2.markAttendance(week);

        assertEquals(history1.hashCode(), history2.hashCode());
    }

    @Test
    public void toString_validFormat() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);

        String result = history.toString();
        assertTrue(result.contains("joinMonth"));
        assertTrue(result.contains("weeklyAttendances"));
    }

    @Test
    public void markAttendance_multipleWeeks_allMarked() {
        JoinMonth joinMonth = new JoinMonth("01-2024");
        AttendanceHistory history = new AttendanceHistory(joinMonth, FIXED_CLOCK_MARCH_2024_WEEK2);
        WeeklyAttendance week1 = new WeeklyAttendance(1, YearMonth.of(2024, 2));
        WeeklyAttendance week2 = new WeeklyAttendance(2, YearMonth.of(2024, 2));
        WeeklyAttendance week3 = new WeeklyAttendance(3, YearMonth.of(2024, 2));

        history.markAttendance(week1);
        history.markAttendance(week2);
        history.markAttendance(week3);

        assertTrue(history.hasAttended(week1));
        assertTrue(history.hasAttended(week2));
        assertTrue(history.hasAttended(week3));
    }
}
