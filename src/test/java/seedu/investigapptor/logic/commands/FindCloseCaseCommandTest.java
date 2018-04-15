package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_CASES_LISTED_OVERVIEW;
import static seedu.investigapptor.testutil.TypicalCrimeCases.ALFA;
import static seedu.investigapptor.testutil.TypicalCrimeCases.CHARLIE;
import static seedu.investigapptor.testutil.TypicalCrimeCases.FOUR;
import static seedu.investigapptor.testutil.TypicalCrimeCases.FOXTROT;
import static seedu.investigapptor.testutil.TypicalCrimeCases.ONE;
import static seedu.investigapptor.testutil.TypicalCrimeCases.THREE;
import static seedu.investigapptor.testutil.TypicalCrimeCases.TWO;
import static seedu.investigapptor.testutil.TypicalCrimeCases.getTypicalInvestigapptor;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.crimecase.CrimeCase;

//@@author pkaijun
/**
 * Contains integration tests (interaction with the Model) for {@code FindCloseCaseCommand}.
 */
public class FindCloseCaseCommandTest {
    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void equals() {
        FindCloseCaseCommand findCloseCaseFirstCommand = new FindCloseCaseCommand();
        FindCloseCaseCommand findCloseCaseSecondCommand = new FindCloseCaseCommand();

        // same object -> returns true
        assertTrue(findCloseCaseFirstCommand.equals(findCloseCaseFirstCommand));

        // same values -> returns true
        FindCloseCaseCommand findFirstCommandCopy = new FindCloseCaseCommand();
        assertTrue(findCloseCaseFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findCloseCaseFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findCloseCaseFirstCommand.equals(null));

        // same object -> returns true
        assertTrue(findCloseCaseFirstCommand.equals(findCloseCaseSecondCommand));
    }

    @Test
    public void execute_command_multipleCrimeCaseFound() {
        String expectedMessage = String.format(MESSAGE_CASES_LISTED_OVERVIEW, 7);
        FindCloseCaseCommand command = prepareCommand();
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALFA, CHARLIE, FOXTROT, ONE, TWO, THREE, FOUR));
    }

    /**
     * Prepare the FindCloseCaseCommand {@code FindCloseCaseCommand}.
     */
    private FindCloseCaseCommand prepareCommand() {
        FindCloseCaseCommand command = new FindCloseCaseCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<CrimeCase>} is equal to {@code expectedList}<br>
     *     - the {@code Investigapptor} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindCloseCaseCommand command, String expectedMessage,
                                      List<CrimeCase> expectedList) {
        Investigapptor expectedInvestigapptor = new Investigapptor(model.getInvestigapptor());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredCrimeCaseList());
        assertEquals(expectedInvestigapptor, model.getInvestigapptor());
    }
}
