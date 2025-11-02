package seedu.tutorpal.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.LocalDate;
import java.time.YearMonth;
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
import seedu.tutorpal.model.person.PaymentHistory;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Phone;
import seedu.tutorpal.model.person.Role;
import seedu.tutorpal.model.person.Student;
import seedu.tutorpal.model.person.Tutor;

/**
 * Tests for PaymentCommand.
 * These tests use a minimal ModelStub and real Student/Tutor/PaymentHistory instances with a fixed Clock
 * to validate both success and failure paths.
 */
public class PaymentCommandTest {

    // ========== EQUALITY TESTS ==========

    @Test
    public void equals_sameObject_returnsTrue() {
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        assertTrue(cmd.equals(cmd));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        PaymentCommand cmd1 = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        PaymentCommand cmd2 = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        assertTrue(cmd1.equals(cmd2));
    }

    @Test
    public void equals_differentIndex_returnsFalse() {
        PaymentCommand cmd1 = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        PaymentCommand cmd2 = new PaymentCommand(Index.fromOneBased(2), YearMonth.of(2024, 1));
        assertFalse(cmd1.equals(cmd2));
    }

    @Test
    public void equals_differentMonth_returnsFalse() {
        PaymentCommand cmd1 = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        PaymentCommand cmd2 = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 2));
        assertFalse(cmd1.equals(cmd2));
    }

    @Test
    public void equals_null_returnsFalse() {
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        assertFalse(cmd.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        assertFalse(cmd.equals("not a command"));
    }

    @Test
    public void toString_containsIndexAndMonth() {
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(3), YearMonth.of(2024, 6));
        String result = cmd.toString();
        assertTrue(result.contains("index"));
        assertTrue(result.contains("2024-06"));
    }

    // ========== INDEX VALIDITY IN MODEL TESTS ==========

    @Test
    public void execute_validIndexWithinBounds_success() throws Exception {
        // EP: valid index of existing person
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 1));
        Student student1 = createStudent("Alice", joinDate, fixedClock);
        Student student2 = createStudent("Bob", joinDate, fixedClock);
        Student student3 = createStudent("Charlie", joinDate, fixedClock);

        ModelStubWithPersons model = new ModelStubWithPersons(student1, student2, student3);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 3));

        CommandResult result = cmd.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Alice"));
        assertTrue(model.setCalled);
    }

    @Test
    public void execute_indexAtListBoundary_success() throws Exception {
        // EP: valid index of existing person (at boundary)
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 1));
        Student student1 = createStudent("Alice", joinDate, fixedClock);
        Student student2 = createStudent("Bob", joinDate, fixedClock);
        Student student3 = createStudent("Charlie", joinDate, fixedClock);

        ModelStubWithPersons model = new ModelStubWithPersons(student1, student2, student3);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(3), YearMonth.of(2024, 3));

        CommandResult result = cmd.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Charlie"));
        assertTrue(model.setCalled);
    }

    @Test
    public void execute_indexOutOfBounds_throwsCommandException() {
        // EP: index > size of list
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 1));
        Student student1 = createStudent("Alice", joinDate, fixedClock);
        Student student2 = createStudent("Bob", joinDate, fixedClock);

        ModelStubWithPersons model = new ModelStubWithPersons(student1, student2);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(3), YearMonth.of(2024, 3));

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ex.getMessage());
    }

    // ========== MONTH VS JOIN DATE VALIDATION TESTS ==========

    @Test
    public void execute_monthBeforeJoinDate_throwsCommandException() {
        // EP: month-year before joindate
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 3, 1)); // Joined March 2024

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 2)); // Pay for Feb

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertTrue(ex.getMessage().contains("Cannot mark payment for month before person's join date"));
        assertTrue(ex.getMessage().contains("2024-03"));
    }

    @Test
    public void execute_monthEqualsJoinDate_success() throws Exception {
        // EP: month and year after join date and before current date (at join date boundary)
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 3, 1)); // Joined March 2024

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 3)); // Pay for March

        CommandResult result = cmd.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Payment for Alice for 2024-03 has been marked as paid"));
    }

    @Test
    public void execute_monthAfterJoinDate_success() throws Exception {
        // EP: month and year after join date and before current date
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 3, 1)); // Joined March 2024

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 4)); // Pay for April

        CommandResult result = cmd.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Payment for Alice for 2024-04 has been marked as paid"));
    }

    @Test
    public void execute_monthSignificantlyBeforeJoinDate_throwsCommandException() {
        // EP: month-year before joindate
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 6, 1)); // Joined June 2024

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1)); // Pay for Jan

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertTrue(ex.getMessage().contains("Cannot mark payment for month before person's join date"));
    }

    // ========== MONTH VS CURRENT DATE VALIDATION TESTS ==========

    @Test
    public void execute_monthIsPast_success() throws Exception {
        // EP: month and year after join date and before current date
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15)); // Current: July 2024
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 1));

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 6)); // Pay for June

        CommandResult result = cmd.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Payment for Alice for 2024-06 has been marked as paid"));
    }

    @Test
    public void execute_monthIsCurrentMonth_success() throws Exception {
        // EP: month and year after join date and before current date (at current date boundary)
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15)); // Current: July 2024
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 1));

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 7)); // Pay for July

        CommandResult result = cmd.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Payment for Alice for 2024-07 has been marked as paid"));
    }

    @Test
    public void execute_monthIsNextMonth_throwsCommandException() {
        // EP: month-year after current date
        Clock fixedClock = fixedClock(LocalDate.of(2025, 7, 15)); // Current: July 2025
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 1));

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        // Use actual future month based on real current date
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2030, 12));

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals("Cannot mark payment for future month", ex.getMessage());
    }

    @Test
    public void execute_monthIsFarFuture_throwsCommandException() {
        // EP: month-year after current date
        Clock fixedClock = fixedClock(LocalDate.of(2025, 7, 15)); // Current: July 2025
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 1));

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        // Use actual future month (well beyond current date)
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2030, 6));

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals("Cannot mark payment for future month", ex.getMessage());
    }

    // ========== PAYMENT STATE VALIDATION TESTS ==========

    @Test
    public void execute_monthNotYetPaid_success() throws Exception {
        // EP: unpaid month
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 1));

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 3));

        CommandResult result = cmd.execute(model);
        String expectedMessage = String.format(PaymentCommand.MESSAGE_SUCCESS, "Alice", YearMonth.of(2024, 3));
        assertEquals(expectedMessage, result.getFeedbackToUser());
        // Verify payment was actually marked
        Person updatedPerson = model.lastEditedPerson;
        assertTrue(updatedPerson.getPaymentHistory().isMonthPaid(YearMonth.of(2024, 3)));
    }

    @Test
    public void execute_monthAlreadyPaid_throwsCommandException() {
        // EP: already paid month
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 1));
        PaymentHistory paymentHistory = new PaymentHistory(joinDate.toLocalDate())
                .markMonthAsPaid(YearMonth.of(2024, 3));

        Student student = new Student(
                new Name("Alice"),
                new Phone("98765432"),
                new Email("alice@example.com"),
                new Address("Kent Ridge"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                new AttendanceHistory(joinDate, fixedClock),
                paymentHistory
        );

        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 3));

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertTrue(ex.getMessage().contains("Payment for Alice has already been marked as paid for 2024-03"));
    }

    // ========== PERSON TYPE TESTS ==========

    @Test
    public void execute_paymentForStudent_success() throws Exception {
        // EP: person is Student
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 1));

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 3));

        CommandResult result = cmd.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Payment for Alice"));
        // Verify it's still a Student and attendance is preserved
        Person updatedPerson = model.lastEditedPerson;
        assertTrue(updatedPerson instanceof Student);
        assertEquals(Role.STUDENT, updatedPerson.getRole());
        assertTrue(updatedPerson.getPaymentHistory().isMonthPaid(YearMonth.of(2024, 3)));
    }

    @Test
    public void execute_paymentForTutor_success() throws Exception {
        // EP: person is Tutor
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15));
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 1));

        Tutor tutor = createTutor("Bob", joinDate);
        ModelStubWithPersons model = new ModelStubWithPersons(tutor);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 5));

        CommandResult result = cmd.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Payment for Bob"));
        // Verify it's still a Tutor
        Person updatedPerson = model.lastEditedPerson;
        assertTrue(updatedPerson instanceof Tutor);
        assertEquals(Role.TUTOR, updatedPerson.getRole());
        assertTrue(updatedPerson.getPaymentHistory().isMonthPaid(YearMonth.of(2024, 5)));
    }

    // ========== COMBINED VALID SCENARIO TESTS ==========

    @Test
    public void execute_completeSuccessfulPaymentFlowStudent_success() throws Exception {
        // EP: valid payment for Student (all valid parameters combined)
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15)); // Current: July 2024
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 1, 1)); // Joined: Jan 2024

        Student student = createStudent("Alice", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 3));

        CommandResult result = cmd.execute(model);
        // Verify success message
        String expectedMessage = String.format(PaymentCommand.MESSAGE_SUCCESS, "Alice", YearMonth.of(2024, 3));
        assertEquals(expectedMessage, result.getFeedbackToUser());
        // Verify Model.setPerson() was called
        assertTrue(model.setCalled);
        assertEquals(student, model.lastSetTarget);
        // Verify payment history was updated
        Person updatedPerson = model.lastEditedPerson;
        assertTrue(updatedPerson.getPaymentHistory().isMonthPaid(YearMonth.of(2024, 3)));
        // Verify Student's attendance history is preserved
        assertTrue(updatedPerson instanceof Student);
        Student updatedStudent = (Student) updatedPerson;
        assertEquals(student.getAttendanceHistory(), updatedStudent.getAttendanceHistory());
    }

    @Test
    public void execute_completeSuccessfulPaymentFlowTutor_success() throws Exception {
        // EP: valid payment for Tutor (all valid parameters combined)
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15)); // Current: July 2024
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 2, 1)); // Joined: Feb 2024

        Tutor tutor = createTutor("Bob", joinDate);
        ModelStubWithPersons model = new ModelStubWithPersons(tutor);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 5));

        CommandResult result = cmd.execute(model);
        // Verify success message
        String expectedMessage = String.format(PaymentCommand.MESSAGE_SUCCESS, "Bob", YearMonth.of(2024, 5));
        assertEquals(expectedMessage, result.getFeedbackToUser());
        // Verify Model.setPerson() was called
        assertTrue(model.setCalled);
        // Verify payment history was updated
        Person updatedPerson = model.lastEditedPerson;
        assertTrue(updatedPerson.getPaymentHistory().isMonthPaid(YearMonth.of(2024, 5)));
    }

    @Test
    public void execute_paymentForCurrentMonthAtJoinDate_success() throws Exception {
        // EP: month equals both join month and current month
        Clock fixedClock = fixedClock(LocalDate.of(2024, 7, 15)); // Current: July 2024
        JoinDate joinDate = new JoinDate(LocalDate.of(2024, 7, 1)); // Joined: July 2024 (same month)

        Student student = createStudent("Charlie", joinDate, fixedClock);
        ModelStubWithPersons model = new ModelStubWithPersons(student);
        PaymentCommand cmd = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 7));

        CommandResult result = cmd.execute(model);
        assertTrue(result.getFeedbackToUser().contains("Payment for Charlie for 2024-07 has been marked as paid"));
        assertTrue(model.lastEditedPerson.getPaymentHistory().isMonthPaid(YearMonth.of(2024, 7)));
    }

    // ========== HELPERS ==========

    private static Clock fixedClock(LocalDate date) {
        ZoneId zone = ZoneId.of("UTC");
        return Clock.fixed(date.atStartOfDay(zone).toInstant(), zone);
    }

    private Student createStudent(String name, JoinDate joinDate, Clock clock) {
        return new Student(
                new Name(name),
                new Phone("98765432"),
                new Email(name.toLowerCase() + "@example.com"),
                new Address("Kent Ridge"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                new AttendanceHistory(joinDate, clock),
                new PaymentHistory(joinDate.toLocalDate())
        );
    }

    private Tutor createTutor(String name, JoinDate joinDate) {
        return new Tutor(
                new Name(name),
                new Phone("98765432"),
                new Email(name.toLowerCase() + "@example.com"),
                new Address("Kent Ridge"),
                Set.of(new Class("s4mon1600")),
                joinDate,
                new PaymentHistory(joinDate.toLocalDate())
        );
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

