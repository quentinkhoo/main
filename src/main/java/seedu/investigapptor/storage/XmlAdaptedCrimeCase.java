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
import seedu.investigapptor.model.crimecase.Date;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.Status;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.tag.Tag;

/**
 * JAXB-friendly version of the CrimeCase.
 */
public class XmlAdaptedCrimeCase {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Case's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private XmlAdaptedInvestigator investigator;
    @XmlElement(required = true)
    private String startDate;
    @XmlElement(required = true)
    private String endDate;
    @XmlElement(required = true)
    private String status;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedCrimeCase.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedCrimeCase() {}

    /**
     * Constructs an {@code XmlAdaptedCrimeCase} with the given case details.
     */
    public XmlAdaptedCrimeCase(String name, String description, XmlAdaptedInvestigator investigator, String startDate,
                               String status, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.description = description;
        this.investigator = investigator;
        this.startDate = startDate;
        this.endDate = null;
        this.status = status;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given CrimeCase into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedCrimeCase
     */
    public XmlAdaptedCrimeCase(CrimeCase source) {
        name = source.getCaseName().crimeCaseName;
        description = source.getDescription().description;
        investigator = new XmlAdaptedInvestigator(source.getCurrentInvestigator());
        startDate = source.getStartDate().date;
        endDate = source.getEndDate().date;
        status = source.getStatus().toString();
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's CrimeCase object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted case
     */
    public CrimeCase toModelType() throws IllegalValueException {
        final List<Tag> crimeCaseTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            crimeCaseTags.add(tag.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    CaseName.class.getSimpleName()));
        }
        if (!CaseName.isValidCaseName(this.name)) {
            throw new IllegalValueException(CaseName.MESSAGE_CASE_NAME_CONSTRAINTS);
        }
        final CaseName name = new CaseName(this.name);

        if (this.description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(this.description)) {
            throw new IllegalValueException(Description.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        final Description description = new Description(this.description);
        if (this.investigator == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Person.class.getSimpleName()));
        }
        final Investigator investigator = this.investigator.toModelType();

        if (this.startDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(this.startDate)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        final Date startDate = new Date(this.startDate);

        if (this.status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Status.class.getSimpleName()));
        }
        if (!Status.isValidStatus(this.status)) {
            throw new IllegalValueException(Status.MESSAGE_STATUS_CONSTRAINTS);
        }
        final Status status = new Status(this.status);

        final Set<Tag> tags = new HashSet<>(crimeCaseTags);
        return new CrimeCase(name, description, investigator, startDate, status, tags);
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
        return Objects.equals(name, otherCrimeCase.name)
                && Objects.equals(description, otherCrimeCase.description)
                && Objects.equals(investigator, otherCrimeCase.investigator)
                && Objects.equals(startDate, otherCrimeCase.startDate)
                && Objects.equals(status, otherCrimeCase.status)
                && tagged.equals(otherCrimeCase.tagged);
    }
}
