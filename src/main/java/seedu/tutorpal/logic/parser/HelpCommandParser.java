package seedu.tutorpal.logic.parser;

import seedu.tutorpal.commons.core.commandword.CommandWord;
import seedu.tutorpal.logic.commands.HelpCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new HelpCommand object
 */
public class HelpCommandParser implements Parser<HelpCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns a HelpCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public HelpCommand parse(String args) throws ParseException {
        if (args.isBlank()) {
            return new HelpCommand();
        }
        CommandWord commandWord = ParserUtil.parseCommandWord(args.trim());
        return new HelpCommand(commandWord);
    }

}
