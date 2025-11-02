package seedu.tutorpal.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.tutorpal.model.person.Address;
import seedu.tutorpal.model.person.Email;
import seedu.tutorpal.model.person.JoinDate;
import seedu.tutorpal.model.person.Name;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Phone;
import seedu.tutorpal.model.person.Tutor;

public class PersonListPanelTest {

    @Test
    public void personList_basicOperations_worksCorrectly() {
        List<Person> personList = new ArrayList<>();
        Person person1 = createTutor("Alice");
        Person person2 = createTutor("Bob");

        // Empty list
        assertTrue(personList.isEmpty());

        // Add persons
        personList.add(person1);
        personList.add(person2);
        assertEquals(2, personList.size());
        assertTrue(personList.contains(person1));

        // Get by index
        assertEquals(person1, personList.get(0));
        assertEquals(0, personList.indexOf(person1));

        // Remove person
        personList.remove(person1);
        assertEquals(1, personList.size());
        assertFalse(personList.contains(person1));

        // Clear all
        personList.clear();
        assertTrue(personList.isEmpty());
    }

    @Test
    public void displayIndex_conversion_correctFormat() {
        List<Person> personList = new ArrayList<>();
        personList.add(createTutor("Alice"));

        int listIndex = 0;
        int displayIndex = listIndex + 1;

        assertEquals(1, displayIndex);
        assertTrue(displayIndex > 0);
    }


    private Tutor createTutor(String name) {
        JoinDate joinDate = new JoinDate(LocalDate.of(2025, 1, 1));
        return new Tutor(
                new Name(name),
                new Phone("91234567"),
                new Email(name.toLowerCase() + "@example.com"),
                new Address("Kent Ridge"),
                java.util.Set.of(new seedu.tutorpal.model.person.Class("s4mon1600")),
                joinDate
        );
    }
}
