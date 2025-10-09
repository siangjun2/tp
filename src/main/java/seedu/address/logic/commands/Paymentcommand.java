package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Updates the payment status of a student in the address book.
 */
public class PaymentCommand extends Command {

    public static final String COMMAND_WORD = "payment";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the payment status of the student identified by the index number.\n"
            + "Parameters: INDEX (must be a positive integer) s/STATUS\n"
            + "Status can be: paid, unpaid, or overdue\n"
            + "Example: " + COMMAND_WORD + " 1 s/paid";

    public static final String MESSAGE_SUCCESS = "Payment status for %1$s has been updated to %2$s.";
    public static final String MESSAGE_NOT_STUDENT =
            "Index belongs to a tutor. Please provide an index tied to a student instead";

    private final Index index;
    private final String paymentStatus;

    /**
     * Creates a PaymentCommand to update the payment status of the specified person.
     */
    public PaymentCommand(Index index, String paymentStatus) {
        requireNonNull(index);
        requireNonNull(paymentStatus);
        this.index = index;
        this.paymentStatus = paymentStatus;
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
        if (personToEdit.getRole().value.equals("tutor")) {
            throw new CommandException(MESSAGE_NOT_STUDENT);
        }

        Person editedPerson = createPersonWithPaymentStatus(personToEdit, paymentStatus);
        model.setPerson(personToEdit, editedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS,
                editedPerson.getName(), paymentStatus));
    }

    /**
     * Creates and returns a {@code Person} with the updated payment status.
     */
    private Person createPersonWithPaymentStatus(Person person, String paymentStatus) {
        return new Person(person.getName(), person.getPhone(), person.getEmail(),
                person.getRole(), person.getAddress(), person.getClasses(),
                person.getTags(), paymentStatus);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PaymentCommand)) {
            return false;
        }

        PaymentCommand otherCommand = (PaymentCommand) other;
        return index.equals(otherCommand.index)
                && paymentStatus.equals(otherCommand.paymentStatus);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("paymentStatus", paymentStatus)
                .toString();
    }
}
