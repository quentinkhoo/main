package seedu.investigapptor.model;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.investigapptor.commons.core.ComponentManager;
import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.events.model.InvestigapptorChangedEvent;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.exceptions.PersonNotFoundException;
import seedu.investigapptor.model.tag.Tag;
import seedu.investigapptor.model.tag.exceptions.TagNotFoundException;

/**
 * Represents the in-memory model of the investigapptor book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Investigapptor investigapptor;
    private final FilteredList<Investigator> filteredInvestigators;
    private final FilteredList<CrimeCase> filteredCrimeCases;
    /**
     * Initializes a ModelManager with the given investigapptor and userPrefs.
     */
    public ModelManager(ReadOnlyInvestigapptor investigapptor, UserPrefs userPrefs) {
        super();
        requireAllNonNull(investigapptor, userPrefs);

        logger.fine("Initializing with investigapptor book: " + investigapptor + " and user prefs " + userPrefs);

        this.investigapptor = new Investigapptor(investigapptor);
        filteredInvestigators = new FilteredList<>(this.investigapptor.getInvestigatorList());
        filteredCrimeCases = new FilteredList<>(this.investigapptor.getCrimeCaseList());
    }

    public ModelManager() {
        this(new Investigapptor(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyInvestigapptor newData) {
        investigapptor.resetData(newData);
        indicateInvestigapptorChanged();
    }

    @Override
    public ReadOnlyInvestigapptor getInvestigapptor() {
        return investigapptor;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateInvestigapptorChanged() {
        raise(new InvestigapptorChangedEvent(investigapptor));
    }

    @Override
    public synchronized void deleteInvestigator(Investigator target) throws PersonNotFoundException {
        investigapptor.removeInvestigator(target);
        indicateInvestigapptorChanged();
    }

    @Override
    public synchronized void addInvestigator(Investigator investigator) throws DuplicatePersonException {
        investigapptor.addInvestigator(investigator);
        updateFilteredInvestigatorList(PREDICATE_SHOW_ALL_INVESTIGATORS);
        indicateInvestigapptorChanged();
    }

    @Override
    public void updateInvestigator(Investigator target, Investigator editedInvestigator)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(target, editedInvestigator);

        investigapptor.updateInvestigator(target, editedInvestigator);
        indicateInvestigapptorChanged();
    }

    @Override
    public synchronized void addCrimeCase(CrimeCase crimecase) throws DuplicateCrimeCaseException {
        investigapptor.addCrimeCase(crimecase);
        updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        indicateInvestigapptorChanged();
    }
    @Override
    public void deleteTag(Tag toDelete) throws TagNotFoundException {
        investigapptor.deleteTag(toDelete);
    }
    //=========== Filtered Investigator List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code investigapptor}
     */
    @Override
    public ObservableList<Investigator> getFilteredInvestigatorList() {
        return FXCollections.unmodifiableObservableList(filteredInvestigators);
    }

    @Override
    public void updateFilteredInvestigatorList(Predicate<Investigator> predicate) {
        requireNonNull(predicate);
        filteredInvestigators.setPredicate(predicate);
    }

    //=========== Filtered Cases List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code CrimeCase} backed by the internal list of
     * {@code investigapptor}
     */
    @Override
    public ObservableList<CrimeCase> getFilteredCrimeCaseList() {
        return FXCollections.unmodifiableObservableList(filteredCrimeCases);
    }

    @Override
    public void updateFilteredCrimeCaseList(Predicate<CrimeCase> predicate) {
        requireNonNull(predicate);
        filteredCrimeCases.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return investigapptor.equals(other.investigapptor)
                && filteredInvestigators.equals(other.filteredInvestigators)
                && filteredCrimeCases.equals(other.filteredCrimeCases);
    }

}
