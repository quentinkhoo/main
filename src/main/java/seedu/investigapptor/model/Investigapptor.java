package seedu.investigapptor.model;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;

import javafx.collections.ObservableList;
import seedu.investigapptor.commons.events.model.InvestigapptorChangedEvent;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.UniqueCrimeCaseList;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.UniquePersonList;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.exceptions.PersonNotFoundException;
import seedu.investigapptor.model.person.investigator.UniqueInvestigatorList;
import seedu.investigapptor.model.tag.Tag;
import seedu.investigapptor.model.tag.UniqueTagList;
import seedu.investigapptor.model.tag.exceptions.TagNotFoundException;

/**
 * Wraps all data at the investigapptor-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class Investigapptor implements ReadOnlyInvestigapptor {

    private final UniqueInvestigatorList investigators;
    private final UniqueCrimeCaseList cases;
    private final UniqueTagList tags;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        investigators = new UniqueInvestigatorList();
        cases = new UniqueCrimeCaseList();
        tags = new UniqueTagList();
    }

    public Investigapptor() {}

    /**
     * Creates an Investigapptor using the Persons and Tags in the {@code toBeCopied}
     */
    public Investigapptor(ReadOnlyInvestigapptor toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setInvestigators(List<Investigator> investigators) throws DuplicatePersonException {
        this.investigators.setInvestigators(investigators);
    }

    public void setCrimeCases(List<CrimeCase> crimeCases) throws DuplicateCrimeCaseException {
        this.cases.setCrimeCases(crimeCases);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    /**
     * Resets the existing data of this {@code Investigapptor} with {@code newData}.
     */
    public void resetData(ReadOnlyInvestigapptor newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Investigator> syncedInvestigatorList = newData.getInvestigatorList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());
        List<CrimeCase> syncedCrimeCaseList = newData.getCrimeCaseList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());
        try {
            setInvestigators(syncedInvestigatorList);
        } catch (DuplicatePersonException e) {
            throw new AssertionError("Investigapptors should not have duplicate investigators");
        }

        try {
            setCrimeCases(syncedCrimeCaseList);
        } catch (DuplicateCrimeCaseException e) {
            throw new AssertionError("Investigapptors should not have duplicate crime cases");
        }
    }
    //// investigator-level operations

    /**
     * Adds a investigator to the investigapptor book.
     * Also checks the new investigator's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #tags}.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addInvestigator(Investigator i) throws DuplicatePersonException {
        Investigator investigator = syncWithMasterTagList(i);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the investigator list.
        investigators.add(investigator);
    }

    /**
     * Replaces the given investigator {@code target} in the list with {@code editedInvestigator}.
     * {@code Investigapptor}'s tag list will be updated with the tags of {@code editedInvestigator}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Investigator)
     */
    public void updateInvestigator(Investigator target, Investigator editedInvestigator)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedInvestigator);

        Investigator syncedEditedInvestigator = syncWithMasterTagList(editedInvestigator);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        investigators.setInvestigator(target, syncedEditedInvestigator);
    }

    /**
     * Removes {@code key} from this {@code Investigapptor}.
     * @throws PersonNotFoundException if the {@code key} is not in this {@code Investigapptor}.
     */
    public boolean removeInvestigator(Investigator key) throws PersonNotFoundException {
        if (investigators.remove(key)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }
    //// case-level operations

    /**
     * Adds a case to the investigapptor book.
     * Also checks the new case's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #tags}.
     *
     * @throws DuplicateCrimeCaseException if an equivalent case already exists.
     */
    public void addCrimeCase(CrimeCase c) throws DuplicateCrimeCaseException {
        CrimeCase crimecase = syncWithMasterTagList(c);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any case
        // in the case list.
        cases.add(crimecase);
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    /**
     * Deletes {@code Investigapptor} from all person and UniqueTagList
     */
    public void deleteTag(Tag toDelete) throws TagNotFoundException {
        if (tags.contains(toDelete)) {
            tags.delete(toDelete);
            investigators.deleteTagFromInvestigators(toDelete);
        } else {
            throw new TagNotFoundException();
        }
    }

    /**
     *  Updates the master tag list to include tags in {@code investigator} that are not in the list.
     *  @return a copy of this {@code investigator} such that every tag in this investigator points to a Tag object in the master
     *  list.
     */
    private Investigator syncWithMasterTagList(Investigator investigator) {
        final UniqueTagList investigatorTags = new UniqueTagList(investigator.getTags());
        tags.mergeFrom(investigatorTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of investigator tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        investigatorTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Investigator(
                investigator.getName(), investigator.getPhone(), investigator.getEmail(), investigator.getAddress(),
                investigator.getCrimeCases(), correctTagReferences);
    }

    /**
     *  Updates the master tag list to include tags in {@code crimecase} that are not in the list.
     *  @return a copy of this {@code crimecase} such that every tag in this case points to a Tag object in the master
     *  list.
     */
    private CrimeCase syncWithMasterTagList(CrimeCase crimecase) {
        final UniqueTagList crimecaseTags = new UniqueTagList(crimecase.getTags());
        tags.mergeFrom(crimecaseTags);

        // Create map with values = tag object references in the master list
        // used for checking case tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of case tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        crimecaseTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new CrimeCase(
                crimecase.getCaseName(), crimecase.getDescription(), crimecase.getCurrentInvestigator(),
                crimecase.getStartDate(), crimecase.getStatus(), correctTagReferences);
    }
    //// util methods

    @Override
    public String toString() {
        return investigators.asObservableList().size() + " investigators, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<Investigator> getInvestigatorList() {
        return investigators.asObservableList();
    }

    @Override
    public ObservableList<CrimeCase> getCrimeCaseList() {
        return cases.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Investigapptor // instanceof handles nulls
                && this.investigators.equals(((Investigapptor) other).investigators)
                && this.cases.equals(((Investigapptor) other).cases)
                && this.tags.equalsOrderInsensitive(((Investigapptor) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(investigators, cases, tags);
    }
}
