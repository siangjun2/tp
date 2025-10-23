package seedu.tutorpal.commons.core.commandword;

import java.util.HashMap;
import java.util.List;

import seedu.tutorpal.logic.commands.AddCommand;
import seedu.tutorpal.logic.commands.ClearCommand;
import seedu.tutorpal.logic.commands.Command;
import seedu.tutorpal.logic.commands.DeleteCommand;
import seedu.tutorpal.logic.commands.ExitCommand;
import seedu.tutorpal.logic.commands.FindCommand;
import seedu.tutorpal.logic.commands.ListCommand;

/**
 * Represents a command word and provides utilities to obtain usage text for that command.
 * <p>
 * This class acts as a lightweight value object around a command word {@link String}, and
 * can resolve the corresponding {@code MESSAGE_USAGE} from known {@link Command} types via reflection.
 * Instances are cached per command word string to avoid repeated allocations.
 * {@code COMMAND_WORD} field equals this instance's {@link #command}. If found, the class' public static
 * {@code MESSAGE_USAGE} field is returned. Any reflection-related exceptions are swallowed and treated
 * as non-matches/empty results.
 * {@link HashMap} with no synchronisation.
 */
public class CommandWord {

    /**
     * Immutable list of known {@link Command} classes to consult when resolving usage text.
     * <p>
     * Each listed class is expected to expose two public static fields:
     * <ul>
     *     <li>{@code COMMAND_WORD}: the canonical command word string</li>
     *     <li>{@code MESSAGE_USAGE}: the usage/help message for that command</li>
     * </ul>
     */
    public static final List<Class<? extends Command>> COMMANDS = List.of(
        AddCommand.class,
        ClearCommand.class,
        DeleteCommand.class,
        ExitCommand.class,
        FindCommand.class,
        ListCommand.class
    );

    /**
     * Cache of created {@link CommandWord} instances keyed by their command string.
     * <p>
     * Avoids repeatedly instantiating identical {@code CommandWord} objects.
     * <strong>Note:</strong> This cache is not synchronised and therefore not thread-safe.
     */
    private static HashMap<String, CommandWord> commandWords = new HashMap<>();

    /**
     * The raw command word string, e.g., {@code "add"} or {@code "list"}.
     */
    private String command;

    /**
     * Constructs a {@code CommandWord} wrapping the given command string.
     *
     * @param command the command word string to wrap; must not be {@code null}
     */
    private CommandWord(String command) {
        this.command = command;
    }

    /**
     * Returns the usage/help message associated with this command word.
     * <p>
     * The method searches {@link #COMMANDS} for a class whose public static {@code COMMAND_WORD}
     * equals this instance's {@link #command}. For each match, it extracts the corresponding public
     * static {@code MESSAGE_USAGE} and concatenates all such messages (if multiple match).
     * <p>
     * Reflection exceptions (e.g., missing fields, access issues) are ignored; unmatched/errored
     * classes contribute nothing to the result.
     *
     * @return the concatenated {@code MESSAGE_USAGE} string(s) for the matching command(s),
     *         or an empty string if no match is found or all lookups fail
     */
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

    /**
     * Returns a cached {@code CommandWord} instance for the given command string, creating and
     * caching a new one if necessary.
     *
     * @param command the command word string; must not be {@code null}
     * @return the {@code CommandWord} instance associated with {@code command}
     * @throws AssertionError if {@code command} is {@code null} and assertions are enabled
     */
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
