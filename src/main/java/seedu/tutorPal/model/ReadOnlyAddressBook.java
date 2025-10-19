package seedu.tutorPal.model;

import javafx.collections.ObservableList;
import seedu.tutorPal.model.person.Person;

/**
 * Unmodifiable view of an tutorPal book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

}
