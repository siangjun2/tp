package seedu.tutorpal.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_MONTH;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ROLE;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.commands.EditCommand;
import seedu.tutorpal.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.tutorpal.logic.parser.exceptions.ParseException;
import seedu.tutorpal.model.person.Class;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ROLE, PREFIX_NAME, PREFIX_PHONE,
                PREFIX_EMAIL, PREFIX_CLASS, PREFIX_ADDRESS, PREFIX_MONTH);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ROLE, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_ROLE).isPresent()) {
            editPersonDescriptor.setRole(ParserUtil.parseRole(argMultimap.getValue(PREFIX_ROLE).get()));
        }
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        if (argMultimap.getValue(PREFIX_MONTH).isPresent()) {
            editPersonDescriptor.setJoinMonth(ParserUtil.parseJoinMonth(argMultimap.getValue(PREFIX_MONTH).get()));
        }
        parseClassesForEdit(argMultimap.getAllValues(PREFIX_CLASS)).ifPresent(editPersonDescriptor::setClasses);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> classes} into a {@code Set<Class>} if
     * {@code classes} is non-empty.
     * If {@code classes} contain only one element which is an empty string, it will
     * be parsed into a
     * {@code Set<Class>} containing zero classes.
     */
    private Optional<Set<Class>> parseClassesForEdit(Collection<String> classes) throws ParseException {
        assert classes != null;

        if (classes.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> classSet = classes.size() == 1 && classes.contains("") ? Collections.emptySet() : classes;
        return Optional.of(ParserUtil.parseClasses(classSet));
    }

}
