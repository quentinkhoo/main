package seedu.investigapptor.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.omg.CORBA.Object;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.person.Address;
import seedu.investigapptor.model.person.Email;
import seedu.investigapptor.model.person.Name;
import seedu.investigapptor.model.person.Phone;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.person.investigator.Rank;
import seedu.investigapptor.model.tag.Tag;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedInvestigator {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Investigator's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private String rank;
    @XmlElement(required = true)
    private List<Integer> caseList = new ArrayList<>();

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedInvestigator() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedInvestigator(String name, String phone, String email, String address, String rank,
                                  List<CrimeCase> caseList, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.rank = rank;
        this.caseList = new ArrayList<>();
        if (caseList != null) {
            for (CrimeCase c : caseList) {
                this.caseList.add(c.hashCode());
            }
        }
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedInvestigator(Investigator source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        rank = source.getRank().getValue();
        caseList = new ArrayList<>();
        for (CrimeCase crimeCase : source.getCrimeCases()) {
            caseList.add(crimeCase.hashCode());
        }
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Investigator object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Investigator toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        final ArrayList<Integer> investigatorCases = new ArrayList<>();
        for (int crimeCase: caseList) {
            investigatorCases.add(crimeCase);
        }
        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        if (this.phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(this.phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone phone = new Phone(this.phone);

        if (this.email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(this.email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email email = new Email(this.email);

        if (this.address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(this.address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address address = new Address(this.address);

        if (this.rank == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Rank.class.getSimpleName()));
        }
        if (!Rank.isValidRank(this.rank)) {
            throw new IllegalValueException(Rank.MESSAGE_RANK_CONSTRAINTS);
        }
        final Rank rank = new Rank(this.rank);

        final Set<Tag> tags = new HashSet<>(personTags);

        return new Investigator(name, phone, email, address, rank, tags, investigatorCases);
    }

    /**
     * Compares if it is equal to another object
     * @param other
     * @return
     */
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedInvestigator)) {
            return false;
        }

        XmlAdaptedInvestigator otherPerson = (XmlAdaptedInvestigator) other;
        return Objects.equals(name, otherPerson.name)
                && Objects.equals(phone, otherPerson.phone)
                && Objects.equals(email, otherPerson.email)
                && Objects.equals(address, otherPerson.address)
                && Objects.equals(rank, otherPerson.rank)
                && tagged.equals(otherPerson.tagged)
                && caseList.equals(otherPerson.caseList);
    }
}
