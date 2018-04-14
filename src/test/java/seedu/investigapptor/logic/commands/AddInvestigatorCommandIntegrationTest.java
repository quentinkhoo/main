package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.investigapptor.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.investigapptor.testutil.TypicalPersons.getTypicalInvestigapptor;

import org.junit.Before;
import org.junit.Test;

import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.testutil.InvestigatorBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddInvestigatorCommand}.
 */
public class AddInvestigatorCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() throws Exception {
        Investigator validPerson = new InvestigatorBuilder().build();

        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        assertCommandSuccess(prepareCommand(validPerson, model), model,
                String.format(AddInvestigatorCommand.MESSAGE_SUCCESS, validPerson), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getInvestigapptor().getPersonList().get(0);
        assertCommandFailure(prepareCommand(personInList, model), model,
                    AddInvestigatorCommand.MESSAGE_DUPLICATE_PERSON);

    }

    /**
     * Generates a new {@code AddInvestigatorCommand} which upon execution,
     * adds {@code person} into the {@code model}.
     */
    private AddInvestigatorCommand prepareCommand(Person person, Model model) {
        AddInvestigatorCommand command = new AddInvestigatorCommand(person);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
