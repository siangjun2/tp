package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.ClassContainsKeywordsPredicate;

/**
 * Lists all persons in the address book to the user.
 * Can optionally filter by class.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all persons in the address book.\n"
            + "Can optionally filter by class.\n"
            + "Parameters: c/CLASS\n"
            + "Example: " + COMMAND_WORD + " (lists all persons)\n"
            + "Example: " + COMMAND_WORD + " c/s4mon1600 (lists persons in class s4mon1600)";

    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String MESSAGE_SUCCESS_FILTERED = "Listed persons in class: %s";

    private final ClassContainsKeywordsPredicate predicate;

    /**
     * Creates a ListCommand to list all persons.
     */
    public ListCommand() {
        this.predicate = null;
    }

    /**
     * Creates a ListCommand to list persons filtered by class.
     */
    public ListCommand(ClassContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        
        if (predicate == null) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            model.updateFilteredPersonList(predicate);
            String joinedKeywords = String.join(", ", predicate.getKeywords());
            return new CommandResult(
                    String.format(MESSAGE_SUCCESS_FILTERED, joinedKeywords) + "\n"
                    + String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListCommand)) {
            return false;
        }

        ListCommand otherListCommand = (ListCommand) other;
        if (predicate == null && otherListCommand.predicate == null) {
            return true;
        }
        if (predicate == null || otherListCommand.predicate == null) {
            return false;
        }
        return predicate.equals(otherListCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
