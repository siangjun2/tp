package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

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

    @Test
    public void execute_personWithMultipleClassesAndTags_success() {
        Person person = new PersonBuilder()
            .withClasses("math101", "phy102")
            .withTags("friend", "teammate")
            .build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(person);

        String role = person.getRole().toString();
        String name = person.getName().toString();
        String capitalizedRole = role.substring(0, 1).toUpperCase() + role.substring(1);
        String expectedMessage = String.format(AddCommand.MESSAGE_SUCCESS, capitalizedRole, name);
        assertCommandSuccess(new AddCommand(person), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_missingRequiredField_failure() {
        Person personMissingEmail = new PersonBuilder().withEmail(null).build();
        assertCommandFailure(new AddCommand(personMissingEmail), model, "NullPointerException");
    }
}
