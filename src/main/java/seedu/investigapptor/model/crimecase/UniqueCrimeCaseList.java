package seedu.investigapptor.model.crimecase;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.investigapptor.commons.util.CollectionUtil;
import seedu.investigapptor.model.crimecase.exceptions.CrimeCaseNotFoundException;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.tag.Tag;

/**
 * A list of crimecases that enforces uniqueness between its elements and does not allow nulls.
 * <p>
 * Supports a minimal set of list operations.
 *
 * @see CrimeCase#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueCrimeCaseList implements Iterable<CrimeCase> {

    private final ObservableList<CrimeCase> internalList = FXCollections.observableArrayList();

    public UniqueCrimeCaseList(){}

    public UniqueCrimeCaseList(Set<CrimeCase> cases) {
        requireAllNonNull(cases);
        internalList.addAll(cases);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns true if the list contains an equivalent person as the given argument.
     */
    public boolean contains(CrimeCase toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a crimeCase to the list.
     *
     * @throws DuplicateCrimeCaseException if the person to add is a duplicate of an existing person in the list.
     */
    public boolean add(CrimeCase toAdd) throws DuplicateCrimeCaseException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateCrimeCaseException();
        }
        internalList.add(toAdd);
        return true;
    }

    /**
     * Replaces the crimeCase {@code target} in the list with {@code editedCrimeCase}.
     *
     * @throws DuplicateCrimeCaseException if the replacement is equivalent to another existing crimecase in the list.
     * @throws CrimeCaseNotFoundException  if {@code target} could not be found in the list.
     */
    public void setCrimeCase(CrimeCase crimeCase, CrimeCase editedCrimeCase)
            throws DuplicateCrimeCaseException, CrimeCaseNotFoundException {
        requireNonNull(editedCrimeCase);

        int index = internalList.indexOf(crimeCase);
        if (index == -1) {
            throw new CrimeCaseNotFoundException();
        }

        if (!crimeCase.equals(editedCrimeCase) && internalList.contains(editedCrimeCase)) {
            throw new DuplicateCrimeCaseException();
        }

        internalList.set(index, editedCrimeCase);
    }

    /**
     * Removes the equivalent person from the list.
     *
     * @throws CrimeCaseNotFoundException if no such person could be found in the list.
     */
    public boolean remove(CrimeCase toRemove) throws CrimeCaseNotFoundException {
        requireNonNull(toRemove);
        final boolean crimeCaseFoundAndDeleted = internalList.remove(toRemove);
        if (!crimeCaseFoundAndDeleted) {
            throw new CrimeCaseNotFoundException();
        }
        return crimeCaseFoundAndDeleted;
    }

    public void setCrimeCases(UniqueCrimeCaseList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setCrimeCases(List<CrimeCase> crimeCases) throws DuplicateCrimeCaseException {
        requireAllNonNull(crimeCases);
        final UniqueCrimeCaseList replacement = new UniqueCrimeCaseList();
        for (final CrimeCase crimeCase : crimeCases) {
            replacement.add(crimeCase);
        }
        setCrimeCases(replacement);
    }

    /**
     * Deletes {@code toDelete} tag from every crimecase in internalList
     */
    public void deleteTagFromCrimeCases(Tag toDelete) {
        for (CrimeCase crimeCase : internalList) {
            crimeCase.deleteTag(toDelete);
        }
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<CrimeCase> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<CrimeCase> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueCrimeCaseList // instanceof handles nulls
                && this.internalList.equals(((UniqueCrimeCaseList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
