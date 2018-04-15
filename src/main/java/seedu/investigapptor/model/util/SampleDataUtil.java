package seedu.investigapptor.model.util;

import static seedu.investigapptor.model.crimecase.EndDate.LARGEST_DATE;

import java.util.HashSet;
import java.util.Set;

import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.ReadOnlyInvestigapptor;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.EndDate;
import seedu.investigapptor.model.crimecase.StartDate;
import seedu.investigapptor.model.crimecase.Status;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.Address;
import seedu.investigapptor.model.person.Email;
import seedu.investigapptor.model.person.Name;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.Phone;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.person.investigator.Rank;
import seedu.investigapptor.model.tag.Tag;

//@@author Marcus-cxc

/**
 * Contains utility methods for populating {@code Investigapptor} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[]{
            new Investigator(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"), new Rank("4"),
                    getTagSet("teamA")),
            new Investigator(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), new Rank("2"),
                    getTagSet("teamB")),
            new Investigator(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                    new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), new Rank("1"),
                    getTagSet("new", "teamC")),
            new Investigator(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), new Rank("3"),
                    getTagSet( "teamA")),
            new Investigator(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"), new Rank("5"),
                    getTagSet("new", "teamB")),
            new Investigator(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"), new Rank("3"),
                    getTagSet("teamC"))
        };
    }

    public static CrimeCase[] getSampleCases() {

        return new CrimeCase[]{
            new CrimeCase(new CaseName("Murder at Bishan"), new Description("Man stab to death"),
                    (Investigator) getSamplePersons()[1], new StartDate("07/08/2017"), new EndDate(LARGEST_DATE),
                    new Status("open"), getTagSet("Murder")),
            new CrimeCase(new CaseName("BnE at Seragoon"), new Description("Unit #03-132 was broken in"),
                    (Investigator) getSamplePersons()[1], new StartDate("01/03/2018"), new EndDate(LARGEST_DATE),
                    new Status("open"), getTagSet("Robbery")),
            new CrimeCase(new CaseName("Assault At Woodlands"), new Description("Man Assaulted at Woodland Blk "
                    + "312 void deck"), (Investigator) getSamplePersons()[3], new StartDate("07/08/2017"),
                    new EndDate(LARGEST_DATE), new Status("open"), getTagSet("Assault")),
            new CrimeCase(new CaseName("Illegal Firearm"), new Description("Man possessing a SAR-21 at home"),
                    (Investigator) getSamplePersons()[5], new StartDate("02/02/2018"), new EndDate(LARGEST_DATE),
                    new Status("open"), getTagSet("Firearm")),
            new CrimeCase(new CaseName("Robbery at AMK Macdonald"), new Description("Man demanded 50 big mac"
                    + "with a knife"),
                    (Investigator) getSamplePersons()[2], new StartDate("07/03/2016"), new EndDate(LARGEST_DATE),
                    new Status("open"), getTagSet("Robbery")),
            new CrimeCase(new CaseName("The Oolong Slayer"), new Description("A serial murderer who always"
                    + "leave behind oolong tea"), (Investigator) getSamplePersons()[0], new StartDate("07/02/2011"),
                    new EndDate(LARGEST_DATE), new Status("open"), getTagSet("Murder", "Serial")),
            new CrimeCase(new CaseName("Punggol Arson"), new Description("Fire started by unknown perp at 7pm"),
                    (Investigator) getSamplePersons()[4], new StartDate("02/01/2018"),
                    new EndDate(LARGEST_DATE), new Status("open"), getTagSet("Arson", "Fire"))
        };
    }

    public static ReadOnlyInvestigapptor getSampleInvestigapptor() {
        try {
            Investigapptor sampleAb = new Investigapptor();

            for (Person samplePerson : getSamplePersons()) {
                sampleAb.addPerson(samplePerson);
            }

            for (CrimeCase crimeCase : getSampleCases()) {
                sampleAb.addCrimeCase(crimeCase);
            }


            return sampleAb;
        } catch (DuplicatePersonException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        } catch (DuplicateCrimeCaseException e) {
            throw new AssertionError("sample data cannot contain duplicate Case");
        }

    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
