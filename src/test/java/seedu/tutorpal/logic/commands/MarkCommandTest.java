package seedu.tutorpal.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tutorpal.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.tutorpal.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tutorpal.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.tutorpal.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.tutorpal.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.ModelManager;
import seedu.tutorpal.model.UserPrefs;
import seedu.tutorpal.model.person.AttendanceHistory;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.WeeklyAttendance;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * MarkCommand.
 */
public class MarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());


    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        WeeklyAttendance week = new WeeklyAttendance("W1-01-2025");
        MarkCommand markCommand = new MarkCommand(outOfBoundIndex, week);

        assertCommandFailure(markCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_markTutor_throwsCommandException() {
        // Find a tutor in the list
        Person tutor = null;
        Index tutorIndex = null;
        for (int i = 0; i < model.getFilteredPersonList().size(); i++) {
            Person person = model.getFilteredPersonList().get(i);
            if (person.getRole().toString().equalsIgnoreCase("tutor")) {
                tutor = person;
                tutorIndex = Index.fromZeroBased(i);
                break;
            }
        }

        // If no tutor found, create one or skip test
        if (tutor == null || tutorIndex == null) {
            // Skip test if no tutor in typical persons
            return;
        }

        WeeklyAttendance week = new WeeklyAttendance("W1-01-2025");
        MarkCommand markCommand = new MarkCommand(tutorIndex, week);

        assertCommandFailure(markCommand, model, "Cannot mark attendance for a tutor.");
    }

    @Test
    public void equals() {
        WeeklyAttendance week1 = new WeeklyAttendance("W1-01-2025");
        WeeklyAttendance week2 = new WeeklyAttendance("W2-01-2025");

        MarkCommand markFirstCommand = new MarkCommand(INDEX_FIRST_PERSON, week1);
        MarkCommand markSecondCommand = new MarkCommand(INDEX_SECOND_PERSON, week1);
        MarkCommand markDifferentWeekCommand = new MarkCommand(INDEX_FIRST_PERSON, week2);

        // same object -> returns true
        assertTrue(markFirstCommand.equals(markFirstCommand));

        // same values -> returns true
        MarkCommand markFirstCommandCopy = new MarkCommand(INDEX_FIRST_PERSON, week1);
        assertTrue(markFirstCommand.equals(markFirstCommandCopy));

        // different types -> returns false
        assertFalse(markFirstCommand.equals(1));

        // null -> returns false
        assertFalse(markFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(markFirstCommand.equals(markSecondCommand));

        // different week -> returns false
        assertFalse(markFirstCommand.equals(markDifferentWeekCommand));
    }

    @Test
    public void toStringMethod() {
        WeeklyAttendance week = new WeeklyAttendance("W1-01-2025");
        MarkCommand markCommand = new MarkCommand(INDEX_FIRST_PERSON, week);
        String expected = MarkCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON
                + ", week=" + week + "}";
        assertEquals(expected, markCommand.toString());
    }

    /**
     * Creates a new Person with marked attendance.
     * This is a helper method to simulate what MarkCommand.execute() does.
     */
    private Person createMarkedPerson(Person personToMark, WeeklyAttendance week) {
        // Create a copy of the attendance history
        AttendanceHistory newAttendanceHistory = new AttendanceHistory(personToMark.getAttendanceHistory());

        try {
            newAttendanceHistory.markAttendance(week);
        } catch (IllegalArgumentException e) {
            // Should not happen in tests
            throw new AssertionError("Week should not be already marked in test setup", e);
        }

        return new Person(
                personToMark.getName(),
                personToMark.getPhone(),
                personToMark.getEmail(),
                personToMark.getRole(),
                personToMark.getAddress(),
                personToMark.getClasses(),
                personToMark.getJoinMonth(),
                newAttendanceHistory,
                personToMark.getPaymentHistory());
    }
}
