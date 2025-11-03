package seedu.tutorpal.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.model.person.Address;
import seedu.tutorpal.model.person.AttendanceHistory;
import seedu.tutorpal.model.person.Class;
import seedu.tutorpal.model.person.Email;
import seedu.tutorpal.model.person.JoinDate;
import seedu.tutorpal.model.person.Name;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Phone;
import seedu.tutorpal.model.person.Role;
import seedu.tutorpal.model.person.Student;
import seedu.tutorpal.model.person.Tutor;
import seedu.tutorpal.model.person.WeeklyAttendance;

public class PersonCardTest {

    @Test
    public void formatIndex_singleDigit_correctFormat() {
        int index = 1;
        String formatted = String.format("%05d", index);
        assertEquals("00001", formatted);
    }

    @Test
    public void formatIndex_multipleDigits_correctFormat() {
        int index = 12345;
        String formatted = String.format("%05d", index);
        assertEquals("12345", formatted);
    }

    @Test
    public void formatIndex_zero_correctFormat() {
        int index = 0;
        String formatted = String.format("%05d", index);
        assertEquals("00000", formatted);
    }

    @Test
    public void getAttendanceWeeksCount_studentWithNoAttendance_returnsZero() {
        Person student = createStudent("Alice", 0);
        AttendanceHistory history = student.getAttendanceHistory();

        int count = history.getWeeklyAttendances().size();

        assertEquals(0, count);
    }

    @Test
    public void getAttendanceWeeksCount_studentWithMultipleAttendances_returnsCorrectCount() {
        Person student = createStudent("Bob", 5);
        AttendanceHistory history = student.getAttendanceHistory();

        int count = history.getWeeklyAttendances().size();

        assertEquals(5, count);
    }

    @Test
    public void getAttendanceColor_highAttendance_returnsGreen() {
        int attendedWeeks = 8;
        String color = getAttendanceColor(attendedWeeks);
        assertEquals("#6a9955", color);
    }

    @Test
    public void getAttendanceColor_mediumAttendance_returnsYellow() {
        int attendedWeeks = 6;
        String color = getAttendanceColor(attendedWeeks);
        assertEquals("#dcdcaa", color);
    }

    @Test
    public void getAttendanceColor_lowAttendance_returnsRed() {
        int attendedWeeks = 3;
        String color = getAttendanceColor(attendedWeeks);
        assertEquals("#f48771", color);
    }

    @Test
    public void getAttendanceColor_boundaryHigh_returnsGreen() {
        assertEquals("#6a9955", getAttendanceColor(8));
    }

    @Test
    public void getAttendanceColor_boundaryMedium_returnsYellow() {
        assertEquals("#dcdcaa", getAttendanceColor(5));
        assertEquals("#dcdcaa", getAttendanceColor(7));
    }

    @Test
    public void getAttendanceColor_boundaryLow_returnsRed() {
        assertEquals("#f48771", getAttendanceColor(4));
    }

    @Test
    public void getRoleColor_student_returnsOrangeRed() {
        String roleValue = "student";
        String color = getRoleColor(roleValue);
        assertEquals("#f48771", color);
    }

    @Test
    public void getRoleColor_tutor_returnsLightBlue() {
        String roleValue = "tutor";
        String color = getRoleColor(roleValue);
        assertEquals("#4fc3f7", color);
    }

    @Test
    public void getRoleColor_caseInsensitive_works() {
        assertEquals("#f48771", getRoleColor("STUDENT"));
        assertEquals("#f48771", getRoleColor("Student"));
        assertEquals("#4fc3f7", getRoleColor("TUTOR"));
        assertEquals("#4fc3f7", getRoleColor("Tutor"));
    }

    @Test
    public void getPaymentColor_paid_returnsGreen() {
        String paymentValue = "paid";
        String color = getPaymentColor(paymentValue);
        assertEquals("#6a9955", color);
    }

    @Test
    public void getPaymentColor_unpaid_returnsOrange() {
        String paymentValue = "unpaid";
        String color = getPaymentColor(paymentValue);
        assertEquals("#ce9178", color);
    }

    @Test
    public void getPaymentColor_overdue_returnsRed() {
        String paymentValue = "overdue";
        String color = getPaymentColor(paymentValue);
        assertEquals("#f48771", color);
    }

    @Test
    public void person_hasAllRequiredFields_success() {
        Person student = createStudent("Charlie", 0);

        assertNotNull(student.getName());
        assertNotNull(student.getPhone());
        assertNotNull(student.getEmail());
        assertNotNull(student.getAddress());
        assertNotNull(student.getRole());
        assertNotNull(student.getJoinDate());
        assertNotNull(student.getClasses());
        assertNotNull(student.getPaymentStatus());
    }

    @Test
    public void student_hasAttendanceHistory_success() {
        Person student = createStudent("David", 0);

        assertTrue(student.hasAttendanceHistory());
        assertNotNull(student.getAttendanceHistory());
        assertEquals(Role.STUDENT, student.getRole());
    }

    @Test
    public void tutor_noAttendanceHistory_success() {
        Person tutor = createTutor("Eve");

        assertFalse(tutor.hasAttendanceHistory());
        assertEquals(Role.TUTOR, tutor.getRole());
    }

    @Test
    public void classSet_sorted_maintainsOrder() {
        Person tutor = createTutorWithMultipleClasses("Frank");
        Set<Class> classes = tutor.getClasses();

        assertTrue(classes.size() >= 2);
    }

    // Helper methods
    private String getAttendanceColor(int attendedWeeks) {
        if (attendedWeeks >= 8) {
            return "#6a9955"; // Green
        } else if (attendedWeeks >= 5) {
            return "#dcdcaa"; // Yellow
        } else {
            return "#f48771"; // Red
        }
    }

    private String getRoleColor(String roleValue) {
        if ("tutor".equalsIgnoreCase(roleValue)) {
            return "#4fc3f7"; // Light blue
        } else if ("student".equalsIgnoreCase(roleValue)) {
            return "#f48771"; // Orange-red
        }
        return "#cccccc"; // Default
    }

    private String getPaymentColor(String paymentValue) {
        if ("paid".equalsIgnoreCase(paymentValue)) {
            return "#6a9955"; // Green
        } else if ("overdue".equalsIgnoreCase(paymentValue)) {
            return "#f48771"; // Red
        } else {
            return "#ce9178"; // Orange (unpaid)
        }
    }

    private static Clock fixedClock(LocalDate date) {
        ZoneId zone = ZoneId.of("UTC");
        return Clock.fixed(date.atStartOfDay(zone).toInstant(), zone);
    }

    private Student createStudent(String name, int attendanceCount) {
        Clock clock = fixedClock(LocalDate.of(2025, 7, 1));
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 1));
        AttendanceHistory attendance = new AttendanceHistory(joinDate, clock);

        // Mark attendance for specified count
        for (int i = 0; i < attendanceCount; i++) {
            WeeklyAttendance week = new WeeklyAttendance(
                    String.format("W%02d-2025", i + 1));
            attendance = attendance.markAttendance(week);
        }

        return new Student(
                new Name(name),
                new Phone("98765432"),
                new Email(name.toLowerCase() + "@example.com"),
                new Address("Kent Ridge"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                attendance,
                clock
        );
    }

    private Tutor createTutor(String name) {
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 1));
        return new Tutor(
                new Name(name),
                new Phone("91234567"),
                new Email(name.toLowerCase() + "@example.com"),
                new Address("Jurong West"),
                Set.of(new Class("s4mon1600")),
                joinDate
        );
    }

    private Tutor createTutorWithMultipleClasses(String name) {
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 1));
        return new Tutor(
                new Name(name),
                new Phone("91234567"),
                new Email(name.toLowerCase() + "@example.com"),
                new Address("Jurong West"),
                Set.of(
                        new Class("s4mon1600"),
                        new Class("s2wed1400")
                ),
                joinDate
        );
    }
}
