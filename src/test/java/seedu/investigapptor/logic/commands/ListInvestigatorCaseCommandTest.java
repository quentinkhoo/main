package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_THIRD_CASE;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.investigapptor.testutil.TypicalInvestigator.ALICE;
import static seedu.investigapptor.testutil.TypicalInvestigator.BENSON;
import static seedu.investigapptor.testutil.TypicalInvestigator.getTypicalInvestigapptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.crimecase.CaseContainsInvestigatorPredicate;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.testutil.CrimeCaseBuilder;
import seedu.investigapptor.testutil.TypicalInvestigator;
//@@author Marcus-cxc
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListInvestigatorCaseCommandTest {

    private Model model = new ModelManager(TypicalInvestigator.getTypicalInvestigapptor(), new UserPrefs());
    private ListInvestigatorCaseCommand listInvestigatorCaseCommand;

    @Test
    public void equals() {
        ListInvestigatorCaseCommand findFirstCommand = new ListInvestigatorCaseCommand(INDEX_FIRST_PERSON);
        ListInvestigatorCaseCommand findSecondCommand = new ListInvestigatorCaseCommand(INDEX_SECOND_PERSON);
        findFirstCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        findSecondCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        try{
            findFirstCommand.execute();
        } catch (CommandException e) {
            throw new AssertionError("First command execute fail");
        }
        try{
            findSecondCommand.execute();
        } catch (CommandException e) {
            throw new AssertionError("Second command execute fail");
        }

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        ListInvestigatorCaseCommand findFirstCommandCopy = new ListInvestigatorCaseCommand(INDEX_FIRST_PERSON);
        findFirstCommandCopy.setData(model, new CommandHistory(), new UndoRedoStack());
        try{
            findFirstCommandCopy.execute();
        } catch (CommandException e) {
            throw new AssertionError("findFirstCommandCopy execute fail");
        }

        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }
    @Test
    public void execute_listInvestigatorCrimeCases_showsSameList() {
        listInvestigatorCaseCommand = new ListInvestigatorCaseCommand(INDEX_FIRST_PERSON);
        listInvestigatorCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        List<CrimeCase> expected = new ArrayList<>();
        expected.add(new CrimeCaseBuilder().withName("Omega").withInvestigator(ALICE).build());
        expected.add(new CrimeCaseBuilder().withName("Stigma").withInvestigator(ALICE).build());
        try {
            assertCommandSuccess(listInvestigatorCaseCommand,
                    String.format(Messages.MESSAGE_CASES_LISTED_OVERVIEW,
                            2), expected);
        } catch (CommandException e) {
            throw new AssertionError("Error");
        }
    }

    @Test
    public void execute_listInvestigatorCrimeCases_emptyResult() {
        listInvestigatorCaseCommand = new ListInvestigatorCaseCommand(INDEX_THIRD_PERSON);
        listInvestigatorCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        List<CrimeCase> expected = new ArrayList<>();
        try {
            assertCommandSuccess(listInvestigatorCaseCommand,
                    String.format(Messages.MESSAGE_CASES_LISTED_OVERVIEW,
                            0), expected);
        } catch (CommandException e) {
            throw new AssertionError("Error");
        }
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     * - the command feedback is equal to {@code expectedMessage}<br>
     * - the {@code FilteredList<CrimeCase>} is equal to {@code expectedList}<br>
     * - the {@code Investigapptor} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(ListInvestigatorCaseCommand command, String expectedMessage,
                                      List<CrimeCase> expectedList) throws CommandException {
        Investigapptor expectedInvestigapptor = new Investigapptor(model.getInvestigapptor());

        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredCrimeCaseList());
        assertEquals(expectedInvestigapptor, model.getInvestigapptor());
    }
}
