package systemtests;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_CASES_LISTED_OVERVIEW;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.investigapptor.testutil.TypicalCrimeCases.ALFA;
import static seedu.investigapptor.testutil.TypicalCrimeCases.BRAVO;
import static seedu.investigapptor.testutil.TypicalCrimeCases.KEYWORD_MATCHING_HOMICIDE;
import static seedu.investigapptor.testutil.TypicalCrimeCases.KEYWORD_MATCHING_MURDER;
import static seedu.investigapptor.testutil.TypicalCrimeCases.ONE;
import static seedu.investigapptor.testutil.TypicalCrimeCases.THREE;
import static seedu.investigapptor.testutil.TypicalCrimeCases.TWO;

import org.junit.Test;

import seedu.investigapptor.logic.commands.FindCaseTagsCommand;
import seedu.investigapptor.logic.commands.RedoCommand;
import seedu.investigapptor.logic.commands.UndoCommand;
import seedu.investigapptor.model.Model;

public class FindCaseTagsCommandSystemTest extends InvestigapptorSystemTest {

    @Test
    public void find() {
        /* Case 1 (murder): find multiple cases in investigapptor book, command with leading spaces and trailing spaces
         * -> 3 cases found
         */
        String command = "   " + FindCaseTagsCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MURDER + "   ";
        Model expectedModel = getModel();
        ModelHelper.setCrimeCaseFilteredList(expectedModel, ALFA, ONE, THREE); // these three cases contain the tag "murder"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case 1 (murder): repeat previous findCaseTag command where crimecase list is displaying the cases
         * we are finding
         * -> 3 cases found
         */
        command = FindCaseTagsCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MURDER;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case 2 (homicide): find multiple cases in investigapptor book, command with leading spaces and trailing spaces
         * we are finding
         * -> 2 cases found
         */
        command = "   " + FindCaseTagsCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_HOMICIDE + "   ";
        ModelHelper.setCrimeCaseFilteredList(expectedModel, BRAVO, ONE); // these two cases contain the tag "homicide"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case 2 (homicide): repeat previous findCaseTag command where crimecase list is displaying the cases
         * we are finding
         * -> 3 cases found
         */
        command = FindCaseTagsCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_HOMICIDE;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person where person list is not displaying the person we are finding -> 1 person found */
        /*command = FindInvestTagsCommand.COMMAND_WORD + " Carl";
        ModelHelper.setCrimeCaseFilteredList(expectedModel, CARL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();*/

        /* Case 3: find multiple cases in investigapptor book, 2 keywords -> 3 cases found */
        command = FindCaseTagsCommand.COMMAND_WORD + " murder kidnap";
        ModelHelper.setCrimeCaseFilteredList(expectedModel, ALFA, BRAVO, ONE, TWO, THREE);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case 4: find multiple cases in investigapptor book,
        2 keywords in reversed order -> 3 cases found */
        command = FindCaseTagsCommand.COMMAND_WORD + " kidnap murder";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple cases in investigapptor book, 2 keywords with 1 repeat -> 3 cases found */
        command = FindCaseTagsCommand.COMMAND_WORD + " kidnap murder kidnap";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in investigapptor book, 2 matching keywords and 1 non-matching keyword
         * -> 2 persons found
         */
        command = FindCaseTagsCommand.COMMAND_WORD + " kidnap murder NonMatchingKeyWord";
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

        /* Case: find same persons in investigapptor book after deleting 1 of them -> 1 person found */
        /*executeCommand(DeleteInvestigatorCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getInvestigapptor().getPersonList().contains(BENSON));
        command = FindInvestTagsCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setCrimeCaseFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();*/

        /* Case: find person in investigapptor book, keyword is same as name but of different case -> 1 person found */
        /*command = FindInvestTagsCommand.COMMAND_WORD + " MeIeR";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();*/

        /* Case: find case in investigapptor book, keyword is substring of tag -> 0 persons found */
        command = FindCaseTagsCommand.COMMAND_WORD + " mur";
        ModelHelper.setCrimeCaseFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person in investigapptor book, tag is substring of keyword -> 0 persons found */
        // keyword -> teamAs, tag -> teamA (substring of keyword)
        command = FindCaseTagsCommand.COMMAND_WORD + " murders";
        ModelHelper.setCrimeCaseFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tag that is not found in any investigators -> 0 persons found */
        command = FindCaseTagsCommand.COMMAND_WORD + " murderie";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find name of case in investigapptor -> 0 persons found */
        command = FindCaseTagsCommand.COMMAND_WORD + " " + ONE.getCaseName().crimeCaseName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find status of investigator in investigapptor -> 0 persons found */
        command = FindCaseTagsCommand.COMMAND_WORD + " " + ONE.getStatus().toString();
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email of investigator in investigapptor book -> 0 persons found */
        /*command = FindCaseTagsCommand.COMMAND_WORD + " " + SIR_LIM.getEmail().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();*/

        /* Case: find while a person is selected -> selected card deselected */
        /*showAllPersons();
        selectPerson(Index.fromOneBased(1));
        assertFalse(getPersonListPanel().getHandleToSelectedCard().getName().equals(DANIEL.getName().fullName));
        command = FindInvestTagsCommand.COMMAND_WORD + " new";
        ModelHelper.setCrimeCaseFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();*/

        /* Case: find person in empty investigapptor book -> 0 persons found */
        /*deleteAllPersons();
        command = FindInvestTagsCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setCrimeCaseFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();*/

        /* Case: mixed case command word -> rejected */
        command = "FiNdcAsEtAgs murder";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_CASES_LISTED_OVERVIEW} with the number of crime cases in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code InvestigapptorSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see InvestigapptorSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_CASES_LISTED_OVERVIEW, expectedModel.getFilteredCrimeCaseList().size());

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
