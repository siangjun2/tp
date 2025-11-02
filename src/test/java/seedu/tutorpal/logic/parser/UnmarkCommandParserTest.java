package seedu.tutorpal.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ATTENDANCE_WEEK;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.UnmarkCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;
import seedu.tutorpal.model.person.WeeklyAttendance;

public class UnmarkCommandParserTest {

    private final UnmarkCommandParser parser = new UnmarkCommandParser();

    @Test
    public void parse_validArgs_success() throws Exception {
        // EP: valid index and valid week format
        String input = "3 " + PREFIX_ATTENDANCE_WEEK + "W10-2025";
        UnmarkCommand result = parser.parse(input);

        UnmarkCommand expected = new UnmarkCommand(Index.fromOneBased(3), new WeeklyAttendance("W10-2025"));
        assertEquals(expected, result);
    }

    @Test
    public void parse_validArgsWithWhitespace_success() throws Exception {
        // EP: valid index and valid week format (with extra whitespace)
        String input = "   1   " + PREFIX_ATTENDANCE_WEEK + "W52-2024   ";
        UnmarkCommand result = parser.parse(input);

        UnmarkCommand expected = new UnmarkCommand(Index.fromOneBased(1), new WeeklyAttendance("W52-2024"));
        assertEquals(expected, result);
    }

    @Test
    public void parse_missingWeekPrefix_failure() {
        // EP: missing week prefix
        String input = "1 W26-2025"; // missing prefix
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_missingIndex_failure() {
        // EP: empty or missing index
        String input = "" + PREFIX_ATTENDANCE_WEEK + "W26-2025";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_missingWeek_failure() {
        // EP: missing week value
        String input = "2";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, UnmarkCommand.MESSAGE_USAGE),
                ex.getMessage());
    }

    @Test
    public void parse_invalidIndex_failure() {
        // EP: index = 0
        assertThrows(ParseException.class, () -> parser.parse("0 " + PREFIX_ATTENDANCE_WEEK + "W26-2025"));
        // EP: index < 0
        assertThrows(ParseException.class, () -> parser.parse("-1 " + PREFIX_ATTENDANCE_WEEK + "W26-2025"));
        // EP: non-numeric index
        assertThrows(ParseException.class, () -> parser.parse("xyz " + PREFIX_ATTENDANCE_WEEK + "W26-2025"));
    }

    @Test
    public void parse_invalidWeek_failure() {
        // EP: invalid week format (not WXX-YYYY)
        String input = "1 " + PREFIX_ATTENDANCE_WEEK + "invalid";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(input));
        assertEquals(WeeklyAttendance.MESSAGE_CONSTRAINTS, ex.getMessage());
    }
}
