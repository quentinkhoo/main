package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.investigapptor.logic.commands.CommandTestUtil.prepareUndoCommand;
//import static seedu.investigapptor.logic.commands.CommandTestUtil.showCrimeCaseAtIndex;
import static seedu.investigapptor.testutil.TypicalCrimeCases.getTypicalInvestigapptor;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_CASE;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_SECOND_CASE;

import org.junit.Test;

import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.crimecase.CrimeCase;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCaseCommand}.
 */
public class DeleteCaseCommandTest {

    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);

        String expectedMessage = String.format(DeleteCaseCommand.MESSAGE_DELETE_CASE_SUCCESS, crimeCaseToDelete);

        ModelManager expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.deleteCrimeCase(crimeCaseToDelete);

        assertCommandSuccess(deleteCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        DeleteCaseCommand deleteCaseCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    /* TO REVIEW */
    /*
    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);

        String expectedMessage = String.format(DeleteCaseCommand.MESSAGE_DELETE_CASE_SUCCESS, crimeCaseToDelete);

        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.deleteCrimeCase(crimeCaseToDelete);
        showNoCrimeCase(expectedModel);

        assertCommandSuccess(deleteCaseCommand, model, expectedMessage, expectedModel);
    }
    */

    /* TO REVIEW */
    /*
    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        Index outOfBoundIndex = INDEX_SECOND_CASE;
        // ensures that outOfBoundIndex is still in bounds of investigapptor book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getInvestigapptor().getCrimeCaseList().size());

        DeleteCaseCommand deleteCaseCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }
    */

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);
        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());

        // delete -> first crimeCase deleted
        deleteCaseCommand.execute();
        undoRedoStack.push(deleteCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first crimeCase deleted again
        expectedModel.deleteCrimeCase(crimeCaseToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        DeleteCaseCommand deleteCaseCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteCaseCommand not pushed into undoRedoStack
        assertCommandFailure(deleteCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /* TO REVIEW */
    /**
     * 1. Deletes a {@code CrimeCase} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted crimeCase in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the crimeCase object regardless of indexing.
     */
    /*
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameCrimeCaseDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);
        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());

        showCrimeCaseAtIndex(model, INDEX_SECOND_CASE);
        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        // delete -> deletes second crimeCase in unfiltered crimeCase list / first crimeCase in filtered crimeCase list
        deleteCaseCommand.execute();
        undoRedoStack.push(deleteCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.deleteCrimeCase(crimeCaseToDelete);
        assertNotEquals(crimeCaseToDelete, model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased()));
        // redo -> deletes same second crimeCase in unfiltered crimeCase list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
    */

    @Test
    public void equals() throws Exception {
        DeleteCaseCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_CASE);
        DeleteCaseCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_CASE);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCaseCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_CASE);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different crimeCase -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteCaseCommand} with the parameter {@code index}.
     */
    private DeleteCaseCommand prepareCommand(Index index) {
        DeleteCaseCommand deleteCaseCommand = new DeleteCaseCommand(index);
        deleteCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCaseCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoCrimeCase(Model model) {
        model.updateFilteredCrimeCaseList(p -> false);

        assertTrue(model.getFilteredCrimeCaseList().isEmpty());
    }
}
