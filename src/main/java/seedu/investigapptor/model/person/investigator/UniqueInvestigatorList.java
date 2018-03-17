package seedu.investigapptor.model.person.investigator;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.investigapptor.commons.util.CollectionUtil;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.exceptions.PersonNotFoundException;
import seedu.investigapptor.model.tag.Tag;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * <p>
 * Supports a minimal set of list operations.
 *
 * @see Investigator#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueInvestigatorList implements Iterable<Investigator> {

    private final ObservableList<Investigator> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(Investigator toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a person to the list.
     *
     * @throws DuplicatePersonException if the person to add is a duplicate of an existing person in the list.
     */
    public void add(Investigator toAdd) throws DuplicatePersonException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the person {@code target} in the list with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if the replacement is equivalent to another existing person in the list.
     * @throws PersonNotFoundException  if {@code target} could not be found in the list.
     */
    public void setInvestigator(Investigator target, Investigator editedInvestigator)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedInvestigator);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        if (!target.equals(editedInvestigator) && internalList.contains(editedInvestigator)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedInvestigator);
    }

    /**
     * Removes the equivalent person from the list.
     *
     * @throws PersonNotFoundException if no such person could be found in the list.
     */
    public boolean remove(Investigator toRemove) throws PersonNotFoundException {
        requireNonNull(toRemove);
        final boolean investigatorFoundAndDeleted = internalList.remove(toRemove);
        if (!investigatorFoundAndDeleted) {
            throw new PersonNotFoundException();
        }
        return investigatorFoundAndDeleted;
    }

    public void setInvestigators(UniqueInvestigatorList replacement) {
        this.internalList.setAll(replacement.internalList);
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
        for (Investigator investigator : internalList) {
            investigator.deleteTag(toDelete);
        }
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Investigator> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Investigator> iterator() {
        return internalList.iterator();
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
