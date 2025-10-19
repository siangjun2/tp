package seedu.tutorpal.logic.commands;

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
        ListCommand listCommand = new ListCommand(predicate);
        // Update expected model to show filtered results
        expectedModel.updateFilteredPersonList(predicate);
        String classMsg = String.format(
                ListCommand.MESSAGE_SUCCESS_FILTERED_CLASS,
                String.join(", ", predicate.getKeywords()));
        String countMsg = String.format(
                seedu.tutorpal.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                expectedModel.getFilteredPersonList().size());
        String expectedMessage = classMsg + "\n" + countMsg;
        assertCommandSuccess(listCommand, model, expectedMessage, expectedModel);
    }
}
