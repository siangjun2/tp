package seedu.tutorpal.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tutorpal.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.tutorpal.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.tutorpal.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tutorpal.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.tutorpal.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.tutorpal.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.tutorpal.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.tutorpal.model.AddressBook;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.ModelManager;
import seedu.tutorpal.model.UserPrefs;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.testutil.EditPersonDescriptorBuilder;
import seedu.tutorpal.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Mirrors the runtime summarizeEditedFields(..) used in EditCommand for building the success message.
     * If you changed the production formatter, reflect those exact changes here for 1:1 matching.
     */
    private static String summarizeEditedFields(EditPersonDescriptor d) {
        StringBuilder sb = new StringBuilder();
        d.getName().ifPresent(v -> sb.append("name: ").append(v).append("; "));
        d.getPhone().ifPresent(v -> sb.append("phone: ").append(v).append("; "));
        d.getEmail().ifPresent(v -> sb.append("email: ").append(v).append("; "));
        d.getAddress().ifPresent(v -> sb.append("address: ").append(v).append("; "));
        d.getClasses().ifPresent(v -> sb.append("class(es): ").append(v).append("; "));
        d.getJoinDate().ifPresent(v -> sb.append("joinDate: ").append(v).append("; "));

        if (sb.length() == 0) {
            return "No fields edited";
        }
        // trim trailing " ;"
        if (sb.length() >= 2 && sb.charAt(sb.length() - 2) == ';') {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        // EP: all editable fields specified in unfiltered list
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(
            EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
            summarizeEditedFields(descriptor));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        // EP: some fields specified in unfiltered list
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
            .withName(VALID_NAME_BOB)
            .withPhone(VALID_PHONE_BOB)
            .build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(
            EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
            summarizeEditedFields(descriptor));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        // EP: no fields specified (no-op edit)
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(
            EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
            summarizeEditedFields(new EditPersonDescriptor())); // -> "No fields edited"

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
            .withName(VALID_NAME_BOB)
            .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(
            EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
            summarizeEditedFields(descriptor));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
            new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        // EP: index > size of unfiltered list
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        // EP: index > size of filtered list (but valid in unfiltered list)
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
            new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
            + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        // EP: null model
        EditCommand cmd = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        assertThrows(NullPointerException.class, () -> cmd.execute(null));
    }

    @Test
    public void execute_studentMultipleClasses_failure() {
        // Build a model with a student
        Model customModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person student = new PersonBuilder().withRole("student").withName("Stu Dent")
            .withClasses("s4mon0900").build();
        customModel.addPerson(student);

        // Try to set multiple classes (students cannot have multiple classes)
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
            .withClasses("s4mon0900", "s4wed1400").build();
        EditCommand edit = new EditCommand(Index.fromOneBased(1), descriptor);

        assertCommandFailure(edit, customModel, EditCommand.MESSAGE_STUDENT_MULTIPLE_CLASSES);
    }

    @Test
    public void execute_emptyClasses_failure() {
        // Build a model with a student
        Model customModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person student = new PersonBuilder().withRole("student").withName("Stu Dent")
            .withClasses("s4mon0900").build();
        customModel.addPerson(student);

        // Explicitly set classes to empty set -> not allowed by command
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setClasses(Collections.emptySet());
        EditCommand edit = new EditCommand(Index.fromOneBased(1), descriptor);

        assertCommandFailure(edit, customModel, EditCommand.MESSAGE_AT_LEAST_ONE_CLASS);
    }

    @Test
    public void execute_tutorMultipleClasses_success() {
        // Build a model with a tutor
        Model customModel = new ModelManager(new AddressBook(), new UserPrefs());
        Person tutor = new PersonBuilder().withRole("tutor").withName("Tu Tor")
            .withClasses("s4mon0900").build();
        customModel.addPerson(tutor);

        // Tutors may have multiple classes
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
            .withClasses("s4mon0900", "s4wed1400").build();
        EditCommand edit = new EditCommand(Index.fromOneBased(1), descriptor);

        Person editedPerson = new PersonBuilder(tutor).withClasses("s4mon0900", "s4wed1400").build();
        String expectedMessage = String.format(
            EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
            summarizeEditedFields(descriptor)); // shows class(es)=...

        Model expectedModel = new ModelManager(new AddressBook(customModel.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(tutor, editedPerson);

        assertCommandSuccess(edit, customModel, expectedMessage, expectedModel);
    }
}
