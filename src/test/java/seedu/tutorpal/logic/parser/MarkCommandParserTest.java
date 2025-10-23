package seedu.tutorpal.logic.parser;

import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.tutorpal.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.tutorpal.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.tutorpal.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.logic.commands.MarkCommand;
import seedu.tutorpal.model.person.WeeklyAttendance;

public class MarkCommandParserTest {

    private MarkCommandParser parser = new MarkCommandParser();

    @Test
    public void parse_validArgs_returnsMarkCommand() {
        // Valid input with week 1
        WeeklyAttendance week1 = new WeeklyAttendance("W1-01-2025");
        assertParseSuccess(parser, "1 w/W1-01-2025", new MarkCommand(INDEX_FIRST_PERSON, week1));

        // Valid input with week 4
        WeeklyAttendance week4 = new WeeklyAttendance("W4-12-2025");
        assertParseSuccess(parser, "1 w/W4-12-2025", new MarkCommand(INDEX_FIRST_PERSON, week4));

        // Valid input with different month and index
        WeeklyAttendance week2 = new WeeklyAttendance("W2-10-2025");
        assertParseSuccess(parser, "3 w/W2-10-2025", new MarkCommand(INDEX_THIRD_PERSON, week2));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        // Zero index
        assertParseFailure(parser, "0 w/W1-01-2025",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        // Negative index
        assertParseFailure(parser, "-1 w/W1-01-2025",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        // Non-numeric index
        assertParseFailure(parser, "abc w/W1-01-2025",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        // Empty index
        assertParseFailure(parser, " w/W1-01-2025",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidWeeklyAttendance_throwsParseException() {
        // Invalid week index (0)
        assertParseFailure(parser, "1 w/W0-01-2025", WeeklyAttendance.MESSAGE_CONSTRAINTS);

        // Invalid week index (5)
        assertParseFailure(parser, "1 w/W5-01-2025", WeeklyAttendance.MESSAGE_CONSTRAINTS);

        // Invalid month (00)
        assertParseFailure(parser, "1 w/W1-00-2025", WeeklyAttendance.MESSAGE_CONSTRAINTS);

        // Invalid month (13)
        assertParseFailure(parser, "1 w/W1-13-2025", WeeklyAttendance.MESSAGE_CONSTRAINTS);

        // Invalid year format (3 digits)
        assertParseFailure(parser, "1 w/W1-01-025", WeeklyAttendance.MESSAGE_CONSTRAINTS);

        // Invalid format (missing W prefix)
        assertParseFailure(parser, "1 w/1-01-2025", WeeklyAttendance.MESSAGE_CONSTRAINTS);

        // Invalid format (wrong separator)
        assertParseFailure(parser, "1 w/W1/01/2025", WeeklyAttendance.MESSAGE_CONSTRAINTS);

        // Empty weekly attendance
        assertParseFailure(parser, "1 w/", WeeklyAttendance.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        // Missing w/ prefix
        assertParseFailure(parser, "1 W1-01-2025",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingParts_throwsParseException() {
        // No index and no weekly attendance
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        // No weekly attendance
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        // Only prefix
        assertParseFailure(parser, "w/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_throwsParseException() {
        // Invalid index followed by valid weekly attendance
        assertParseFailure(parser, "0 w/W1-01-2025",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));

        // Valid index followed by invalid weekly attendance
        assertParseFailure(parser, "1 w/W5-01-2025", WeeklyAttendance.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_extraWhitespace_success() {
        // Extra whitespace around index
        WeeklyAttendance week = new WeeklyAttendance("W1-01-2025");
        assertParseSuccess(parser, "  1  w/W1-01-2025", new MarkCommand(INDEX_FIRST_PERSON, week));

        // Extra whitespace around weekly attendance
        assertParseSuccess(parser, "1 w/  W1-01-2025  ", new MarkCommand(INDEX_FIRST_PERSON, week));
    }
}
