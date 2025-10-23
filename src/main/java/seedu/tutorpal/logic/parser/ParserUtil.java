package seedu.tutorpal.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.tutorpal.commons.core.commandword.CommandWord.COMMANDS;
import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.tutorpal.commons.core.commandword.CommandWord;
import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.commons.util.StringUtil;
import seedu.tutorpal.logic.commands.HelpCommand;
import seedu.tutorpal.logic.parser.exceptions.ParseException;
import seedu.tutorpal.model.person.Address;
import seedu.tutorpal.model.person.Class;
import seedu.tutorpal.model.person.Email;
import seedu.tutorpal.model.person.JoinMonth;
import seedu.tutorpal.model.person.Name;
import seedu.tutorpal.model.person.Phone;
import seedu.tutorpal.model.person.Role;
import seedu.tutorpal.model.person.WeeklyAttendance;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 * <p>
 * All methods in this class are pure utilities: they validate and transform raw {@link String}
 * inputs (typically from user commands) into strongly-typed model objects, or throw a
 * {@link ParseException} if validation fails.
 */
public class ParserUtil {

    /**
     * Error message used when an index fails validation.
     * <p>
     * An index is considered valid only if it is a non-zero unsigned integer (e.g. {@code "1"}, {@code "23"}).
     */
    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading
     * and trailing whitespaces will be
     * trimmed.
     *
     * @param oneBasedIndex string representing a 1-based index
     * @return an {@link Index} corresponding to the given string
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a raw command word string into a cached {@link CommandWord}.
     * <p>
     * This method checks the given {@code commandWord} against the set of known command classes referenced by
     * {@link seedu.tutorpal.commons.core.commandword.CommandWord#COMMANDS}. If the word is not recognised,
     * a {@link ParseException} is thrown with a hint to use {@link HelpCommand}.
     *
     * @param commandWord the raw command word provided by the user (e.g. {@code "add"}, {@code "list"})
     * @return the corresponding {@link CommandWord} instance
     * @throws ParseException if {@code commandWord} does not match any known command
     */
    public static CommandWord parseCommandWord(String commandWord) throws ParseException {
        List<String> commands = COMMANDS.stream()
            .map(f -> {
                try {
                    return (String) f.getField("COMMAND_WORD").get(null);
                } catch (IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
                    return null;
                }
            })
            .collect(Collectors.toUnmodifiableList());

        if (!commands.contains(commandWord)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        return CommandWord.of(commandWord);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param name raw name string
     * @return a validated {@link Name}
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param phone raw phone string
     * @return a validated {@link Phone}
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param address raw address string
     * @return a validated {@link Address}
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param email raw email string
     * @return a validated {@link Email}
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String role} into a {@code Role}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param role raw role string (e.g. {@code "student"} or {@code "tutor"})
     * @return a validated {@link Role}
     * @throws ParseException if the given {@code role} is invalid.
     */
    public static Role parseRole(String role) throws ParseException {
        requireNonNull(role);
        String trimmedRole = role.trim();
        if (!Role.isValidRole(trimmedRole)) {
            throw new ParseException(Role.MESSAGE_CONSTRAINTS);
        }
        return new Role(trimmedRole);
    }

    /**
     * Parses a {@code String className} into a {@code Class}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @param className raw class identifier string
     * @return a validated {@link Class}
     * @throws ParseException if the given {@code className} is invalid.
     */
    public static Class parseClass(String className) throws ParseException {
        requireNonNull(className);
        String trimmedClass = className.trim();
        if (!Class.isValidClass(trimmedClass)) {
            throw new ParseException(Class.MESSAGE_CONSTRAINTS);
        }
        return new Class(trimmedClass);
    }

    /**
     * Parses {@code Collection<String> classes} into a {@code Set<Class>}.
     */
    public static Set<Class> parseClasses(Collection<String> classes) throws ParseException {
        requireNonNull(classes);
        final Set<Class> classSet = new HashSet<>();
        for (String className : classes) {
            classSet.add(parseClass(className));
        }
        return classSet;
    }

    /**
     * Parses a {@code String joinMonth} into a {@code JoinMonth}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code joinMonth} is invalid.
     */
    public static JoinMonth parseJoinMonth(String joinMonth) throws ParseException {
        requireNonNull(joinMonth);
        String trimmedMonth = joinMonth.trim();
        if (!JoinMonth.isValidJoinMonth(trimmedMonth)) {
            throw new ParseException(JoinMonth.MESSAGE_CONSTRAINTS);
        }
        return new JoinMonth(trimmedMonth);
    }

    /**
     * Parses a {@code String weeklyAttendance} into a {@code WeeklyAttendance}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code weeklyAttendance} is invalid.
     */
    public static WeeklyAttendance parseWeeklyAttendance(String weeklyAttendance) throws ParseException {
        requireNonNull(weeklyAttendance);
        String trimmedAttendance = weeklyAttendance.trim();
        if (!WeeklyAttendance.isValidWeeklyAttendance(trimmedAttendance)) {
            throw new ParseException(WeeklyAttendance.MESSAGE_CONSTRAINTS);
        }
        return new WeeklyAttendance(trimmedAttendance);
    }
}
