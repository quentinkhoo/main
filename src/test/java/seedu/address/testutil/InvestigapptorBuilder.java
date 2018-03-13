package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Investigapptor;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;

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

    public Investigapptor build() {
        return investigapptor;
    }
}
