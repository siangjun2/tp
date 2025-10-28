package seedu.tutorpal.logic.parser;

import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PAYMENT_MONTH;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.commands.PaymentCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new PaymentCommand object.
 */
public class PaymentCommandParser implements Parser<PaymentCommand> {

    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MM-yyyy");

    /**
     * Parses the given {@code String} of arguments in the context of the PaymentCommand
     * and returns a PaymentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public PaymentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PAYMENT_MONTH);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PaymentCommand.MESSAGE_USAGE), pe);
        }

        if (!argMultimap.getValue(PREFIX_PAYMENT_MONTH).isPresent()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, PaymentCommand.MESSAGE_USAGE));
        }

        String monthString = argMultimap.getValue(PREFIX_PAYMENT_MONTH).get().trim();
        YearMonth month;
        try {
            month = YearMonth.parse(monthString, MONTH_FORMAT);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)");
        }

        return new PaymentCommand(index, month);
    }
}
