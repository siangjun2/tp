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
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "W26-2025";
        MarkCommand result = parser.parse(input);

        MarkCommand expected = new MarkCommand(Index.fromOneBased(1), new WeeklyAttendance("W26-2025"));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validArgsWithWhitespace_success() throws Exception {
        String input = "   2   " + PREFIX_ATTENDANCE_WEEK + "W01-2025   ";
        MarkCommand result = parser.parse(input);

        MarkCommand expected = new MarkCommand(Index.fromOneBased(2), new WeeklyAttendance("W01-2025"));
        assertEquals(expected, result);
    }

    @Test
    public void parse_missingWeekPrefix_failure() {
        String input = "1 W26-2025"; // missing prefix
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_missingIndex_failure() {
        String input = "" + PREFIX_ATTENDANCE_WEEK + "W26-2025";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_missingWeek_failure() {
        String input = "1";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_invalidIndex_failure() {
        assertThrows(ParseException.class, () -> parser.parse("0 " + PREFIX_ATTENDANCE_WEEK + "W26-2025"));
        assertThrows(ParseException.class, () -> parser.parse("-5 " + PREFIX_ATTENDANCE_WEEK + "W26-2025"));
        assertThrows(ParseException.class, () -> parser.parse("abc " + PREFIX_ATTENDANCE_WEEK + "W26-2025"));
    }

    @Test
    public void parse_invalidWeek_failure() {
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "not-a-week";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        // MarkCommandParser should surface WeeklyAttendance constraints for invalid
        // week format
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    //More edge cases
    @Test
    public void parse_duplicateWeekPrefix_failure() {
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "W26-2025 "
                + PREFIX_ATTENDANCE_WEEK + "W27-2025";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_blankWeekValue_failure() {
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "   ";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    public void parse_prefixBeforeIndex_failure() {
        String input = PREFIX_ATTENDANCE_WEEK + "W26-2025 1";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_onlySpaces_failure() {
        String input = "   \t  \n  ";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_largeIndex_failure() { //Is long alr not int
        String input = "99999999999999999999 " + PREFIX_ATTENDANCE_WEEK + "W26-2025";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }

    @Test
    public void parse_indexWithLeadingZeros_success() throws Exception {
        String input = "01 " + PREFIX_ATTENDANCE_WEEK + "W06-2025";
        MarkCommand result = parser.parse(input);
        MarkCommand expected = new MarkCommand(Index.fromOneBased(1), new WeeklyAttendance("W06-2025"));
        assertEquals(expected, result);
    }

    @Test
    public void parse_weekWrongSeparator_failure() {
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "W26/2025";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    public void parse_weekOutOfRangeZero_failure() {
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "W00-2025";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }

    @Test
    public void parse_weekWithExtraTokens_failure() {
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
        String input = "1 t/John " + PREFIX_ATTENDANCE_WEEK + "W26-2025";
        assertThrows(ParseException.class, () -> parser.parse(input));
    }
}
