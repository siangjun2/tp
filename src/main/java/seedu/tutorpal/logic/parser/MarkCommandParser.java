package seedu.tutorpal.logic.parser;

import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_WEEK;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_WEEK);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE), pe);
        }

        if (!argMultimap.getValue(PREFIX_WEEK).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }

        WeeklyAttendance week = ParserUtil.parseWeeklyAttendance(argMultimap.getValue(PREFIX_WEEK).get());

        return new MarkCommand(index, week);
    }
}
