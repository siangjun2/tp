package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ATTENDANCE_WEEK;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.commons.util.ToStringBuilder;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.exceptions.CommandException;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.person.AttendanceHistory;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Role;
import seedu.tutorpal.model.person.Student;
import seedu.tutorpal.model.person.WeeklyAttendance;
import seedu.tutorpal.model.person.exceptions.InvalidRangeException;

/**
 * Marks the attendance of a student in the address book.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the attendance of the student as having attended class for the specified week.\n"
            + "Parameters: INDEX "
            + PREFIX_ATTENDANCE_WEEK + "WEEK"
            + "\n"
            + "Example: " + COMMAND_WORD + " 3 " + PREFIX_ATTENDANCE_WEEK + "W26-2025";

    // SHORTENED is used for help command
    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t\t" + COMMAND_WORD + " INDEX "
        + PREFIX_ATTENDANCE_WEEK + "WEEK"
        + "\n\t\tExample: " + COMMAND_WORD + " 1 " + PREFIX_ATTENDANCE_WEEK + "W26-2025";

    public static final String MESSAGE_SUCCESS = "Marked attendance for: %1$s on %2$s.";
    public static final String MESSAGE_CANNOT_MARK_FOR_ROLE = "Cannot mark attendance for %1$s.";

    private static final Logger LOGGER = Logger.getLogger(MarkCommand.class.getName());

    private final Index index;
    private final WeeklyAttendance week;

    /**
     * Creates a MarkCommand to mark attendance of the specified person on specified
     * week.
     */
    public MarkCommand(Index index, WeeklyAttendance week) {
        //Based on AddressBookParser and ParserUtil implementation, impossible for null to be passed to commands
        //constructor. No input validation here, only checking invariant.
        assert index != null : "Index should not be null (guaranteed by parser)";
        assert week != null : "Week should not be null (guaranteed by parser)";
        this.index = index;
        this.week = week;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToMark = lastShownList.get(index.getZeroBased());

        //Block tutor roles as it does not have attendance history.
        if (personToMark.getRole() == Role.TUTOR) {
            throw new CommandException(String.format(MESSAGE_CANNOT_MARK_FOR_ROLE, Role.TUTOR));
        }

        //Should be student role only. Change to switch statement if got more roles.
        assert personToMark instanceof Student;
        AttendanceHistory newAttendanceHistory;
        AttendanceHistory oldAttendanceHistory = personToMark.getAttendanceHistory(); //Should not throw errors
        try {
            LOGGER.log(Level.FINE, "Attempting to mark " + personToMark.getName() + " on " + week);
            newAttendanceHistory = oldAttendanceHistory.markAttendance(week);
        } catch (InvalidRangeException e) {
            LOGGER.log(Level.WARNING, "A date related error occurred!");
            throw new CommandException(e.getMessage()); //For range Messages, no need to append additional details.
        } catch (IllegalStateException e) {
            // Adding Person name for better user feedback.
            LOGGER.log(Level.WARNING, "Attempted to mark already marked person! Person="
                    + personToMark.getName() + " Week=" + week);
            throw new CommandException(String.format(e.getMessage(), personToMark.getName()));
        }

        // Create a new Student with updated attendance history
        Person markedPerson = new Student(
                personToMark.getName(),
                personToMark.getPhone(),
                personToMark.getEmail(),
                personToMark.getAddress(),
                personToMark.getClasses(),
                personToMark.getJoinDate(),
                newAttendanceHistory,
                personToMark.getPaymentHistory());

        model.setPerson(personToMark, markedPerson);
        LOGGER.log(Level.FINE, "Mark success! Marked " + markedPerson.getName() + " on " + week);
        return new CommandResult(String.format(MESSAGE_SUCCESS, markedPerson.getName(), this.week));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MarkCommand)) {
            return false;
        }

        MarkCommand otherCommand = (MarkCommand) other;
        return index.equals(otherCommand.index)
                && week.equals(otherCommand.week);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("week", week)
                .toString();
    }
}
