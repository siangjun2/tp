package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tutorpal.model.person.WeeklyAttendance.FIRST_WEEK_NUMBER;
import static seedu.tutorpal.model.person.WeeklyAttendance.getNumberOfWeeksInIsoYear;
import static seedu.tutorpal.model.person.WeeklyAttendance.isValidWeeklyAttendance;

import java.time.Clock;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;

public class WeeklyAttendanceTest {

    // ===== CONSTRUCTOR TESTS (WEEK INDEX AND YEAR) =====

    @Test
    public void constructor_validWeekIndexAndYear_success() {
        WeeklyAttendance attendance = new WeeklyAttendance(5, Year.of(2024));
        assertEquals("W05-2024", attendance.toString());
    }

    @Test
    public void constructor_firstWeek_success() {
        WeeklyAttendance firstWeek = new WeeklyAttendance(FIRST_WEEK_NUMBER, Year.of(2024));
        assertEquals("W01-2024", firstWeek.toString());
    }

    @Test
    public void constructor_lastWeekOf52WeekYear_success() {
        WeeklyAttendance lastWeek = new WeeklyAttendance(52, Year.of(2024));
        assertEquals("W52-2024", lastWeek.toString());
    }

    @Test
    public void constructor_week53Of53WeekYear_success() {
        WeeklyAttendance week53 = new WeeklyAttendance(53, Year.of(2020));
        assertEquals("W53-2020", week53.toString());
    }

    @Test
    public void constructor_nullYear_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WeeklyAttendance(1, null));
    }

    @Test
    public void constructor_weekIndexZero_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance(0, Year.of(2024)));
    }

    @Test
    public void constructor_negativeWeekIndex_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance(-1, Year.of(2024)));
    }

    @Test
    public void constructor_week53In52WeekYear_throwsIllegalArgumentException() {
        // 2024 has only 52 weeks
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance(53, Year.of(2024)));
    }

    @Test
    public void constructor_week54_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance(54, Year.of(2020)));
    }

    // ===== CONSTRUCTOR TESTS (STRING) =====

    @Test
    public void constructor_validStringLowerCase_success() {
        WeeklyAttendance attendance = new WeeklyAttendance("w01-2024");
        assertEquals("W01-2024", attendance.toString());
    }

    @Test
    public void constructor_validStringUpperCase_success() {
        WeeklyAttendance attendance = new WeeklyAttendance("W01-2024");
        assertEquals("W01-2024", attendance.toString());
    }

    @Test
    public void constructor_validStringMidRangeWeek_success() {
        WeeklyAttendance attendance = new WeeklyAttendance("W26-2024");
        assertEquals("W26-2024", attendance.toString());
    }

    @Test
    public void constructor_nullString_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new WeeklyAttendance((String) null));
    }

    @Test
    public void constructor_emptyString_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("");
    }

    @Test
    public void constructor_noLeadingZero_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W1-2024"));
    }

    @Test
    public void constructor_week00_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W00-2024"));
    }

    @Test
    public void constructor_twoDigitYear_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W01-24"));
    }

    @Test
    public void constructor_wrongPrefix_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("X01-2024"));
    }

    @Test
    public void constructor_wrongSeparator_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W01/2024"));
    }

    @Test
    public void constructor_nonDigitWeek_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("Wab-2024"));
    }

    @Test
    public void constructor_nonDigitYear_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W01-202a"));
    }

    // ===== ISO WEEK VALIDATION TESTS =====

    @Test
    public void isValidWeeklyAttendance_validFormats_returnsTrue() {
        assertTrue(isValidWeeklyAttendance("W01-2024"));
        assertTrue(isValidWeeklyAttendance("w01-2024"));
        assertTrue(isValidWeeklyAttendance("W52-2024"));
        assertTrue(isValidWeeklyAttendance("W53-2020")); // 2020 has 53 weeks
    }

    @Test
    public void isValidWeeklyAttendance_week53In52WeekYear_returnsFalse() {
        assertFalse(isValidWeeklyAttendance("W53-2024")); // 2024 has only 52 weeks
        assertFalse(isValidWeeklyAttendance("W53-2023")); // 2023 has only 52 weeks
    }

    @Test
    public void isValidWeeklyAttendance_invalidFormats_returnsFalse() {
        assertFalse(isValidWeeklyAttendance("");
        assertFalse(isValidWeeklyAttendance("W00-2024"));
        assertFalse(isValidWeeklyAttendance("W54-2024"));
        assertFalse(isValidWeeklyAttendance("W1-2024"));
        assertFalse(isValidWeeklyAttendance("W01-24"));
        assertFalse(isValidWeeklyAttendance("X01-2024"));
        assertFalse(isValidWeeklyAttendance("W01/2024"));
    }

    @Test
    public void isValidWeeklyAttendance_nullInput_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> isValidWeeklyAttendance(null));
    }

    // ===== ISO YEAR WEEK COUNT TESTS =====

    @Test
    public void getNumberOfWeeksInIsoYear_52WeekYears_returns52() {
        assertEquals(52, getNumberOfWeeksInIsoYear(2024));
        assertEquals(52, getNumberOfWeeksInIsoYear(2023));
        assertEquals(52, getNumberOfWeeksInIsoYear(2022));
        assertEquals(52, getNumberOfWeeksInIsoYear(2019));
    }

    @Test
    public void getNumberOfWeeksInIsoYear_53WeekYears_returns53() {
        assertEquals(53, getNumberOfWeeksInIsoYear(2020));
        assertEquals(53, getNumberOfWeeksInIsoYear(2015));
        assertEquals(53, getNumberOfWeeksInIsoYear(2009));
        assertEquals(53, getNumberOfWeeksInIsoYear(2004));
    }

    @Test
    public void getNumberOfWeeksInIsoYear_edgeCaseYears_correctCount() {
        // Years with 53 weeks occur when Jan 1 is Thursday or leap year with Jan 1 as
        // Wednesday
        assertEquals(53, getNumberOfWeeksInIsoYear(2026)); // Jan 1 is Thursday
        assertEquals(52, getNumberOfWeeksInIsoYear(2025)); // Jan 1 is Wednesday (non-leap)
    }

    // ===== COMPARISON TESTS =====

    @Test
    public void isBefore_sameYearEarlierWeek_returnsTrue() {
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance(2, Year.of(2024));
        assertTrue(week1.isBefore(week2));
    }

    @Test
    public void isBefore_sameYearLaterWeek_returnsFalse() {
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance(2, Year.of(2024));
        assertFalse(week2.isBefore(week1));
    }

    @Test
    public void isBefore_earlierYear_returnsTrue() {
        WeeklyAttendance week2023 = new WeeklyAttendance(52, Year.of(2023));
        WeeklyAttendance week2024 = new WeeklyAttendance(1, Year.of(2024));
        assertTrue(week2023.isBefore(week2024));
    }

    @Test
    public void isBefore_laterYear_returnsFalse() {
        WeeklyAttendance week2023 = new WeeklyAttendance(52, Year.of(2023));
        WeeklyAttendance week2024 = new WeeklyAttendance(1, Year.of(2024));
        assertFalse(week2024.isBefore(week2023));
    }

    @Test
    public void isBefore_sameWeek_returnsFalse() {
        WeeklyAttendance week = new WeeklyAttendance(1, Year.of(2024));
        assertFalse(week.isBefore(week));
    }

    @Test
    public void isBefore_nullInput_throwsNullPointerException() {
        WeeklyAttendance week = new WeeklyAttendance(1, Year.of(2024));
        assertThrows(NullPointerException.class, () -> week.isBefore(null));
    }

    @Test
    public void isAfter_sameYearLaterWeek_returnsTrue() {
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance(2, Year.of(2024));
        assertTrue(week2.isAfter(week1));
    }

    @Test
    public void isAfter_sameYearEarlierWeek_returnsFalse() {
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance(2, Year.of(2024));
        assertFalse(week1.isAfter(week2));
    }

    @Test
    public void isAfter_laterYear_returnsTrue() {
        WeeklyAttendance week2023 = new WeeklyAttendance(52, Year.of(2023));
        WeeklyAttendance week2024 = new WeeklyAttendance(1, Year.of(2024));
        assertTrue(week2024.isAfter(week2023));
    }

    @Test
    public void isAfter_earlierYear_returnsFalse() {
        WeeklyAttendance week2023 = new WeeklyAttendance(52, Year.of(2023));
        WeeklyAttendance week2024 = new WeeklyAttendance(1, Year.of(2024));
        assertFalse(week2023.isAfter(week2024));
    }

    @Test
    public void isAfter_sameWeek_returnsFalse() {
        WeeklyAttendance week = new WeeklyAttendance(1, Year.of(2024));
        assertFalse(week.isAfter(week));
    }

    @Test
    public void isAfter_nullInput_throwsNullPointerException() {
        WeeklyAttendance week = new WeeklyAttendance(1, Year.of(2024));
        assertThrows(NullPointerException.class, () -> week.isAfter(null));
    }

    // ===== EQUALITY AND HASHCODE TESTS =====

    @Test
    public void equals_sameObject_returnsTrue() {
        WeeklyAttendance week = new WeeklyAttendance(1, Year.of(2024));
        assertEquals(week, week);
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance(1, Year.of(2024));
        assertEquals(week1, week2);
    }

    @Test
    public void equals_constructedFromString_returnsTrue() {
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance("W01-2024");
        assertEquals(week1, week2);
    }

    @Test
    public void equals_differentWeekSameYear_returnsFalse() {
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance(2, Year.of(2024));
        assertNotEquals(week1, week2);
    }

    @Test
    public void equals_sameWeekDifferentYear_returnsFalse() {
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance(1, Year.of(2025));
        assertNotEquals(week1, week2);
    }

    @Test
    public void equals_null_returnsFalse() {
        WeeklyAttendance week = new WeeklyAttendance(1, Year.of(2024));
        assertNotEquals(week, null);
    }

    @Test
    public void equals_differentType_returnsFalse() {
        WeeklyAttendance week = new WeeklyAttendance(1, Year.of(2024));
        assertNotEquals(week, "W01-2024");
    }

    @Test
    public void hashCode_sameValues_sameHashCode() {
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance(1, Year.of(2024));
        assertEquals(week1.hashCode(), week2.hashCode());
    }

    @Test
    public void hashCode_differentValues_differentHashCode() {
        WeeklyAttendance week1 = new WeeklyAttendance(1, Year.of(2024));
        WeeklyAttendance week2 = new WeeklyAttendance(2, Year.of(2024));
        assertNotEquals(week1.hashCode(), week2.hashCode());
    }

    // ===== CURRENT WEEK TESTS =====

    @Test
    public void getCurrentWeek_midYearDate_returnsCorrectWeek() {
        // Jan 29, 2024 is Monday of week 5
        Instant instant = Instant.parse("2024-01-29T10:00:00Z");
        Clock clock = Clock.fixed(instant, ZoneId.of("UTC"));
        WeeklyAttendance currentWeek = WeeklyAttendance.getCurrentWeek(clock);
        assertEquals(new WeeklyAttendance(5, Year.of(2024)), currentWeek);
    }

    @Test
    public void getCurrentWeek_firstDayOfYear_handlesCorrectly() {
        // Jan 1, 2024 is Monday, week 1 of 2024
        Instant instant = Instant.parse("2024-01-01T10:00:00Z");
        Clock clock = Clock.fixed(instant, ZoneId.of("UTC"));
        WeeklyAttendance currentWeek = WeeklyAttendance.getCurrentWeek(clock);
        assertEquals(new WeeklyAttendance(1, Year.of(2024)), currentWeek);
    }

    @Test
    public void getCurrentWeek_lastDayOfYear_handlesCorrectly() {
        // Dec 31, 2024 is Tuesday, week 1 of 2025 (ISO week-year boundary)
        Instant instant = Instant.parse("2024-12-31T10:00:00Z");
        Clock clock = Clock.fixed(instant, ZoneId.of("UTC"));
        WeeklyAttendance currentWeek = WeeklyAttendance.getCurrentWeek(clock);
        assertEquals(new WeeklyAttendance(1, Year.of(2025)), currentWeek);
    }

    @Test
    public void getCurrentWeek_yearBoundaryEarlyJanuary_belongsToPreviousYear() {
        // Jan 1, 2023 is Sunday, belongs to week 52 of 2022
        Instant instant = Instant.parse("2023-01-01T10:00:00Z");
        Clock clock = Clock.fixed(instant, ZoneId.of("UTC"));
        WeeklyAttendance currentWeek = WeeklyAttendance.getCurrentWeek(clock);
        assertEquals(new WeeklyAttendance(52, Year.of(2022)), currentWeek);
    }

    @Test
    public void getCurrentWeek_yearBoundaryLateDecember_belongsToNextYear() {
        // Dec 30, 2024 is Monday, week 1 of 2025
        Instant instant = Instant.parse("2024-12-30T10:00:00Z");
        Clock clock = Clock.fixed(instant, ZoneId.of("UTC"));
        WeeklyAttendance currentWeek = WeeklyAttendance.getCurrentWeek(clock);
        assertEquals(new WeeklyAttendance(1, Year.of(2025)), currentWeek);
    }

    @Test
    public void getCurrentWeek_week53Year_handlesCorrectly() {
        // Dec 28, 2020 is Monday, week 53 of 2020
        Instant instant = Instant.parse("2020-12-28T10:00:00Z");
        Clock clock = Clock.fixed(instant, ZoneId.of("UTC"));
        WeeklyAttendance currentWeek = WeeklyAttendance.getCurrentWeek(clock);
        assertEquals(new WeeklyAttendance(53, Year.of(2020)), currentWeek);
    }

    // ===== TO STRING TESTS =====

    @Test
    public void toString_singleDigitWeek_padsWithZero() {
        WeeklyAttendance week = new WeeklyAttendance(5, Year.of(2024));
        assertEquals("W05-2024", week.toString());
    }

    @Test
    public void toString_doubleDigitWeek_noPadding() {
        WeeklyAttendance week = new WeeklyAttendance(15, Year.of(2024));
        assertEquals("W15-2024", week.toString());
    }

    @Test
    public void toString_week01_correctFormat() {
        WeeklyAttendance week = new WeeklyAttendance(1, Year.of(2024));
        assertEquals("W01-2024", week.toString());
    }

    @Test
    public void toString_week52_correctFormat() {
        WeeklyAttendance week = new WeeklyAttendance(52, Year.of(2024));
        assertEquals("W52-2024", week.toString());
    }

    @Test
    public void toString_week53_correctFormat() {
        WeeklyAttendance week = new WeeklyAttendance(53, Year.of(2020));
        assertEquals("W53-2020", week.toString());
    }
}