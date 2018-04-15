package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.investigapptor.logic.commands.CloseCaseCommand.MESSAGE_CASE_ALREADY_CLOSE;
import static seedu.investigapptor.logic.commands.CloseCaseCommand.MESSAGE_CLOSE_CASE_SUCCESS;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.investigapptor.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.investigapptor.logic.commands.CommandTestUtil.showCrimeCaseAtIndex;
import static seedu.investigapptor.model.crimecase.Status.CASE_CLOSE;

import static seedu.investigapptor.testutil.TypicalCrimeCases.getTypicalInvestigapptor;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_CASE;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_SECOND_CASE;

import org.junit.Test;

import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.testutil.CrimeCaseBuilder;

//@@author pkaijun
/**
 * Contains integration tests (interaction with the Model, UndoCommand)
 * and unit tests for CloseCaseCommand.
 */
public class CloseCaseCommandTest {
    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void equals() throws Exception {
        final CloseCaseCommand standardCommand = prepareCommand(INDEX_SECOND_CASE);

        // same values -> returns true
        CloseCaseCommand commandWithSameValues = prepareCommand(INDEX_SECOND_CASE);
        //assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new CloseCaseCommand(INDEX_FIRST_CASE)));
    }

    @Test
    public void execute_validCrimeCaseIndexFilteredList_success() throws Exception {
        Index indexLastCrimeCase = Index.fromOneBased(model.getFilteredCrimeCaseList().size());
        CrimeCase lastCrimeCase = model.getFilteredCrimeCaseList().get(indexLastCrimeCase.getZeroBased());

        CrimeCaseBuilder crimeCaseInList = new CrimeCaseBuilder(lastCrimeCase);
        CrimeCase closedCrimeCase = crimeCaseInList.withStatus(CASE_CLOSE).build();

        CloseCaseCommand closeCaseCommand = prepareCommand(indexLastCrimeCase);

        String expectedMessage = String.format(MESSAGE_CLOSE_CASE_SUCCESS, closedCrimeCase.getStatus().toString());

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(lastCrimeCase, closedCrimeCase);

        assertCommandSuccess(closeCaseCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of investigapptor book
     */
    @Test
    public void execute_invalidCrimeCaseIndexFilteredList_failure() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);
        Index outOfBoundIndex = INDEX_SECOND_CASE;

        // ensures that outOfBoundIndex is still in bounds of investigapptor book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getInvestigapptor().getCrimeCaseList().size());

        CloseCaseCommand closeCaseCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(closeCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    /**
     * Command applied on a case whose status is already closed - test should fail
     */
    @Test
    public void execute_invalidCaseStatusClosed_failure() {
        final CloseCaseCommand closeCaseCommand = prepareCommand(INDEX_FIRST_CASE); // case status already closed
        assertCommandFailure(closeCaseCommand, model, MESSAGE_CASE_ALREADY_CLOSE);
    }

    /**
     * Invalid index as the input - test should fail
     */
    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);

        CloseCaseCommand closeCaseCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> editCaseCommand not pushed into undoRedoStack
        assertCommandFailure(closeCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Closes a {@code CrimeCase} from a filtered list.
     * 2. Undo the close.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited crimeCase in the
     * unfiltered list is different from the index at the filtered list.
     */
    @Test
    public void executeUndo_validIndexFilteredList_sameCrimeCaseClosed() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);

        CrimeCase closedCrimeCase = new CrimeCaseBuilder().withStatus(CASE_CLOSE).build();
        CloseCaseCommand closeCaseCommand = prepareCommand(INDEX_FIRST_CASE);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        showCrimeCaseAtIndex(model, INDEX_SECOND_CASE);
        CrimeCase crimeCaseToClose = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());

        // close -> closes second crimeCase in unfiltered crimeCase list / first crimeCase in filtered crimeCase list
        closeCaseCommand.execute();
        undoRedoStack.push(closeCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateCrimeCase(crimeCaseToClose, closedCrimeCase);
        assertNotEquals(model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased()), crimeCaseToClose);
    }

    /**
     * Returns an {@code CloseCaseCommand} with parameters {@code index}
     */
    private CloseCaseCommand prepareCommand(Index index) {
        CloseCaseCommand closeCaseCommand = new CloseCaseCommand(index);
        closeCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return closeCaseCommand;
    }
}
