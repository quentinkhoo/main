package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.model.Password.generatePasswordHash;
import static seedu.investigapptor.testutil.TypicalPersons.getTypicalInvestigapptor;

import org.junit.Before;
import org.junit.Test;

import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.Password;
import seedu.investigapptor.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code PasswordCommand}.
 */
public class PasswordCommandTest {

    private static final String DEFAULT_PASSWORD = "password";
    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager();
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        PasswordCommand passwordCommand = prepareCommand(DEFAULT_PASSWORD);

        String expectedMessage = String.format(PasswordCommand.MESSAGE_SUCCESS);

        ModelManager expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.updatePassword(new Password(generatePasswordHash(DEFAULT_PASSWORD)));

        assertCommandSuccess(passwordCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        PasswordCommand passwordFirstCommand = prepareCommand(DEFAULT_PASSWORD);
        PasswordCommand passwordSecondCommand = prepareCommand(DEFAULT_PASSWORD + "1");

        // same object -> returns true
        assertTrue(passwordFirstCommand.equals(passwordFirstCommand));

        // same values -> returns true
        PasswordCommand passwordFirstCommandCopy = prepareCommand(DEFAULT_PASSWORD);
        assertTrue(passwordFirstCommand.equals(passwordFirstCommandCopy));

        // different types -> returns false
        assertFalse(passwordFirstCommand.equals(1));

        // null -> returns false
        assertFalse(passwordFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(passwordFirstCommand.equals(passwordSecondCommand));
    }

    /**
     * Returns a {@code PasswordCommand} with the parameter {@code password}.
     */
    private PasswordCommand prepareCommand(String password) {
        PasswordCommand passwordCommand = new PasswordCommand(new Password(password), model);
        passwordCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return passwordCommand;
    }
}
