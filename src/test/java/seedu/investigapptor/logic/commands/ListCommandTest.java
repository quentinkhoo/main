package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.logic.commands.CommandTestUtil.showCaseAtIndex;
import static seedu.investigapptor.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.investigapptor.testutil.TypicalCrimeCases.getTypicalCrimeCaseInvestigapptor;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.investigapptor.testutil.TypicalPersons.getTypicalInvestigapptor;

import org.junit.Before;
import org.junit.Test;

import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model investigatorModel;
    private Model expectedInvestigatorModel;
    private ListCommand listCommandInvestigators;
    private Model crimeCaseModel;
    private Model expectedCrimeCaseModel;
    private ListCommand listCommandCases;

    @Before
    public void setUp() {
        investigatorModel = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
        expectedInvestigatorModel = new ModelManager(investigatorModel.getInvestigapptor(), new UserPrefs());

        listCommandInvestigators = new ListCommand("investigators");
        listCommandInvestigators.setData(investigatorModel, new CommandHistory(), new UndoRedoStack());

        crimeCaseModel = new ModelManager(getTypicalCrimeCaseInvestigapptor(), new UserPrefs());
        expectedCrimeCaseModel = new ModelManager(crimeCaseModel.getInvestigapptor(), new UserPrefs());

        listCommandCases = new ListCommand("cases");
        listCommandCases.setData(investigatorModel, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_investigatorListIsNotFiltered_showsSameList() {
        assertCommandSuccess(listCommandInvestigators, investigatorModel, String.format(ListCommand.MESSAGE_SUCCESS,
                "investigators"), expectedInvestigatorModel);
    }

    @Test
    public void execute_investigatorListIsFiltered_showsEverything() {
        showPersonAtIndex(investigatorModel, INDEX_FIRST_PERSON);
        assertCommandSuccess(listCommandInvestigators, investigatorModel, String.format(ListCommand.MESSAGE_SUCCESS,
                "investigators"), expectedInvestigatorModel);
    }

    @Test
    public void execute_caseListIsNotFiltered_showsSameList() {
        assertCommandSuccess(listCommandCases, crimeCaseModel, String.format(ListCommand.MESSAGE_SUCCESS, "cases"),
                expectedCrimeCaseModel);
    }

    @Test
    public void execute_caseListIsFiltered_showsEverything() {
        showCaseAtIndex(crimeCaseModel, INDEX_FIRST_PERSON);
        assertCommandSuccess(listCommandCases, crimeCaseModel, String.format(ListCommand.MESSAGE_SUCCESS, "cases"),
                expectedCrimeCaseModel);
    }
}
