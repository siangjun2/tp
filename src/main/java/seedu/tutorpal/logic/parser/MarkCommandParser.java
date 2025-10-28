package seedu.tutorpal.logic.parser;

import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ATTENDANCE_WEEK;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.commands.MarkCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;
import seedu.tutorpal.model.person.WeeklyAttendance;

/**
 * Parses input arguments and creates a new MarkCommand object.
 */
public class MarkCommandParser implements Parser<MarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * MarkCommand
     * and returns a MarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ATTENDANCE_WEEK);

        Index index;

        //Get index
        //Throw ParseError if invalid Index ie non-negative. Check for whether Index is out of range is done
        //during run time.
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE), pe);
        }

        //Get AttendanceWeek string
        //Throw ParseError if not found
        if (argMultimap.getValue(PREFIX_ATTENDANCE_WEEK).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }
        //Throw parseError if invalid format
        WeeklyAttendance week = ParserUtil.parseWeeklyAttendance(argMultimap.getValue(PREFIX_ATTENDANCE_WEEK).get());

        return new MarkCommand(index, week);
    }
}
