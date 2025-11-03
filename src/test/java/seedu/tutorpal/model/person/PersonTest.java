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
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.testutil.PersonBuilder;

public class PersonTest {

    private static final DateTimeFormatter JOIN_FMT = DateTimeFormatter.ofPattern("dd-MM-uuuu");

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

        // name differs in case, all other attributes same -> returns true
        // (case-insensitive)
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

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

    @Test
    public void student_equalsSameDataWithFixedClock_returnsTrue() {
        Clock fixed = Clock.fixed(Instant.parse("2024-01-15T00:00:00Z"), ZoneOffset.UTC);
        JoinDate jd = new JoinDate(LocalDate.now(fixed)); // same value for both, using fixed clock

        Student s1 = new Student(
                new Name("Chris"),
                new Phone("81111111"),
                new Email("chris@example.com"),
                new Address("Blk 1, Street 1"),
                ALICE.getClasses(),
                jd,
                fixed);
        Student s2 = new Student(
                new Name("Chris"),
                new Phone("81111111"),
                new Email("chris@example.com"),
                new Address("Blk 1, Street 1"),
                ALICE.getClasses(),
                jd,
                fixed);

        assertTrue(s1.equals(s2));
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void printPaymentHistory_lastSixMonths_paidUnpaidOverdue() {
        YearMonth today = YearMonth.now();
        // Join 5 months ago (inclusive) to cover exactly 6 months window
        LocalDate joinLocal = today.minusMonths(5).atDay(1);

        PaymentHistory ph = new PaymentHistory(joinLocal);
        // Mark last month and 3 months ago as paid
        ph = ph.markMonthAsPaid(today.minusMonths(1));
        ph = ph.markMonthAsPaid(today.minusMonths(3));

        Person student = new PersonBuilder()
                .withRole("student")
                .withJoinDate(JOIN_FMT.format(joinLocal))
                .withPaymentHistory(ph)
                .build();

        String out = student.printPaymentHistory();
        String[] tokens = out.trim().split("\\s+");
        assertEquals(6, tokens.length);

        assertTrue(out.contains(today.toString() + ":unpaid"));
        assertTrue(out.contains(today.minusMonths(1).toString() + ":paid")); // last month paid
        assertTrue(out.contains(today.minusMonths(2).toString() + ":overdue")); // an older unpaid month becomes overdue
    }
}
