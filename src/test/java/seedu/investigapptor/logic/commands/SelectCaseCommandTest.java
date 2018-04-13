package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.investigapptor.logic.commands.CommandTestUtil.showCrimeCaseAtIndex;
import static seedu.investigapptor.testutil.TypicalCrimeCases.getTypicalInvestigapptor;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_CASE;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_SECOND_CASE;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_THIRD_CASE;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.ui.FilteredCrimeCaseListChangedEvent;
import seedu.investigapptor.commons.events.ui.JumpToCrimeCaseListRequestEvent;
import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.ui.testutil.EventsCollectorRule;

//@@author leowweiching
/**
 * Contains integration tests (interaction with the Model) for {@code SelectCaseCommand}.
 */
public class SelectCaseCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastCrimeCaseIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size());

        assertExecutionSuccess(INDEX_FIRST_CASE);
        assertExecutionSuccess(INDEX_THIRD_CASE);
        assertExecutionSuccess(lastCrimeCaseIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        assertExecutionSuccess(INDEX_FIRST_CASE);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        Index outOfBoundsIndex = INDEX_SECOND_CASE;
        // ensures that outOfBoundIndex is still in bounds of investigapptor book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getInvestigapptor().getCrimeCaseList().size());

        assertExecutionFailureWithEvent(outOfBoundsIndex, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectCaseCommand selectFirstCommand = new SelectCaseCommand(INDEX_FIRST_CASE);
        SelectCaseCommand selectSecondCommand = new SelectCaseCommand(INDEX_SECOND_CASE);

        // same object -> returns true
        assertTrue(selectFirstCommand.equals(selectFirstCommand));

        // same values -> returns true
        SelectCaseCommand selectFirstCommandCopy = new SelectCaseCommand(INDEX_FIRST_CASE);
        assertTrue(selectFirstCommand.equals(selectFirstCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(selectFirstCommand.equals(selectSecondCommand));
    }

    /**
     * Executes a {@code SelectCaseCommand} with the given {@code index},
     * and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        SelectCaseCommand selectCaseCommand = prepareCommand(index);

        try {
            CommandResult commandResult = selectCaseCommand.execute();
            assertEquals(String.format(SelectCaseCommand.MESSAGE_SELECT_CASE_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        JumpToCrimeCaseListRequestEvent lastEvent =
                (JumpToCrimeCaseListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
    }

    /**
     * Executes a {@code SelectCaseCommand} with the given {@code index},
     * and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     *
     * This function checks that no events were raised
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SelectCaseCommand selectCaseCommand = prepareCommand(index);

        try {
            selectCaseCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Executes a {@code SelectCaseCommand} with the given {@code index},
     * and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     *
     * This function also checks that the event raised is FilteredCrimeCaseListChangedEvent
     */
    private void assertExecutionFailureWithEvent(Index index, String expectedMessage) {
        SelectCaseCommand selectCaseCommand = prepareCommand(index);

        try {
            selectCaseCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1
                    && eventsCollectorRule.eventsCollector.getMostRecent() instanceof FilteredCrimeCaseListChangedEvent);
        }
    }

    /**
     * Returns a {@code SelectCaseCommand} with parameters {@code index}.
     */
    private SelectCaseCommand prepareCommand(Index index) {
        SelectCaseCommand selectCaseCommand = new SelectCaseCommand(index);
        selectCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return selectCaseCommand;
    }
}
