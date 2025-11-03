package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.tutorpal.model.AddressBook;
import seedu.tutorpal.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Clears all TutorPal entries\n"
        + "Parameters: -\n"
        + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t\t" + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "TutorPal has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
