package seedu.tutorpal.logic.parser;

import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PAYMENT_MONTH;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.commands.DelpayCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DelpayCommand.
 * Expected format: INDEX m/MM-yyyy
 * Example: delpay 1 m/01-2024
 */
public class DelpayCommandParser implements Parser<DelpayCommand> {
    private static final Logger logger = Logger.getLogger(DelpayCommandParser.class.getName());
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MM-yyyy");

    @Override
    public DelpayCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PAYMENT_MONTH);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PAYMENT_MONTH);

        Index index = parseIndex(argMultimap);
        YearMonth month = parsePaymentMonth(argMultimap);

        logger.info("Parsed DelpayCommand: index=" + index + ", month=" + month);
        return new DelpayCommand(index, month);
    }

    private Index parseIndex(ArgumentMultimap argMultimap) throws ParseException {
        try {
            return ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DelpayCommand.MESSAGE_USAGE), pe);
        }
    }

    private YearMonth parsePaymentMonth(ArgumentMultimap argMultimap) throws ParseException {
        String monthValue = argMultimap.getValue(PREFIX_PAYMENT_MONTH)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DelpayCommand.MESSAGE_USAGE)));
        try {
            return YearMonth.parse(monthValue, MONTH_FORMAT);
        } catch (DateTimeParseException e) {
            throw new ParseException("Invalid month format. Use MM-yyyy, e.g., 01-2024");
        }
    }
}


