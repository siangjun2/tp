package seedu.tutorpal.logic.commands;

import java.time.YearMonth;
import java.util.List;
import static java.util.Objects.requireNonNull;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.commons.util.ToStringBuilder;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.exceptions.CommandException;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_JOIN_DATE;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.person.PaymentHistory;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Role;
import seedu.tutorpal.model.person.Student;

/**
 * Updates the payment status of a student in the address book by marking a
 * month as unpaid.
 */
public class UnpayCommand extends Command {

    public static final String COMMAND_WORD = "unpay";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks a specific month's payment as unpaid for the student identified by the index number.\n"
            + "Parameters: INDEX (must be a positive integer) m/MM-yyyy\n"
            + "Example: " + COMMAND_WORD + " 1 m/01-2024";

    // SHORTENED is used for help command
    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t" + COMMAND_WORD
        + " INDEX " + PREFIX_JOIN_DATE + "MM-yyyy\n"
        + "\t\tExample: " + COMMAND_WORD + " 1 m/01-2024";

    public static final String MESSAGE_SUCCESS = "Payment for %1$s for %2$s has been marked as unpaid.";
    public static final String MESSAGE_NOT_STUDENT =
            "Index belongs to a tutor. Please provide an index tied to a student instead";
    public static final String MESSAGE_MONTH_BEFORE_JOIN =
            "Cannot mark payment for month before student's join date (%1$s)";
    public static final String MESSAGE_FUTURE_MONTH = "Cannot mark payment for future month";

    private final Index index;
    private final YearMonth month;

    /**
     * Creates an UnpayCommand to mark the specified month's payment as unpaid for
     * the specified person.
     */
    public UnpayCommand(Index index, YearMonth month) {
        requireNonNull(index);
        requireNonNull(month);
        this.index = index;
        this.month = month;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        // Check if the person is a student
        if (personToEdit.getRole() == Role.TUTOR) {
            throw new CommandException(MESSAGE_NOT_STUDENT);
        }

        // Validate month constraints
        YearMonth joinMonth = personToEdit.getJoinDate().toYearMonth();
        if (month.isBefore(joinMonth)) {
            throw new CommandException(String.format(MESSAGE_MONTH_BEFORE_JOIN, joinMonth));
        }

        if (month.isAfter(YearMonth.now())) {
            throw new CommandException(MESSAGE_FUTURE_MONTH);
        }

        Person editedPerson = createPersonWithUpdatedPayment(personToEdit, month);
        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                editedPerson.getName(), month));
    }

    /**
     * Creates and returns a {@code Person} with the updated payment status for the
     * specified month.
     * Note: This method assumes the person is a Student (validated in execute()).
     */
    private Person createPersonWithUpdatedPayment(Person person, YearMonth month) {
        PaymentHistory updatedPaymentHistory = person.getPaymentHistory().markMonthAsUnpaid(month);
        // Since we validated it's a student, create a new Student with updated payment history
        return new Student(person.getName(), person.getPhone(), person.getEmail(),
                person.getAddress(), person.getClasses(), person.getJoinDate(), updatedPaymentHistory);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnpayCommand)) {
            return false;
        }

        UnpayCommand otherUnpayCommand = (UnpayCommand) other;
        return index.equals(otherUnpayCommand.index)
                && month.equals(otherUnpayCommand.month);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("month", month)
                .toString();
    }
}
