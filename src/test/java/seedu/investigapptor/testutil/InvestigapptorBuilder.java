package seedu.investigapptor.testutil;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Password;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.tag.Tag;

/**
 * A utility class to help with building Investigapptor objects.
 * Example usage: <br>
 *     {@code Investigapptor ab = new InvestigapptorBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class InvestigapptorBuilder {

    private Investigapptor investigapptor;

    public InvestigapptorBuilder() {
        investigapptor = new Investigapptor();
    }

    public InvestigapptorBuilder(Investigapptor investigapptor) {
        this.investigapptor = investigapptor;
    }

    /**
     * Adds a new {@code CrimeCase} to the {@code Investigapptor} that we are building.
     */
    public InvestigapptorBuilder withCrimeCase(CrimeCase crimeCase) {
        try {
            investigapptor.addCrimeCase(crimeCase);
        } catch (DuplicateCrimeCaseException dce) {
            throw new IllegalArgumentException("case is expected to be unique.");
        }
        return this;
    }

    /**
     * Adds a new {@code Person} to the {@code Investigapptor} that we are building.
     */
    public InvestigapptorBuilder withPerson(Person person) {
        try {
            investigapptor.addPerson(person);
        } catch (DuplicatePersonException dpe) {
            throw new IllegalArgumentException("person is expected to be unique.");
        }
        return this;
    }

    /**
     * Parses {@code tagName} into a {@code Tag} and adds it to the {@code Investigapptor} that we are building.
     */
    public InvestigapptorBuilder withTag(String tagName) {
        try {
            investigapptor.addTag(new Tag(tagName));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("tagName is expected to be valid.");
        }
        return this;
    }

    //@@author quentinkhoo
    /**
     * Parses {@code password} into a {@code Password} and adds it to the {@code Investigapptor} that we are building.
     */
    public InvestigapptorBuilder withPassword(String password) {
        investigapptor.updatePassword(new Password(password));
        return this;
    }
    //@@author

    public Investigapptor build() {
        return investigapptor;
    }
}
