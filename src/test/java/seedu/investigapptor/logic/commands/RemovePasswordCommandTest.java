package seedu.investigapptor.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.testutil.TypicalInvestigator.getTypicalInvestigapptor;

import org.junit.Before;
import org.junit.Test;

import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.logic.commands.exceptions.InvalidPasswordException;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.Password;
import seedu.investigapptor.model.UserPrefs;

//@@author quentinkhoo
public class RemovePasswordCommandTest {
    private static final Password TEST_PASSWORD = new Password("password");
    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Before
    public void setUp() {
        try {
            model.updatePassword(TEST_PASSWORD);
        } catch (InvalidPasswordException ipe) {
            throw new AssertionError("Shouldn't reach here");
        }

    }
    @Test
    public void execute_removePassword_success() throws Exception {
        RemovePasswordCommand removepasswordCommand = prepareCommand();
        ModelManager expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.updatePassword(TEST_PASSWORD);

        String expectedMessageSuccess = RemovePasswordCommand.MESSAGE_SUCCESS;
        expectedModel.removePassword();
        assertCommandSuccess(removepasswordCommand, model, expectedMessageSuccess, expectedModel);
        assertEquals(model.getInvestigapptor().getPassword(), null);

        String expectedMessageNoPassword = RemovePasswordCommand.MESSAGE_NO_PASSWORD;
        assertCommandSuccess(removepasswordCommand, model, expectedMessageNoPassword, expectedModel);
        assertEquals(model.getInvestigapptor().getPassword(), null);

    }

    /**
     * Returns a {@code RemovePasswordCommand}.
     */
    private RemovePasswordCommand prepareCommand() {
        RemovePasswordCommand removepasswordCommand = new RemovePasswordCommand();
        removepasswordCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return removepasswordCommand;
    }
}
//@@author
