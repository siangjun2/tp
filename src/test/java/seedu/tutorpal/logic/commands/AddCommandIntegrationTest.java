package seedu.tutorpal.logic.commands;

import static seedu.tutorpal.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.tutorpal.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tutorpal.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.ModelManager;
import seedu.tutorpal.model.UserPrefs;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        String role = validPerson.getRole().toString();
        String name = validPerson.getName().toString();
        String capitalizedRole = role.substring(0, 1).toUpperCase() + role.substring(1);
        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS, capitalizedRole, name);
        assertCommandSuccess(new AddCommand(validPerson), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(new AddCommand(personInList), model,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }
}
