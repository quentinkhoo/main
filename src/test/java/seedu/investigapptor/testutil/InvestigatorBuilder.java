package seedu.investigapptor.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.UniqueCrimeCaseList;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.Address;
import seedu.investigapptor.model.person.Email;
import seedu.investigapptor.model.person.Name;
import seedu.investigapptor.model.person.Phone;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.person.investigator.Rank;
import seedu.investigapptor.model.tag.Tag;
import seedu.investigapptor.model.util.SampleDataUtil;

/**
 * A utility class to help with building Investigator objects.
 */
public class InvestigatorBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_RANK = "3";
    public static final String DEFAULT_TAGS = "friends";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Rank rank;
    private UniqueCrimeCaseList caseList;
    private Set<Tag> tags;

    public InvestigatorBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        rank = new Rank(DEFAULT_RANK);
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
        caseList = new UniqueCrimeCaseList();
    }

    /**
     * Initializes the InvestigatorBuilder with the data of {@code personToCopy}.
     */
    public InvestigatorBuilder(Investigator investigatorToCopy) {
        name = investigatorToCopy.getName();
        phone = investigatorToCopy.getPhone();
        email = investigatorToCopy.getEmail();
        address = investigatorToCopy.getAddress();
        tags = new HashSet<>(investigatorToCopy.getTags());
        caseList = new UniqueCrimeCaseList(investigatorToCopy.getCaseAsSet());
    }

    /**
     * Sets the {@code Name} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withRank(String rank) {
        this.rank = new Rank(rank);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder addCase(CrimeCase crimeCase) {
        try {
            CrimeCase c = new CrimeCase(crimeCase.getCaseName(), crimeCase.getDescription(),
                    new Investigator(name, phone, email, address, rank, tags), crimeCase.getStartDate(),
                    crimeCase.getEndDate(), crimeCase.getStatus(), crimeCase.getTags());
            caseList.add(crimeCase);
        } catch (DuplicateCrimeCaseException e) {
            throw new AssertionError("not possible");
        }
        return this;
    }

    public Investigator build() {
        return new Investigator(name, phone, email, address, rank, caseList.toSet(), tags);
    }

}
