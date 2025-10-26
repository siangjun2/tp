package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.commons.util.ToStringBuilder;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.exceptions.CommandException;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.person.Person;

/**
 * Displays the detailed information of a person identified using it's displayed index from the address book.
 */
public class DisplayCommand extends Command {

    public static final String COMMAND_WORD = "display";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Displays detailed information of the person identified by the index number "
        + "used in the displayed person list. List is one-indexed.\n"
        + "Parameters: INDEX (must be a positive integer)\n"
        + "Example: " + COMMAND_WORD + " 1";

    // SHORTENED is used for help command
    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t" + COMMAND_WORD + " INDEX"
        + "\n\t\tExample: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DISPLAY_PERSON_SUCCESS = "Displayed Person: %1$s";

    private final Index targetIndex;

    public DisplayCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        // Check that index does not exceed size of list
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDisplay = lastShownList.get(targetIndex.getZeroBased());
        // TODO: Display person information in new window
        return new CommandResult(String.format(MESSAGE_DISPLAY_PERSON_SUCCESS, Messages.format(personToDisplay)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DisplayCommand)) {
            return false;
        }

        DisplayCommand otherDisplayCommand = (DisplayCommand) other;
        return targetIndex.equals(otherDisplayCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("targetIndex", targetIndex)
            .toString();
    }
}
