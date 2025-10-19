package seedu.tutorPal.logic.commands;

import static seedu.tutorPal.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tutorPal.testutil.Assert.assertThrows;
import static seedu.tutorPal.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.tutorPal.model.AddressBook;
import seedu.tutorPal.model.Model;
import seedu.tutorPal.model.ModelManager;
import seedu.tutorPal.model.UserPrefs;

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
