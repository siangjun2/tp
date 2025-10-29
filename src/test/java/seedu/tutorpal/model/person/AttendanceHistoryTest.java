package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

public class AttendanceHistoryTest {

    // Fixed clock: 2024-03-10 is ISO week 10 of 2024
    private static final Clock FIXED_CLOCK_2024_W10 = Clock.fixed(
            Instant.parse("2024-03-10T10:00:00Z"), ZoneId.of("UTC"));

    @Test
    public void hasAttended_noAttendanceMarked_returnsFalse() {
        JoinDate joinDate = new JoinDate("01-01-2024"); // ISO week 1
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        assertFalse(history.hasAttended(week));
    }

    @Test
    public void markAttendance_validWeek_success() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        history = history.markAttendance(week);
        assertTrue(history.hasAttended(week));
    }

    @Test
    public void markAttendance_alreadyMarked_throwsException() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        history = history.markAttendance(week);
        AttendanceHistory finalHistory = history;
        assertThrows(IllegalArgumentException.class, () -> finalHistory.markAttendance(week));
    }

    @Test
    public void unmarkAttendance_markedWeek_success() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        history = history.markAttendance(week);
        history = history.unmarkAttendance(week);
        assertFalse(history.hasAttended(week));
    }

    @Test
    public void unmarkAttendance_notMarked_throwsException() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance week = new WeeklyAttendance(2, Year.of(2024));

        assertThrows(IllegalArgumentException.class, () -> history.unmarkAttendance(week));
    }

    @Test
    public void markAttendance_beforeJoinDate_throwsException() {
        JoinDate joinDate = new JoinDate("15-02-2024"); // ISO week 7
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance weekBeforeJoin = new WeeklyAttendance(6, Year.of(2024)); // before join week

        assertThrows(IllegalArgumentException.class, () -> history.markAttendance(weekBeforeJoin));
    }

    @Test
    public void markAttendance_atJoinWeek_success() {
        JoinDate joinDate = new JoinDate("15-02-2024"); // ISO week 7
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance joinWeek = joinDate.getJoinWeek();

        history = history.markAttendance(joinWeek);
        assertTrue(history.hasAttended(joinWeek));
    }

    @Test
    public void markAttendance_atCurrentWeek_success() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);
        WeeklyAttendance currentWeek = WeeklyAttendance.getCurrentWeek(FIXED_CLOCK_2024_W10); // W10-2024

        history = history.markAttendance(currentWeek);
        assertTrue(history.hasAttended(currentWeek));
    }

    @Test
    public void markAttendance_afterCurrentWeek_throwsException() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        WeeklyAttendance futureWeek = new WeeklyAttendance(11, Year.of(2024)); // after current week 10
        assertThrows(IllegalArgumentException.class, () -> history.markAttendance(futureWeek));
    }

    @Test
    public void hasAttended_weekOutOfRange_returnsFalse() {
        JoinDate joinDate = new JoinDate("15-02-2024"); // join week 7
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        WeeklyAttendance outOfRange = new WeeklyAttendance(12, Year.of(2024)); // after current week 10
        assertFalse(history.hasAttended(outOfRange));
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

        assertTrue(history.hasAttended(week1));
        assertTrue(history.hasAttended(week2));
        assertTrue(history.hasAttended(week3));
    }

    @Test
    public void getWeeklyAttendances_isUnmodifiable() {
        JoinDate joinDate = new JoinDate("01-01-2024");
        AttendanceHistory history = new AttendanceHistory(joinDate, FIXED_CLOCK_2024_W10);

        var view = history.getWeeklyAttendances();
        assertThrows(UnsupportedOperationException.class, () -> view.add(new WeeklyAttendance(1, Year.of(2024))));
        assertThrows(UnsupportedOperationException.class, () -> view.remove(new WeeklyAttendance(1, Year.of(2024))));
    }

    // ===== ISO EDGE CASES (week-year boundaries) =====

    @Test
    public void isoBoundaryJoinOnSundayBelongsToPrevIsoYearMarkJoinWeekAllowedAndFutureDisallowed() {
        // 2016-01-03 (Sun) is ISO week 53 of 2015
        Clock clock = Clock.fixed(Instant.parse("2016-01-03T10:00:00Z"), ZoneId.of("UTC"));
        JoinDate joinDate = new JoinDate("03-01-2016"); // Sunday
        AttendanceHistory history = new AttendanceHistory(joinDate, clock);

        WeeklyAttendance joinWeek = joinDate.getJoinWeek(); // W53-2015
        assertEquals(new WeeklyAttendance(53, Year.of(2015)), joinWeek);

        // Marking join week is allowed
        AttendanceHistory history2 = history.markAttendance(joinWeek);
        assertTrue(history2.hasAttended(joinWeek));
        assertFalse(history.hasAttended(joinWeek)); // immutability

        // Current week with this clock is also W53-2015; trying to mark next week
        // (W01-2016) is after current -> throws
        WeeklyAttendance nextWeek = new WeeklyAttendance(1, Year.of(2016));
        assertThrows(IllegalArgumentException.class, () -> history2.markAttendance(nextWeek));
        assertFalse(history.hasAttended(nextWeek));
    }

    @Test
    public void isoBoundary_joinOnFirstMondayOfIsoYear_prevYearWeekDisallowed() {
        // 2016-01-04 (Mon) is ISO week 1 of 2016
        Clock clock = Clock.fixed(Instant.parse("2016-01-04T10:00:00Z"), ZoneId.of("UTC"));
        JoinDate joinDate = new JoinDate("04-01-2016"); // Monday
        final AttendanceHistory history = new AttendanceHistory(joinDate, clock);

        WeeklyAttendance joinWeek = joinDate.getJoinWeek(); // W01-2016
        assertEquals(new WeeklyAttendance(1, Year.of(2016)), joinWeek);

        // Week 53 of 2015 is before join week -> disallowed
        WeeklyAttendance prevYearLastWeek = new WeeklyAttendance(53, Year.of(2015));
        assertThrows(IllegalArgumentException.class, () -> history.markAttendance(prevYearLastWeek));
        assertFalse(history.hasAttended(prevYearLastWeek));

        // Marking join week is allowed
        AttendanceHistory history2 = history.markAttendance(joinWeek);
        assertTrue(history2.hasAttended(joinWeek));
    }

    @Test
    public void isoBoundary_calendarYearEndJoinsNextIsoYearJoinWeekW01nextYear_prevYearWeeksDisallowed() {
        // 2014-12-29 (Mon) belongs to ISO week 1 of 2015
        Clock clock = Clock.fixed(Instant.parse("2014-12-29T10:00:00Z"), ZoneId.of("UTC"));
        JoinDate joinDate = new JoinDate("29-12-2014");
        AttendanceHistory history = new AttendanceHistory(joinDate, clock);

        WeeklyAttendance joinWeek = joinDate.getJoinWeek(); // W01-2015
        assertEquals(new WeeklyAttendance(1, Year.of(2015)), joinWeek);

        // Marking join week is allowed
        final AttendanceHistory history2 = history.markAttendance(joinWeek);
        assertTrue(history2.hasAttended(joinWeek));

        // Week 52 of 2014 is before join week -> disallowed
        WeeklyAttendance prevWeek = new WeeklyAttendance(52, Year.of(2014));
        assertThrows(IllegalArgumentException.class, () -> history.markAttendance(prevWeek));
        assertFalse(history.hasAttended(prevWeek));

        // Any week after current (current is W01-2015 at this clock) is disallowed
        WeeklyAttendance futureWeekSameYear = new WeeklyAttendance(2, Year.of(2015));
        assertThrows(IllegalArgumentException.class, () -> history.markAttendance(futureWeekSameYear));
        assertFalse(history.hasAttended(futureWeekSameYear));
    }
}
