package seedu.tutorpal.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PAYMENT_MONTH;

import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.PaymentCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;

/**
 * Tests for PaymentCommandParser.
 */
public class PaymentCommandParserTest {

    private final PaymentCommandParser parser = new PaymentCommandParser();

    // ========== INDEX PARAMETER TESTS ==========

    // --- Valid Cases ---

    @Test
    public void parse_validPositiveIndex_success() throws Exception {
        // EP: valid index (positive integer that can be parsed)
        String input = "1 " + PREFIX_PAYMENT_MONTH + "01-2024";
        PaymentCommand result = parser.parse(input);

        PaymentCommand expected = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validIndexWithLeadingZeros_success() throws Exception {
        // EP: valid index (positive integer with leading zeros)
        String input = "01 " + PREFIX_PAYMENT_MONTH + "01-2024";
        PaymentCommand result = parser.parse(input);

        PaymentCommand expected = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validIndexWithWhitespace_success() throws Exception {
        // EP: valid index (positive integer with surrounding whitespace)
        String input = "   2   " + PREFIX_PAYMENT_MONTH + "01-2024";
        PaymentCommand result = parser.parse(input);

        PaymentCommand expected = new PaymentCommand(Index.fromOneBased(2), YearMonth.of(2024, 1));
        assertEquals(expected, result);
    }

    // --- Invalid Cases (test invalid inputs individually) ---

    @Test
    public void parse_zeroIndex_failure() {
        // EP: index = 0
        String input = "0 " + PREFIX_PAYMENT_MONTH + "01-2024";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ex.getMessage());
    }

    @Test
    public void parse_negativeIndex_failure() {
        // EP: index < 0
        String input = "-1 " + PREFIX_PAYMENT_MONTH + "01-2024";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ex.getMessage());
    }

    @Test
    public void parse_missingIndex_failure() {
        // EP: empty or missing index
        String input = PREFIX_PAYMENT_MONTH + "01-2024";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PaymentCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    // ========== MONTH PARAMETER TESTS ==========

    // --- Valid Cases ---

    @Test
    public void parse_validMonthMinimum_success() throws Exception {
        // EP: valid MM-yyyy format
        String input = "1 " + PREFIX_PAYMENT_MONTH + "01-2024";
        PaymentCommand result = parser.parse(input);

        PaymentCommand expected = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validMonthMaximum_success() throws Exception {
        // EP: valid MM-yyyy format
        String input = "1 " + PREFIX_PAYMENT_MONTH + "12-2024";
        PaymentCommand result = parser.parse(input);

        PaymentCommand expected = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 12));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validMonthMidRange_success() throws Exception {
        // EP: valid MM-yyyy format
        String input = "1 " + PREFIX_PAYMENT_MONTH + "06-2024";
        PaymentCommand result = parser.parse(input);

        PaymentCommand expected = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 6));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validMonthWithWhitespace_success() throws Exception {
        // EP: valid MM-yyyy format with surrounding whitespace
        String input = "1 " + PREFIX_PAYMENT_MONTH + "   01-2024   ";
        PaymentCommand result = parser.parse(input);

        PaymentCommand expected = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2024, 1));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validYear2000_success() throws Exception {
        // EP: valid MM-yyyy format
        String input = "1 " + PREFIX_PAYMENT_MONTH + "01-2000";
        PaymentCommand result = parser.parse(input);

        PaymentCommand expected = new PaymentCommand(Index.fromOneBased(1), YearMonth.of(2000, 1));
        assertEquals(expected, result);
    }

    // --- Invalid Cases (test invalid inputs individually) ---

    @Test
    public void parse_monthZero_failure() {
        // EP: invalid month (00)
        String input = "1 " + PREFIX_PAYMENT_MONTH + "00-2024";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)", ex.getMessage());
    }

    @Test
    public void parse_monthThirteen_failure() {
        // EP: invalid month (>12)
        String input = "1 " + PREFIX_PAYMENT_MONTH + "13-2024";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)", ex.getMessage());
    }

    @Test
    public void parse_singleDigitMonthWithoutLeadingZero_failure() {
        // EP: invalid format (missing leading zero in month)
        String input = "1 " + PREFIX_PAYMENT_MONTH + "1-2024";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)", ex.getMessage());
    }

    @Test
    public void parse_twoDigitYear_failure() {
        // EP: invalid format (wrong year length)
        String input = "1 " + PREFIX_PAYMENT_MONTH + "01-24";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)", ex.getMessage());
    }

    @Test
    public void parse_wrongSeparatorSlash_failure() {
        // EP: invalid format (missing dash, wrong separator)
        String input = "1 " + PREFIX_PAYMENT_MONTH + "01/2024";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)", ex.getMessage());
    }

    @Test
    public void parse_wrongSeparatorDot_failure() {
        // EP: invalid format (missing dash, wrong separator)
        String input = "1 " + PREFIX_PAYMENT_MONTH + "01.2024";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)", ex.getMessage());
    }

    @Test
    public void parse_missingMonthPrefix_failure() {
        // EP: empty prefix (missing m/)
        String input = "1 01-2024";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PaymentCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_missingMonthValue_failure() {
        // EP: empty or missing month/year
        String input = "1 " + PREFIX_PAYMENT_MONTH;
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)", ex.getMessage());
    }

    @Test
    public void parse_blankMonthValue_failure() {
        // EP: empty or missing month/year (whitespace only)
        String input = "1 " + PREFIX_PAYMENT_MONTH + "   ";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)", ex.getMessage());
    }

    @Test
    public void parse_duplicateMonthPrefix_failure() {
        // EP: duplicate m/ prefix
        String input = "1 " + PREFIX_PAYMENT_MONTH + "01-2024 " + PREFIX_PAYMENT_MONTH + "02-2024";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_nonNumericMonth_failure() {
        // EP: alphabetic month (e.g., Jan)
        String input = "1 " + PREFIX_PAYMENT_MONTH + "Jan-2024";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals("Invalid month format. Please use MM-yyyy format (e.g., 01-2024)", ex.getMessage());
    }

    @Test
    public void parse_prefixBeforeIndex_failure() {
        // EP: wrong parameter order (prefix before index)
        String input = PREFIX_PAYMENT_MONTH + "01-2024 1";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PaymentCommand.MESSAGE_USAGE),
                ex.getMessage());
    }


    // ========== COMBINED CASES ==========

    @Test
    public void parse_emptyCommand_failure() {
        // EP: empty string
        String input = "";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PaymentCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_onlyWhitespace_failure() {
        // EP: whitespace-only string
        String input = "   \t  \n  ";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, PaymentCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_unknownPrefixInPreamble_failure() {
        // EP: unknown prefix in preamble (e.g., t/)
        String input = "1 t/Student " + PREFIX_PAYMENT_MONTH + "01-2024";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_extraTokensAfterMonth_failure() {
        // EP: extra tokens after month value
        String input = "1 " + PREFIX_PAYMENT_MONTH + "01-2024 extra";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }
}

