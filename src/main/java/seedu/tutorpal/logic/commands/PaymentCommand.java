package seedu.tutorpal.logic.commands;

import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_JOIN_DATE;

import java.time.YearMonth;
import java.util.List;
import java.util.logging.Logger;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.commons.util.ToStringBuilder;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.exceptions.CommandException;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.person.PaymentHistory;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Role;
import seedu.tutorpal.model.person.Student;
import seedu.tutorpal.model.person.Tutor;

/**
 * Marks a specific month's payment as paid for a person in the address book.
 * Applies to both students and tutors. The month must be on or after the person's join date
 * and cannot be in the future.
 */
public class PaymentCommand extends Command {
    private static final Logger logger = Logger.getLogger(PaymentCommand.class.getName());

    public static final String COMMAND_WORD = "pay";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks a specific month's payment as paid for the person identified by the index number.\n"
            + "Parameters: INDEX (must be a positive integer) m/MM-yyyy\n"
            + "Example: " + COMMAND_WORD + " 1 m/01-2024";

    // SHORTENED is used for help command
    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t\t" + COMMAND_WORD
        + " INDEX " + PREFIX_JOIN_DATE + "MM-yyyy\n"
        + "\t\tExample: " + COMMAND_WORD + " 1 m/01-2024";

    public static final String MESSAGE_SUCCESS = "Payment for %1$s for %2$s has been marked as paid.";
    public static final String MESSAGE_MONTH_BEFORE_JOIN =
            "Cannot mark payment for month before person's join date (%1$s)";
    public static final String MESSAGE_FUTURE_MONTH =
            "Cannot mark payment for future month";
    public static final String MESSAGE_ALREADY_PAID = "Payment for %1$s has already been marked as paid for %2$s.";

    private final Index index;
    private final YearMonth month;

    /**
     * Creates a PaymentCommand to mark the specified month's payment as paid
     * for the person at the given index.
     *
     * @param index the index of the person in the filtered list
     * @param month the month to mark as paid (format: MM-yyyy)
     */
    public PaymentCommand(Index index, YearMonth month) {
        assert index != null : "Index cannot be null";
        assert month != null : "Month cannot be null";
        this.index = index;
        this.month = month;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null : "Model cannot be null";
        logger.info("Executing PaymentCommand for person at index: " + index + ", month: " + month);

        Person personToEdit = getPersonFromModel(model);
        logger.info("Person to edit: " + personToEdit.getName() + " (Role: " + personToEdit.getRole() + ")");
        
        validateMonthConstraints(personToEdit, month);
        ensureNotAlreadyPaid(personToEdit, month);

        PaymentHistory updatedPaymentHistory = personToEdit.getPaymentHistory().markMonthAsPaid(month);
        Person editedPerson = createEditedPerson(personToEdit, updatedPaymentHistory);

        model.setPerson(personToEdit, editedPerson);
        
        String resultMessage = String.format(MESSAGE_SUCCESS, editedPerson.getName(), month);
        logger.info("Payment marked as paid: " + resultMessage);
        return new CommandResult(resultMessage);
    }

    /**
     * Retrieves the person from the model based on the index.
     *
     * @param model the model containing the person list
     * @return the person at the specified index
     * @throws CommandException if the index is invalid
     */
    private Person getPersonFromModel(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(index.getZeroBased());
    }

    /**
     * Validates that the month is valid for payment.
     * The month must be on or after the person's join date and cannot be in the future.
     *
     * @param person the person whose payment is being updated
     * @param month the month to validate
     * @throws CommandException if the month violates any constraints
     */
    private void validateMonthConstraints(Person person, YearMonth month) throws CommandException {
        YearMonth joinMonth = person.getJoinDate().toYearMonth();

        if (month.isBefore(joinMonth)) {
            logger.warning("Invalid payment month: " + month + " is before join date: " + joinMonth);
            throw new CommandException(String.format(MESSAGE_MONTH_BEFORE_JOIN, joinMonth));
        }

        if (month.isAfter(YearMonth.now())) {
            logger.warning("Invalid payment month: " + month + " is in the future");
            throw new CommandException(MESSAGE_FUTURE_MONTH);
        }
        
        assert month.compareTo(joinMonth) >= 0 : "Month must be >= join month after validation";
        assert month.compareTo(YearMonth.now()) <= 0 : "Month must be <= current month after validation";
    }

    /**
     * Ensures the specified month is not already marked as paid.
     *
     * @param person the person whose payment is being checked
     * @param month the month to check
     * @throws CommandException if the month is already marked as paid
     */
    private void ensureNotAlreadyPaid(Person person, YearMonth month) throws CommandException {
        if (person.getPaymentHistory().isMonthPaid(month)) {
            logger.warning("Month already paid: " + month + " for " + person.getName());
            throw new CommandException(String.format(MESSAGE_ALREADY_PAID, person.getName(), month));
        }
    }

    /**
     * Creates an edited person with the updated payment history.
     * Preserves all other attributes including attendance history for students.
     *
     * @param person the person to edit
     * @param updatedPaymentHistory the updated payment history
     * @return the edited person
     * @throws CommandException if person creation fails
     */
    private Person createEditedPerson(Person person, PaymentHistory updatedPaymentHistory) 
            throws CommandException {
        try {
            if (person.getRole() == Role.STUDENT) {
                return createEditedStudent((Student) person, updatedPaymentHistory);
            } else {
                return createEditedTutor(person, updatedPaymentHistory);
            }
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
    }

    /**
     * Creates an edited student with updated payment history.
     *
     * @param student the student to edit
     * @param updatedPaymentHistory the updated payment history
     * @return the edited student with all attributes preserved
     */
    private Student createEditedStudent(Student student, PaymentHistory updatedPaymentHistory) {
        Student editedStudent = new Student(
                student.getName(),
                student.getPhone(),
                student.getEmail(),
                student.getAddress(),
                student.getClasses(),
                student.getJoinDate(),
                updatedPaymentHistory,
                student.getAttendanceHistory()
        );
        assert editedStudent.getRole() == Role.STUDENT : "Created person should be a student";
        return editedStudent;
    }

    /**
     * Creates an edited tutor with updated payment history.
     *
     * @param tutor the tutor to edit
     * @param updatedPaymentHistory the updated payment history
     * @return the edited tutor with all attributes preserved
     */
    private Person createEditedTutor(Person tutor, PaymentHistory updatedPaymentHistory) {
        Tutor editedTutor = new Tutor(
                tutor.getName(),
                tutor.getPhone(),
                tutor.getEmail(),
                tutor.getAddress(),
                tutor.getClasses(),
                tutor.getJoinDate(),
                updatedPaymentHistory
        );
        assert editedTutor.getRole() == Role.TUTOR : "Created person should be a tutor";
        return editedTutor;
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
                && month.equals(otherCommand.month);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("month", month)
                .toString();
    }
}
