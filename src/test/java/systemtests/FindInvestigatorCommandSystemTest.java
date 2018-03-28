package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.investigapptor.testutil.TypicalPersons.BENSON;
import static seedu.investigapptor.testutil.TypicalPersons.CARL;
import static seedu.investigapptor.testutil.TypicalPersons.DANIEL;
import static seedu.investigapptor.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.logic.commands.DeleteInvestigatorCommand;
import seedu.investigapptor.logic.commands.FindInvestigatorCommand;
import seedu.investigapptor.logic.commands.RedoCommand;
import seedu.investigapptor.logic.commands.UndoCommand;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.tag.Tag;

public class FindInvestigatorCommandSystemTest extends InvestigapptorSystemTest {

    @Test
    public void find() {
        /* Case: find multiple persons in investigapptor book, command with leading spaces and trailing spaces
         * -> 2 persons found
         */
        String command = "   " + FindInvestigatorCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredPersonList(expectedModel, BENSON, DANIEL); // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where person list is displaying the persons we are finding
         * -> 2 persons found
         */
        command = FindInvestigatorCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person where person list is not displaying the person we are finding -> 1 person found */
        command = FindInvestigatorCommand.COMMAND_WORD + " Carl";
        ModelHelper.setFilteredPersonList(expectedModel, CARL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in investigapptor book, 2 keywords -> 2 persons found */
        command = FindInvestigatorCommand.COMMAND_WORD + " Benson Daniel";
        ModelHelper.setFilteredPersonList(expectedModel, BENSON, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in investigapptor book, 2 keywords in reversed order -> 2 persons found */
        command = FindInvestigatorCommand.COMMAND_WORD + " Daniel Benson";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in investigapptor book, 2 keywords with 1 repeat -> 2 persons found */
        command = FindInvestigatorCommand.COMMAND_WORD + " Daniel Benson Daniel";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple persons in investigapptor book, 2 matching keywords and 1 non-matching keyword
         * -> 2 persons found
         */
        command = FindInvestigatorCommand.COMMAND_WORD + " Daniel Benson NonMatchingKeyWord";
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
        executeCommand(DeleteInvestigatorCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getInvestigapptor().getPersonList().contains(BENSON));
        command = FindInvestigatorCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredPersonList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person in investigapptor book, keyword is same as name but of different case -> 1 person found */
        command = FindInvestigatorCommand.COMMAND_WORD + " MeIeR";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person in investigapptor book, keyword is substring of name -> 0 persons found */
        command = FindInvestigatorCommand.COMMAND_WORD + " Mei";
        ModelHelper.setFilteredPersonList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person in investigapptor book, name is substring of keyword -> 0 persons found */
        command = FindInvestigatorCommand.COMMAND_WORD + " Meiers";
        ModelHelper.setFilteredPersonList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find person not in investigapptor book -> 0 persons found */
        command = FindInvestigatorCommand.COMMAND_WORD + " Mark";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find phone number of person in investigapptor book -> 0 persons found */
        command = FindInvestigatorCommand.COMMAND_WORD + " " + DANIEL.getPhone().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find investigapptor of person in investigapptor book -> 0 persons found */
        command = FindInvestigatorCommand.COMMAND_WORD + " " + DANIEL.getAddress().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find email of person in investigapptor book -> 0 persons found */
        command = FindInvestigatorCommand.COMMAND_WORD + " " + DANIEL.getEmail().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of person in investigapptor book -> 0 persons found */
        List<Tag> tags = new ArrayList<>(DANIEL.getTags());
        command = FindInvestigatorCommand.COMMAND_WORD + " " + tags.get(0).tagName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a person is selected -> selected card deselected */
        showAllPersons();
        selectPerson(Index.fromOneBased(1));
        assertFalse(getPersonListPanel().getHandleToSelectedCard().getName().equals(DANIEL.getName().fullName));
        command = FindInvestigatorCommand.COMMAND_WORD + " Daniel";
        ModelHelper.setFilteredPersonList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find person in empty investigapptor book -> 0 persons found */
        deleteAllPersons();
        command = FindInvestigatorCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredPersonList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd Meier";
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
