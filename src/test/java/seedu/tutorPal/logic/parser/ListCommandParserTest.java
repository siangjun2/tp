package seedu.tutorPal.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.tutorPal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorPal.testutil.Assert.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.tutorPal.logic.commands.ListCommand;
import seedu.tutorPal.logic.parser.exceptions.ParseException;
import seedu.tutorPal.model.person.ClassContainsKeywordsPredicate;

public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_emptyArgs_returnsListCommand() throws Exception {
        ListCommand command = parser.parse("");
        assertEquals(new ListCommand(), command);
    }

    @Test
    public void parse_validClassFilter_returnsListCommandWithPredicate() throws Exception {
        ListCommand command = parser.parse(" c/s4mon1600");
        assertEquals(new ListCommand(new ClassContainsKeywordsPredicate(Arrays.asList("s4mon1600"))), command);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () -> parser.parse(" invalid"));
    }

    @Test
    public void parse_emptyClassPrefix_throwsParseException() {
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE), () -> parser.parse(" c/"));
    }
}

