package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.logic.commands.CommandTestUtil.showCaseAtIndex;
import static seedu.investigapptor.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.BeforeClass;
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

    private static Model investigatorModel;
    private static Model expectedInvestigatorModel;
    private static ListCommand listCommandInvestigators;

    private static Model investigatorAliasModel;
    private static Model expectedInvestigatorAliasModel;
    private static ListCommand listCommandInvestigatorAlias;

    private static Model crimeCaseModel;
    private static Model expectedCrimeCaseModel;
    private static ListCommand listCommandCases;

    private static Model crimeCaseAliasModel;
    private static Model expectedCrimeCaseAliasModel;
    private static ListCommand listCommandCrimeCaseAlias;

    @BeforeClass
    public static void setUp() {
        investigatorModel = new ModelManager(TypicalPersons.getTypicalInvestigapptor(), new UserPrefs());
        expectedInvestigatorModel = new ModelManager(investigatorModel.getInvestigapptor(), new UserPrefs());

        listCommandInvestigators = new ListCommand("investigators");
        listCommandInvestigators.setData(investigatorModel, new CommandHistory(), new UndoRedoStack());

        crimeCaseModel = new ModelManager(TypicalCrimeCases.getTypicalInvestigapptor(), new UserPrefs());
        expectedCrimeCaseModel = new ModelManager(crimeCaseModel.getInvestigapptor(), new UserPrefs());

        listCommandCases = new ListCommand("cases");
        listCommandCases.setData(investigatorModel, new CommandHistory(), new UndoRedoStack());

        investigatorAliasModel = new ModelManager(TypicalPersons.getTypicalInvestigapptor(), new UserPrefs());
        expectedInvestigatorAliasModel = new ModelManager(investigatorAliasModel.getInvestigapptor(), new UserPrefs());

        listCommandInvestigatorAlias = new ListCommand(" inv");
        listCommandInvestigatorAlias.setData(investigatorAliasModel, new CommandHistory(), new UndoRedoStack());

        crimeCaseAliasModel = new ModelManager(TypicalPersons.getTypicalInvestigapptor(), new UserPrefs());
        expectedCrimeCaseAliasModel = new ModelManager(crimeCaseAliasModel.getInvestigapptor(), new UserPrefs());

        listCommandCrimeCaseAlias = new ListCommand(" c");
        listCommandCrimeCaseAlias.setData(crimeCaseAliasModel, new CommandHistory(), new UndoRedoStack());

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
