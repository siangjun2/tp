package seedu.tutorpal.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.tutorpal.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.tutorpal.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.commons.core.commandword.CommandWord;
import seedu.tutorpal.model.Model;
import seedu.tutorpal.model.ModelManager;

public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_help_withCommandWord_returnsUsage() {
        // Pick any real command word your project defines (e.g., ADD)
        CommandWord cw = CommandWord.of("add");

        CommandResult result = new HelpCommand(cw).execute(model);

        assertEquals(cw.getMessageUsage(), result.getFeedbackToUser());
        assertFalse(result.isShowHelp());
        assertFalse(result.isExit());
        assertFalse(result.isShowDisplay());
    }

    @Test
    public void execute_help_explicitNullBehavesLikeNoArg() {
        CommandResult fromNullCtor = new HelpCommand(null).execute(model);
        CommandResult fromNoArgCtor = new HelpCommand().execute(model);

        // Both should open the help window
        assertEquals(fromNoArgCtor.getFeedbackToUser(), fromNullCtor.getFeedbackToUser());
        assertEquals(fromNoArgCtor.isShowHelp(), fromNullCtor.isShowHelp());
        assertEquals(fromNoArgCtor.isExit(), fromNullCtor.isExit());
        assertEquals(fromNoArgCtor.isShowDisplay(), fromNullCtor.isShowDisplay());
    }

    @Test
    public void execute_help_resultHasNoDisplay_andDefaultPersonInfoMessage() {
        CommandResult result = new HelpCommand().execute(model);

        // Ensure we are not triggering any display panel behaviour
        assertFalse(result.isShowDisplay());
        // Default message when no person info is present
        assertEquals("There is no information on this person.", result.getPersonInfo());
    }
}
