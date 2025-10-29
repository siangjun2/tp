package seedu.tutorpal.logic.parser;

import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_CLASS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_TUTOR;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.tutorpal.logic.commands.ListCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;
import seedu.tutorpal.model.person.ClassContainsKeywordsPredicate;
import seedu.tutorpal.model.person.Payment;
import seedu.tutorpal.model.person.PaymentStatusMatchesPredicate;
import seedu.tutorpal.model.person.StudentBelongsToTutorPredicate;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListCommandParser implements Parser<ListCommand> {
    private static Logger logger = Logger.getLogger(ListCommandParser.class.getName());

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListCommand parse(String args) throws ParseException {
        logger.log(Level.INFO, "Parsing ListCommand with args: \"" + args + "\"");
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
            PREFIX_CLASS, PREFIX_TUTOR, PREFIX_PAYMENT_STATUS);

        // If no arguments provided, return command to list all persons
        if (args.trim().isEmpty()) {
            return new ListCommand();
        }

        boolean hasClassFilter = argMultimap.getValue(PREFIX_CLASS).isPresent();
        boolean hasTutorFilter = argMultimap.getValue(PREFIX_TUTOR).isPresent();
        boolean hasPaymentStatusFilter = argMultimap.getValue(PREFIX_PAYMENT_STATUS).isPresent();

        ClassContainsKeywordsPredicate classPredicate = null;
        StudentBelongsToTutorPredicate tutorPredicate = null;
        PaymentStatusMatchesPredicate paymentPredicate = null;

        // Class filter (support multiple occurrences of c/)
        if (hasClassFilter) {
            List<String> classValues = argMultimap.getAllValues(PREFIX_CLASS);
            List<String> keywords = new ArrayList<>();
            for (String cls : classValues) {
                String trimmed = cls.trim();
                if (trimmed.isEmpty()) {
                    logger.log(Level.WARNING, "Empty class value after trim");
                    throw new ParseException(ListCommand.MESSAGE_EMPTY_CLASS_FILTER);
                }
                keywords.add(trimmed);
            }
            logger.log(Level.FINE, "List filter selected: class=" + keywords);
            classPredicate = new ClassContainsKeywordsPredicate(keywords);
        }

        // Tutor filter (support multiple occurrences of t/)
        if (hasTutorFilter) {
            List<String> tutorValues = argMultimap.getAllValues(PREFIX_TUTOR);
            List<String> tutorNames = new ArrayList<>();
            for (String t : tutorValues) {
                String trimmed = t.trim();
                if (trimmed.isEmpty()) {
                    logger.log(Level.WARNING, "Empty tutor name after trim");
                    throw new ParseException(ListCommand.MESSAGE_EMPTY_TUTOR_FILTER);
                }
                tutorNames.add(trimmed);
            }
            logger.log(Level.FINE, "List filter selected: tutor=" + tutorNames);
            tutorPredicate = new StudentBelongsToTutorPredicate(tutorNames);
        }

        // Payment status filter (comma-separated supported)
        if (hasPaymentStatusFilter) {
            List<String> statuses = argMultimap.getAllValues(PREFIX_PAYMENT_STATUS);
            List<String> keywords = new ArrayList<>();

            for (String status : statuses) {
                String trimmedStatus = status.trim();
                if (trimmedStatus.isEmpty()) {
                    logger.log(Level.WARNING, "Empty payment status after trim");
                    throw new ParseException(ListCommand.MESSAGE_EMPTY_PAYMENT_STATUS_FILTER);
                }
                if (!Payment.isValidPayment(trimmedStatus)) {
                    logger.log(Level.WARNING, "Invalid payment status: " + trimmedStatus);
                    throw new ParseException(Payment.MESSAGE_CONSTRAINTS);
                }
                keywords.add(trimmedStatus);
            }

            logger.log(Level.FINE, "List filter selected: paymentStatus=" + keywords);
            paymentPredicate = new PaymentStatusMatchesPredicate(keywords);
        }

        if (classPredicate != null || tutorPredicate != null || paymentPredicate != null) {
            return new ListCommand(classPredicate, tutorPredicate, paymentPredicate);
        }

        // If arguments are provided but no valid prefix, throw exception
        throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }
}

