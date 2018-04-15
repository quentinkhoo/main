package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.investigapptor.logic.commands.CommandTestUtil.DESC_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.DESC_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_CASENAME_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_MURDER;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.investigapptor.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.investigapptor.logic.commands.CommandTestUtil.showCrimeCaseAtIndex;
import static seedu.investigapptor.testutil.TypicalCrimeCases.getTypicalInvestigapptor;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_CASE;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_SECOND_CASE;

import org.junit.Test;

import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.logic.commands.EditCaseCommand.EditCrimeCaseDescriptor;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.testutil.CrimeCaseBuilder;
import seedu.investigapptor.testutil.EditCrimeCaseDescriptorBuilder;
//@@author leowweiching
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for EditCaseCommand.
 */
public class EditCaseCommandTest {

    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        CrimeCase editedCrimeCase = new CrimeCaseBuilder().build();
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(editedCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_SECOND_CASE, descriptor);

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(model.getFilteredCrimeCaseList().get(1), editedCrimeCase);

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastCrimeCase = Index.fromOneBased(model.getFilteredCrimeCaseList().size());
        CrimeCase lastCrimeCase = model.getFilteredCrimeCaseList().get(indexLastCrimeCase.getZeroBased());

        CrimeCaseBuilder crimeCaseInList = new CrimeCaseBuilder(lastCrimeCase);
        CrimeCase editedCrimeCase = crimeCaseInList.withName(VALID_CASENAME_BANANA)
                .withDescription(VALID_DESCRIPTION_BANANA)
                .withTags(VALID_TAG_MURDER).build();

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_BANANA)
                .withDescription(VALID_DESCRIPTION_BANANA).withTags(VALID_TAG_MURDER).build();
        EditCaseCommand editCaseCommand = prepareCommand(indexLastCrimeCase, descriptor);

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(lastCrimeCase, editedCrimeCase);

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE,
                new EditCrimeCaseDescriptor());
        CrimeCase editedCrimeCase = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        CrimeCase crimeCaseInFilteredList = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        CrimeCase editedCrimeCase = new CrimeCaseBuilder(crimeCaseInFilteredList)
                .withName(VALID_CASENAME_BANANA).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE,
                new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_BANANA).build());

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(model.getFilteredCrimeCaseList().get(0), editedCrimeCase);

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateCrimeCaseUnfilteredList_failure() {
        CrimeCase firstCrimeCase = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(firstCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_SECOND_CASE, descriptor);

        assertCommandFailure(editCaseCommand, model, EditCaseCommand.MESSAGE_DUPLICATE_CASE);
    }

    @Test
    public void execute_duplicateCrimeCaseFilteredList_failure() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        // edit crimeCase in filtered list into a duplicate in investigapptor book
        CrimeCase crimeCaseInList = model.getInvestigapptor().getCrimeCaseList().get(INDEX_SECOND_CASE.getZeroBased());
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE,
                new EditCrimeCaseDescriptorBuilder(crimeCaseInList).build());

        assertCommandFailure(editCaseCommand, model, EditCaseCommand.MESSAGE_DUPLICATE_CASE);
    }

    @Test
    public void execute_invalidCrimeCaseIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withCaseName(VALID_CASENAME_BANANA).build();
        EditCaseCommand editCaseCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
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

        EditCaseCommand editCaseCommand = prepareCommand(outOfBoundIndex,
                new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_BANANA).build());

        assertCommandFailure(editCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CrimeCase editedCrimeCase = new CrimeCaseBuilder().build();
        CrimeCase crimeCaseToEdit = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(editedCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE, descriptor);
        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        // edit -> first crimeCase edited
        editCaseCommand.execute();
        undoRedoStack.push(editCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first crimeCase edited again
        expectedModel.updateCrimeCase(crimeCaseToEdit, editedCrimeCase);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withCaseName(VALID_CASENAME_BANANA).build();
        EditCaseCommand editCaseCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> editCaseCommand not pushed into undoRedoStack
        assertCommandFailure(editCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code CrimeCase} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited crimeCase in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the crimeCase object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameCrimeCaseEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CrimeCase editedCrimeCase = new CrimeCaseBuilder().build();
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(editedCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE, descriptor);
        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        showCrimeCaseAtIndex(model, INDEX_SECOND_CASE);
        CrimeCase crimeCaseToEdit = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        // edit -> edits second crimeCase in unfiltered crimeCase list / first crimeCase in filtered crimeCase list
        editCaseCommand.execute();
        undoRedoStack.push(editCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateCrimeCase(crimeCaseToEdit, editedCrimeCase);
        assertNotEquals(model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased()), crimeCaseToEdit);
        // redo -> edits same second crimeCase in unfiltered crimeCase list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        final EditCaseCommand standardCommand = prepareCommand(INDEX_FIRST_CASE, DESC_APPLE);

        // same values -> returns true
        EditCrimeCaseDescriptor copyDescriptor = new EditCrimeCaseDescriptor(DESC_APPLE);
        EditCaseCommand commandWithSameValues = prepareCommand(INDEX_FIRST_CASE, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

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
        assertFalse(standardCommand.equals(new EditCaseCommand(INDEX_SECOND_CASE, DESC_APPLE)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCaseCommand(INDEX_FIRST_CASE, DESC_BANANA)));
    }

    /**
     * Returns an {@code EditCaseCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCaseCommand prepareCommand(Index index, EditCrimeCaseDescriptor descriptor) {
        EditCaseCommand editCaseCommand = new EditCaseCommand(index, descriptor);
        editCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editCaseCommand;
    }
}
