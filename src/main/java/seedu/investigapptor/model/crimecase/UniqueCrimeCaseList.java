package seedu.investigapptor.model.crimecase;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.CollectionUtil.requireAllNonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.investigapptor.commons.util.CollectionUtil;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.crimecase.exceptions.CrimeCaseNotFoundException;


/**
 * A list of CrimeCases that enforces uniqueness between its elements and does not allow nulls.
 * Supports a minimal set of list operations.
 *
 * @see CrimeCase#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueCrimeCaseList implements Iterable<CrimeCase> {

    private final ObservableList<CrimeCase> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty CrimeCaseList.
     */
    public UniqueCrimeCaseList() {
    }

    /**
     * Returns all tags in this list as a Set.
     * This set is mutable and change-insulated against the internal list.
     */
    public Set<CrimeCase> toSet() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return new HashSet<>(internalList);
    }

    /**
     * Replaces the CrimeCases in this list with those in the argument tag list.
     */
    public void setCrimeCases(Set<CrimeCase> tags) {
        requireAllNonNull(tags);
        internalList.setAll(tags);
        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Ensures every tag in the argument list exists in this object.
     */
    public void mergeFrom(UniqueCrimeCaseList from) {
        final Set<CrimeCase> alreadyInside = this.toSet();
        from.internalList.stream()
                .filter(tag -> !alreadyInside.contains(tag))
                .forEach(internalList::add);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns true if the list contains an equivalent CrimeCase as the given argument.
     */
    public boolean contains(CrimeCase toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a CrimeCase to the list.
     *
     * @throws DuplicateCrimeCaseException if the CrimeCase to add is a duplicate of an existing CrimeCase in the list.
     */
    public void add(CrimeCase toAdd) throws DuplicateCrimeCaseException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateCrimeCaseException();
        }
        internalList.add(toAdd);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Removes a CrimeCase from the list.
     */
    public void remove(CrimeCase toDelete) {
        requireNonNull(toDelete);
        if (contains(toDelete)) {
            internalList.remove(toDelete);
        }
        assert CollectionUtil.elementsAreUnique(internalList);
    }

    @Override
    public Iterator<CrimeCase> iterator() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.iterator();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<CrimeCase> asObservableList() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        return other == this // short circuit if same object
                || (other instanceof UniqueCrimeCaseList // instanceof handles nulls
                && this.internalList.equals(((UniqueCrimeCaseList) other).internalList));
    }

    /**
     * Returns true if the element in this list is equal to the elements in {@code other}.
     * The elements do not have to be in the same order.
     */
    public boolean equalsOrderInsensitive(UniqueCrimeCaseList other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        assert CollectionUtil.elementsAreUnique(other.internalList);
        return this == other || new HashSet<>(this.internalList).equals(new HashSet<>(other.internalList));
    }

    @Override
    public int hashCode() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.hashCode();
    }
}
