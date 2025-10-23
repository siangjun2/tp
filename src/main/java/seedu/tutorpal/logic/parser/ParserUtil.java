package seedu.tutorpal.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import seedu.tutorpal.commons.core.index.Index;
import seedu.tutorpal.commons.util.StringUtil;
import seedu.tutorpal.logic.parser.exceptions.ParseException;
import seedu.tutorpal.model.person.Address;
import seedu.tutorpal.model.person.Class;
import seedu.tutorpal.model.person.Email;
import seedu.tutorpal.model.person.JoinMonth;
import seedu.tutorpal.model.person.Name;
import seedu.tutorpal.model.person.Payment;
import seedu.tutorpal.model.person.Phone;
import seedu.tutorpal.model.person.Role;
import seedu.tutorpal.model.person.WeeklyAttendance;
import seedu.tutorpal.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser
 * classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading
     * and trailing whitespaces will be
     * trimmed.
     * 
     * @throws ParseException if the specified index is invalid (not non-zero
     *                        unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
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
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
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

    /**
     * Parses a {@code String paymentStatus} into a {@code Payment}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code paymentStatus} is invalid.
     */
    public static Payment parsePayment(String paymentStatus) throws ParseException {
        requireNonNull(paymentStatus);
        String trimmedPayment = paymentStatus.trim();
        if (!Tag.isValidTagName(trimmedPayment)) {
            throw new ParseException(Payment.MESSAGE_CONSTRAINTS);
        }
        return new Payment(trimmedPayment);
    }
}
