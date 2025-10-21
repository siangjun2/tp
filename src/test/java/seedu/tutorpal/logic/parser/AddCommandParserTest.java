package seedu.tutorpal.logic.parser;

import static seedu.tutorpal.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.tutorpal.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.tutorpal.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.CLASS_DESC_AMY;
import static seedu.tutorpal.logic.commands.CommandTestUtil.CLASS_DESC_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.tutorpal.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.tutorpal.logic.commands.CommandTestUtil.INVALID_CLASS_DESC;
import static seedu.tutorpal.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.tutorpal.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.tutorpal.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.tutorpal.logic.commands.CommandTestUtil.INVALID_ROLE_DESC;
import static seedu.tutorpal.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.tutorpal.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.tutorpal.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.tutorpal.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.tutorpal.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.tutorpal.logic.commands.CommandTestUtil.ROLE_DESC_AMY;
import static seedu.tutorpal.logic.commands.CommandTestUtil.ROLE_DESC_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.tutorpal.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.tutorpal.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.tutorpal.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.tutorpal.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.tutorpal.testutil.TypicalPersons.AMY;
import static seedu.tutorpal.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.logic.Messages;
import seedu.tutorpal.logic.commands.AddCommand;
import seedu.tutorpal.model.person.Address;
import seedu.tutorpal.model.person.Class;
import seedu.tutorpal.model.person.Email;
import seedu.tutorpal.model.person.Name;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Phone;
import seedu.tutorpal.model.person.Role;
import seedu.tutorpal.model.tag.Tag;
import seedu.tutorpal.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Person expectedPerson = new PersonBuilder(BOB).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + TAG_DESC_FRIEND, new AddCommand(expectedPerson));


        // multiple tags - all accepted
        Person expectedPersonMultipleTags = new PersonBuilder(BOB)
                .build();
        assertParseSuccess(parser,
                ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleTags));
    }

    @Test
    public void parse_multipleClasses_success() {
        // Multiple classes - all accepted
        Person expectedPersonMultipleClasses = new PersonBuilder(BOB)
                .withClasses("s4mon1600", "s4wed1400")
                .build();
        assertParseSuccess(parser,
                ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + " c/s4mon1600 c/s4wed1400" + TAG_DESC_FRIEND,
                new AddCommand(expectedPersonMultipleClasses));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple roles
        assertParseFailure(parser, ROLE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + ROLE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ROLE, PREFIX_ADDRESS,
                        PREFIX_EMAIL, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid role
        assertParseFailure(parser, INVALID_ROLE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid role
        assertParseFailure(parser, validExpectedPersonString + INVALID_ROLE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags, address optional (defaults to "-")
        Person expectedPerson = new PersonBuilder(AMY).withAddress("-").build();
        assertParseSuccess(parser, ROLE_DESC_AMY + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + CLASS_DESC_AMY,
                new AddCommand(expectedPerson));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        String expectedMessageMissingClass = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                "At least one class must be specified using c/ prefix");

        // missing role prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB, expectedMessage);

        // missing name prefix
        assertParseFailure(parser, ROLE_DESC_BOB + VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, ROLE_DESC_BOB + NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB, expectedMessage);

        // missing email prefix
        assertParseFailure(parser, ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB, expectedMessage);

        // missing class prefix
        assertParseFailure(parser, ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB, expectedMessageMissingClass);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, ROLE_DESC_BOB + INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, ROLE_DESC_BOB + NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid role
        assertParseFailure(parser, INVALID_ROLE_DESC + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Role.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + INVALID_ADDRESS_DESC + CLASS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Address.MESSAGE_CONSTRAINTS);

        // invalid class
        assertParseFailure(parser, ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + INVALID_CLASS_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Class.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + CLASS_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, ROLE_DESC_BOB + INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + INVALID_ADDRESS_DESC + CLASS_DESC_BOB, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + ROLE_DESC_BOB + NAME_DESC_BOB + PHONE_DESC_BOB
                + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + CLASS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidRole_failure() {
        // invalid role value (not student or tutor)
        assertParseFailure(parser, " r/teacher" + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + CLASS_DESC_BOB + TAG_DESC_FRIEND, Role.MESSAGE_CONSTRAINTS);
    }
}
