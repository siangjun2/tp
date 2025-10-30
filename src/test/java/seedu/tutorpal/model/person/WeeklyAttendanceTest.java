package seedu.tutorpal.model.person;

import static java.util.Objects.requireNonNull;
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
import java.time.LocalDate;
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
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance(""));
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

    @Test
    public void constructor_year0000_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new WeeklyAttendance("W01-0000"));
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
        assertFalse(isValidWeeklyAttendance(""));
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
    public void getNumberOfWeeksInIsoYear52WeekYears_returns52() {
        assertEquals(52, getNumberOfWeeksInIsoYear(2024));
        assertEquals(52, getNumberOfWeeksInIsoYear(2023));
        assertEquals(52, getNumberOfWeeksInIsoYear(2022));
        assertEquals(52, getNumberOfWeeksInIsoYear(2019));
    }

    @Test
    public void getNumberOfWeeksInIsoYear53WeekYears_returns53() {
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
        requireNonNull(week);
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

    @Test
    public void at_typicalDate_success() {
        // 2025-02-10 is a Monday in ISO week 7 of 2025
        LocalDate date = LocalDate.of(2025, 2, 10);
        WeeklyAttendance result = WeeklyAttendance.at(date);
        assertEquals("W07-2025", result.toString());
    }

    @Test
    public void at_startOfYearWeekOverlap_success() {
        // 2021-01-01 (Friday) is still part of ISO week 53 of 2020
        LocalDate date = LocalDate.of(2021, 1, 1);
        WeeklyAttendance result = WeeklyAttendance.at(date);
        assertEquals("W53-2020", result.toString());
    }

    @Test
    public void at_firstMondayOfYear_success() {
        // 2024-01-01 (Monday) is ISO week 1 of 2024
        LocalDate date = LocalDate.of(2024, 1, 1);
        WeeklyAttendance result = WeeklyAttendance.at(date);
        assertEquals("W01-2024", result.toString());
    }

    @Test
    public void at_endOfYearWeekOverlap_success() {
        // 2020-12-31 (Thursday) belongs to ISO week 53 of 2020
        LocalDate date = LocalDate.of(2020, 12, 31);
        WeeklyAttendance result = WeeklyAttendance.at(date);
        assertEquals("W53-2020", result.toString());
    }

    @Test
    public void at_firstWeekOfNextYear_success() {
        // 2021-01-04 (Monday) is ISO week 1 of 2021
        LocalDate date = LocalDate.of(2021, 1, 4);
        WeeklyAttendance result = WeeklyAttendance.at(date);
        assertEquals("W01-2021", result.toString());
    }

    @Test
    public void at_middleOfYear_success() {
        // 2023-06-15 (Thursday) → ISO week 24 of 2023
        LocalDate date = LocalDate.of(2023, 6, 15);
        WeeklyAttendance result = WeeklyAttendance.at(date);
        assertEquals("W24-2023", result.toString());
    }

    @Test
    public void at_leapYearDate_success() {
        // 2020-02-29 (Saturday) is ISO week 9 of 2020
        LocalDate date = LocalDate.of(2020, 2, 29);
        WeeklyAttendance result = WeeklyAttendance.at(date);
        assertEquals("W09-2020", result.toString());
    }

    @Test
    public void at_consistencyWithConstructor_success() {
        LocalDate date = LocalDate.of(2025, 3, 12);
        WeeklyAttendance fromMethod = WeeklyAttendance.at(date);

        int expectedWeek = date.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int expectedYear = date.get(java.time.temporal.IsoFields.WEEK_BASED_YEAR);
        WeeklyAttendance expected = new WeeklyAttendance(expectedWeek, Year.of(expectedYear));

        assertEquals(expected, fromMethod);
    }

    // ===== ISO EDGE CASES =====

    @Test
    public void at_allDaysOfSameIsoWeek_mapToSameWeek() {
        // 2024-03-04 (Mon) .. 2024-03-10 (Sun) are all ISO week 10 of 2024
        WeeklyAttendance expected = new WeeklyAttendance(10, Year.of(2024));
        // Monday
        assertEquals(expected, WeeklyAttendance.at(LocalDate.of(2024, 3, 4)));
        // Tuesday
        assertEquals(expected, WeeklyAttendance.at(LocalDate.of(2024, 3, 5)));
        // Wednesday
        assertEquals(expected, WeeklyAttendance.at(LocalDate.of(2024, 3, 6)));
        assertEquals(expected, WeeklyAttendance.at(LocalDate.of(2024, 3, 7)));
        // Thursday
        // Friday
        assertEquals(expected, WeeklyAttendance.at(LocalDate.of(2024, 3, 8)));
        // Saturday
        assertEquals(expected, WeeklyAttendance.at(LocalDate.of(2024, 3, 9)));
        // Sunday
        assertEquals(expected, WeeklyAttendance.at(LocalDate.of(2024, 3, 10)));
    }

    @Test
    public void at_firstThursdayRule_boundaries() {
        // 2016-01-03 (Sun) is ISO week 53 of 2015
        assertEquals(new WeeklyAttendance(53, Year.of(2015)), WeeklyAttendance.at(LocalDate.of(2016, 1, 3)));
        // 2016-01-04 (Mon) starts ISO week 1 of 2016
        assertEquals(new WeeklyAttendance(1, Year.of(2016)), WeeklyAttendance.at(LocalDate.of(2016, 1, 4)));

        // 2015-01-01 (Thu) is ISO week 1 of 2015
        assertEquals(new WeeklyAttendance(1, Year.of(2015)), WeeklyAttendance.at(LocalDate.of(2015, 1, 1)));
        // 2010-01-01 (Fri) is ISO week 53 of 2009
        assertEquals(new WeeklyAttendance(53, Year.of(2009)), WeeklyAttendance.at(LocalDate.of(2010, 1, 1)));
    }

    @Test
    public void at_yearEndWeekToNextYearWeek1() {
        // 2014-12-29 (Mon) belongs to ISO week 1 of 2015
        assertEquals(new WeeklyAttendance(1, Year.of(2015)), WeeklyAttendance.at(LocalDate.of(2014, 12, 29)));
        // 2015-12-31 (Thu) belongs to ISO week 53 of 2015
        assertEquals(new WeeklyAttendance(53, Year.of(2015)), WeeklyAttendance.at(LocalDate.of(2015, 12, 31)));
        // 2021-01-01 (Fri) belongs to ISO week 53 of 2020
        assertEquals(new WeeklyAttendance(53, Year.of(2020)), WeeklyAttendance.at(LocalDate.of(2021, 1, 1)));
    }

    @Test
    public void isValidWeeklyAttendance_extremeYears_respectsIsoWeekCount() {
        int weeksYear1 = getNumberOfWeeksInIsoYear(1);
        assertTrue(isValidWeeklyAttendance(String.format("W%02d-0001", weeksYear1)));
        assertFalse(isValidWeeklyAttendance(String.format("W%02d-0001", weeksYear1 + 1)));

        int weeks9999 = getNumberOfWeeksInIsoYear(9999);
        assertTrue(isValidWeeklyAttendance(String.format("W%02d-9999", weeks9999)));
        assertFalse(isValidWeeklyAttendance(String.format("W%02d-9999", weeks9999 + 1)));

        // Also ensure lower bound is valid for extreme years
        assertTrue(isValidWeeklyAttendance("W01-0001"));
        assertTrue(isValidWeeklyAttendance("W01-9999"));
    }

    @Test
    public void isValidWeeklyAttendance_moreNon53Years_returnFalse() {
        assertFalse(isValidWeeklyAttendance("W53-2021")); // 2021 has 52 weeks
        assertFalse(isValidWeeklyAttendance("W53-2022")); // 2022 has 52 weeks
    }

    @Test
    public void comparison_across53WeekBoundary() {
        WeeklyAttendance w532015 = new WeeklyAttendance(53, Year.of(2015));
        WeeklyAttendance w12016 = new WeeklyAttendance(1, Year.of(2016));

        assertTrue(w532015.isBefore(w12016));
        assertTrue(w12016.isAfter(w532015));
    }

    @Test
    public void getCurrentWeek_matchesAtForSameClock_onBoundaries() {
        // 2016-01-03 (Sun) -> W53-2015
        Clock c1 = Clock.fixed(Instant.parse("2016-01-03T10:00:00Z"), ZoneId.of("UTC"));
        assertEquals(WeeklyAttendance.at(LocalDate.of(2016, 1, 3)), WeeklyAttendance.getCurrentWeek(c1));

        // 2016-01-04 (Mon) -> W01-2016
        Clock c2 = Clock.fixed(Instant.parse("2016-01-04T10:00:00Z"), ZoneId.of("UTC"));
        assertEquals(WeeklyAttendance.at(LocalDate.of(2016, 1, 4)), WeeklyAttendance.getCurrentWeek(c2));

        // 2014-12-29 (Mon) -> W01-2015
        Clock c3 = Clock.fixed(Instant.parse("2014-12-29T10:00:00Z"), ZoneId.of("UTC"));
        assertEquals(WeeklyAttendance.at(LocalDate.of(2014, 12, 29)), WeeklyAttendance.getCurrentWeek(c3));
    }

    @Test
    void getNumberOfWeeksInIsoYear_knownYears() {
        assertEquals(53, WeeklyAttendance.getNumberOfWeeksInIsoYear(2015)); // Thu start
        assertEquals(53, WeeklyAttendance.getNumberOfWeeksInIsoYear(2020)); // Leap, Wed start
        assertEquals(52, WeeklyAttendance.getNumberOfWeeksInIsoYear(2021));
        assertEquals(52, WeeklyAttendance.getNumberOfWeeksInIsoYear(2024));
        assertEquals(53, WeeklyAttendance.getNumberOfWeeksInIsoYear(2026)); // Thu start
    }

    @Test
    void at_handlesYearBoundaries_noDaysLost() {
        // 2020 had 53 weeks; year-end days map correctly
        WeeklyAttendance d1 = WeeklyAttendance.at(LocalDate.of(2020, 12, 31));
        assertEquals("W53-2020", d1.toString());

        // Jan 1–3, 2021 are still W53-2020
        assertEquals("W53-2020", WeeklyAttendance.at(LocalDate.of(2021, 1, 1)).toString());
        assertEquals("W53-2020", WeeklyAttendance.at(LocalDate.of(2021, 1, 3)).toString());

        // First ISO week of 2021 starts on Jan 4
        assertEquals("W01-2021", WeeklyAttendance.at(LocalDate.of(2021, 1, 4)).toString());
    }

    @Test
    void validation_respects53WeekYearsOnlyWhenApplicable() {
        assertTrue(WeeklyAttendance.isValidWeeklyAttendance("W53-2020"));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W53-2021"));
        assertTrue(WeeklyAttendance.isValidWeeklyAttendance("w01-0001")); // case-insensitive, min year
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W00-2020"));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W54-2020"));
        assertFalse(WeeklyAttendance.isValidWeeklyAttendance("W01-0000")); // 0000 disallowed
    }

    @Test
    void ordering_beforeAfter() {
        WeeklyAttendance w201553 = new WeeklyAttendance("W53-2015");
        WeeklyAttendance w201601 = new WeeklyAttendance("W01-2016");
        assertTrue(w201553.isBefore(w201601));
        assertTrue(w201601.isAfter(w201553));
    }

    @Test
    void getCurrentWeek_withFixedClock() {
        Clock fixed = Clock.fixed(Instant.parse("2021-01-02T12:00:00Z"), ZoneId.of("UTC"));
        WeeklyAttendance cur = WeeklyAttendance.getCurrentWeek(fixed);
        assertEquals("W53-2020", cur.toString());
    }
}
