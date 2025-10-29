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

public class UnmarkCommandTest {

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        UnmarkCommand cmd = new UnmarkCommand(Index.fromOneBased(1), new WeeklyAttendance("W01-2025"));
        assertThrows(NullPointerException.class, () -> cmd.execute(null));
    }

    @Test
    public void equals() {
        WeeklyAttendance w1 = new WeeklyAttendance("W01-2025");
        WeeklyAttendance w2 = new WeeklyAttendance("W02-2025");

        UnmarkCommand a = new UnmarkCommand(Index.fromOneBased(1), w1);
        UnmarkCommand aCopy = new UnmarkCommand(Index.fromOneBased(1), w1);
        UnmarkCommand b = new UnmarkCommand(Index.fromOneBased(2), w1);
        UnmarkCommand c = new UnmarkCommand(Index.fromOneBased(1), w2);

        assertTrue(a.equals(a));
        assertTrue(a.equals(aCopy));
        assertFalse(a.equals(null));
        assertFalse(a.equals("x"));
        assertFalse(a.equals(b));
        assertFalse(a.equals(c));
    }

    @Test
    public void toString_includesIndexAndWeek() {
        WeeklyAttendance week = new WeeklyAttendance("W26-2025");
        UnmarkCommand cmd = new UnmarkCommand(Index.fromOneBased(3), week);
        String s = cmd.toString();
        assertTrue(s.contains("index"));
        assertTrue(s.contains("week"));
        assertTrue(s.contains("W26-2025"));
    }

    @Test
    public void execute_markedWeek_unmarksSuccessfully() throws Exception {
        Clock fixedClock = fixedClock(LocalDate.of(2025, 7, 1)); // around W27-2025
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 15));
        WeeklyAttendance week = new WeeklyAttendance("W26-2025");

        AttendanceHistory history = new AttendanceHistory(joinDate, fixedClock).markAttendance(week);
        Student student = new Student(
                new Name("Alice"),
                new Phone("98765432"),
                new Email("alice@example.com"),
                new Address("Kent Ridge"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                history
        );

        ModelStubWithPersons model = new ModelStubWithPersons(student);

        CommandResult result = new UnmarkCommand(Index.fromOneBased(1), week).execute(model);
        String expected = String.format(UnmarkCommand.MESSAGE_SUCCESS, student.getName(), week);
        assertEquals(expected, result.getFeedbackToUser());

        Person updated = model.lastEditedPerson;
        AttendanceHistory updatedHistory = ((Student) updated).getAttendanceHistory();
        assertFalse(updatedHistory.hasBeenMarked(week));
    }

    @Test
    public void execute_notMarked_throwsCommandException() {
        Clock fixedClock = fixedClock(LocalDate.of(2025, 7, 1));
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 15));
        WeeklyAttendance week = new WeeklyAttendance("W26-2025");

        AttendanceHistory history = new AttendanceHistory(joinDate, fixedClock);
        Student student = new Student(
                new Name("Bob"),
                new Phone("91234567"),
                new Email("bob@example.com"),
                new Address("Jurong West"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                history
        );

        ModelStubWithPersons model = new ModelStubWithPersons(student);
        UnmarkCommand cmd = new UnmarkCommand(Index.fromOneBased(1), week);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertTrue(ex.getMessage().toLowerCase().contains("not marked"));
    }

    @Test
    public void execute_weekOutOfRange_throwsCommandException() {
        Clock fixedClock = fixedClock(LocalDate.of(2025, 3, 1)); // ~W09-2025
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 3, 1)); // join ~W09-2025
        WeeklyAttendance outOfRange = new WeeklyAttendance("W05-2025");

        AttendanceHistory history = new AttendanceHistory(joinDate, fixedClock);
        Student student = new Student(
                new Name("Carol"),
                new Phone("99887766"),
                new Email("carol@example.com"),
                new Address("Clementi"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                history
        );

        ModelStubWithPersons model = new ModelStubWithPersons(student);
        UnmarkCommand cmd = new UnmarkCommand(Index.fromOneBased(1), outOfRange);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertTrue(ex.getMessage().toLowerCase().contains("valid range"));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
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
                history
        );

        ModelStubWithPersons model = new ModelStubWithPersons(student);
        UnmarkCommand cmd = new UnmarkCommand(Index.fromOneBased(2), new WeeklyAttendance("W26-2025"));

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ex.getMessage());
    }

    private static Clock fixedClock(LocalDate date) {
        ZoneId zone = ZoneId.of("UTC");
        return Clock.fixed(date.atStartOfDay(zone).toInstant(), zone);
    }

    private static class ModelStubWithPersons implements Model {
        private final ObservableList<Person> filtered = FXCollections.observableArrayList();
        private boolean setCalled = false;
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
            lastEditedPerson = editedPerson;
        }

        // Unused methods
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
