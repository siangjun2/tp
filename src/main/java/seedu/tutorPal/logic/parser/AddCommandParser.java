package seedu.tutorPal.logic.parser;

import static seedu.tutorPal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorPal.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.tutorPal.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.tutorPal.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.tutorPal.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.tutorPal.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.tutorPal.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.tutorPal.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.tutorPal.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.tutorPal.logic.commands.AddCommand;
import seedu.tutorPal.logic.parser.exceptions.ParseException;
import seedu.tutorPal.model.person.Address;
import seedu.tutorPal.model.person.Class;
import seedu.tutorPal.model.person.Email;
import seedu.tutorPal.model.person.Name;
import seedu.tutorPal.model.person.Payment;
import seedu.tutorPal.model.person.Person;
import seedu.tutorPal.model.person.Phone;
import seedu.tutorPal.model.person.Role;
import seedu.tutorPal.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ROLE,
                        PREFIX_ADDRESS, PREFIX_CLASS, PREFIX_TAG, PREFIX_STATUS);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ROLE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ROLE, PREFIX_ADDRESS);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Role role = ParserUtil.parseRole(argMultimap.getValue(PREFIX_ROLE).get());
        //Defaults the tutorPal to an empty string if not provided
        Address address = argMultimap.getValue(PREFIX_ADDRESS).isPresent()
                ? ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get())
                : new Address("-");
        Set<Class> classList = ParserUtil.parseClasses(argMultimap.getAllValues(PREFIX_CLASS));
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        //Defaults the payment status to unpaid if not provided
        Payment paymentStatus = argMultimap.getValue(PREFIX_STATUS).isPresent()
            ? ParserUtil.parsePayment(argMultimap.getValue(PREFIX_STATUS).get())
            : new Payment("unpaid");

        if (classList.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    "At least one class must be specified using c/ prefix"));
        }

        Person person = new Person(name, phone, email, role, address, classList, tagList, paymentStatus, false);

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
