package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.logic.commands.CommandTestUtil.showCrimeCaseAtIndex;
import static seedu.investigapptor.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_CASE;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Before;
import org.junit.Test;

import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.testutil.TypicalCrimeCases;
import seedu.investigapptor.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model investigatorModel;
    private Model expectedInvestigatorModel;
    private ListInvestigatorCommand listCommandInvestigators;

    private Model investigatorAliasModel;
    private Model expectedInvestigatorAliasModel;
    private ListInvestigatorCommand listCommandInvestigatorAlias;

    private Model crimeCaseModel;
    private Model expectedCrimeCaseModel;
    private ListCaseCommand listCommandCases;

    private Model crimeCaseAliasModel;
    private Model expectedCrimeCaseAliasModel;
    private ListCaseCommand listCommandCrimeCaseAlias;

    @Before
    public void setUp() {
        investigatorModel = new ModelManager(TypicalPersons.getTypicalInvestigapptor(), new UserPrefs());
        expectedInvestigatorModel = new ModelManager(investigatorModel.getInvestigapptor(), new UserPrefs());

        listCommandInvestigators = new ListInvestigatorCommand();
        listCommandInvestigators.setData(investigatorModel, new CommandHistory(), new UndoRedoStack());

        crimeCaseModel = new ModelManager(TypicalCrimeCases.getTypicalInvestigapptor(), new UserPrefs());
        expectedCrimeCaseModel = new ModelManager(crimeCaseModel.getInvestigapptor(), new UserPrefs());

        listCommandCases = new ListCaseCommand();
        listCommandCases.setData(investigatorModel, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_investigatorListIsNotFiltered_showsSameList() {
        assertCommandSuccess(listCommandInvestigators, investigatorModel, ListInvestigatorCommand.MESSAGE_SUCCESS,
                expectedInvestigatorModel);
    }

    @Test
    public void execute_investigatorListIsFiltered_showsEverything() {
        showPersonAtIndex(investigatorModel, INDEX_FIRST_PERSON);
        assertCommandSuccess(listCommandInvestigators, investigatorModel, ListInvestigatorCommand.MESSAGE_SUCCESS,
                expectedInvestigatorModel);
    }

    @Test
    public void execute_caseListIsNotFiltered_showsSameList() {
        assertCommandSuccess(listCommandCases, crimeCaseModel, ListCaseCommand.MESSAGE_SUCCESS,
                expectedCrimeCaseModel);
    }

    @Test
    public void execute_caseListIsFiltered_showsEverything() {
        showCrimeCaseAtIndex(crimeCaseModel, INDEX_FIRST_CASE);
        assertCommandSuccess(listCommandCases, crimeCaseModel, ListCaseCommand.MESSAGE_SUCCESS,
                expectedCrimeCaseModel);
    }

}
