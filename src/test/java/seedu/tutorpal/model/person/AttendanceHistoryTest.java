package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.model.person.exceptions.InvalidRangeException;

public class AttendanceHistoryTest {

    // Fixed clock: 2024-03-10 is ISO week 10 of 2024
    private static final Clock FIXED_CLOCK_2024_W10 = Clock.fixed(
            Instant.parse("2024-03-10T10:00:00Z"), ZoneId.of("UTC"));

    @Test
    public void hasBeenMarked_noAttendanceMarked_returnsFalse() {
        JoinDate joinDate = new JoinDate("01-01-2024"); // ISO week 1
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        assertFalse(history.hasBeenMarked(week));
    }

    @Test
    public void markAttendance_validWeek_success() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        history = history.markAttendance(week);
        assertTrue(history.hasBeenMarked(week));
    }

    @Test
    public void markAttendance_alreadyMarked_throwsException() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        history = history.markAttendance(week);
        AttendanceHistory finalHistory = history;
        assertThrows(IllegalStateException.class, () -> finalHistory.markAttendance(week));
    }

    @Test
    public void unmarkAttendance_markedWeek_success() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        history = history.markAttendance(week);
        history = history.unmarkAttendance(week);
        assertFalse(history.hasBeenMarked(week));
    }

    @Test
    public void unmarkAttendance_notMarked_throwsException() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        assertThrows(IllegalStateException.class, () -> history.unmarkAttendance(week));
    }

    @Test
    public void markAttendance_beforeJoinDate_throwsException() {
        JoinDate joinDate = new JoinDate("15-02-2024"); // ISO week 7
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance weekBeforeJoin = new WeeklyAttendance(6, Year.of(2024)); // before join week

        assertThrows(InvalidRangeException.class, () -> history.markAttendance(weekBeforeJoin));
    }

    @Test
    public void markAttendance_atJoinWeek_success() {
        JoinDate joinDate = new JoinDate("15-02-2024"); // ISO week 7
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance joinWeek = joinDate.getJoinWeek();

        history = history.markAttendance(joinWeek);
        assertTrue(history.hasBeenMarked(joinWeek));
    }

    @Test
    public void markAttendance_atCurrentWeek_success() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance currentWeek = WeeklyAttendance.getCurrentWeek(FIXED_CLOCK_2024_W10); // W10-2024

        history = history.markAttendance(currentWeek);
        assertTrue(history.hasBeenMarked(currentWeek));
    }

    @Test
    public void markAttendance_afterCurrentWeek_throwsException() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        WeeklyAttendance futureWeek = new WeeklyAttendance(11, Year.of(2024)); // after current week 10
        assertThrows(InvalidRangeException.class, () -> history.markAttendance(futureWeek));
    }

    @Test
    public void hasBeenMarked_weekOutOfRange_returnsFalse() {
        JoinDate joinDate = new JoinDate("15-02-2024"); // join week 7
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        WeeklyAttendance outOfRange = new WeeklyAttendance(12, Year.of(2024)); // after current week 10
        assertFalse(history.hasBeenMarked(outOfRange));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        assertTrue(history.equals(history));
    }

    @Test
    public void equals_differentTypeOrNull_returnsFalse() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        assertFalse(history.equals("not an attendance history"));
        assertFalse(history.equals(null));
    }

    @Test
    public void equals_sameJoinDateNoAttendanceIgnoresClock_returnsTrue() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history1 = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        Clock anotherClock = Clock.fixed(Instant.parse("2024-03-20T10:00:00Z"),
                ZoneId.of("UTC")); // different time
        AttendanceHistory history2 = new AttendanceHistory(joinDate, anotherClock);

        assertTrue(history1.equals(history2));
    }

    @Test
    public void equals_sameJoinDateSameAttendance_returnsTrue() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history1 = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        AttendanceHistory history2 = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        history1 = history1.markAttendance(week);
        history2 = history2.markAttendance(week);

        assertTrue(history1.equals(history2));
    }

    @Test
    public void equals_differentJoinDate_returnsFalse() {
        JoinDate joinDate1 = new JoinDate("01-01-2024");
        JoinDate joinDate2 = new JoinDate("01-02-2024");
        AttendanceHistory history1 = new AttendanceHistory(joinDate1, FIXED_CLOCK_2024_W10);
        AttendanceHistory history2 = new AttendanceHistory(joinDate2, FIXED_CLOCK_2024_W10);

        assertFalse(history1.equals(history2));
    }

    @Test
    public void equals_differentAttendance_returnsFalse() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history1 = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        AttendanceHistory history2 = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        history1 = history1.markAttendance(week);

        assertFalse(history1.equals(history2));
    }

    @Test
    public void hashCode_sameJoinDateSameAttendance_sameHashCode() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history1 = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        AttendanceHistory history2 = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        history1 = history1.markAttendance(week);
        history2 = history2.markAttendance(week);

        assertEquals(history1.hashCode(), history2.hashCode());
    }

    @Test
    public void toString_validFormat() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        String result = history.toString();
        assertTrue(result.contains("joinDate"));
        assertTrue(result.contains("weeklyAttendances"));
    }

    @Test
    public void markAttendance_multipleWeeks_allMarked() {
        JoinDate joinDate = new JoinDate("01-01-2024"); // join week 1
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance(2, Year.of(2024));
        WeeklyAttendance week3 = new WeeklyAttendance(3, Year.of(2024));

        history = history.markAttendance(week1);
        history = history.markAttendance(week2);
        history = history.markAttendance(week3);

        assertTrue(history.hasBeenMarked(week1));
        assertTrue(history.hasBeenMarked(week2));
        assertTrue(history.hasBeenMarked(week3));
    }

    @Test
    public void getWeeklyAttendances_isUnmodifiable() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        var view = history.getWeeklyAttendances();
        assertThrows(UnsupportedOperationException.class, () -> view.add(new WeeklyAttendance(1, Year.of(2024))));
        assertThrows(UnsupportedOperationException.class, () -> view.remove(new WeeklyAttendance(1, Year.of(2024))));
    }

    // ===== ISO EDGE CASES =====

    @Test
    public void isoBoundaryJoinOnSundayBelongsToPrevIsoYearMarkJoinWeekAllowedAndFutureDisallowed() {
        Clock clock = Clock.fixed(Instant.parse("2016-01-03T10:00:00Z"), ZoneId.of("UTC"));
        JoinDate joinDate = new JoinDate("03-01-2016");
        AttendanceHistory history = new AttendanceHistory(joinDate, clock);

        WeeklyAttendance joinWeek = joinDate.getJoinWeek(); // W53-2015
        assertEquals(new WeeklyAttendance(53, Year.of(2015)), joinWeek);

        AttendanceHistory history2 = history.markAttendance(joinWeek);
        assertTrue(history2.hasBeenMarked(joinWeek));
        WeeklyAttendance nextWeek = new WeeklyAttendance(1, Year.of(2016));
        assertThrows(InvalidRangeException.class, () -> history2.markAttendance(nextWeek));
    }

    // ===== NEW TESTS: changeJoinDate & findAttendanceBeforeJoinWeek =====

    @Test
    public void changeJoinDate_sameDate_returnsSameInstance() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        AttendanceHistory result = history.changeJoinDate(new JoinDate("01-01-2024"));
        assertEquals(history, result);
    }

    @Test
    public void changeJoinDate_futureDate_throwsException() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        JoinDate futureDate = new JoinDate("01-04-2024");
        assertThrows(InvalidRangeException.class, () -> history.changeJoinDate(futureDate));
    }

    @Test
    public void changeJoinDate_attendanceBeforeNewJoinDate_throwsException() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        WeeklyAttendance week2 = new WeeklyAttendance(2, Year.of(2024));

        final AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10).markAttendance(week2);

        JoinDate newJoinDate = new JoinDate("15-02-2024"); // W7 — after week2
        assertThrows(InvalidRangeException.class, () -> history.changeJoinDate(newJoinDate));
    }

    @Test
    public void changeJoinDate_validPastJoinDate_success() {
        JoinDate joinDate = new JoinDate("15-02-2024"); // week 7
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week8 = new WeeklyAttendance(8, Year.of(2024));
        history = history.markAttendance(week8);

        JoinDate earlierJoinDate = new JoinDate("01-01-2024"); // week 1
        AttendanceHistory result = history.changeJoinDate(earlierJoinDate);

        assertEquals(earlierJoinDate, result.getJoinDate());
        assertTrue(result.hasBeenMarked(week8));
    }

    @Test
    public void getLatestAttendance_empty_returnsEmptyList() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        List<WeeklyAttendance> latest = history.getLatestAttendance();
        assertTrue(latest.isEmpty());
    }

    @Test
    public void getLatestAttendance_sortedDescending_withinSameYear() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        WeeklyAttendance w1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance w2 = new WeeklyAttendance(2, Year.of(2024));
        WeeklyAttendance w3 = new WeeklyAttendance(3, Year.of(2024));

        history = history.markAttendance(w2);
        history = history.markAttendance(w1);
        history = history.markAttendance(w3);

        List<WeeklyAttendance> latest = history.getLatestAttendance();
        assertEquals(List.of(w3, w2, w1), latest);
    }

    @Test
    public void getLatestAttendance_crossYearOrdering_descending() {
        // Allow marking 2023 weeks by choosing an earlier join date
        JoinDate joinDate = new JoinDate("01-01-2023");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        WeeklyAttendance w522023 = new WeeklyAttendance(52, Year.of(2023));
        WeeklyAttendance w12024 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance w22024 = new WeeklyAttendance(2, Year.of(2024));

        history = history.markAttendance(w522023);
        history = history.markAttendance(w12024);
        history = history.markAttendance(w22024);

        List<WeeklyAttendance> latest = history.getLatestAttendance();
        assertEquals(List.of(w22024, w12024, w522023), latest);
    }

    @Test
    public void getLatestAttendance_limitsTo10_latestTenOnly() {
        // Join early to allow marking across years up to current week (W10-2024)
        JoinDate joinDate = new JoinDate("01-01-2023");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        // Mark 11 weeks: W52-2023 and W01..W10-2024 (current week is W10-2024)
        WeeklyAttendance w522023 = new WeeklyAttendance(52, Year.of(2023));
        history = history.markAttendance(w522023);

        for (int w = 1; w <= 10; w++) {
            history = history.markAttendance(new WeeklyAttendance(w, Year.of(2024)));
        }

        List<WeeklyAttendance> latest = history.getLatestAttendance();
        assertEquals(10, latest.size());
        // Should be W10 down to W01 (W52-2023 excluded)
        assertEquals(new WeeklyAttendance(10, Year.of(2024)), latest.get(0));
        assertEquals(new WeeklyAttendance(1, Year.of(2024)), latest.get(9));
    }
}
