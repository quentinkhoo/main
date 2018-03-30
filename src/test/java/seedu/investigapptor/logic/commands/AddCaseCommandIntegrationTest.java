package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.testutil.TypicalCrimeCases.getTypicalInvestigapptor;

import org.junit.BeforeClass;
import org.junit.Test;

import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.testutil.CrimeCaseBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCaseCommand}.
 */
public class AddCaseCommandIntegrationTest {

    private static Model model;

    @BeforeClass
    public static void setUp() {
        model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
    }

    @Test
    public void execute_newCrimeCase_success() throws Exception {
        CrimeCase validCrimeCase = new CrimeCaseBuilder().build();

        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.addCrimeCase(validCrimeCase);

        assertCommandSuccess(prepareCommand(new CrimeCaseBuilder().build(), model), model,
                String.format(AddCaseCommand.MESSAGE_SUCCESS, validCrimeCase), expectedModel);
    }

    @Test
    public void execute_duplicateCrimeCase_throwsCommandException() {
        CrimeCase crimeCaseInList = model.getInvestigapptor().getCrimeCaseList().get(0);
        assertCommandFailure(prepareCommand(crimeCaseInList, model), model,
                AddCaseCommand.MESSAGE_DUPLICATE_CASE);
    }

    /**
     * Generates a new {@code AddCaseCommand} which upon execution,
     * adds {@code crimeCase} into the {@code model}.
     */
    private AddCaseCommand prepareCommand(CrimeCase crimeCase, Model model) {
        AddCaseCommand command = new AddCaseCommand(crimeCase);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
