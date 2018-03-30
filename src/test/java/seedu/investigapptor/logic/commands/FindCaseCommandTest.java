package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_CASES_LISTED_OVERVIEW;
import static seedu.investigapptor.testutil.TypicalCrimeCases.CHARLIE;
import static seedu.investigapptor.testutil.TypicalCrimeCases.ECHO;
import static seedu.investigapptor.testutil.TypicalCrimeCases.FOXTROT;
import static seedu.investigapptor.testutil.TypicalCrimeCases.getTypicalInvestigapptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.crimecase.CaseNameContainsKeywordsPredicate;
import seedu.investigapptor.model.crimecase.CrimeCase;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCaseCommand}.
 */
public class FindCaseCommandTest {
    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
    }

    @Test
    public void equals() {
        CaseNameContainsKeywordsPredicate firstPredicate =
                new CaseNameContainsKeywordsPredicate(Collections.singletonList("first"));
        CaseNameContainsKeywordsPredicate secondPredicate =
                new CaseNameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCaseCommand findFirstCommand = new FindCaseCommand(firstPredicate);
        FindCaseCommand findSecondCommand = new FindCaseCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCaseCommand findFirstCommandCopy = new FindCaseCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different case -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noCrimeCaseFound() {
        String expectedMessage = String.format(MESSAGE_CASES_LISTED_OVERVIEW, 0);
        FindCaseCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multipleCrimeCasesFound() {
        String expectedMessage = String.format(MESSAGE_CASES_LISTED_OVERVIEW, 3);
        FindCaseCommand command = prepareCommand("Charlie Echo Foxtrot");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CHARLIE, ECHO, FOXTROT));
    }

    /**
     * Parses {@code userInput} into a {@code FindCaseCommand}.
     */
    private FindCaseCommand prepareCommand(String userInput) {
        FindCaseCommand command =
                new FindCaseCommand(new CaseNameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<CrimeCase>} is equal to {@code expectedList}<br>
     *     - the {@code Investigapptor} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindCaseCommand command, String expectedMessage, List<CrimeCase> expectedList) {
        Investigapptor expectedInvestigapptor = new Investigapptor(model.getInvestigapptor());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredCrimeCaseList());
        assertEquals(expectedInvestigapptor, model.getInvestigapptor());
    }
}
