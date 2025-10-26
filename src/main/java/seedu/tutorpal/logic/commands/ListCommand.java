package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_TUTOR;
import static seedu.tutorpal.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.tutorpal.commons.util.ToStringBuilder;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.person.ClassContainsKeywordsPredicate;
import seedu.tutorpal.model.person.PaymentStatusMatchesPredicate;
import seedu.tutorpal.model.person.StudentBelongsToTutorPredicate;

/**
 * Lists all persons in the address book to the user.
 * Can optionally filter by class or tutor.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all persons in the address book. "
            + "Can optionally filter by class or tutor, but not both.\n"
            + "Parameters: [c/CLASS] or [tu/TUTOR_NAME]\n"
            + "Example: " + COMMAND_WORD + " (lists all persons)\n"
            + "Example: " + COMMAND_WORD + " c/s4mon1600 (lists persons in class s4mon1600)\n"
            + "Example: " + COMMAND_WORD + " tu/John Doe (lists students taught by John Doe)";

    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t\t" + COMMAND_WORD + " "
        + "[" + PREFIX_CLASS + "CLASS] "
        + "[" + PREFIX_TUTOR + "TUTOR]\n"
        + "\t\tExample: " + COMMAND_WORD + " "
        + PREFIX_CLASS + "s4mon1600";

    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String MESSAGE_SUCCESS_FILTERED_CLASS = "Listed persons in class: %s";
    public static final String MESSAGE_SUCCESS_FILTERED_TUTOR = "Listed students taught by: %s";
    public static final String MESSAGE_SUCCESS_FILTERED_PAYMENT = "Listed persons with payment status: %s";

    private final ClassContainsKeywordsPredicate classPredicate;
    private final StudentBelongsToTutorPredicate tutorPredicate;
    private final PaymentStatusMatchesPredicate paymentStatusPredicate;

    /**
     * Creates a ListCommand to list all persons.
     */
    public ListCommand() {
        this.classPredicate = null;
        this.tutorPredicate = null;
        this.paymentStatusPredicate = null;
    }

    /**
     * Creates a ListCommand to list persons filtered by class.
     */
    public ListCommand(ClassContainsKeywordsPredicate predicate) {
        this.classPredicate = predicate;
        this.tutorPredicate = null;
        this.paymentStatusPredicate = null;
    }

    /**
     * Creates a ListCommand to list students filtered by tutor.
     */
    public ListCommand(StudentBelongsToTutorPredicate predicate) {
        this.classPredicate = null;
        this.tutorPredicate = predicate;
        this.paymentStatusPredicate = null;
    }

    /**
     * Creates a ListCommand to list persons filtered by payment status.
     */
    public ListCommand(PaymentStatusMatchesPredicate predicate) {
        this.classPredicate = null;
        this.tutorPredicate = null;
        this.paymentStatusPredicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        // Assert that only one filter is applied at a time
        assert classPredicate == null || tutorPredicate == null
                || paymentStatusPredicate == null : "Only one filter can be applied at a time";
        if (classPredicate == null && tutorPredicate == null && paymentStatusPredicate == null) {
            return executeListAll(model);
        } else if (classPredicate != null) {
            return executeListClass(model);
        } else if (tutorPredicate != null) {
            return executeListTutor(model);
        } else {
            // Handle payment status predicate
            return executeListPaymentStatus(model);
        }
    }

    /**
     * Executes the command to list all students.
     * @param model The model containing address book data.
     * @return a CommandResult with success message including class keyword.
     */
    private CommandResult executeListAll(Model model) {
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Executes the command to list students filtered by class.
     * @param model The model containing address book data.
     * @return a CommandResult with success message including class keyword.
     */
    private CommandResult executeListClass(Model model) {
        model.updateFilteredPersonList(classPredicate);
        String joinedKeywords = String.join(", ", classPredicate.getKeywords());
        return new CommandResult(
                String.format(MESSAGE_SUCCESS_FILTERED_CLASS, joinedKeywords) + "\n"
                + String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    /**
     * Executes the command to list students filtered by tutor.
     * @param model The model containing address book data.
     * @return a CommandResult with success message including class keyword.
     */
    private CommandResult executeListTutor(Model model) {
        List<String> tutorClasses = tutorPredicate.findTutorClasses(model.getAddressBook().getPersonList());
        tutorPredicate.setTutorClassKeywords(tutorClasses);
        model.updateFilteredPersonList(tutorPredicate);
        String joinedTutorNames = String.join(", ", tutorPredicate.getTutorNames());
        return new CommandResult(
                String.format(MESSAGE_SUCCESS_FILTERED_TUTOR, joinedTutorNames) + "\n"
                + String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    /**
     * Executes the command to list persons filtered by payment status.
     * @param model The model containing address book data.
     * @return a CommandResult with success message including payment status keywords.
     */
    private CommandResult executeListPaymentStatus(Model model) {
        model.updateFilteredPersonList(paymentStatusPredicate);
        String joinedKeywords = String.join(", ", paymentStatusPredicate.getKeywords());
        return new CommandResult(
                String.format(MESSAGE_SUCCESS_FILTERED_PAYMENT, joinedKeywords) + "\n"
                + String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
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
                && tutorPredicate == null && otherListCommand.tutorPredicate == null
                && paymentStatusPredicate == null && otherListCommand.paymentStatusPredicate == null) {
            return true;
        }
        if (classPredicate != null && otherListCommand.classPredicate != null) {
            return classPredicate.equals(otherListCommand.classPredicate);
        }
        if (tutorPredicate != null && otherListCommand.tutorPredicate != null) {
            return tutorPredicate.equals(otherListCommand.tutorPredicate);
        }
        if (paymentStatusPredicate != null && otherListCommand.paymentStatusPredicate != null) {
            return paymentStatusPredicate.equals(otherListCommand.paymentStatusPredicate);
        }
        return false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("classPredicate", classPredicate)
                .add("tutorPredicate", tutorPredicate)
                .add("paymentStatusPredicate", paymentStatusPredicate)
                .toString();
    }
}
