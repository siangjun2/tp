package seedu.tutorpal.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.tutorpal.model.AddressBook;
import seedu.tutorpal.model.ReadOnlyAddressBook;
import seedu.tutorpal.model.person.Address;
import seedu.tutorpal.model.person.Class;
import seedu.tutorpal.model.person.Email;
import seedu.tutorpal.model.person.Name;
import seedu.tutorpal.model.person.Person;
import seedu.tutorpal.model.person.Phone;
import seedu.tutorpal.model.person.Role;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
        public static Person[] getSamplePersons() {
                return new Person[] {
                                new Person(new Name("Alice Tan"), new Phone("98765432"),
                                                new Email("alicetan@example.com"),
                                                new Role("student"), new Address("Blk 123 Clementi Ave 3, #05-12"),
                                                getClassSet("s4mon1600")),
                                new Person(new Name("Benjamin Wong"), new Phone("87654321"),
                                                new Email("benwong@example.com"),
                                                new Role("student"), new Address("Blk 456 Jurong West St 42, #08-20"),
                                                getClassSet("s3tue1400", "s3fri1000")),
                                new Person(new Name("Catherine Lim"), new Phone("91234567"),
                                                new Email("catherinelim@example.com"),
                                                new Role("tutor"), new Address("Blk 789 Ang Mo Kio Ave 5, #10-15"),
                                                getClassSet("s1mon0900", "s2wed1400")),
                                new Person(new Name("Daniel Koh"), new Phone("92345678"),
                                                new Email("danielkoh@example.com"),
                                                new Role("student"), new Address("Blk 234 Bedok North St 1, #12-34"),
                                                getClassSet("s5thu1600")),
                                new Person(new Name("Emily Chen"), new Phone("93456789"),
                                                new Email("emilychen@example.com"),
                                                new Role("student"), new Address("Blk 567 Tampines Ave 7, #06-45"),
                                                getClassSet("s2mon1000")),
                                new Person(new Name("Francis Ng"), new Phone("94567890"),
                                                new Email("francisng@example.com"),
                                                new Role("tutor"), new Address("Blk 890 Hougang St 91, #14-22"),
                                                getClassSet("s4mon1600", "s5wed1400"))
                };
        }

        public static ReadOnlyAddressBook getSampleAddressBook() {
                AddressBook sampleAb = new AddressBook();
                for (Person samplePerson : getSamplePersons()) {
                        sampleAb.addPerson(samplePerson);
                }
                return sampleAb;
        }

        /**
         * Returns a class set containing the list of strings given.
         */
        public static Set<Class> getClassSet(String... strings) {
                return Arrays.stream(strings)
                                .map(Class::new)
                                .collect(Collectors.toSet());
        }

}
