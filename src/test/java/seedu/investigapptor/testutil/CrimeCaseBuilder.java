package seedu.investigapptor.testutil;

import static seedu.investigapptor.testutil.TypicalPersons.POLICE;

import java.util.Set;

import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.StartDate;
import seedu.investigapptor.model.crimecase.Status;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.tag.Tag;
import seedu.investigapptor.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class CrimeCaseBuilder {

    public static final String DEFAULT_NAME = "Murder Mall";
    public static final String DEFAULT_DESCRIPTION = "Just a Test";
    public static final String DEFAULT_DATE = "01/01/2011";
    public static final String DEFAULT_TAGS = "Homicide";

    private CaseName name;
    private Description description;
    private Investigator currentInvestigator;
    private StartDate startDate;
    private Status status;
    private Set<Tag> tags;


    public CrimeCaseBuilder() {
        name = new CaseName(DEFAULT_NAME);
        description = new Description(DEFAULT_DESCRIPTION);
        status = new Status();
        startDate = new StartDate(DEFAULT_DATE);
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
        currentInvestigator = new InvestigatorBuilder(POLICE).build();

    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public CrimeCaseBuilder(CrimeCase caseToCopy) {
        name = caseToCopy.getCaseName();
        description = caseToCopy.getDescription();
        status = caseToCopy.getStatus();
        startDate = caseToCopy.getStartDate();
        currentInvestigator = caseToCopy.getCurrentInvestigator();
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public CrimeCaseBuilder withName(String name) {
        this.name = new CaseName(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public CrimeCaseBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public CrimeCaseBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public CrimeCaseBuilder toggleStatus() {
        this.status.toggleCase();
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public CrimeCaseBuilder withStartDate(String date) {
        this.startDate = new StartDate(date);
        return this;
    }

    public CrimeCase build() {
        return new CrimeCase(name, description, currentInvestigator, startDate, status, tags);
    }

}
