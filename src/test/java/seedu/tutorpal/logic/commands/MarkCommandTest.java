package seedu.tutorpal.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.tutorpal.commons.core.GuiSettings;
import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.exceptions.CommandException;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.ReadOnlyAddressBook;
import seedu.tutorpal.model.ReadOnlyUserPrefs;
import seedu.tutorpal.model.person.Address;
import seedu.tutorpal.model.person.AttendanceHistory;
import seedu.tutorpal.model.person.Class;
import seedu.tutorpal.model.person.Email;
import seedu.tutorpal.model.person.JoinDate;
import seedu.tutorpal.model.person.Name;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Phone;
import seedu.tutorpal.model.person.Student;
import seedu.tutorpal.model.person.WeeklyAttendance;

/**
 * Tests for MarkCommand.
 * These tests use a minimal ModelStub and real Student/AttendanceHistory instances with a fixed Clock
 * to validate both success and failure paths.
 */
public class MarkCommandTest {

    // -----------------------------
    // Constructor and equality tests
    // -----------------------------

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        WeeklyAttendance week = new WeeklyAttendance("W01-2025");
        assertThrows(NullPointerException.class, () -> new MarkCommand(null, week));
    }

    @Test
    public void constructor_nullWeek_throwsNullPointerException() {
        Index index = Index.fromOneBased(1);
        assertThrows(NullPointerException.class, () -> new MarkCommand(index, null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        MarkCommand cmd = new MarkCommand(Index.fromOneBased(1), new WeeklyAttendance("W01-2025"));
        assertThrows(NullPointerException.class, () -> cmd.execute(null));
    }

    @Test
    public void equals() {
        WeeklyAttendance w1 = new WeeklyAttendance("W01-2025");
        WeeklyAttendance w2 = new WeeklyAttendance("W02-2025");

        MarkCommand cmdA = new MarkCommand(Index.fromOneBased(1), w1);
        MarkCommand cmdACopy = new MarkCommand(Index.fromOneBased(1), w1);
        MarkCommand cmdB = new MarkCommand(Index.fromOneBased(2), w1);
        MarkCommand cmdC = new MarkCommand(Index.fromOneBased(1), w2);

        // same object -> true
        assertTrue(cmdA.equals(cmdA));

        // same values -> true
        assertTrue(cmdA.equals(cmdACopy));

        // null -> false
        assertFalse(cmdA.equals(null));

        // different type -> false
        assertFalse(cmdA.equals("not a command"));

        // different index -> false
        assertFalse(cmdA.equals(cmdB));

        // different week -> false
        assertFalse(cmdA.equals(cmdC));
    }

    @Test
    public void toString_includesIndexAndWeek() {
        WeeklyAttendance week = new WeeklyAttendance("W26-2025");
        MarkCommand cmd = new MarkCommand(Index.fromOneBased(3), week);
        String s = cmd.toString();
        assertTrue(s.contains("W26-2025"));
        assertTrue(s.contains("index"));
        assertTrue(s.contains("week"));
    }

    // -----------------------------
    // Execute-path tests
    // -----------------------------

    @Test
    public void execute_validStudentUnmarkedWeek_marksSuccessfully() throws Exception {
        // Fixed clock so "current week" is within/after W26-2025
        Clock fixedClock = fixedClock(LocalDate.of(2025, 7, 1)); // ~W27-2025

        // Student joined before W26-2025; empty attendance
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 15));
        AttendanceHistory attendance = new AttendanceHistory(joinDate, fixedClock);
        Student student = new Student(
                new Name("Alice"),
                new Phone("98765432"),
                new Email("alice@example.com"),
                new Address("Kent Ridge"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                attendance,
                fixedClock
        );

        WeeklyAttendance week = new WeeklyAttendance("W26-2025");
        ModelStubWithPersons model = new ModelStubWithPersons(student);

        MarkCommand cmd = new MarkCommand(Index.fromOneBased(1), week);
        CommandResult result = cmd.execute(model);

        // Message
        String expectedMessage = String.format(MarkCommand.MESSAGE_SUCCESS, student.getName(), week);
        assertEquals(expectedMessage, result.getFeedbackToUser());

        // Model should be updated with edited person
        assertTrue(model.setCalled);
        Person updated = model.lastEditedPerson;
        assertTrue(updated instanceof Student);

        // Verify attendance really marked
        AttendanceHistory updatedHistory = ((Student) updated).getAttendanceHistory();
        assertTrue(updatedHistory.hasAttended(week));
    }

    @Test
    public void execute_alreadyMarked_throwsCommandException() throws Exception {
        Clock fixedClock = fixedClock(LocalDate.of(2025, 7, 1));
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 15));
        WeeklyAttendance week = new WeeklyAttendance("W26-2025");

        // Pre-mark attendance
        AttendanceHistory history = new AttendanceHistory(joinDate, fixedClock).markAttendance(week);

        Student student = new Student(
                new Name("Bob"),
                new Phone("91234567"),
                new Email("bob@example.com"),
                new Address("Jurong West"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                history,
                fixedClock
        );

        ModelStubWithPersons model = new ModelStubWithPersons(student);
        MarkCommand cmd = new MarkCommand(Index.fromOneBased(1), week);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        // Avoid brittle exact message; ensure it references "already marked"
        assertTrue(ex.getMessage().toLowerCase().contains("already"));
        assertTrue(ex.getMessage().toLowerCase().contains("mark"));
    }

    @Test
    public void execute_weekOutOfRange_throwsCommandException() throws Exception {
        // Fix "now" to a week before the attempted mark (or join too late)
        Clock fixedClock = fixedClock(LocalDate.of(2025, 3, 1)); // ~W09-2025
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 3, 1)); // join ~W09-2025

        // Attempt to mark earlier week (e.g., W05-2025) which is out of range
        WeeklyAttendance outOfRangeWeek = new WeeklyAttendance("W05-2025");

        AttendanceHistory history = new AttendanceHistory(joinDate, fixedClock);
        Student student = new Student(
                new Name("Carol"),
                new Phone("99887766"),
                new Email("carol@example.com"),
                new Address("Clementi"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                history,
                fixedClock
        );

        ModelStubWithPersons model = new ModelStubWithPersons(student);
        MarkCommand cmd = new MarkCommand(Index.fromOneBased(1), outOfRangeWeek);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        // Message should indicate invalid week range
        assertTrue(ex.getMessage().toLowerCase().contains("out of valid range")
                || ex.getMessage().toLowerCase().contains("valid range"));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        // One person in list, but use index=2
        Clock fixedClock = fixedClock(LocalDate.of(2025, 7, 1));
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 1));
        AttendanceHistory history = new AttendanceHistory(joinDate, fixedClock);
        Student student = new Student(
                new Name("Dave"),
                new Phone("92345678"),
                new Email("dave@example.com"),
                new Address("Tampines"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                history,
                fixedClock
        );

        ModelStubWithPersons model = new ModelStubWithPersons(student);
        MarkCommand cmd = new MarkCommand(Index.fromOneBased(2), new WeeklyAttendance("W26-2025"));

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ex.getMessage());
    }

    // -----------------------------
    // Helpers
    // -----------------------------

    private static Clock fixedClock(LocalDate date) {
        ZoneId zone = ZoneId.of("UTC");
        return Clock.fixed(date.atStartOfDay(zone).toInstant(), zone);
    }

    /**
     * A minimal Model stub that holds a small filtered list and supports setPerson.
     */
    private static class ModelStubWithPersons implements Model {

        private final ObservableList<Person> filtered = FXCollections.observableArrayList();
        private boolean setCalled = false;
        private Person lastSetTarget;
        private Person lastEditedPerson;

        ModelStubWithPersons(Person... persons) {
            filtered.addAll(List.of(persons));
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.unmodifiableObservableList(filtered);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            int idx = filtered.indexOf(target);
            if (idx < 0) {
                throw new AssertionError("Target person not in list");
            }
            filtered.set(idx, editedPerson);
            setCalled = true;
            lastSetTarget = target;
            lastEditedPerson = editedPerson;
        }

        @Override public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError();
        }
        @Override public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError();
        }
        @Override public GuiSettings getGuiSettings() {
            throw new AssertionError();
        }
        @Override public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError();
        }
        @Override public java.nio.file.Path getAddressBookFilePath() {
            throw new AssertionError();
        }
        @Override public void setAddressBookFilePath(java.nio.file.Path addressBookFilePath) {
            throw new AssertionError();
        }
        @Override public void setAddressBook(ReadOnlyAddressBook addressBook) {
            throw new AssertionError();
        }
        @Override public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError();
        }
        @Override public boolean hasPerson(Person person) {
            throw new AssertionError();
        }
        @Override public void deletePerson(Person target) {
            throw new AssertionError();
        }
        @Override public void addPerson(Person person) {
            throw new AssertionError();
        }
        @Override public void updateFilteredPersonList(java.util.function.Predicate<Person> predicate) {
            throw new AssertionError();
        }
    }
}
