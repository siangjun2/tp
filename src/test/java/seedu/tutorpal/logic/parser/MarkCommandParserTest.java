package seedu.tutorpal.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ATTENDANCE_WEEK;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.MarkCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;
import seedu.tutorpal.model.person.WeeklyAttendance;

public class MarkCommandParserTest {

    private final MarkCommandParser parser = new MarkCommandParser();

    @Test
    public void parse_validArgs_success() throws Exception {
        // EP: valid index and valid week format
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "W26-2025";
        MarkCommand result = parser.parse(input);

        MarkCommand expected = new MarkCommand(Index.fromOneBased(1), new WeeklyAttendance("W26-2025"));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validArgsWithWhitespace_success() throws Exception {
        // EP: valid index and valid week format (with extra whitespace)
        String input = "   2   " + PREFIX_ATTENDANCE_WEEK + "W01-2025   ";
        MarkCommand result = parser.parse(input);

        MarkCommand expected = new MarkCommand(Index.fromOneBased(2), new WeeklyAttendance("W01-2025"));
        assertEquals(expected, result);
    }

    @Test
    public void parse_missingWeekPrefix_failure() {
        // EP: missing week prefix
        String input = "1 W26-2025"; // missing prefix
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_missingIndex_failure() {
        // EP: empty or missing index
        String input = "" + PREFIX_ATTENDANCE_WEEK + "W26-2025";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_missingWeek_failure() {
        // EP: missing week value
        String input = "1";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_invalidIndex_failure() {
        // EP: index = 0
        assertThrows(ParseException.class, () -> parser.parse("0 " + PREFIX_ATTENDANCE_WEEK + "W26-2025"));
        // EP: index < 0
        assertThrows(ParseException.class, () -> parser.parse("-5 " + PREFIX_ATTENDANCE_WEEK + "W26-2025"));
        // EP: non-numeric index
        assertThrows(ParseException.class, () -> parser.parse("abc " + PREFIX_ATTENDANCE_WEEK + "W26-2025"));
    }

    @Test
    public void parse_invalidWeek_failure() {
        // EP: invalid week format (not WXX-YYYY)
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "not-a-week";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        // MarkCommandParser should surface WeeklyAttendance constraints for invalid
        // week format
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    //More edge cases
    @Test
    public void parse_duplicateWeekPrefix_failure() {
        // EP: duplicate week prefix
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "W26-2025 "
                + PREFIX_ATTENDANCE_WEEK + "W27-2025";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_blankWeekValue_failure() {
        // EP: empty or whitespace-only week value
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "   ";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    public void parse_prefixBeforeIndex_failure() {
        // EP: wrong parameter order (prefix before index)
        String input = PREFIX_ATTENDANCE_WEEK + "W26-2025 1";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_onlySpaces_failure() {
        // EP: empty or whitespace-only string
        String input = "   \t  \n  ";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_largeIndex_failure() { //Is long alr not int
        // EP: index exceeds integer range
        String input = "99999999999999999999 " + PREFIX_ATTENDANCE_WEEK + "W26-2025";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_indexWithLeadingZeros_success() throws Exception {
        // EP: valid index with leading zeros
        String input = "01 " + PREFIX_ATTENDANCE_WEEK + "W06-2025";
        MarkCommand result = parser.parse(input);
        MarkCommand expected = new MarkCommand(Index.fromOneBased(1), new WeeklyAttendance("W06-2025"));
        assertEquals(expected, result);
    }

    @Test
    public void parse_weekWrongSeparator_failure() {
        // EP: invalid week format (wrong separator)
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "W26/2025";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    public void parse_weekOutOfRangeZero_failure() {
        // EP: invalid week number (week = 0)
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "W00-2025";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    public void parse_weekWithExtraTokens_failure() {
        // EP: extra tokens after week value
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "W26-2025 extra";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    public void parse_unicodeDigitsInWeek_failure() {
        // Full-width digits should be rejected
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "W２６-２０２５";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    public void parse_unknownTokensInPreamble_failure() {
        // EP: unknown prefix in preamble
        String input = "1 t/John " + PREFIX_ATTENDANCE_WEEK + "W26-2025";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }
}
