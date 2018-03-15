package seedu.investigapptor.model.person.investigator;

import java.util.Set;

import javafx.collections.ObservableList;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.UniqueCrimeCaseList;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.Address;
import seedu.investigapptor.model.person.Email;
import seedu.investigapptor.model.person.Name;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.Phone;
import seedu.investigapptor.model.tag.Tag;

/**
 * Represents a Investigator in the investigapptor book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Investigator extends Person {

    private UniqueCrimeCaseList crimeCases;

    /**
     * Every field must be present and not null.
     */
    public Investigator(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
        crimeCases = new UniqueCrimeCaseList();
    }

    public void addCrimeCase(CrimeCase caseToAdd) throws DuplicateCrimeCaseException {
        crimeCases.add(caseToAdd);
    }

    /**
     * Returns an immutable crime case set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public ObservableList<CrimeCase> getCrimeCases() {
        return crimeCases.asObservableList();
    }

    public void removeCrimeCase(CrimeCase caseToRemove) {
        crimeCases.remove(caseToRemove);
    }
}
