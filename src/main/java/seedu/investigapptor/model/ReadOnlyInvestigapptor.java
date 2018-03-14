package seedu.investigapptor.model;

import javafx.collections.ObservableList;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.tag.Tag;

/**
 * Unmodifiable view of an investigapptor book
 */
public interface ReadOnlyInvestigapptor {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

}
