package seedu.tutorpal.logic.commands;

import static seedu.tutorpal.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tutorpal.testutil.Assert.assertThrows;
import static seedu.tutorpal.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.model.AddressBook;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.ModelManager;
import seedu.tutorpal.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        ClearCommand clearCommand = new ClearCommand();
        assertThrows(NullPointerException.class, () -> clearCommand.execute(null));
    }

}
