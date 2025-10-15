package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.ClassContainsKeywordsPredicate;
import seedu.address.model.person.StudentBelongsToTutorPredicate;

/**
 * Lists all persons in the address book to the user.
 * Can optionally filter by class or tutor.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all persons in the address book.\n"
            + "Can optionally filter by class or tutor.\n"
            + "Parameters: c/CLASS or tu/TUTOR_NAME\n"
            + "Example: " + COMMAND_WORD + " (lists all persons)\n"
            + "Example: " + COMMAND_WORD + " c/s4mon1600 (lists persons in class s4mon1600)\n"
            + "Example: " + COMMAND_WORD + " tu/John Doe (lists students taught by John Doe)";

    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String MESSAGE_SUCCESS_FILTERED_CLASS = "Listed persons in class: %s";
    public static final String MESSAGE_SUCCESS_FILTERED_TUTOR = "Listed students taught by: %s";

    private final ClassContainsKeywordsPredicate classPredicate;
    private final StudentBelongsToTutorPredicate tutorPredicate;

    /**
     * Creates a ListCommand to list all persons.
     */
    public ListCommand() {
        this.classPredicate = null;
        this.tutorPredicate = null;
    }

    /**
     * Creates a ListCommand to list persons filtered by class.
     */
    public ListCommand(ClassContainsKeywordsPredicate predicate) {
        this.classPredicate = predicate;
        this.tutorPredicate = null;
    }

    /**
     * Creates a ListCommand to list students filtered by tutor.
     */
    public ListCommand(StudentBelongsToTutorPredicate predicate) {
        this.classPredicate = null;
        this.tutorPredicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (classPredicate == null && tutorPredicate == null) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_SUCCESS);
        } else if (classPredicate != null) {
            model.updateFilteredPersonList(classPredicate);
            String joinedKeywords = String.join(", ", classPredicate.getKeywords());
            return new CommandResult(
                    String.format(MESSAGE_SUCCESS_FILTERED_CLASS, joinedKeywords) + "\n"
                    + String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
        } else {
            // Handle tutor predicate
            List<String> tutorClasses = tutorPredicate.findTutorClasses(model.getAddressBook().getPersonList());
            tutorPredicate.setTutorClassKeywords(tutorClasses);
            model.updateFilteredPersonList(tutorPredicate);
            String joinedTutorNames = String.join(", ", tutorPredicate.getTutorNames());
            return new CommandResult(
                    String.format(MESSAGE_SUCCESS_FILTERED_TUTOR, joinedTutorNames) + "\n"
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
        // Compare predicates
        if (classPredicate == null && otherListCommand.classPredicate == null
                && tutorPredicate == null && otherListCommand.tutorPredicate == null) {
            return true;
        }
        if (classPredicate != null && otherListCommand.classPredicate != null) {
            return classPredicate.equals(otherListCommand.classPredicate);
        }
        if (tutorPredicate != null && otherListCommand.tutorPredicate != null) {
            return tutorPredicate.equals(otherListCommand.tutorPredicate);
        }
        return false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("classPredicate", classPredicate)
                .add("tutorPredicate", tutorPredicate)
                .toString();
    }
}
