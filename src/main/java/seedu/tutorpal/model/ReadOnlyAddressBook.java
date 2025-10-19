package seedu.tutorpal.model;

import javafx.collections.ObservableList;
import seedu.tutorpal.model.person.Person;

/**
 * Unmodifiable view of an tutorpal book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
