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
 * Deletes the payment record for a specific month for a person in the address book.
 * Applies to both students and tutors. The month must be on or after the person's join date
 * and cannot be in the future.
 */
public class DelpayCommand extends Command {

    public static final String COMMAND_WORD = "delpay";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the payment record for a specific month for the person identified by the index number.\n"
            + "Parameters: INDEX (must be a positive integer) m/MM-yyyy\n"
            + "Example: " + COMMAND_WORD + " 1 m/01-2024";

    // SHORTENED is used for help command
    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t\t" + COMMAND_WORD
        + " INDEX " + PREFIX_JOIN_DATE + "MM-yyyy\n"
        + "\t\tExample: " + COMMAND_WORD + " 1 m/01-2024";

    public static final String MESSAGE_SUCCESS = "Deleted payment record for %1$s for %2$s.";
    public static final String MESSAGE_MONTH_BEFORE_JOIN =
            "Cannot delete record for month before person's join date (%1$s)";
    public static final String MESSAGE_FUTURE_MONTH =
            "Cannot delete record for future month";

    private static final Logger logger = Logger.getLogger(DelpayCommand.class.getName());

    private final Index index;
    private final YearMonth month;

    public DelpayCommand(Index index, YearMonth month) {
        assert index != null : "Index cannot be null";
        assert month != null : "Month cannot be null";
        this.index = index;
        this.month = month;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        assert model != null : "Model cannot be null";
        logger.info("Executing DelpayCommand for person at index: " + index + ", month: " + month);

        Person personToEdit = getPersonFromModel(model);
        validateMonthConstraints(personToEdit, month);

        PaymentHistory updatedPaymentHistory = personToEdit.getPaymentHistory().deleteMonth(month);
        Person editedPerson = createEditedPerson(personToEdit, updatedPaymentHistory);

        model.setPerson(personToEdit, editedPerson);
        String resultMessage = String.format(MESSAGE_SUCCESS, editedPerson.getName(), month);
        logger.info("Payment record deleted: " + resultMessage);
        return new CommandResult(resultMessage);
    }

    private Person getPersonFromModel(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(index.getZeroBased());
    }

    private void validateMonthConstraints(Person person, YearMonth month) throws CommandException {
        YearMonth joinMonth = person.getJoinDate().toYearMonth();

        if (month.isBefore(joinMonth)) {
            throw new CommandException(String.format(MESSAGE_MONTH_BEFORE_JOIN, joinMonth));
        }

        if (month.isAfter(YearMonth.now())) {
            throw new CommandException(MESSAGE_FUTURE_MONTH);
        }
        assert month.compareTo(joinMonth) >= 0 : "Month must be >= join month after validation";
        assert month.compareTo(YearMonth.now()) <= 0 : "Month must be <= current month after validation";
    }

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

    private Student createEditedStudent(Student student, PaymentHistory updatedPaymentHistory) {
        Student editedStudent = new Student(
                student.getName(),
                student.getPhone(),
                student.getEmail(),
                student.getAddress(),
                student.getClasses(),
                student.getJoinDate(),
                student.getAttendanceHistory(),
                updatedPaymentHistory
        );
        assert editedStudent.getRole() == Role.STUDENT : "Created person should be a student";
        return editedStudent;
    }

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
        if (!(other instanceof DelpayCommand)) {
            return false;
        }
        DelpayCommand otherCommand = (DelpayCommand) other;
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


