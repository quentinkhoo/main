package systemtests;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.investigapptor.testutil.TypicalPersons.KEYWORD_MATCHING_TEAMA;
import static seedu.investigapptor.testutil.TypicalPersons.KEYWORD_MATCHING_TEAMB;
import static seedu.investigapptor.testutil.TypicalPersons.MDM_ONG;
import static seedu.investigapptor.testutil.TypicalPersons.SIR_CHONG;
import static seedu.investigapptor.testutil.TypicalPersons.SIR_LIM;
import static seedu.investigapptor.testutil.TypicalPersons.SIR_LOO;

import org.junit.Test;

import seedu.investigapptor.logic.commands.FindInvestTagsCommand;
import seedu.investigapptor.logic.commands.RedoCommand;
import seedu.investigapptor.logic.commands.UndoCommand;
import seedu.investigapptor.model.Model;

//@@author pkaijun
public class FindInvestTagsCommandSystemTest extends InvestigapptorSystemTest {

    @Test
    public void find() {
        /* Case 1 (teamA): find multiple persons in investigapptor book, command with leading spaces and trailing spaces
         * -> 2 persons found
         */
        String command = "   " + FindInvestTagsCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TEAMA + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredPersonList(expectedModel,
                SIR_LIM, SIR_LOO); // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case 1 (teamA): repeat previous findInvestTag command where person list is displaying the investigators
         * we are finding
         * -> 2 persons found
         */
        command = FindInvestTagsCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TEAMA;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case 2 (teamB): find multiple persons in investigapptor book, command with leading spaces and trailing spaces
         * we are finding
         * -> 2 persons found
         */
        command = "   " + FindInvestTagsCommand.COMMAND_WORD + " "
                + KEYWORD_MATCHING_TEAMB + "   ";
        ModelHelper.setFilteredPersonList(expectedModel,
                MDM_ONG, SIR_CHONG); // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case 2 (teamB): repeat previous findInvestTag command where person list is displaying the investigators
         * we are finding
         * -> 2 persons found
         */
        command = FindInvestTagsCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_TEAMB;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case 3: find multiple investigators in investigapptor book, 2 keywords -> 3 persons found */
        command = FindInvestTagsCommand.COMMAND_WORD + " new teamB";
        ModelHelper.setFilteredPersonList(expectedModel, SIR_LIM, MDM_ONG, SIR_CHONG);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case 4: find multiple investigators in investigapptor book,
        2 keywords in reversed order -> 3 persons found */
        command = FindInvestTagsCommand.COMMAND_WORD + " teamB new";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in investigapptor book, 2 keywords with 1 repeat -> 3 persons found */
        command = FindInvestTagsCommand.COMMAND_WORD + " teamB new teamB";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in investigapptor book, 2 matching keywords and 1 non-matching keyword
         * -> 2 persons found
         */
        command = FindInvestTagsCommand.COMMAND_WORD + " teamB new NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find person in investigapptor book, keyword is substring of tag -> 0 persons found */
        command = FindInvestTagsCommand.COMMAND_WORD + " Mei";
        ModelHelper.setFilteredPersonList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person in investigapptor book, tag is substring of keyword -> 0 persons found */
        // keyword -> teamAs, tag -> teamA (substring of keyword)
        command = FindInvestTagsCommand.COMMAND_WORD + " teamAs";
        ModelHelper.setFilteredPersonList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tag that is not found in any investigators -> 0 persons found */
        command = FindInvestTagsCommand.COMMAND_WORD + " teamie";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find name of investigator in investigapptor -> 0 persons found */
        command = FindInvestTagsCommand.COMMAND_WORD + " " + SIR_LIM.getName().fullName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find address of investigator in investigapptor -> 0 persons found */
        command = FindInvestTagsCommand.COMMAND_WORD + " " + SIR_LIM.getAddress().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email of investigator in investigapptor book -> 0 persons found */
        command = FindInvestTagsCommand.COMMAND_WORD + " " + SIR_LIM.getEmail().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNdinVesttAgs teamA";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_PERSONS_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code InvestigapptorSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see InvestigapptorSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_PERSONS_LISTED_OVERVIEW, expectedModel.getFilteredPersonList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code InvestigapptorSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see InvestigapptorSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
