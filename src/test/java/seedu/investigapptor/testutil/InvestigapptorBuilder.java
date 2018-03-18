package seedu.investigapptor.testutil;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.investigator.Investigator;
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
     * Adds a new {@code Person} to the {@code Investigapptor} that we are building.
     */
    public InvestigapptorBuilder withInvestigator(Investigator investigator) {
        try {
            investigapptor.addInvestigator(investigator);
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
