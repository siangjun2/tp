package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_TUTOR;
import static seedu.tutorpal.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

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
            + "Optionally filter by one or more of class, tutor, or payment status. "
            + "Within the same filter type (e.g. multiple c/, t/, or ps/), values are OR-ed; "
            + "across different filter types, filters are AND-ed.\n"
            + "Parameters: [" + PREFIX_CLASS + "CLASS]... [" + PREFIX_TUTOR + "TUTOR_NAME]... ["
            + PREFIX_PAYMENT_STATUS + "STATUS]...\n"
            + "Example: " + COMMAND_WORD + " (lists all persons)\n"
            + "Example: " + COMMAND_WORD + " c/s4mon1600 (lists persons in class s4mon1600)\n"
            + "Example: " + COMMAND_WORD + " t/John Doe (lists students taught by John Doe)\n"
            + "Example: " + COMMAND_WORD + " ps/paid (lists persons with paid payment status)\n"
            + "Example: " + COMMAND_WORD + " c/s2 c/s3 (lists persons in s2 or s3)\n"
            + "Example: " + COMMAND_WORD + " c/s4 c/s2 ps/unpaid (lists persons in (s4 OR s2) AND unpaid)";

    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t\t" + COMMAND_WORD + " "
        + "[" + PREFIX_CLASS + "CLASS]... "
        + "[" + PREFIX_TUTOR + "TUTOR]... "
        + "[" + PREFIX_PAYMENT_STATUS + "STATUS]...\n"
        + "\t\tExample: " + COMMAND_WORD + " "
        + PREFIX_CLASS + "s4mon1600";

    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String MESSAGE_SUCCESS_FILTERED_CLASS = "List persons in class: %s";
    public static final String MESSAGE_SUCCESS_FILTERED_TUTOR = "List students taught by: %s";
    public static final String MESSAGE_SUCCESS_FILTERED_PAYMENT = "List persons with payment status: %s";
    public static final String MESSAGE_SUCCESS_FILTERED_ANY = "Listed persons who: %s";
    public static final String MESSAGE_EMPTY_CLASS_FILTER = "Class filter cannot be empty. "
        + PREFIX_CLASS + "CLASS must have a value.";
    public static final String MESSAGE_EMPTY_TUTOR_FILTER = "Tutor filter cannot be empty. "
        + PREFIX_TUTOR + "TUTOR_NAME must have a value.";
    public static final String MESSAGE_EMPTY_PAYMENT_STATUS_FILTER = "Payment status filter cannot be empty. "
               + PREFIX_PAYMENT_STATUS + "STATUS must have a value.";

    private static final Logger LOGGER = Logger.getLogger(ListCommand.class.getName());

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
     * Creates a ListCommand that may include any combination of predicates. Filters are OR-ed.
     */
    public ListCommand(ClassContainsKeywordsPredicate classPredicate,
                       StudentBelongsToTutorPredicate tutorPredicate,
                       PaymentStatusMatchesPredicate paymentStatusPredicate) {
        this.classPredicate = classPredicate;
        this.tutorPredicate = tutorPredicate;
        this.paymentStatusPredicate = paymentStatusPredicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        LOGGER.fine("Executing ListCommand with filters: " + toString());

        if (classPredicate == null && tutorPredicate == null && paymentStatusPredicate == null) {
            return executeListAll(model);
        }

        prepareTutorPredicateIfNeeded(model);

        // Build combined predicate and details for the success message
        Predicate<seedu.tutorpal.model.person.Person> combined = buildCombinedPredicate();
        assert combined != null : "Combined predicate must not be null";

        final String details = buildFilterDetails();

        model.updateFilteredPersonList(combined);

        final String header = buildSuccessHeader(details);

        return new CommandResult(
                header + "\n" + String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                 model.getFilteredPersonList().size()));
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

    private void prepareTutorPredicateIfNeeded(Model model) {
        if (tutorPredicate == null) {
            return;
        }
        final List<String> tutorClasses = tutorPredicate.findTutorClasses(model.getAddressBook().getPersonList());
        tutorPredicate.setTutorClassKeywords(tutorClasses);
        LOGGER.fine("Resolved tutor classes: " + tutorClasses);
    }

    private Predicate<seedu.tutorpal.model.person.Person> buildCombinedPredicate() {
        Predicate<seedu.tutorpal.model.person.Person> combined = person -> true;
        if (classPredicate != null) {
            combined = combined.and(classPredicate);
        }
        if (tutorPredicate != null) {
            combined = combined.and(tutorPredicate);
        }
        if (paymentStatusPredicate != null) {
            combined = combined.and(paymentStatusPredicate);
        }
        return combined;
    }

    /**
     * Builds a detailed description of all active filters for use in combined filter messages.
     * Values within the same filter type are joined with "or", and different filter types are separated with " AND ".
     * @return A formatted string describing all filters, e.g.,
     *         "Belongs to class s4 or s2 AND Payment Status: unpaid or paid AND Students taught by: X or Y"
     */
    private String buildFilterDetails() {
        final StringBuilder details = new StringBuilder();
        boolean hasAnyDetail = false;
        if (classPredicate != null) {
            String classes = String.join(" or ", classPredicate.getKeywords());
            details.append("Belongs to class ").append(classes);
            hasAnyDetail = true;
        }
        if (tutorPredicate != null) {
            if (hasAnyDetail) {
                details.append(" AND ");
            }
            String tutors = String.join(" or ", tutorPredicate.getTutorNames());
            details.append("Students taught by: ").append(tutors);
            hasAnyDetail = true;
        }
        if (paymentStatusPredicate != null) {
            if (hasAnyDetail) {
                details.append(" AND ");
            }
            String statuses = String.join(" or ", paymentStatusPredicate.getKeywords());
            details.append("Payment Status: ").append(statuses);
        }
        return details.toString();
    }

    /**
     * Builds the success message header based on which filters are active.
     * For single filters, uses simple format. For combined filters, uses structured format.
     * @param details The formatted details string (only used for combined filters).
     * @return The appropriate success message header.
     */
    private String buildSuccessHeader(String details) {
        // Single filter: class only
        if (classPredicate != null && tutorPredicate == null && paymentStatusPredicate == null) {
            String classes = String.join(" or ", classPredicate.getKeywords());
            return String.format(MESSAGE_SUCCESS_FILTERED_CLASS, classes);
        }
        // Single filter: tutor only
        if (tutorPredicate != null && classPredicate == null && paymentStatusPredicate == null) {
            String tutors = String.join(" or ", tutorPredicate.getTutorNames());
            return String.format(MESSAGE_SUCCESS_FILTERED_TUTOR, tutors);
        }
        // Single filter: payment status only
        if (paymentStatusPredicate != null && classPredicate == null && tutorPredicate == null) {
            String statuses = String.join(" or ", paymentStatusPredicate.getKeywords());
            return String.format(MESSAGE_SUCCESS_FILTERED_PAYMENT, statuses);
        }
        // Combined filters: use structured format
        return String.format(MESSAGE_SUCCESS_FILTERED_ANY, details);
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
        return Objects.equals(classPredicate, otherListCommand.classPredicate)
                && Objects.equals(tutorPredicate, otherListCommand.tutorPredicate)
                && Objects.equals(paymentStatusPredicate, otherListCommand.paymentStatusPredicate);
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
