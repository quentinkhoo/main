package seedu.investigapptor.testutil;

import static seedu.investigapptor.testutil.TypicalPersons.POLICE;

import java.util.Set;

import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.StartDate;
import seedu.investigapptor.model.crimecase.Status;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.tag.Tag;
import seedu.investigapptor.model.util.SampleDataUtil;

/**
 * A utility class to help with building CrimeCase objects.
 */
public class CrimeCaseBuilder {

    public static final String DEFAULT_NAME = "Murder Mall";
    public static final String DEFAULT_DESCRIPTION = "Just a Test";
    public static final String DEFAULT_DATE = "01/01/2011";
    public static final String DEFAULT_TAGS = "Homicide";

    private CaseName name;
    private Description description;
    private Person currentInvestigator;
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
     * Initializes the CrimeCaseBuilder with the data of {@code caseToCopy}.
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
     * Sets the {@code Name} of the {@code CrimeCase} that we are building.
     */
    public CrimeCaseBuilder withName(String name) {
        this.name = new CaseName(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code CrimeCase} that we are building.
     */
    public CrimeCaseBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code CrimeCase} that we are building.
     */
    public CrimeCaseBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code Status} of the {@code CrimeCase} that we are building.
     */
    public CrimeCaseBuilder withStatus(String status) {
        this.status = new Status(status);
        return this;
    }

    /**
     * Sets the {@code StartDate} of the {@code CrimeCase} that we are building.
     */
    public CrimeCaseBuilder withStartDate(String date) {
        this.startDate = new StartDate(date);
        return this;
    }

    /**
     * Sets the {@code currentInvestigator} of the {@code CrimeCase} that we are building.
     */
    public CrimeCaseBuilder withCurrentInvestigator(Person investigator) {
        this.currentInvestigator = new PersonBuilder(investigator).build();
        return this;
    }

    public CrimeCase build() {
        return new CrimeCase(name, description, currentInvestigator, startDate, status, tags);
    }

}
