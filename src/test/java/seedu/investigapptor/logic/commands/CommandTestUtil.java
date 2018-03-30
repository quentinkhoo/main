package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_INVESTIGATOR;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_RANK;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.crimecase.CaseNameContainsKeywordsPredicate;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.person.NameContainsKeywordsPredicate;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.exceptions.PersonNotFoundException;
import seedu.investigapptor.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    /* Person util */

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_RANK_CAPTAIN = "3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String RANK_DESC_CAP = " " + PREFIX_RANK + VALID_RANK_CAPTAIN;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_RANK_DESC = " " + PREFIX_RANK + "6"; // rank only ranges from 1-5
    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditInvestigatorCommand.EditPersonDescriptor DESC_AMY;
    public static final EditInvestigatorCommand.EditPersonDescriptor DESC_BOB;
    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }
    /* CrimeCase Util */

    public static final String VALID_CASENAME_APPLE = "Project Apple";
    public static final String VALID_CASENAME_BANANA = "Project Banana";
    public static final String VALID_DESCRIPTION_APPLE = "Description for Project Apple.";
    public static final String VALID_DESCRIPTION_BANANA = "Description for Project Banana.";
    public static final String VALID_INVESTIGATOR_APPLE = "1";
    public static final String VALID_INVESTIGATOR_BANANA = "2";
    public static final String VALID_STARTDATE_APPLE = "31/12/2017";
    public static final String VALID_STARTDATE_BANANA = "4/03/1998";
    public static final String VALID_TAG_FRAUD = "Fraud";
    public static final String VALID_TAG_MURDER = "Murder";

    public static final String CASENAME_DESC_APPLE = " " + PREFIX_NAME + VALID_CASENAME_APPLE;
    public static final String CASENAME_DESC_BANANA = " " + PREFIX_NAME + VALID_CASENAME_BANANA;
    public static final String DESCRIPTION_DESC_APPLE = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_APPLE;
    public static final String DESCRIPTION_DESC_BANANA = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_BANANA;
    public static final String INVESTIGATOR_DESC_APPLE = " " + PREFIX_INVESTIGATOR + VALID_INVESTIGATOR_APPLE;
    public static final String INVESTIGATOR_DESC_BANANA = " " + PREFIX_INVESTIGATOR + VALID_INVESTIGATOR_BANANA;
    public static final String STARTDATE_DESC_APPLE = " " + PREFIX_STARTDATE + VALID_STARTDATE_APPLE;
    public static final String STARTDATE_DESC_BANANA = " " + PREFIX_STARTDATE + VALID_STARTDATE_BANANA;
    public static final String TAG_DESC_FRAUD = " " + PREFIX_TAG + VALID_TAG_FRAUD;
    public static final String TAG_DESC_MURDER = " " + PREFIX_TAG + VALID_TAG_MURDER;

    public static final String INVALID_CASENAME_DESC = " " + PREFIX_NAME + "Yellow&"; // '&' not allowed in names
    public static final String INVALID_DESCRIPTION_DESC = " "
            + PREFIX_DESCRIPTION + ""; // empty string not allowed for descriptions
    public static final String INVALID_INVESTIGATOR_DESC = " "
            + PREFIX_INVESTIGATOR + "-5"; // negative index
    public static final String INVALID_STARTDATE_DESC = " "
            + PREFIX_STARTDATE + "132/11/17"; // is not a valid date

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the investigapptor book and the filtered person list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        Investigapptor expectedInvestigapptor = new Investigapptor(actualModel.getInvestigapptor());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedInvestigapptor, actualModel.getInvestigapptor());
            assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s investigapptor book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s investigapptor book.
     */
    public static void showCrimeCaseAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredCrimeCaseList().size());

        CrimeCase crimeCase = model.getFilteredCrimeCaseList().get(targetIndex.getZeroBased());
        final String[] splitName = crimeCase.getCaseName().crimeCaseName.split("\\s+");
        model.updateFilteredCrimeCaseList(new CaseNameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(7, model.getFilteredCrimeCaseList().size());
    }

    /**
     * Deletes the first person in {@code model}'s filtered list from {@code model}'s investigapptor book.
     */
    public static void deleteFirstPerson(Model model) {
        Person firstPerson = model.getFilteredPersonList().get(0);
        try {
            model.deletePerson(firstPerson);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("Person in filtered list must exist in model.", pnfe);
        }
    }

    /**
     * Returns an {@code UndoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static UndoCommand prepareUndoCommand(Model model, UndoRedoStack undoRedoStack) {
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return undoCommand;
    }

    /**
     * Returns a {@code RedoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static RedoCommand prepareRedoCommand(Model model, UndoRedoStack undoRedoStack) {
        RedoCommand redoCommand = new RedoCommand();
        redoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return redoCommand;
    }
}
