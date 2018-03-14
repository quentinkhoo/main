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

    /**
     * Initializes a ModelManager with the given investigapptor and userPrefs.
     */
    public ModelManager(ReadOnlyInvestigapptor investigapptor, UserPrefs userPrefs) {
        super();
        requireAllNonNull(investigapptor, userPrefs);

        logger.fine("Initializing with investigapptor book: " + investigapptor + " and user prefs " + userPrefs);

        this.investigapptor = new Investigapptor(investigapptor);
        filteredPersons = new FilteredList<>(this.investigapptor.getPersonList());
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
    @Override
    public void deleteTag(Tag toDelete) throws TagNotFoundException {
        investigapptor.deleteTag(toDelete);
    }
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
                && filteredPersons.equals(other.filteredPersons);
    }

}
