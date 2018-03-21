package seedu.investigapptor.testutil;

import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.investigator.Investigator;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("85355255")
            .withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street").build();

    public static final Person POLICE = new PersonBuilder().withName("Police").build();
    public static final Person SIR_LIM = new PersonBuilder().withName("Chao Lim")
            .withPhone("91672345").withEmail("limck@investiagency.com").withAddress("20th Street")
            .withTags("teamA", "new").build();
    public static final Person MDM_ONG = new PersonBuilder().withName("Cally Ong")
            .withPhone("90123489").withEmail("cong@investiagency.com").withAddress("50th Street")
            .withTags("teamB", "new").build();
    public static final Person SIR_CHONG = new PersonBuilder().withName("Ka Chong")
            .withPhone("80122469").withEmail("kchong@investiagency.com").withAddress("80th Street")
            .withTags("teamB", "experienced").build();
    public static final Person SIR_LOO = new PersonBuilder().withName("Andy Loo")
            .withPhone("91126469").withEmail("aloo@investiagency.com").withAddress("90th Street")
            .withTags("teamA", "experienced").build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER
    public static final String KEYWORD_MATCHING_TEAMA = "TEAMA"; // A keyword that matches TEAMA
    public static final String KEYWORD_MATCHING_TEAMB = "teamB"; // A keyword that matches TEAMB
    public static final String KEYWORD_MATCHING_NEW = "new"; // A keyword that matches NEW
    public static final String KEYWORD_MATCHING_EXPERIENCED = "experienced"; // A keyword that matches EXPERIENCED

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code Investigapptor} with all the typical persons.
     */
    public static Investigapptor getTypicalInvestigapptor() {
        Investigapptor ia = new Investigapptor();
        for (Person person : getTypicalPersons()) {
            try {
                ia.addPerson(person);
            } catch (DuplicatePersonException e) {
                throw new AssertionError("not possible");
            }
        }
        return ia;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
