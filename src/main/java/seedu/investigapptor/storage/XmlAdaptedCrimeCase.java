package seedu.investigapptor.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.StartDate;
import seedu.investigapptor.model.crimecase.Status;
import seedu.investigapptor.model.person.Name;
import seedu.investigapptor.model.tag.Tag;

/**
 * JAXB-friendly version of the Investigator.
 */
public class XmlAdaptedCrimeCase {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "CrimeCase's %s field is missing!";

    @XmlElement(required = true)
    private String caseName;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private String currentInvestigator;
    @XmlElement(required = true)
    private String startDate;
    @XmlElement(required = true)
    private String status;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();


    /**
     * Constructs an XmlAdaptedInvestigator.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedCrimeCase() {}

    /**
     * Constructs an {@code XmlAdaptedInvestigator} with the given person details.
     */
    public XmlAdaptedCrimeCase(String caseName, String description, String currentInvestigator, String startDate,
                               String status, List<XmlAdaptedTag> tagged) {
        this.caseName = caseName;
        this.description = description;
        this.currentInvestigator = currentInvestigator;
        this.startDate = startDate;
        this.status = status;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given CrimeCase into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedInvestigator
     */
    public XmlAdaptedCrimeCase(CrimeCase source) {
        caseName = source.getCaseName().crimeCaseName;
        description = source.getDescription().toString();
        currentInvestigator = source.getCurrentInvestigator().toString();
        startDate = source.getStartDate().date;
        status = source.getStatus().toString();
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted crimecase object into the model's CrimeCase object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted crimecase
     */
    public CrimeCase toModelType() throws IllegalValueException {
        final List<Tag> crimeCaseTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            crimeCaseTags.add(tag.toModelType());
        }

        if (this.caseName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    CaseName.class.getSimpleName()));
        }
        if (!Name.isValidName(this.caseName)) {
            throw new IllegalValueException(CaseName.MESSAGE_CASE_NAME_CONSTRAINTS);
        }
        final CaseName caseName = new CaseName(this.caseName);

        if (this.description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(this.description)) {
            throw new IllegalValueException(Description.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        final Description description = new Description(this.description);

        if (this.currentInvestigator == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        //Todo: Add constraint for non existent investigator name
        final Name currentInvestigator = new Name(this.currentInvestigator);

        if (this.startDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    StartDate.class.getSimpleName()));
        }
        if (!StartDate.isValidDate(this.startDate)) {
            throw new IllegalValueException(StartDate.MESSAGE_DATE_CONSTRAINTS);
        }
        final StartDate startDate = new StartDate(this.startDate);

        final Status status = new Status();

        final Set<Tag> tags = new HashSet<>(crimeCaseTags);
        return new CrimeCase(caseName, description, currentInvestigator, startDate, status, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedCrimeCase)) {
            return false;
        }

        XmlAdaptedCrimeCase otherCrimeCase = (XmlAdaptedCrimeCase) other;
        return Objects.equals(caseName, otherCrimeCase.caseName)
                && Objects.equals(description, otherCrimeCase.description)
                && Objects.equals(currentInvestigator, otherCrimeCase.currentInvestigator)
                && Objects.equals(startDate, otherCrimeCase.startDate)
                && Objects.equals(status, otherCrimeCase.status)
                && tagged.equals(otherCrimeCase.tagged);
    }
}
