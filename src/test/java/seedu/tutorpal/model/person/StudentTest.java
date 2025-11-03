package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.testutil.PersonBuilder;

public class StudentTest {

    private static final DateTimeFormatter JD_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter JOIN_FMT = DateTimeFormatter.ofPattern("dd-MM-uuuu");

    private static Set<Class> classSet(String... ids) {
        Set<Class> set = new HashSet<>();
        Arrays.stream(ids).forEach(id -> set.add(new Class(id)));
        return set;
    }

    private static String jd(LocalDate date) {
        return JD_FMT.format(date);
    }

    private static String fmt(LocalDate date) {
        return JOIN_FMT.format(date);
    }

    private static Student makeStudent(Set<Class> classes, JoinDate joinDate) {
        return new Student(
                new Name("Amy Bee"),
                new Phone("85355255"),
                new Email("amy@gmail.com"),
                new Address("123, Jurong West Ave 6, #08-111"),
                classes,
                joinDate);
    }

    private static Student makeStudentWithAttendance(Set<Class> classes, JoinDate joinDate, AttendanceHistory ah) {
        return new Student(
                new Name("Amy Bee"),
                new Phone("85355255"),
                new Email("amy@gmail.com"),
                new Address("123, Jurong West Ave 6, #08-111"),
                classes,
                joinDate,
                ah);
    }

    @Test
    void getRole_and_attendanceFlags() {
        LocalDate today = LocalDate.now();
        JoinDate joinDate = new JoinDate(jd(today.minusWeeks(12)));
        Student s = makeStudent(classSet("s4mon1600"), joinDate);

        assertEquals(Role.STUDENT, s.getRole());
        assertTrue(s.hasAttendanceHistory());
        assertTrue(s.getAttendanceHistory() != null);
    }

    @Test
    void validateClassSize_throwsWhenNotExactlyOne() {
        LocalDate today = LocalDate.now();
        JoinDate joinDate = new JoinDate(jd(today.minusWeeks(4)));

        // 0 classes
        assertThrows(IllegalArgumentException.class, () -> makeStudent(classSet(), joinDate));

        // >1 classes
        assertThrows(IllegalArgumentException.class, () -> makeStudent(classSet("s4mon1600", "s4tue1600"), joinDate));
    }

    @Test
    void validateJoinDateSync_throwsWhenMismatched() {
        LocalDate today = LocalDate.now();
        JoinDate studentJoin = new JoinDate(jd(today.minusWeeks(5)));
        JoinDate attendanceJoin = new JoinDate(jd(today.minusWeeks(6)));
        AttendanceHistory ah = new AttendanceHistory(attendanceJoin);

        assertThrows(IllegalArgumentException.class, ()
                -> makeStudentWithAttendance(classSet("s4mon1600"), studentJoin, ah));
    }

    @Test
    void printAttendanceHistory_fullWindow_currentWeekMarkedPresent() {
        LocalDate today = LocalDate.now();
        // Ensure join date is well before the last 10 weeks so we get 10 tokens
        JoinDate joinDate = new JoinDate(jd(today.minusWeeks(20)));
        AttendanceHistory ah = new AttendanceHistory(joinDate);

        WeeklyAttendance currentWeek = WeeklyAttendance.of(today);
        ah = ah.markAttendance(currentWeek);

        Student s = makeStudentWithAttendance(classSet("s4mon1600"), joinDate, ah);

        String out = s.printAttendanceHistory();
        String[] tokens = Arrays.stream(out.trim().split("\\s+"))
                .filter(t -> !t.isBlank())
                .toArray(String[]::new);

        // Should always list 10 weeks when joined more than 10 weeks ago
        assertEquals(10, tokens.length);

        int thisWeekIndex = today.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        String expectedCurrentToken = "W" + thisWeekIndex + ":present";

        boolean foundCurrentWeek = Arrays.stream(tokens).anyMatch(t -> t.equals(expectedCurrentToken));
        assertTrue(foundCurrentWeek, "Expected current week token present: " + expectedCurrentToken);
    }

    @Test
    void equalsAndHashCode_considerAttendanceHistory() {
        LocalDate today = LocalDate.now();
        JoinDate joinDate = new JoinDate(jd(today.minusWeeks(12)));

        AttendanceHistory base = new AttendanceHistory(joinDate);

        Student s1 = makeStudentWithAttendance(classSet("s4mon1600"), joinDate, base);
        Student s2 = makeStudentWithAttendance(classSet("s4mon1600"), joinDate, base);

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        // Change attendance -> should not be equal
        WeeklyAttendance currentWeek = WeeklyAttendance.of(today);
        AttendanceHistory changed = base.markAttendance(currentWeek);

        Student s3 = makeStudentWithAttendance(classSet("s4mon1600"), joinDate, changed);
        assertNotEquals(s1, s3);
    }

    @Test
    public void constructor_invalidClassSize_throwsIllegalArgumentException() {
        // Two classes for a student should fail
        assertThrows(IllegalArgumentException.class, () -> new PersonBuilder()
                .withRole("student")
                .withClasses("s4mon1600", "s4wed1400")
                .build());
    }

    @Test
    public void displayInfo_includesAttendanceSummary() {
        LocalDate joinDate = LocalDate.now(ZoneId.systemDefault()).minusWeeks(1);
        Student student = (Student) new PersonBuilder()
                .withRole("student")
                .withJoinDate(fmt(joinDate))
                .build();

        String info = student.displayInfo();
        // Attendance summary line should include week labels like "Wxx"
        assertTrue(info.contains("W"));
    }
}
