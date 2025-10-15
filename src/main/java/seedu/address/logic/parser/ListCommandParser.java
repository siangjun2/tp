package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTOR;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ClassContainsKeywordsPredicate;
import seedu.address.model.person.StudentBelongsToTutorPredicate;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CLASS, PREFIX_TUTOR);

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
            List<String> keywords = new ArrayList<>();
            keywords.add(classKeyword);
            return new ListCommand(new ClassContainsKeywordsPredicate(keywords));
        } else if (argMultimap.getValue(PREFIX_TUTOR).isPresent()) {
            String tutorName = argMultimap.getValue(PREFIX_TUTOR).get().trim();
            if (tutorName.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
            }
            // For now, we'll pass the tutor name to the predicate
            // The actual tutor finding logic will be handled in the predicate or command execution
            List<String> tutorNames = new ArrayList<>();
            tutorNames.add(tutorName);
            return new ListCommand(new StudentBelongsToTutorPredicate(tutorNames));
        }

        // If arguments are provided but no valid prefix, throw exception
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }
}

