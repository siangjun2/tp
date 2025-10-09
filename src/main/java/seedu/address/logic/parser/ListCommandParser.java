package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;

import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ClassContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLASS);

        // If no arguments provided, return command to list all persons
        if (args.trim().isEmpty()) {
            return new ListCommand();
        }

        // Check if class prefix is provided
        if (argMultimap.getValue(PREFIX_CLASS).isPresent()) {
            String classKeyword = argMultimap.getValue(PREFIX_CLASS).get().trim();
            if (classKeyword.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
            }
            
            List<String> keywords = Arrays.asList(classKeyword);
            return new ListCommand(new ClassContainsKeywordsPredicate(keywords));
        }

        // If arguments are provided but no valid prefix, throw exception
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }
}

