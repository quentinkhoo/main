package seedu.investigapptor.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.investigapptor.logic.commands.EditCaseCommand.EditCrimeCaseDescriptor;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Date;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.tag.Tag;

/**
 * A utility class to help with building EditCrimeCaseDescriptor objects.
 */
public class EditCrimeCaseDescriptorBuilder {

    private EditCrimeCaseDescriptor descriptor;

    public EditCrimeCaseDescriptorBuilder() {
        descriptor = new EditCrimeCaseDescriptor();
    }

    public EditCrimeCaseDescriptorBuilder(EditCrimeCaseDescriptor descriptor) {
        this.descriptor = new EditCrimeCaseDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditCrimeCaseDescriptor} with fields containing {@code person}'s details
     */
    public EditCrimeCaseDescriptorBuilder(CrimeCase crimeCase) {
        descriptor = new EditCrimeCaseDescriptor();
        descriptor.setCaseName(crimeCase.getCaseName());
        descriptor.setDescription(crimeCase.getDescription());
        descriptor.setCurrentInvestigator(crimeCase.getCurrentInvestigator());
        descriptor.setStartDate(crimeCase.getStartDate());
        descriptor.setTags(crimeCase.getTags());
    }

    /**
     * Sets the {@code CaseName} of the {@code EditCrimeCaseDescriptor} that we are building.
     */
    public EditCrimeCaseDescriptorBuilder withCaseName(String name) {
        descriptor.setCaseName(new CaseName(name));
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code EditCrimeCaseDescriptor} that we are building.
     */
    public EditCrimeCaseDescriptorBuilder withDescription(String description) {
        descriptor.setDescription(new Description(description));
        return this;
    }

    /**
     * Sets the {@code Investigator} of the {@code EditCrimeCaseDescriptor} that we are building.
     */
    public EditCrimeCaseDescriptorBuilder withInvestigator(Investigator investigator) {
        descriptor.setCurrentInvestigator(investigator);
        return this;
    }

    /**
     * Sets the {@code StartDate} of the {@code EditCrimeCaseDescriptor} that we are building.
     */
    public EditCrimeCaseDescriptorBuilder withStartDate(String startDate) {
        descriptor.setStartDate(new Date(startDate));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditCrimeCaseDescriptor}
     * that we are building.
     */
    public EditCrimeCaseDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditCrimeCaseDescriptor build() {
        return descriptor;
    }
}
