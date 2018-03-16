package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.logic.UndoRedoStackUtil.prepareStack;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.logic.commands.CommandTestUtil.deleteFirstPerson;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.investigapptor.testutil.TypicalPersons.getTypicalInvestigapptor;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;

public class UndoCommandTest {
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();
    private static final UndoRedoStack EMPTY_STACK = new UndoRedoStack();

    private final Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
    private final DeleteInvestigatorCommand deleteInvestigatorCommandOne
            = new DeleteInvestigatorCommand(INDEX_FIRST_PERSON);
    private final DeleteInvestigatorCommand deleteInvestigatorCommandTwo
            = new DeleteInvestigatorCommand(INDEX_FIRST_PERSON);

    @Before
    public void setUp() {
        deleteInvestigatorCommandOne.setData(model, EMPTY_COMMAND_HISTORY, EMPTY_STACK);
        deleteInvestigatorCommandTwo.setData(model, EMPTY_COMMAND_HISTORY, EMPTY_STACK);
    }

    @Test
    public void execute() throws Exception {
        UndoRedoStack undoRedoStack = prepareStack(
                Arrays.asList(deleteInvestigatorCommandOne, deleteInvestigatorCommandTwo), Collections.emptyList());
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, EMPTY_COMMAND_HISTORY, undoRedoStack);
        deleteInvestigatorCommandOne.execute();
        deleteInvestigatorCommandTwo.execute();

        // multiple commands in undoStack
        Model expectedModel = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
        deleteFirstPerson(expectedModel);
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // single command in undoStack
        expectedModel = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // no command in undoStack
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
    }
}
