package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ATTENDANCE_WEEK;

import java.util.List;

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
 * Unmarks the attendance of a student for a specified week.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the attendance of the student for the specified week.\n"
            + "Parameters: INDEX "
            + PREFIX_ATTENDANCE_WEEK + "WEEK\n"
            + "Example: " + COMMAND_WORD + " 2 " + PREFIX_ATTENDANCE_WEEK + "W26-2025";

    // SHORTENED is used for help command
    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t" + COMMAND_WORD + " INDEX "
            + PREFIX_ATTENDANCE_WEEK + "WEEK\n\t\tExample: "
            + COMMAND_WORD + " 1 " + PREFIX_ATTENDANCE_WEEK + "W26-2025";

    public static final String MESSAGE_SUCCESS = "Unmarked attendance for: %1$s on %2$s.";
    public static final String MESSAGE_CANNOT_UNMARK_FOR_ROLE = "Cannot unmark attendance for %1$s.";

    private final Index index;
    private final WeeklyAttendance week;

    /**
     * Creates an Unmark Command to be executed later.
     */
    public UnmarkCommand(Index index, WeeklyAttendance week) {
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

        Person personToUnmark = lastShownList.get(index.getZeroBased());

        if (personToUnmark.getRole() == Role.TUTOR) {
            throw new CommandException(String.format(MESSAGE_CANNOT_UNMARK_FOR_ROLE, Role.TUTOR));
        }

        //Should be student role only. Test with assertion in case more roles are added.
        assert personToUnmark instanceof Student;
        AttendanceHistory newAttendanceHistory;
        AttendanceHistory oldHistory = personToUnmark.getAttendanceHistory(); //should not throw errors
        try {
            newAttendanceHistory = oldHistory.unmarkAttendance(week);
        } catch (InvalidRangeException e) {
            throw new CommandException(e.getMessage()); //For range Messages, no need to append additional details.
        } catch (IllegalStateException e) {
            // Adding Person name for better user feedback.
            throw new CommandException(String.format(e.getMessage(), personToUnmark.getName()));
        }

        // Create a new Student with updated attendance history
        Person unmarkedPerson = new Student(
                personToUnmark.getName(),
                personToUnmark.getPhone(),
                personToUnmark.getEmail(),
                personToUnmark.getAddress(),
                personToUnmark.getClasses(),
                personToUnmark.getJoinDate(),
                newAttendanceHistory,
                personToUnmark.getPaymentHistory());

        model.setPerson(personToUnmark, unmarkedPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, unmarkedPerson.getName(), week));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UnmarkCommand)) {
            return false;
        }
        UnmarkCommand o = (UnmarkCommand) other;
        return index.equals(o.index) && week.equals(o.week);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("week", week)
                .toString();
    }
}
