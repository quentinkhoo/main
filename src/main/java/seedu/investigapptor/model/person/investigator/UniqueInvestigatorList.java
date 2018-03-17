package seedu.investigapptor.model.person.investigator;

import static seedu.investigapptor.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.investigapptor.commons.util.CollectionUtil;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.exceptions.PersonNotFoundException;
import seedu.investigapptor.model.person.UniquePersonList;
import seedu.investigapptor.model.tag.Tag;

/**
 * A list of investigators that enforces uniqueness between its elements and does not allow nulls.
 * <p>
 * Supports a minimal set of list operations.
 *
 * @see Investigator#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueInvestigatorList extends UniquePersonList {

    private final ObservableList<Investigator> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent investigator as the given argument.
     */
    public boolean contains(Investigator toCheck) {
        return super.contains(toCheck);
    }

    /**
     * Adds a investigator to the list.
     *
     * @throws DuplicatePersonException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(Investigator toAdd) throws DuplicatePersonException {
        super.add(toAdd);
    }

    /**
     * Replaces the investigator {@code target} in the list with {@code editedInvestigator}.
     *
     * @throws DuplicatePersonException if the replacement is equivalent to another existing investigator in the list.
     * @throws PersonNotFoundException  if {@code target} could not be found in the list.
     */
    public void setInvestigator(Investigator target, Investigator editedInvestigator)
            throws DuplicatePersonException, PersonNotFoundException {
        super.setPerson(target, editedInvestigator);
    }

    /**
     * Removes the equivalent investigator from the list.
     *
     * @throws PersonNotFoundException if no such investigator could be found in the list.
     */
    public boolean remove(Investigator toRemove) throws PersonNotFoundException {
        return super.remove(toRemove);
    }

    public void setInvestigators(UniqueInvestigatorList replacement) {
        super.setPersons(replacement);
    }

    public void setInvestigators(List<Investigator> investigators) throws DuplicatePersonException {
        requireAllNonNull(investigators);
        final UniqueInvestigatorList replacement = new UniqueInvestigatorList();
        for (final Investigator investigator : investigators) {
            replacement.add(investigator);
        }
        setInvestigators(replacement);
    }

    /**
     * Deletes {@code toDelete} tag from every person in internalList
     */
    public void deleteTagFromInvestigators(Tag toDelete) {
        super.deleteTagFromPersons(toDelete);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Investigator> asObservableInvestigatorList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueInvestigatorList // instanceof handles nulls
                && this.internalList.equals(((UniqueInvestigatorList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
