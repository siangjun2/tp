package seedu.tutorpal.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.util.CollectionUtil.requireAllNonNull;
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
 * Marks the attendance of a student in the address book.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the attendance of the student as having attended class for the specified week.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_ATTENDANCE_WEEK + "WEEK"
            + "\n"
            + "Example: " + COMMAND_WORD + " 3 "
            + PREFIX_ATTENDANCE_WEEK + "W2-10-2025";

    // SHORTENED is used for help command
    public static final String MESSAGE_USAGE_SHORTENED = COMMAND_WORD + ":\t" + COMMAND_WORD + " INDEX"
        + "\n\t\tExample: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Marked attendance for: %1$s on %2$s.";
    public static final String MESSAGE_ALREADY_MARKED = "%1$s is already marked on %2$s.";

    private final Index index;
    private final WeeklyAttendance week;

    /**
     * Creates a MarkCommand to mark attendance of the specified person on specified
     * week.
     */
    public MarkCommand(Index index, WeeklyAttendance week) {
        requireAllNonNull(index, week);
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

        if (personToMark.getRole() != Role.STUDENT) {
            throw new CommandException("Cannot mark attendance for a tutor.");
        }

        AttendanceHistory newAttendanceHistory;
        try {
            newAttendanceHistory = personToMark.getAttendanceHistory().markAttendance(week);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Attendance for the given week is already marked.")) {
                throw new CommandException(String.format(MESSAGE_ALREADY_MARKED, personToMark.getName(), week));
            } else {
                throw new CommandException(e.getMessage());
            }
        }

        // Create a new Student with updated attendance history
        Person markedPerson = new Student(
                personToMark.getName(),
                personToMark.getPhone(),
                personToMark.getEmail(),
                personToMark.getAddress(),
                personToMark.getClasses(),
                personToMark.getJoinDate(),
                personToMark.getPaymentHistory(),
                newAttendanceHistory);

        model.setPerson(personToMark, markedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, markedPerson.getName(), week));
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
