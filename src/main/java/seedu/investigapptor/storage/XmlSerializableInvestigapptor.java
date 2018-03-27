package seedu.investigapptor.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.ReadOnlyInvestigapptor;

/**
 * An Immutable Investigapptor that is serializable to XML format
 */
@XmlRootElement(name = "investigapptor")
public class XmlSerializableInvestigapptor {

    @XmlElement
    private List<XmlAdaptedCrimeCase> cases;
    @XmlElement
    private List<XmlAdaptedPerson> persons;
    @XmlElement
    private List<XmlAdaptedTag> tags;
    @XmlElement
    private List<XmlAdaptedInvestigator> investigators;
    @XmlElement
    private XmlAdaptedPassword password;

    /**
     * Creates an empty XmlSerializableInvestigapptor.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableInvestigapptor() {
        cases = new ArrayList<>();
        persons = new ArrayList<>();
        investigators = new ArrayList<>();
        tags = new ArrayList<>();
        password = new XmlAdaptedPassword();
    }

    /**
     * Conversion
     */
    public XmlSerializableInvestigapptor(ReadOnlyInvestigapptor src) {
        this();
        cases.addAll(src.getCrimeCaseList().stream().map(XmlAdaptedCrimeCase::new).collect(Collectors.toList()));
        persons.addAll(src.getPersonOnlyList().stream().map(XmlAdaptedPerson::new).collect(Collectors.toList()));
        investigators.addAll(src.getInvestigatorList().stream()
                .map(XmlAdaptedInvestigator::new).collect(Collectors.toList()));
        tags.addAll(src.getTagList().stream().map(XmlAdaptedTag::new).collect(Collectors.toList()));
        password = new XmlAdaptedPassword(src.getPassword());
    }

    /**
     * Converts this investigapptor into the model's {@code Investigapptor} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     *                               {@code XmlAdaptedCrimeCase}, {@code XmlAdaptedPerson} or {@code XmlAdaptedTag}.
     */
    public Investigapptor toModelType() throws IllegalValueException {
        Investigapptor investigapptor = new Investigapptor();
        for (XmlAdaptedTag t : tags) {
            investigapptor.addTag(t.toModelType());
        }
        for (XmlAdaptedCrimeCase c : cases) {
            investigapptor.addCrimeCase(c.toModelType());
        }
        for (XmlAdaptedPerson p : persons) {
            investigapptor.addPerson(p.toModelType());
        }
        for (XmlAdaptedInvestigator i : investigators) {
            investigapptor.addPerson(i.toModelType());
        }
        investigapptor.updatePassword(password.toModelType());
        return investigapptor;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableInvestigapptor)) {
            return false;
        }

        XmlSerializableInvestigapptor otherAb = (XmlSerializableInvestigapptor) other;
        return cases.equals(otherAb.cases) && persons.equals(otherAb.persons) && tags.equals(otherAb.tags)
                && password.equals(otherAb.password);
    }
}
