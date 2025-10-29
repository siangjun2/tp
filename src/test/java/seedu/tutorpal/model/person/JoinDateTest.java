package seedu.tutorpal.model.person;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import static seedu.tutorpal.testutil.Assert.assertThrows;

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
        assertFalse(JoinDate.isValidJoinDate(" 15-01-2024")); // leading space
        assertFalse(JoinDate.isValidJoinDate("15-01-2024 ")); // trailing space

        // invalid join dates (year constraints and future dates)
        assertFalse(JoinDate.isValidJoinDate("01-01-0001")); // year before 2000
        assertFalse(JoinDate.isValidJoinDate("31-12-1999")); // year before 2000
        assertFalse(JoinDate.isValidJoinDate("31-12-9999")); // future date

        // valid join dates (extremes and typical)
        assertTrue(JoinDate.isValidJoinDate("01-01-2000")); // earliest valid year
        assertTrue(JoinDate.isValidJoinDate("15-06-2023")); // valid date
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
    public void now_withFixedUtcClock_returnsExpectedDate() {
        Instant instant = Instant.parse("2025-07-01T00:00:00Z");
        Clock clock = Clock.fixed(instant, ZoneId.of("UTC"));

        JoinDate jd = JoinDate.now(clock);
        assertEquals("01-07-2025", jd.toString());
    }

    @Test
    public void now_withDifferentZones_reflectsZoneCalendarDate() {
        // Same instant, different zones -> different LocalDate
        Instant instant = Instant.parse("2025-01-01T05:00:00Z");

        Clock kiritimati = Clock.fixed(instant, ZoneId.of("Pacific/Kiritimati")); // UTC+14
        Clock honolulu = Clock.fixed(instant, ZoneId.of("Pacific/Honolulu")); // UTC-10

        assertEquals("01-01-2025", JoinDate.now(kiritimati).toString());
        assertEquals("31-12-2024", JoinDate.now(honolulu).toString());
    }

    @Test
    public void now_nullClock_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> JoinDate.now((Clock) null));
    }

    @Test
    public void toYearMonth_returnsExpectedYearMonth() {
        JoinDate jd = new JoinDate("15-07-2024");
        YearMonth ym = jd.toYearMonth();
        assertEquals(YearMonth.of(2024, 7), ym);
    }

    @Test
    public void getJoinWeek_knownIsoWeek_returnsExpectedWeek() {
        // 04-01-2025 is within ISO week 1 of 2025
        JoinDate jd = new JoinDate("04-01-2025");
        WeeklyAttendance expected = new WeeklyAttendance("W01-2025");
        assertEquals(expected, jd.getJoinWeek());
    }

    @Test
    public void equals_hashCode_contract() {
        JoinDate a = new JoinDate("15-01-2024");
        JoinDate b = new JoinDate(LocalDate.of(2024, 1, 15));
        JoinDate c = new JoinDate("16-01-2024");

        // equals
        assertEquals(a, b);
        assertNotEquals(a, c);

        // hashCode
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a.hashCode(), c.hashCode());
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
