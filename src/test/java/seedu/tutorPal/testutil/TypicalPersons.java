package seedu.tutorPal.testutil;

import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_CLASS_AMY;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_CLASS_BOB;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_ROLE_AMY;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_ROLE_BOB;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.tutorPal.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.tutorPal.model.AddressBook;
import seedu.tutorPal.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253").withRole("student").withClasses("s4mon1600")
            .withTags("friends").withPayment("unpaid").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432").withRole("student")
            .withClasses("s3tue1400").withTags("owesMoney", "friends").withPayment("unpaid").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street").withRole("tutor")
            .withClasses("s1mon0900").withPayment("unpaid").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").withRole("student")
            .withClasses("s5thu1600").withTags("friends").withPayment("unpaid").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("94822241")
            .withEmail("werner@example.com").withAddress("michegan ave").withRole("student")
            .withClasses("s2mon1000").withPayment("unpaid").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("94824271")
            .withEmail("lydia@example.com").withAddress("little tokyo").withRole("tutor")
            .withClasses("s3wed1400").withPayment("unpaid").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("94824421")
            .withEmail("anna@example.com").withAddress("4th street").withRole("student")
            .withClasses("s4fri1500").withPayment("unpaid").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("84824241")
            .withEmail("stefan@example.com").withAddress("little india").withRole("student")
            .withClasses("s2wed1000").withPayment("unpaid").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("84821311")
            .withEmail("hans@example.com").withAddress("chicago ave").withRole("tutor")
            .withClasses("s1fri0900").withPayment("unpaid").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withRole(VALID_ROLE_AMY).withAddress(VALID_ADDRESS_AMY)
            .withClasses(VALID_CLASS_AMY).withTags(VALID_TAG_FRIEND).withPayment("unpaid").build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withRole(VALID_ROLE_BOB).withAddress(VALID_ADDRESS_BOB)
            .withClasses(VALID_CLASS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).withPayment("unpaid").build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
