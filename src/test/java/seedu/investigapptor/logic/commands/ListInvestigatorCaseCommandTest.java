package seedu.investigapptor.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.investigapptor.testutil.TypicalInvestigator.ALICE;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.testutil.CrimeCaseBuilder;
import seedu.investigapptor.testutil.TypicalInvestigator;
//@@author Marcus-cxc
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListInvestigatorCaseCommandTest {

    private Model model = new ModelManager(TypicalInvestigator.getTypicalInvestigapptor(), new UserPrefs());
    private Model expectedInvestigatorModel;
    private ListInvestigatorCaseCommand listInvestigatorCaseCommand;


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
