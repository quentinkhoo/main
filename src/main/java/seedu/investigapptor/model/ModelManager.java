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
import seedu.investigapptor.commons.events.model.InvestigapptorBackupEvent;
import seedu.investigapptor.commons.events.model.InvestigapptorChangedEvent;
import seedu.investigapptor.commons.events.ui.FilteredCrimeCaseListChangedEvent;
import seedu.investigapptor.logic.commands.exceptions.InvalidPasswordException;
import seedu.investigapptor.logic.commands.exceptions.NoPasswordException;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.exceptions.CrimeCaseNotFoundException;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.Person;
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
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<CrimeCase> filteredCrimeCases;
    private Password password;
    /**
     * Initializes a ModelManager with the given investigapptor and userPrefs.
     */
    public ModelManager(ReadOnlyInvestigapptor investigapptor, UserPrefs userPrefs) {
        super();
        requireAllNonNull(investigapptor, userPrefs);

        logger.fine("Initializing with investigapptor: " + investigapptor + " and user prefs " + userPrefs);

        this.investigapptor = new Investigapptor(investigapptor);
        filteredCrimeCases = new FilteredList<>(this.investigapptor.getCrimeCaseList());
        filteredPersons = new FilteredList<>(this.investigapptor.getPersonList());
        password = this.investigapptor.getPassword();
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

    //@@author pkaijun
    /** Raises an event to indicate the filtered crime cases list has changed */
    private void indicateFilteredCrimeCaseListChanged() {
        raise(new FilteredCrimeCaseListChangedEvent(filteredCrimeCases));
    }
    //@@author

    @Override
    public synchronized void deletePerson(Person target) throws PersonNotFoundException {
        investigapptor.removePerson(target);
        indicateInvestigapptorChanged();
    }

    @Override
    public synchronized void addPerson(Person person) throws DuplicatePersonException {
        investigapptor.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateInvestigapptorChanged();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(target, editedPerson);

        investigapptor.updatePerson(target, editedPerson);
        indicateInvestigapptorChanged();
    }

    //@@author leowweiching
    @Override
    public synchronized void deleteCrimeCase(CrimeCase target) throws CrimeCaseNotFoundException {
        investigapptor.removeCrimeCase(target);
        indicateInvestigapptorChanged();
    }

    //@@author leowweiching
    @Override
    public synchronized void addCrimeCase(CrimeCase crimecase) throws DuplicateCrimeCaseException {
        investigapptor.addCrimeCase(crimecase);
        updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        indicateInvestigapptorChanged();
    }

    //@@author leowweiching
    @Override
    public void updateCrimeCase(CrimeCase target, CrimeCase editedCase)
            throws DuplicateCrimeCaseException, CrimeCaseNotFoundException {
        requireAllNonNull(target, editedCase);

        investigapptor.updateCrimeCase(target, editedCase);
        indicateInvestigapptorChanged();
    }

    //@@author
    @Override
    public void deleteTag(Tag toDelete) throws TagNotFoundException {
        investigapptor.deleteTag(toDelete);
    }
    @Override
    public void backUpInvestigapptor(String fileName) {
        raise(new InvestigapptorBackupEvent(investigapptor, fileName));
    }

    //@@author quentinkhoo
    @Override
    public void updatePassword(Password password) throws InvalidPasswordException {
        investigapptor.updatePassword(password);
        indicateInvestigapptorChanged();
    }

    @Override
    public void removePassword() throws NoPasswordException {
        try {
            investigapptor.removePassword();
            indicateInvestigapptorChanged();
        } catch (NoPasswordException npe) {
            throw new NoPasswordException(npe.getMessage());
        }
    }
    //@@author

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code investigapptor}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== Filtered Cases List Accessors =============================================================

    //@@author leowweiching
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
        indicateFilteredCrimeCaseListChanged();
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
                && filteredPersons.equals(other.filteredPersons)
                && filteredCrimeCases.equals(other.filteredCrimeCases);
    }

}
