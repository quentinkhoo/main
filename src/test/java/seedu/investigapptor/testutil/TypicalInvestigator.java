package seedu.investigapptor.testutil;

import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_RANK_CAPTAIN;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.investigator.Investigator;

/**
 * A utility class containing a list of {@code Investigator} objects to be used in tests.
 */
public class TypicalInvestigator {

    public static final Investigator ALICE = new InvestigatorBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("85355255")
            .withTags("friends").build();
    public static final Investigator BENSON = new InvestigatorBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withTags("owesMoney", "friends").build();
    public static final Investigator CARL = new InvestigatorBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street").build();
    public static final Investigator DANIEL = new InvestigatorBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").build();
    public static final Investigator ELLE = new InvestigatorBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave").build();
    public static final Investigator FIONA = new InvestigatorBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo").build();
    public static final Investigator GEORGE = new InvestigatorBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street").build();
    public static final Investigator TOM = new InvestigatorBuilder().withName("Tom").build();

    // Manually added
    public static final Investigator HOON = new InvestigatorBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").build();
    public static final Investigator IDA = new InvestigatorBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - Investigator's details found in {@code CommandTestUtil}
    public static final Investigator AMY = new InvestigatorBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Investigator BOB = new InvestigatorBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withRank(VALID_RANK_CAPTAIN)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalInvestigator() {} // prevents instantiation

    /**
     * Returns an {@code Investigapptor} with all the typical persons.
     */
    public static Investigapptor getTypicalInvestigapptor() {
        Investigapptor ia = new Investigapptor();
        for (Investigator investigator : getTypicalInvestigators()) {
            try {
                ia.addPerson(investigator);
            } catch (DuplicatePersonException e) {
                throw new AssertionError("not possible");
            }
        }
        return ia;
    }

    public static List<Investigator> getTypicalInvestigators() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
