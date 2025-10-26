package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.commons.util.ToStringBuilder;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.exceptions.CommandException;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.person.Person;

public class DisplayCommand extends Command {

    public static final String COMMAND_WORD = "display";

    public static final String MESSAGE_USAGE = "UNDER CONSTRUCTION";

    // SHORTENED is used for help command
    public static final String MESSAGE_USAGE_SHORTENED = "UNDER CONSTRUCTION";

    public static final String MESSAGE_DISPLAY_PERSON_SUCCESS = "Deleted Person: %1$s";

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
