package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.investigapptor.logic.CommandHistory;
import seedu.investigapptor.logic.UndoRedoStack;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ReadOnlyInvestigapptor;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.exceptions.PersonNotFoundException;
import seedu.investigapptor.model.tag.Tag;
import seedu.investigapptor.model.tag.exceptions.TagNotFoundException;
import seedu.investigapptor.testutil.CrimeCaseBuilder;

public class AddCaseCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullCrimeCase_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCaseCommand(null);
    }

    @Test
    public void execute_crimeCaseAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingCrimeCaseAdded modelStub = new ModelStubAcceptingCrimeCaseAdded();
        CrimeCase validCrimeCase = new CrimeCaseBuilder().build();

        CommandResult commandResult = getAddCommandForCrimeCase(validCrimeCase, modelStub).execute();

        assertEquals(String.format(AddCaseCommand.MESSAGE_SUCCESS, validCrimeCase),
                commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validCrimeCase), modelStub.crimeCasesAdded);
    }

    @Test
    public void execute_duplicateCrimeCase_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateCrimeCaseException();
        CrimeCase validCrimeCase = new CrimeCaseBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCaseCommand.MESSAGE_DUPLICATE_CASE);

        getAddCommandForCrimeCase(validCrimeCase, modelStub).execute();
    }

    @Test
    public void equals() {
        CrimeCase projHappy = new CrimeCaseBuilder().withName("Project Happy").build();
        CrimeCase projSad = new CrimeCaseBuilder().withName("Project Sad").build();
        AddCaseCommand addProjHappyCommand = new AddCaseCommand(projHappy);
        AddCaseCommand addProjSadCommand = new AddCaseCommand(projSad);

        // same object -> returns true
        assertTrue(addProjHappyCommand.equals(addProjHappyCommand));

        // same values -> returns true
        AddCaseCommand addProjHappyCommandCopy = new AddCaseCommand(projHappy);
        assertTrue(addProjHappyCommand.equals(addProjHappyCommandCopy));

        // different types -> returns false
        assertFalse(addProjHappyCommand.equals(1));

        // null -> returns false
        assertFalse(addProjHappyCommand.equals(null));

        // different case -> returns false
        assertFalse(addProjHappyCommand.equals(addProjSadCommand));
    }

    /**
     * Generates a new AddCaseCommand with the details of the given case.
     */
    private AddCaseCommand getAddCommandForCrimeCase(CrimeCase crimeCase, Model model) {
        AddCaseCommand command = new AddCaseCommand(crimeCase);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyInvestigapptor newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyInvestigapptor getInvestigapptor() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(Person target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void addCrimeCase(CrimeCase crimecase)
                throws DuplicateCrimeCaseException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteTag(Tag toDelete)
                throws TagNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void backUpInvestigapptor(String fileName) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<CrimeCase> getFilteredCrimeCaseList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredCrimeCaseList(Predicate<CrimeCase> predicate) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicateCrimeCaseException when trying to add a case.
     */
    private class ModelStubThrowingDuplicateCrimeCaseException extends ModelStub {
        @Override
        public void addCrimeCase(CrimeCase crimeCase) throws DuplicateCrimeCaseException {
            throw new DuplicateCrimeCaseException();
        }

        @Override
        public ReadOnlyInvestigapptor getInvestigapptor() {
            return new Investigapptor();
        }
    }

    /**
     * A Model stub that always accept the case being added.
     */
    private class ModelStubAcceptingCrimeCaseAdded extends ModelStub {
        final ArrayList<CrimeCase> crimeCasesAdded = new ArrayList<>();

        @Override
        public void addCrimeCase(CrimeCase crimeCase) throws DuplicateCrimeCaseException {
            requireNonNull(crimeCase);
            crimeCasesAdded.add(crimeCase);
        }

        @Override
        public ReadOnlyInvestigapptor getInvestigapptor() {
            return new Investigapptor();
        }
    }

}
