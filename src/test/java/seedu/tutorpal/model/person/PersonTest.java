package seedu.tutorpal.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.tutorpal.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.tutorpal.testutil.Assert.assertThrows;
import static seedu.tutorpal.testutil.TypicalPersons.ALICE;
import static seedu.tutorpal.testutil.TypicalPersons.BOB;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getClasses().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name and phone, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different phone, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // same person -> returns true
        editedAlice = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = ALICE.getClass().getCanonicalName()
                + "{name=" + ALICE.getName()
                + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail()
                + ", address=" + ALICE.getAddress()
                + ", classes=" + ALICE.getClasses()
                + ", joinDate=" + ALICE.getJoinDate()
                + ", paymentHistory=" + ALICE.getPaymentHistory().toString()
                + ", attendanceHistory=" + ALICE.getAttendanceHistory().toString()
                + "}";
        assertEquals(expected, ALICE.toString());
    }

    // --- Tutor tests ---

    @Test
    public void tutor_attendanceHistoryFlagsAndAccess() {
        Tutor tutor = new Tutor(
                new Name("Tutor Tim"),
                new Phone("91234567"),
                new Email("tutor@example.com"),
                new Address("Blk 123, Bedok Ave 1"),
                Set.of());
        assertFalse(tutor.hasAttendanceHistory());
        assertThrows(IllegalStateException.class, tutor::getAttendanceHistory);
    }

    // --- Student tests ---

    @Test
    public void student_hasAttendanceHistory_andJoinDateSynchronized() {
        Student student = new Student(
                new Name("Student Sue"),
                new Phone("98765432"),
                new Email("student@example.com"),
                new Address("Blk 456, Clementi Ave 2"),
                Set.of());
        assertTrue(student.hasAttendanceHistory());
        assertEquals(student.getJoinDate(), student.getAttendanceHistory().getJoinDate());
    }

    @Test
    public void student_equals_sameDataWithFixedClock_returnsTrue() {
        Clock fixed = Clock.fixed(Instant.parse("2024-01-15T00:00:00Z"), ZoneOffset.UTC);
        JoinDate jd = new JoinDate(LocalDate.now(fixed)); // same value for both, using fixed clock

        Student s1 = new Student(
                new Name("Chris"),
                new Phone("81111111"),
                new Email("chris@example.com"),
                new Address("Blk 1, Street 1"),
                Set.of(),
                jd,
                fixed);
        Student s2 = new Student(
                new Name("Chris"),
                new Phone("81111111"),
                new Email("chris@example.com"),
                new Address("Blk 1, Street 1"),
                Set.of(),
                jd,
                fixed);

        assertTrue(s1.equals(s2));
        assertEquals(s1.hashCode(), s2.hashCode());
    }
}
