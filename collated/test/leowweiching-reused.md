# leowweiching-reused
###### /java/seedu/investigapptor/logic/commands/AddCaseCommandIntegrationTest.java
``` java
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
```
###### /java/seedu/investigapptor/logic/commands/AddCaseCommandTest.java
``` java
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
        public void updateCrimeCase(CrimeCase target, CrimeCase editedCase)
                throws DuplicateCrimeCaseException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteCrimeCase(CrimeCase target)
            throws CrimeCaseNotFoundException {
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

        @Override
        public void updatePassword(Password password) {
            fail("This method should not be called");
        }

        @Override
        public void removePassword() {
            fail("This method should not be called");
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
```
###### /java/seedu/investigapptor/logic/commands/DeleteCaseCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCaseCommand}.
 */
public class DeleteCaseCommandTest {

    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);

        String expectedMessage = String.format(DeleteCaseCommand.MESSAGE_DELETE_CASE_SUCCESS, crimeCaseToDelete);

        ModelManager expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.deleteCrimeCase(crimeCaseToDelete);

        assertCommandSuccess(deleteCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        DeleteCaseCommand deleteCaseCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    /* TO REVIEW */
    /*
    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);

        String expectedMessage = String.format(DeleteCaseCommand.MESSAGE_DELETE_CASE_SUCCESS, crimeCaseToDelete);

        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.deleteCrimeCase(crimeCaseToDelete);
        showNoCrimeCase(expectedModel);

        assertCommandSuccess(deleteCaseCommand, model, expectedMessage, expectedModel);
    }
    */

    /* TO REVIEW */
    /*
    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        Index outOfBoundIndex = INDEX_SECOND_CASE;
        // ensures that outOfBoundIndex is still in bounds of investigapptor book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getInvestigapptor().getCrimeCaseList().size());

        DeleteCaseCommand deleteCaseCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }
    */

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);
        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());

        // delete -> first crimeCase deleted
        deleteCaseCommand.execute();
        undoRedoStack.push(deleteCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first crimeCase deleted again
        expectedModel.deleteCrimeCase(crimeCaseToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        DeleteCaseCommand deleteCaseCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteCaseCommand not pushed into undoRedoStack
        assertCommandFailure(deleteCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /* TO REVIEW */
    /**
     * 1. Deletes a {@code CrimeCase} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted crimeCase in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the crimeCase object regardless of indexing.
     */
    /*
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameCrimeCaseDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);
        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());

        showCrimeCaseAtIndex(model, INDEX_SECOND_CASE);
        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        // delete -> deletes second crimeCase in unfiltered crimeCase list / first crimeCase in filtered crimeCase list
        deleteCaseCommand.execute();
        undoRedoStack.push(deleteCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.deleteCrimeCase(crimeCaseToDelete);
        assertNotEquals(crimeCaseToDelete, model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased()));
        // redo -> deletes same second crimeCase in unfiltered crimeCase list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
    */

    @Test
    public void equals() throws Exception {
        DeleteCaseCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_CASE);
        DeleteCaseCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_CASE);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCaseCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_CASE);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different crimeCase -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteCaseCommand} with the parameter {@code index}.
     */
    private DeleteCaseCommand prepareCommand(Index index) {
        DeleteCaseCommand deleteCaseCommand = new DeleteCaseCommand(index);
        deleteCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCaseCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoCrimeCase(Model model) {
        model.updateFilteredCrimeCaseList(p -> false);

        assertTrue(model.getFilteredCrimeCaseList().isEmpty());
    }
}
```
###### /java/seedu/investigapptor/logic/commands/EditCaseCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for EditCaseCommand.
 */
public class EditCaseCommandTest {

    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    /* TO REVIEW
    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        CrimeCase editedCrimeCase = new CrimeCaseBuilder().build();
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(editedCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE, descriptor);

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(model.getFilteredCrimeCaseList().get(0), editedCrimeCase);

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }*/

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastCrimeCase = Index.fromOneBased(model.getFilteredCrimeCaseList().size());
        CrimeCase lastCrimeCase = model.getFilteredCrimeCaseList().get(indexLastCrimeCase.getZeroBased());

        CrimeCaseBuilder crimeCaseInList = new CrimeCaseBuilder(lastCrimeCase);
        CrimeCase editedCrimeCase = crimeCaseInList.withName(VALID_CASENAME_BANANA)
                .withDescription(VALID_DESCRIPTION_BANANA)
                .withTags(VALID_TAG_MURDER).build();

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_BANANA)
                .withDescription(VALID_DESCRIPTION_BANANA).withTags(VALID_TAG_MURDER).build();
        EditCaseCommand editCaseCommand = prepareCommand(indexLastCrimeCase, descriptor);

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(lastCrimeCase, editedCrimeCase);

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE,
                new EditCrimeCaseDescriptor());
        CrimeCase editedCrimeCase = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }

    /* TO REVIEW
    @Test
    public void execute_filteredList_success() throws Exception {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        CrimeCase crimeCaseInFilteredList = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        CrimeCase editedCrimeCase = new CrimeCaseBuilder(crimeCaseInFilteredList)
                .withName(VALID_CASENAME_BANANA).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE,
                new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_BANANA).build());

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(model.getFilteredCrimeCaseList().get(0), editedCrimeCase);

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }*/

    @Test
    public void execute_duplicateCrimeCaseUnfilteredList_failure() {
        CrimeCase firstCrimeCase = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(firstCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_SECOND_CASE, descriptor);

        assertCommandFailure(editCaseCommand, model, EditCaseCommand.MESSAGE_DUPLICATE_CASE);
    }

    @Test
    public void execute_duplicateCrimeCaseFilteredList_failure() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        // edit crimeCase in filtered list into a duplicate in investigapptor book
        CrimeCase crimeCaseInList = model.getInvestigapptor().getCrimeCaseList().get(INDEX_SECOND_CASE.getZeroBased());
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE,
                new EditCrimeCaseDescriptorBuilder(crimeCaseInList).build());

        assertCommandFailure(editCaseCommand, model, EditCaseCommand.MESSAGE_DUPLICATE_CASE);
    }

    @Test
    public void execute_invalidCrimeCaseIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withCaseName(VALID_CASENAME_BANANA).build();
        EditCaseCommand editCaseCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of investigapptor book
     */
    /* TO REVIEW
    @Test
    public void execute_invalidCrimeCaseIndexFilteredList_failure() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);
        Index outOfBoundIndex = INDEX_SECOND_CASE;
        // ensures that outOfBoundIndex is still in bounds of investigapptor book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getInvestigapptor().getCrimeCaseList().size());

        EditCaseCommand editCaseCommand = prepareCommand(outOfBoundIndex,
                new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_BANANA).build());

        assertCommandFailure(editCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }*/

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CrimeCase editedCrimeCase = new CrimeCaseBuilder().build();
        CrimeCase crimeCaseToEdit = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(editedCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE, descriptor);
        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        // edit -> first crimeCase edited
        editCaseCommand.execute();
        undoRedoStack.push(editCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first crimeCase edited again
        expectedModel.updateCrimeCase(crimeCaseToEdit, editedCrimeCase);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withCaseName(VALID_CASENAME_BANANA).build();
        EditCaseCommand editCaseCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> editCaseCommand not pushed into undoRedoStack
        assertCommandFailure(editCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code CrimeCase} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited crimeCase in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the crimeCase object regardless of indexing.
     */
    /* TO REVIEW
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameCrimeCaseEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CrimeCase editedCrimeCase = new CrimeCaseBuilder().build();
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(editedCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE, descriptor);
        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        showCrimeCaseAtIndex(model, INDEX_SECOND_CASE);
        CrimeCase crimeCaseToEdit = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        // edit -> edits second crimeCase in unfiltered crimeCase list / first crimeCase in filtered crimeCase list
        editCaseCommand.execute();
        undoRedoStack.push(editCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateCrimeCase(crimeCaseToEdit, editedCrimeCase);
        assertNotEquals(model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased()), crimeCaseToEdit);
        // redo -> edits same second crimeCase in unfiltered crimeCase list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }*/

    @Test
    public void equals() throws Exception {
        final EditCaseCommand standardCommand = prepareCommand(INDEX_FIRST_CASE, DESC_APPLE);

        // same values -> returns true
        EditCrimeCaseDescriptor copyDescriptor = new EditCrimeCaseDescriptor(DESC_APPLE);
        EditCaseCommand commandWithSameValues = prepareCommand(INDEX_FIRST_CASE, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCaseCommand(INDEX_SECOND_CASE, DESC_APPLE)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCaseCommand(INDEX_FIRST_CASE, DESC_BANANA)));
    }

    /**
     * Returns an {@code EditCaseCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCaseCommand prepareCommand(Index index, EditCrimeCaseDescriptor descriptor) {
        EditCaseCommand editCaseCommand = new EditCaseCommand(index, descriptor);
        editCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editCaseCommand;
    }
}
```
###### /java/seedu/investigapptor/logic/commands/FindCaseCommandTest.java
``` java
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
```
###### /java/seedu/investigapptor/logic/parser/DeleteCaseCommandParserTest.java
``` java
/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCaseCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCaseCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCaseCommandParserTest {

    private DeleteCaseCommandParser parser = new DeleteCaseCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCaseCommand(INDEX_FIRST_CASE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCaseCommand.MESSAGE_USAGE));
    }
}
```
###### /java/seedu/investigapptor/logic/parser/EditCaseCommandParserTest.java
``` java
public class EditCaseCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCaseCommand.MESSAGE_USAGE);

    private EditCaseCommandParser parser = new EditCaseCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_CASENAME_APPLE, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCaseCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + CASENAME_DESC_APPLE, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + CASENAME_DESC_APPLE, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 k/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_CASENAME_DESC,
                CaseName.MESSAGE_CASE_NAME_CONSTRAINTS); // invalid case name
        assertParseFailure(parser, "1" + INVALID_DESCRIPTION_DESC,
                Description.MESSAGE_DESCRIPTION_CONSTRAINTS); // invalid description
        assertParseFailure(parser, "1" + INVALID_INVESTIGATOR_INDEX_DESC,
                MESSAGE_INVALID_INDEX); // invalid investigator index
        assertParseFailure(parser, "1" + INVALID_STARTDATE_DESC,
                Date.MESSAGE_DATE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, "1" + INVALID_TAG_DESC,
                Tag.MESSAGE_TAG_CONSTRAINTS); // invalid tag

        // invalid description followed by valid investigator index
        assertParseFailure(parser, "1" + INVALID_DESCRIPTION_DESC + INVESTIGATOR_DESC_APPLE,
                Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        // valid description followed by invalid description. The test case for invalid description followed by
        // valid description is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + DESCRIPTION_DESC_BANANA + INVALID_DESCRIPTION_DESC,
                Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code CrimeCase} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_FRAUD + TAG_DESC_MURDER
                + TAG_EMPTY, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_FRAUD + TAG_EMPTY
                + TAG_DESC_MURDER, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_FRAUD
                + TAG_DESC_MURDER, Tag.MESSAGE_TAG_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_CASENAME_DESC + INVALID_INVESTIGATOR_INDEX_DESC
                        + VALID_STARTDATE_APPLE + VALID_DESCRIPTION_APPLE,
                CaseName.MESSAGE_CASE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_CASE;
        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_BANANA + TAG_DESC_MURDER
                + STARTDATE_DESC_APPLE + CASENAME_DESC_APPLE + TAG_DESC_FRAUD;

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_APPLE)
                .withDescription(VALID_DESCRIPTION_BANANA)
                .withStartDate(VALID_STARTDATE_APPLE)
                .withTags(VALID_TAG_MURDER, VALID_TAG_FRAUD).build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_CASE;

        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_BANANA + STARTDATE_DESC_APPLE;

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_BANANA)
                .withStartDate(VALID_STARTDATE_APPLE).build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // case name
        Index targetIndex = INDEX_THIRD_CASE;
        String userInput = targetIndex.getOneBased() + CASENAME_DESC_APPLE;
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withCaseName(VALID_CASENAME_APPLE).build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // description
        userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_APPLE;
        descriptor = new EditCrimeCaseDescriptorBuilder().withDescription(VALID_DESCRIPTION_APPLE).build();
        expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // investigapptor
        userInput = targetIndex.getOneBased() + STARTDATE_DESC_APPLE;
        descriptor = new EditCrimeCaseDescriptorBuilder().withStartDate(VALID_STARTDATE_APPLE).build();
        expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_FRAUD;
        descriptor = new EditCrimeCaseDescriptorBuilder().withTags(VALID_TAG_FRAUD).build();
        expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_CASE;
        String userInput = targetIndex.getOneBased()  + DESCRIPTION_DESC_APPLE + STARTDATE_DESC_APPLE
                + TAG_DESC_FRAUD + DESCRIPTION_DESC_APPLE + STARTDATE_DESC_APPLE
                + TAG_DESC_FRAUD + DESCRIPTION_DESC_BANANA + STARTDATE_DESC_BANANA
                + TAG_DESC_MURDER;

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_BANANA)
                .withStartDate(VALID_STARTDATE_BANANA)
                .withTags(VALID_TAG_FRAUD, VALID_TAG_MURDER)
                .build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_CASE;
        String userInput = targetIndex.getOneBased() + INVALID_DESCRIPTION_DESC + DESCRIPTION_DESC_BANANA;
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_BANANA).build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + CASENAME_DESC_BANANA + INVALID_DESCRIPTION_DESC
                + STARTDATE_DESC_BANANA + DESCRIPTION_DESC_BANANA;
        descriptor = new EditCrimeCaseDescriptorBuilder().withDescription(VALID_DESCRIPTION_BANANA)
                .withCaseName(VALID_CASENAME_BANANA)
                .withStartDate(VALID_STARTDATE_BANANA).build();
        expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_CASE;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder().withTags().build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
```
###### /java/seedu/investigapptor/logic/parser/InvestigapptorParserTest.java
``` java
    @Test
    public void parseCommand_findCase() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCaseCommand command = (FindCaseCommand) parser.parseCommand(
                FindCaseCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCaseCommand(new CaseNameContainsKeywordsPredicate(keywords)), command);
    }

```
###### /java/seedu/investigapptor/logic/parser/InvestigapptorParserTest.java
``` java
    @Test
    public void parseCommand_findCaseAlias() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCaseCommand command = (FindCaseCommand) parser.parseCommand(
                FindCaseCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCaseCommand(new CaseNameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findInvestigator() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindInvestigatorCommand command = (FindInvestigatorCommand) parser.parseCommand(
                FindInvestigatorCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindInvestigatorCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findInvestigatorAlias() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindInvestigatorCommand command = (FindInvestigatorCommand) parser.parseCommand(
                FindInvestigatorCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindInvestigatorCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_helpAlias() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_ALIAS) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_ALIAS + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_historyAlias() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_listInvestigators() throws Exception {
        assertTrue(parser.parseCommand("listinvestigators") instanceof ListInvestigatorCommand);
        assertTrue(parser.parseCommand(ListInvestigatorCommand.COMMAND_WORD) instanceof ListInvestigatorCommand);
    }

    @Test
    public void parseCommand_listInvestigatorsAlias() throws Exception {
        assertTrue(parser.parseCommand("li") instanceof ListInvestigatorCommand);
        assertTrue(parser.parseCommand(ListInvestigatorCommand.COMMAND_ALIAS) instanceof ListInvestigatorCommand);
    }

    @Test
    public void parseCommand_listCases() throws Exception {
        assertTrue(parser.parseCommand("listcases") instanceof ListCaseCommand);
        assertTrue(parser.parseCommand(ListCaseCommand.COMMAND_WORD) instanceof ListCaseCommand);
    }

    @Test
    public void parseCommand_listCasesAlias() throws Exception {
        assertTrue(parser.parseCommand("lc") instanceof ListCaseCommand);
        assertTrue(parser.parseCommand(ListCaseCommand.COMMAND_ALIAS) instanceof ListCaseCommand);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectInvestigatorCommand command = (SelectInvestigatorCommand) parser.parseCommand(
                SelectInvestigatorCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectInvestigatorCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_selectAlias() throws Exception {
        SelectInvestigatorCommand command = (SelectInvestigatorCommand) parser.parseCommand(
                SelectInvestigatorCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectInvestigatorCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_redoCommandAlias_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_undoCommandAlias_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_ALIAS) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
```
###### /java/seedu/investigapptor/storage/XmlSerializableInvestigapptorTest.java
``` java
    @Test
    public void toModelType_invalidCrimeCaseFile_throwsIllegalValueException() throws Exception {
        XmlSerializableInvestigapptor dataFromFile = XmlUtil.getDataFromFile(INVALID_CRIMECASE_FILE,
                XmlSerializableInvestigapptor.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

```
