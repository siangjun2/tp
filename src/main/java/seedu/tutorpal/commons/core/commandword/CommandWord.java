package seedu.tutorpal.commons.core.commandword;

import seedu.tutorpal.logic.commands.AddCommand;
import seedu.tutorpal.logic.commands.ClearCommand;
import seedu.tutorpal.logic.commands.Command;
import seedu.tutorpal.logic.commands.DeleteCommand;
import seedu.tutorpal.logic.commands.ExitCommand;
import seedu.tutorpal.logic.commands.FindCommand;
import seedu.tutorpal.logic.commands.ListCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CommandWord {
    public static final List<Class<? extends Command>> COMMANDS = List.of(
        AddCommand.class,
        ClearCommand.class,
        DeleteCommand.class,
        ExitCommand.class,
        FindCommand.class,
        ListCommand.class
    );

    private static HashMap<String, CommandWord> commandWords = new HashMap<>();

    private String command;

    private CommandWord(String command) {
        this.command = command;
    }

    public String getMessageUsage() {
        return COMMANDS.stream()
            .filter(f -> {
                try {
                    String currCommandWord = (String) f.getField("COMMAND_WORD").get(null);
                    return currCommandWord.equals(this.command);
                } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                    return false;
                }
            })
            .map(f -> {
                try {
                    return (String) f.getField("MESSAGE_USAGE").get(null);
                } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
                    return "";
                }
            })
            .reduce("", (a, b) -> a + b);
    }

    public static CommandWord of(String command) {
        assert command != null;
        if (commandWords.containsKey(command)) {
            return commandWords.get(command);
        }

        CommandWord newCommandWord = new CommandWord(command);
        commandWords.put(command, newCommandWord);
        return newCommandWord;
    }
}
