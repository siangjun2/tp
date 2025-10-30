package seedu.tutorpal.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tutorpal.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tutorpal.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.tutorpal.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.tutorpal.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.ModelManager;
import seedu.tutorpal.model.UserPrefs;
import seedu.tutorpal.model.person.ClassContainsKeywordsPredicate;
import seedu.tutorpal.model.person.StudentBelongsToTutorPredicate;

import java.util.Arrays;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertCommandSuccess(new ListCommand(), model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listWithClassFilter_showsFilteredList() {
        // Test with a class filter
        ClassContainsKeywordsPredicate predicate =
                new ClassContainsKeywordsPredicate(java.util.Arrays.asList("s4mon1600"));
        ListCommand listCommand = new ListCommand(predicate, null, null);
        // Update expected model to show filtered results
        expectedModel.updateFilteredPersonList(predicate);
        String classMsg = String.format(
                ListCommand.MESSAGE_SUCCESS_FILTERED_CLASS,
                String.join(" or ", predicate.getKeywords()));
        String countMsg = String.format(
                seedu.tutorpal.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedModel.getFilteredPersonList().size());
        String expectedMessage = classMsg + "\n" + countMsg;
        assertCommandSuccess(listCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_class_orMatch() {
        ClassContainsKeywordsPredicate classPred =
            new ClassContainsKeywordsPredicate(Arrays.asList("s2", "s4"));
        ListCommand cmd = new ListCommand(classPred, null, null);

        expectedModel.updateFilteredPersonList(classPred);

        String header = String.format(ListCommand.MESSAGE_SUCCESS_FILTERED_CLASS, "s2 or s4");
        String count = String.format(seedu.tutorpal.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
            expectedModel.getFilteredPersonList().size());
        assertCommandSuccess(cmd, model, header + "\n" + count, expectedModel);
    }

    @Test
    public void execute_tutor_only() {
        // Ensure "John Doe" exists as a tutor in your TypicalPersons; otherwise change the name.
        StudentBelongsToTutorPredicate tutorPred =
            new StudentBelongsToTutorPredicate(Arrays.asList("John Doe"));

        // Mirror ListCommand.prepareTutorPredicateIfNeeded(model)
        tutorPred.setTutorClassKeywords(
            tutorPred.findTutorClasses(model.getAddressBook().getPersonList()));

        ListCommand cmd = new ListCommand(null, tutorPred, null);

        expectedModel.updateFilteredPersonList(tutorPred);

        String tutors = String.join(" or ", tutorPred.getTutorNames());
        String header = String.format(ListCommand.MESSAGE_SUCCESS_FILTERED_TUTOR, tutors);
        String count = String.format(seedu.tutorpal.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
            expectedModel.getFilteredPersonList().size());
        assertCommandSuccess(cmd, model, header + "\n" + count, expectedModel);
    }

    @Test
    public void execute_class_and_tutor() {
        ClassContainsKeywordsPredicate classPred =
            new ClassContainsKeywordsPredicate(Arrays.asList("s4"));

        // Ensure these tutor names exist in your dataset
        StudentBelongsToTutorPredicate tutorPred =
            new StudentBelongsToTutorPredicate(Arrays.asList("John", "Jane"));
        tutorPred.setTutorClassKeywords(
            tutorPred.findTutorClasses(model.getAddressBook().getPersonList()));

        ListCommand cmd = new ListCommand(classPred, tutorPred, null);

        expectedModel.updateFilteredPersonList(p -> classPred.test(p) && tutorPred.test(p));

        String details = "Belongs to class s4 AND Students taught by: John or Jane";
        String header = String.format(ListCommand.MESSAGE_SUCCESS_FILTERED_ANY, details);
        String count = String.format(seedu.tutorpal.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
            expectedModel.getFilteredPersonList().size());
        assertCommandSuccess(cmd, model, header + "\n" + count, expectedModel);
    }

    @Test
    public void equals_same_values_true() {
        ClassContainsKeywordsPredicate c1 =
            new ClassContainsKeywordsPredicate(Arrays.asList("s4"));
        StudentBelongsToTutorPredicate t1 =
            new StudentBelongsToTutorPredicate(Arrays.asList("John"));
        ListCommand a = new ListCommand(c1, t1, null);
        ListCommand b = new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4")),
            new StudentBelongsToTutorPredicate(Arrays.asList("John")),
            null);
        assertTrue(a.equals(b));
        assertTrue(a.equals(a));
    }

    @Test
    public void equals_different_values_false() {
        ListCommand all = new ListCommand();
        ListCommand classOnly = new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s3")), null, null);
        ListCommand tutorOnly = new ListCommand(
            null, new StudentBelongsToTutorPredicate(Arrays.asList("Jane")), null);

        assertFalse(all.equals(classOnly));
        assertFalse(classOnly.equals(tutorOnly));
        assertFalse(classOnly.equals(null));
        assertFalse(classOnly.equals("not a command"));
    }

    @Test
    public void toString_contains_filters() {
        ClassContainsKeywordsPredicate classPred =
            new ClassContainsKeywordsPredicate(Arrays.asList("s4", "s2"));
        StudentBelongsToTutorPredicate tutorPred =
            new StudentBelongsToTutorPredicate(Arrays.asList("John"));

        ListCommand cmd = new ListCommand(classPred, tutorPred, null);
        String s = cmd.toString();

        assertTrue(s.contains("classPredicate"));
        assertTrue(s.contains("tutorPredicate"));
        assertTrue(s.contains("paymentStatusPredicate")); // should be shown as null
        assertTrue(s.contains("s4"));
        assertTrue(s.contains("s2"));
        assertTrue(s.contains("John"));
    }

}
