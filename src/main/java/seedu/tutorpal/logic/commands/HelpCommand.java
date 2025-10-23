package seedu.tutorpal.logic.commands;

import seedu.tutorpal.commons.core.commandword.CommandWord;
import seedu.tutorpal.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    private CommandWord commandWord;

    public HelpCommand() {

    }

    public HelpCommand(CommandWord commandWord) {
        this.commandWord = commandWord;
    }

    @Override
    public CommandResult execute(Model model) {
        if (this.commandWord == null) {
            return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        }
        return new CommandResult(this.commandWord.getMessageUsage(), false, false);
    }
}
