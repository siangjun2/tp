package seedu.tutorpal.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ATTENDANCE_WEEK;

import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.commands.MarkCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;
import seedu.tutorpal.model.person.WeeklyAttendance;

/**
 * Parses input arguments and creates a new MarkCommand object.
 */
public class MarkCommandParser implements Parser<MarkCommand> {
    private static Logger logger = Logger.getLogger(MarkCommandParser.class.getName());
    /**
     * Parses the given {@code String} of arguments in the context of the
     * MarkCommand
     * and returns a MarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        logger.log(Level.INFO, "Parsing MarkCommand with args: \"" + args + "\"");

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ATTENDANCE_WEEK);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ATTENDANCE_WEEK);

        if (argMultimap.getPreamble().isEmpty()
                || argMultimap.getValue(PREFIX_ATTENDANCE_WEEK).isEmpty()) {
            logger.log(Level.WARNING, "Some arguments not found!");
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
        }

        // Get index. Throws parse exception.
        Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

        String weekStr = argMultimap.getValue(PREFIX_ATTENDANCE_WEEK).get();
        WeeklyAttendance week;
        try {
            week = new WeeklyAttendance(weekStr);
        } catch (IllegalArgumentException e) {
            // Wrap validation error into a ParseException for the parser layer
            logger.log(Level.WARNING, "WeeklyAttendance format is wrong! Given : " + weekStr);
            throw new ParseException(WeeklyAttendance.MESSAGE_CONSTRAINTS);
        }
        logger.log(Level.FINE, "Mark Command parsed with index =" + index + " and week=" + week);
        return new MarkCommand(index, week);
    }
}
