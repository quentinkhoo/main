package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.investigapptor.testutil.TypicalCrimeCases.ALFA;
import static seedu.investigapptor.testutil.TypicalCrimeCases.BRAVO;
import static seedu.investigapptor.testutil.TypicalCrimeCases.FOUR;
import static seedu.investigapptor.testutil.TypicalCrimeCases.ONE;
import static seedu.investigapptor.testutil.TypicalCrimeCases.THREE;
import static seedu.investigapptor.testutil.TypicalCrimeCases.TWO;
import static seedu.investigapptor.testutil.TypicalCrimeCases.getTypicalInvestigapptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.TagContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCaseTagsCommand}.
 */
public class FindCaseTagsCommandTest {
    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void equals() {
        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("first"));
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCaseTagsCommand findFirstCommand = new FindCaseTagsCommand(firstPredicate);
        FindCaseTagsCommand findSecondCommand = new FindCaseTagsCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCaseTagsCommand findFirstCommandCopy = new FindCaseTagsCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);
        FindCaseTagsCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 4);
        String userInput = "Murder Homicide".toLowerCase();  // Tags are converted to lowercase during comparison
        FindCaseTagsCommand command = prepareCommand(userInput);
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALFA, BRAVO, ONE, THREE));
    }

    /**
     * Parses {@code userInput} into a {@code FindCaseTagsCommand}.
     */
    private FindCaseTagsCommand prepareCommand(String userInput) {
        FindCaseTagsCommand command =
                new FindCaseTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<CrimCase>} is equal to {@code expectedList}<br>
     *     - the {@code Investigapptor} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindCaseTagsCommand command, String expectedMessage,
                                      List<CrimeCase> expectedList) {
        Investigapptor expectedInvestigapptor = new Investigapptor(model.getInvestigapptor());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedInvestigapptor, model.getInvestigapptor());
    }
}
