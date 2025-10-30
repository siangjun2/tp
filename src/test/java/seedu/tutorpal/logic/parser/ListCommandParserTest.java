package seedu.tutorpal.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.testutil.Assert.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.logic.commands.ListCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;
import seedu.tutorpal.model.person.ClassContainsKeywordsPredicate;
import seedu.tutorpal.model.person.Payment;
import seedu.tutorpal.model.person.PaymentStatusMatchesPredicate;
import seedu.tutorpal.model.person.StudentBelongsToTutorPredicate;

public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_emptyArgs_returnsListCommand() throws ParseException {
        ListCommand command = parser.parse("");
        assertEquals(new ListCommand(), command);
    }

    @Test
    public void parse_validClassFilter_returnsListCommandWithPredicate() throws ParseException {
        ListCommand command = parser.parse(" c/s4mon1600");
        assertEquals(new ListCommand(new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600")),
            null, null), command);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () -> parser.parse(" invalid"));
    }

    @Test
    public void parse_emptyClassPrefix_throwsParseException() {
        assertThrows(ParseException.class,
            ListCommand.MESSAGE_EMPTY_CLASS_FILTER, () -> parser.parse(" c/"));
    }

    @Test
    public void parse_emptyTutorPrefix_throwsParseException() {
        assertThrows(ParseException.class,
            ListCommand.MESSAGE_EMPTY_TUTOR_FILTER, () -> parser.parse(" t/"));
    }

    @Test
    public void parse_emptyPaymentStatusPrefix_throwsParseException() {
        assertThrows(ParseException.class,
            ListCommand.MESSAGE_EMPTY_PAYMENT_STATUS_FILTER, () -> parser.parse(" ps/"));
    }

    @Test
    public void parse_combinedFilters_returnsListCommand() throws ParseException {
        // Test that multiple filter types can be combined (they are AND-ed)
        ListCommand command = parser.parse(" c/s4mon1600 t/John");
        // Should successfully parse and create a command with both predicates
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600")),
            new StudentBelongsToTutorPredicate(Arrays.asList("John")),
            null), command);
    }

    @Test
    public void parse_combinedFiltersReverseOrder_returnsListCommand() throws ParseException {
        // Test that filter order doesn't matter
        ListCommand command = parser.parse(" t/John c/s4mon1600");
        // Should successfully parse and create a command with both predicates
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600")),
            new StudentBelongsToTutorPredicate(Arrays.asList("John")),
            null), command);
    }

    @Test
    public void parse_invalidPrefixTu_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () -> parser.parse(" tu/John"));
    }

    @Test
    public void parse_invalidPrefixP_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () -> parser.parse(" p/paid"));
    }

    @Test
    public void parse_multipleClassFilters_returnsListCommand() throws ParseException {
        // Test that multiple class filters are OR-ed together
        ListCommand command = parser.parse(" c/s4mon1600 c/s2tue1400");
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600", "s2tue1400")),
            null,
            null), command);
    }

    @Test
    public void parse_multipleTutorFilters_returnsListCommand() throws ParseException {
        // Test that multiple tutor filters are OR-ed together
        ListCommand command = parser.parse(" t/John Doe t/Alice Smith");
        assertEquals(new ListCommand(
            null,
            new StudentBelongsToTutorPredicate(Arrays.asList("John Doe", "Alice Smith")),
            null), command);
    }

    @Test
    public void parse_multiplePaymentStatusFilters_returnsListCommand() throws ParseException {
        // Test that multiple payment status filters are OR-ed together
        ListCommand command = parser.parse(" ps/paid ps/unpaid");
        assertEquals(new ListCommand(
            null,
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("paid", "unpaid"))), command);
    }

    @Test
    public void parse_caseInsensitivePaymentStatus_returnsListCommand() throws ParseException {
        // Test that payment status is case-insensitive (uNpaID should be valid)
        ListCommand command = parser.parse(" ps/uNpaID");
        assertEquals(new ListCommand(
            null,
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("uNpaID"))), command);
    }

    @Test
    public void parse_invalidPaymentStatus_throwsParseException() {
        // Test that invalid payment status (unpad) throws Payment.MESSAGE_CONSTRAINTS
        assertThrows(ParseException.class,
            Payment.MESSAGE_CONSTRAINTS, () -> parser.parse(" ps/unpad"));
    }

    @Test
    public void parse_classAndPaymentStatusFilters_returnsListCommand() throws ParseException {
        // Test combining multiple classes with payment status (two filter types)
        ListCommand command = parser.parse(" c/s4mon1600 c/s2tue1400 ps/unpaid");
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600", "s2tue1400")),
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("unpaid"))), command);
    }

    @Test
    public void parse_allThreeFilterTypes_returnsListCommand() throws ParseException {
        // Test combining all three filter types together
        ListCommand command = parser.parse(" c/s4mon1600 c/s2tue1400 ps/unpaid t/John Doe");
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600", "s2tue1400")),
            new StudentBelongsToTutorPredicate(Arrays.asList("John Doe")),
            new PaymentStatusMatchesPredicate(Arrays.asList("unpaid"))), command);
    }
}

