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

    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t" + COMMAND_WORD + " INDEX "
            + PREFIX_ATTENDANCE_WEEK + "WEEK\n\t\tExample: "
            + COMMAND_WORD + " 1 " + PREFIX_ATTENDANCE_WEEK + "W26-2025";

    public static final String MESSAGE_SUCCESS = "Unmarked attendance for: %1$s on %2$s.";
    public static final String MESSAGE_ERROR_FOR = "%1$s for %2$s!";
    public static final String MESSAGE_CANNOT_UNMARK_FOR = "Cannot unmark attendance for %1$s.";

    private final Index index;
    private final WeeklyAttendance week;

    /**
     * Constuctor of Unmark Command.
     * @param index
     * @param week
     */
    public UnmarkCommand(Index index, WeeklyAttendance week) {
        requireNonNull(index);
        requireNonNull(week);
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
        if (personToUnmark.getRole() != Role.STUDENT) {
            throw new CommandException(String.format(MESSAGE_CANNOT_UNMARK_FOR, personToUnmark.getRole()));
        }

        Student student = (Student) personToUnmark;
        AttendanceHistory oldHistory = student.getAttendanceHistory();

        // Pre-check to avoid relying on exception message matching
        if (!oldHistory.hasBeenMarked(week)) {
            throw new CommandException(String.format(
                    MESSAGE_ERROR_FOR,
                    String.format(AttendanceHistory.MESSAGE_CANNOT_UNMARK, week), student.getName()));
        }

        AttendanceHistory newHistory;
        try {
            newHistory = oldHistory.unmarkAttendance(week);
        } catch (IllegalArgumentException e) {
            // Range or validation issues from model layer
            throw new CommandException(e.getMessage());
        }

        Student editedStudent = new Student(
                student.getName(),
                student.getPhone(),
                student.getEmail(),
                student.getAddress(),
                student.getClasses(),
                student.getJoinDate(),
                newHistory
        );

        model.setPerson(student, editedStudent);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedStudent.getName(), week));
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
