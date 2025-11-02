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

    // ========== NO FILTER TESTS ==========

    @Test
    public void parse_emptyArgs_returnsListCommand() throws ParseException {
        // EP: no arguments; lists all persons
        ListCommand command = parser.parse("");
        assertEquals(new ListCommand(), command);
    }

    @Test
    public void parse_whitespaceOnly_returnsListCommand() throws ParseException {
        // EP: no arguments; lists all persons (whitespace only)
        ListCommand command = parser.parse("   \t  ");
        assertEquals(new ListCommand(), command);
    }

    @Test
    public void parse_extraTextAfterCommand_throwsParseException() {
        // EP: extra or invalid text after command
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () -> parser.parse(" all"));
    }

    @Test
    public void parse_extraTextWithPrefix_throwsParseException() {
        // EP: extra or invalid text after command
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () -> parser.parse(" everyone"));
    }

    // ========== CLASS FILTER TESTS ==========

    @Test
    public void parse_validClassFilter_returnsListCommandWithPredicate() throws ParseException {
        // EP: full class code matches valid format
        ListCommand command = parser.parse(" c/s4mon1600");
        assertEquals(new ListCommand(new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600")),
            null, null), command);
    }

    @Test
    public void parse_classPrefix_returnsListCommandWithPredicate() throws ParseException {
        // EP: prefix of class code (acts as wildcard)
        ListCommand command = parser.parse(" c/s4");
        assertEquals(new ListCommand(new ClassContainsKeywordsPredicate(Arrays.asList("s4")),
            null, null), command);
    }

    @Test
    public void parse_classLongerPrefix_returnsListCommandWithPredicate() throws ParseException {
        // EP: prefix of class code (acts as wildcard)
        ListCommand command = parser.parse(" c/s4mon");
        assertEquals(new ListCommand(new ClassContainsKeywordsPredicate(Arrays.asList("s4mon")),
            null, null), command);
    }

    @Test
    public void parse_emptyClassPrefix_throwsParseException() {
        // EP: empty class argument
        assertThrows(ParseException.class,
            ListCommand.MESSAGE_EMPTY_CLASS_FILTER, () -> parser.parse(" c/"));
    }

    @Test
    public void parse_multipleClassFilters_returnsListCommand() throws ParseException {
        // EP: multiple class filters (OR logic)
        ListCommand command = parser.parse(" c/s4mon1600 c/s2tue1400");
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600", "s2tue1400")),
            null,
            null), command);
    }

    // ========== TUTOR FILTER TESTS ==========

    @Test
    public void parse_validTutorFilter_returnsListCommand() throws ParseException {
        // EP: tutor name keyword (substring)
        ListCommand command = parser.parse(" t/alex");
        assertEquals(new ListCommand(
            null,
            new StudentBelongsToTutorPredicate(Arrays.asList("alex")),
            null), command);
    }

    @Test
    public void parse_tutorFilterCaseInsensitive_returnsListCommand() throws ParseException {
        // EP: tutor name keyword (substring, case-insensitive)
        ListCommand command = parser.parse(" t/LEE");
        assertEquals(new ListCommand(
            null,
            new StudentBelongsToTutorPredicate(Arrays.asList("LEE")),
            null), command);
    }

    @Test
    public void parse_multipleTutorFilters_returnsListCommand() throws ParseException {
        // EP: multiple tutor filters (OR logic)
        ListCommand command = parser.parse(" t/John Doe t/Alice Smith");
        assertEquals(new ListCommand(
            null,
            new StudentBelongsToTutorPredicate(Arrays.asList("John Doe", "Alice Smith")),
            null), command);
    }

    @Test
    public void parse_emptyTutorPrefix_throwsParseException() {
        // EP: empty keyword
        assertThrows(ParseException.class,
            ListCommand.MESSAGE_EMPTY_TUTOR_FILTER, () -> parser.parse(" t/"));
    }

    // ========== PAYMENT STATUS FILTER TESTS ==========

    @Test
    public void parse_validPaymentStatusPaid_returnsListCommand() throws ParseException {
        // EP: one allowed value
        ListCommand command = parser.parse(" ps/paid");
        assertEquals(new ListCommand(
            null,
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("paid"))), command);
    }

    @Test
    public void parse_validPaymentStatusUnpaid_returnsListCommand() throws ParseException {
        // EP: one allowed value
        ListCommand command = parser.parse(" ps/unpaid");
        assertEquals(new ListCommand(
            null,
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("unpaid"))), command);
    }

    @Test
    public void parse_validPaymentStatusOverdue_returnsListCommand() throws ParseException {
        // EP: one allowed value
        ListCommand command = parser.parse(" ps/overdue");
        assertEquals(new ListCommand(
            null,
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("overdue"))), command);
    }

    @Test
    public void parse_multiplePaymentStatusFilters_returnsListCommand() throws ParseException {
        // EP: multiple payment statuses (OR logic)
        ListCommand command = parser.parse(" ps/paid ps/unpaid");
        assertEquals(new ListCommand(
            null,
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("paid", "unpaid"))), command);
    }

    @Test
    public void parse_multiplePaymentStatusWithOverdue_returnsListCommand() throws ParseException {
        // EP: multiple payment statuses (OR logic)
        ListCommand command = parser.parse(" ps/paid ps/overdue");
        assertEquals(new ListCommand(
            null,
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("paid", "overdue"))), command);
    }

    @Test
    public void parse_caseInsensitivePaymentStatus_returnsListCommand() throws ParseException {
        // EP: one allowed value (case-insensitive)
        ListCommand command = parser.parse(" ps/uNpaID");
        assertEquals(new ListCommand(
            null,
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("uNpaID"))), command);
    }

    @Test
    public void parse_caseInsensitivePaymentStatusUpper_returnsListCommand() throws ParseException {
        // EP: one allowed value (case-insensitive)
        ListCommand command = parser.parse(" ps/UNPAID");
        assertEquals(new ListCommand(
            null,
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("UNPAID"))), command);
    }

    @Test
    public void parse_invalidPaymentStatus_throwsParseException() {
        // EP: invalid payment status value
        assertThrows(ParseException.class,
            Payment.MESSAGE_CONSTRAINTS, () -> parser.parse(" ps/pending"));
    }

    @Test
    public void parse_invalidPaymentStatusLate_throwsParseException() {
        // EP: invalid payment status value
        assertThrows(ParseException.class,
            Payment.MESSAGE_CONSTRAINTS, () -> parser.parse(" ps/late"));
    }

    @Test
    public void parse_emptyPaymentStatusPrefix_throwsParseException() {
        // EP: empty status argument
        assertThrows(ParseException.class,
            ListCommand.MESSAGE_EMPTY_PAYMENT_STATUS_FILTER, () -> parser.parse(" ps/"));
    }

    @Test
    public void parse_mixedValidInvalidPaymentStatus_throwsParseException() {
        // EP: mixed valid and invalid statuses (fails on first invalid)
        assertThrows(ParseException.class,
            Payment.MESSAGE_CONSTRAINTS, () -> parser.parse(" ps/paid ps/late"));
    }

    // ========== CROSS-FILTER COMBINATION TESTS ==========

    @Test
    public void parse_combinedFilters_returnsListCommand() throws ParseException {
        // EP: different filter types combined (AND logic)
        ListCommand command = parser.parse(" c/s4mon1600 t/John");
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600")),
            new StudentBelongsToTutorPredicate(Arrays.asList("John")),
            null), command);
    }

    @Test
    public void parse_combinedFiltersReverseOrder_returnsListCommand() throws ParseException {
        // EP: different filter types combined (AND logic, order doesn't matter)
        ListCommand command = parser.parse(" t/John c/s4mon1600");
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600")),
            new StudentBelongsToTutorPredicate(Arrays.asList("John")),
            null), command);
    }

    @Test
    public void parse_classAndPaymentStatusFilters_returnsListCommand() throws ParseException {
        // EP: different filter types combined (AND logic)
        ListCommand command = parser.parse(" c/s4mon1600 c/s2tue1400 ps/unpaid");
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600", "s2tue1400")),
            null,
            new PaymentStatusMatchesPredicate(Arrays.asList("unpaid"))), command);
    }

    @Test
    public void parse_allThreeFilterTypes_returnsListCommand() throws ParseException {
        // EP: different filter types combined (AND logic)
        ListCommand command = parser.parse(" c/s4mon1600 c/s2tue1400 ps/unpaid t/John Doe");
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600", "s2tue1400")),
            new StudentBelongsToTutorPredicate(Arrays.asList("John Doe")),
            new PaymentStatusMatchesPredicate(Arrays.asList("unpaid"))), command);
    }

    @Test
    public void parse_multipleSameTypeFilters_returnsListCommand() throws ParseException {
        // EP: multiple filters of same type (OR logic)
        ListCommand command = parser.parse(" c/s4 c/s2");
        assertEquals(new ListCommand(
            new ClassContainsKeywordsPredicate(Arrays.asList("s4", "s2")),
            null,
            null), command);
    }

    @Test
    public void parse_multipleTutorsSameType_returnsListCommand() throws ParseException {
        // EP: multiple filters of same type (OR logic)
        ListCommand command = parser.parse(" t/alex t/john");
        assertEquals(new ListCommand(
            null,
            new StudentBelongsToTutorPredicate(Arrays.asList("alex", "john")),
            null), command);
    }

    @Test
    public void parse_invalidPrefixTu_throwsParseException() {
        // EP: invalid prefix type
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () -> parser.parse(" tu/John"));
    }

    @Test
    public void parse_invalidPrefixP_throwsParseException() {
        // EP: invalid prefix type
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () -> parser.parse(" p/paid"));
    }

    @Test
    public void parse_invalidPrefixX_throwsParseException() {
        // EP: invalid prefix type
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () -> parser.parse(" x/abc"));
    }
}

