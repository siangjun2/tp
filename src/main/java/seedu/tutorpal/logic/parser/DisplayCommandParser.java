package seedu.tutorpal.logic.parser;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.commands.DisplayCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;

import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses input arguments and creates a new DisplayCommand object
 */
public class DisplayCommandParser implements Parser<DisplayCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DisplayCommand
     * and returns a DisplayCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DisplayCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args); // parses args, throws exception if it is not non-zero uint
            return new DisplayCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DisplayCommand.MESSAGE_USAGE), pe);
        }
    }

}
