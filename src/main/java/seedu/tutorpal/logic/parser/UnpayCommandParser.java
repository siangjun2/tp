package seedu.tutorpal.logic.parser;

import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PAYMENT_MONTH;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.commands.UnpayCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnpayCommand.
 * Expected format: INDEX m/MM-yyyy
 * Example: unpay 1 m/01-2024
 */
public class UnpayCommandParser implements Parser<UnpayCommand> {
    private static final Logger logger = Logger.getLogger(UnpayCommandParser.class.getName());
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MM-yyyy");

    /**
     * Parses the given {@code String} of arguments in the context of the UnpayCommand
     * and returns an UnpayCommand object for execution.
     *
     * @param args the arguments string to parse
     * @return an UnpayCommand object
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public UnpayCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PAYMENT_MONTH);

        Index index = parseIndex(argMultimap);
        YearMonth month = parsePaymentMonth(argMultimap);

        logger.info("Parsed UnpayCommand: index=" + index + ", month=" + month);
        return new UnpayCommand(index, month);
    }

    /**
     * Parses the index from the preamble of the argument multimap.
     *
     * @param argMultimap the argument multimap containing the index
     * @return the parsed index
     * @throws ParseException if the index is invalid or missing
     */
    private Index parseIndex(ArgumentMultimap argMultimap) throws ParseException {
        try {
            return ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            logger.warning("Invalid index in unpay command: " + argMultimap.getPreamble());
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnpayCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Parses the payment month from the argument multimap.
     *
     * @param argMultimap the argument multimap containing the month
     * @return the parsed YearMonth
     * @throws ParseException if the month is missing or has an invalid format
     */
    private YearMonth parsePaymentMonth(ArgumentMultimap argMultimap) throws ParseException {
        if (!argMultimap.getValue(PREFIX_PAYMENT_MONTH).isPresent()) {
            logger.warning("Missing payment month prefix in unpay command");
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnpayCommand.MESSAGE_USAGE));
        }

        String monthString = argMultimap.getValue(PREFIX_PAYMENT_MONTH).get().trim();
        
        try {
            YearMonth month = YearMonth.parse(monthString, MONTH_FORMAT);
            logger.info("Parsed payment month: " + month);
            return month;
        } catch (DateTimeParseException e) {
            logger.warning("Invalid month format: " + monthString);
            throw new ParseException("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)");
        }
    }
}
