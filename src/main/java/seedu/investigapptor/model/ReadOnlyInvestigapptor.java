package seedu.investigapptor.model;

import javafx.collections.ObservableList;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.tag.Tag;

/**
 * Unmodifiable view of an investigapptor book
 */
public interface ReadOnlyInvestigapptor {

    /**
     * Returns an unmodifiable view of the investigators list.
     * This list will not contain any duplicate investigators.
     */
    ObservableList<Investigator> getInvestigatorList();

    /**
     * Returns an unmodifiable view of the cases list.
     * This list will not contain any duplicate cases.
     */
    ObservableList<CrimeCase> getCrimeCaseList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

}
