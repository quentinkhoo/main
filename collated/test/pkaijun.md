# pkaijun
###### /java/seedu/investigapptor/logic/commands/CloseCaseCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand)
 * and unit tests for CloseCaseCommand.
 */
public class CloseCaseCommandTest {
    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void equals() throws Exception {
        final CloseCaseCommand standardCommand = prepareCommand(INDEX_SECOND_CASE);

        // same values -> returns true
        CloseCaseCommand commandWithSameValues = prepareCommand(INDEX_SECOND_CASE);
        //assertTrue(standardCommand.equals(commandWithSameValues));

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
        assertFalse(standardCommand.equals(new CloseCaseCommand(INDEX_FIRST_CASE)));
    }

    @Test
    public void execute_validCrimeCaseIndexFilteredList_success() throws Exception {
        Index indexLastCrimeCase = Index.fromOneBased(model.getFilteredCrimeCaseList().size());
        CrimeCase lastCrimeCase = model.getFilteredCrimeCaseList().get(indexLastCrimeCase.getZeroBased());

        CrimeCaseBuilder crimeCaseInList = new CrimeCaseBuilder(lastCrimeCase);
        CrimeCase closedCrimeCase = crimeCaseInList.withStatus(CASE_CLOSE).build();

        CloseCaseCommand closeCaseCommand = prepareCommand(indexLastCrimeCase);

        String expectedMessage = String.format(MESSAGE_CLOSE_CASE_SUCCESS, closedCrimeCase.getStatus().toString());

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(lastCrimeCase, closedCrimeCase);

        assertCommandSuccess(closeCaseCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of investigapptor book
     */
    @Test
    public void execute_invalidCrimeCaseIndexFilteredList_failure() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);
        Index outOfBoundIndex = INDEX_SECOND_CASE;

        // ensures that outOfBoundIndex is still in bounds of investigapptor book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getInvestigapptor().getCrimeCaseList().size());

        CloseCaseCommand closeCaseCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(closeCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    /**
     * Command applied on a case whose status is already closed - test should fail
     */
    @Test
    public void execute_invalidCaseStatusClosed_failure() {
        final CloseCaseCommand closeCaseCommand = prepareCommand(INDEX_FIRST_CASE); // case status already closed
        assertCommandFailure(closeCaseCommand, model, MESSAGE_CASE_ALREADY_CLOSE);
    }

    /**
     * Invalid index as the input - test should fail
     */
    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);

        CloseCaseCommand closeCaseCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> editCaseCommand not pushed into undoRedoStack
        assertCommandFailure(closeCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Closes a {@code CrimeCase} from a filtered list.
     * 2. Undo the close.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited crimeCase in the
     * unfiltered list is different from the index at the filtered list.
     */
    @Test
    public void executeUndo_validIndexFilteredList_sameCrimeCaseClosed() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);

        CrimeCase closedCrimeCase = new CrimeCaseBuilder().withStatus(CASE_CLOSE).build();
        CloseCaseCommand closeCaseCommand = prepareCommand(INDEX_FIRST_CASE);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        showCrimeCaseAtIndex(model, INDEX_SECOND_CASE);
        CrimeCase crimeCaseToClose = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());

        // close -> closes second crimeCase in unfiltered crimeCase list / first crimeCase in filtered crimeCase list
        closeCaseCommand.execute();
        undoRedoStack.push(closeCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateCrimeCase(crimeCaseToClose, closedCrimeCase);
        assertNotEquals(model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased()), crimeCaseToClose);
    }

    /**
     * Returns an {@code CloseCaseCommand} with parameters {@code index}
     */
    private CloseCaseCommand prepareCommand(Index index) {
        CloseCaseCommand closeCaseCommand = new CloseCaseCommand(index);
        closeCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return closeCaseCommand;
    }
}
```
###### /java/seedu/investigapptor/logic/commands/FindCaseTagsCommandTest.java
``` java
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
    public void execute_zeroKeywords_noCrimeCaseFound() {
        String expectedMessage = String.format(MESSAGE_CASES_LISTED_OVERVIEW, 0);
        FindCaseTagsCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multipleCrimeCaseFound() {
        String expectedMessage = String.format(MESSAGE_CASES_LISTED_OVERVIEW, 5);
        String userInput = "Murder Kidnap".toLowerCase();  // Tags are converted to lowercase during comparison
        FindCaseTagsCommand command = prepareCommand(userInput);
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALFA, BRAVO, ONE, TWO, THREE));
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
     *     - the {@code FilteredList<CrimeCase>} is equal to {@code expectedList}<br>
     *     - the {@code Investigapptor} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindCaseTagsCommand command, String expectedMessage,
                                      List<CrimeCase> expectedList) {
        Investigapptor expectedInvestigapptor = new Investigapptor(model.getInvestigapptor());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredCrimeCaseList());
        assertEquals(expectedInvestigapptor, model.getInvestigapptor());
    }
}
```
###### /java/seedu/investigapptor/logic/commands/FindCloseCaseCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindCloseCaseCommand}.
 */
public class FindCloseCaseCommandTest {
    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void equals() {
        FindCloseCaseCommand findCloseCaseFirstCommand = new FindCloseCaseCommand();
        FindCloseCaseCommand findCloseCaseSecondCommand = new FindCloseCaseCommand();

        // same object -> returns true
        assertTrue(findCloseCaseFirstCommand.equals(findCloseCaseFirstCommand));

        // same values -> returns true
        FindCloseCaseCommand findFirstCommandCopy = new FindCloseCaseCommand();
        assertTrue(findCloseCaseFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findCloseCaseFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findCloseCaseFirstCommand.equals(null));

        // same object -> returns true
        assertTrue(findCloseCaseFirstCommand.equals(findCloseCaseSecondCommand));
    }

    @Test
    public void execute_command_multipleCrimeCaseFound() {
        String expectedMessage = String.format(MESSAGE_CASES_LISTED_OVERVIEW, 7);
        FindCloseCaseCommand command = prepareCommand();
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALFA, CHARLIE, FOXTROT, ONE, TWO, THREE, FOUR));
    }

    /**
     * Prepare the FindCloseCaseCommand {@code FindCloseCaseCommand}.
     */
    private FindCloseCaseCommand prepareCommand() {
        FindCloseCaseCommand command = new FindCloseCaseCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<CrimeCase>} is equal to {@code expectedList}<br>
     *     - the {@code Investigapptor} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindCloseCaseCommand command, String expectedMessage,
                                      List<CrimeCase> expectedList) {
        Investigapptor expectedInvestigapptor = new Investigapptor(model.getInvestigapptor());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredCrimeCaseList());
        assertEquals(expectedInvestigapptor, model.getInvestigapptor());
    }
}
```
###### /java/seedu/investigapptor/logic/commands/FindInvestTagsCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindInvestTagsCommand}.
 */
public class FindInvestTagsCommandTest {
    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void equals() {
        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("first"));
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("second"));

        FindInvestTagsCommand findFirstCommand = new FindInvestTagsCommand(firstPredicate);
        FindInvestTagsCommand findSecondCommand = new FindInvestTagsCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindInvestTagsCommand findFirstCommandCopy = new FindInvestTagsCommand(firstPredicate);
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
        FindInvestTagsCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 3);
        String userInput = "teamB new".toLowerCase();  // Tags are converted to lowercase during comparison
        FindInvestTagsCommand command = prepareCommand(userInput);
        assertCommandSuccess(command, expectedMessage, Arrays.asList(SIR_LIM, MDM_ONG, SIR_CHONG));
    }

    /**
     * Parses {@code userInput} into a {@code FindInvestTagsCommand}.
     */
    private FindInvestTagsCommand prepareCommand(String userInput) {
        FindInvestTagsCommand command =
                new FindInvestTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code Investigapptor} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindInvestTagsCommand command, String expectedMessage,
                                      List<Person> expectedList) {
        Investigapptor expectedInvestigapptor = new Investigapptor(model.getInvestigapptor());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedInvestigapptor, model.getInvestigapptor());
    }
}
```
###### /java/seedu/investigapptor/logic/commands/FindOpenCaseCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindOpenCaseCommand}.
 */
public class FindOpenCaseCommandTest {
    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void equals() {
        FindOpenCaseCommand findOpenCaseFirstCommand = new FindOpenCaseCommand();
        FindOpenCaseCommand findOpenCaseSecondCommand = new FindOpenCaseCommand();

        // same object -> returns true
        assertTrue(findOpenCaseFirstCommand.equals(findOpenCaseFirstCommand));

        // same values -> returns true
        FindOpenCaseCommand findFirstCommandCopy = new FindOpenCaseCommand();
        assertTrue(findOpenCaseFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findOpenCaseFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findOpenCaseFirstCommand.equals(null));

        // same object -> returns true
        assertTrue(findOpenCaseFirstCommand.equals(findOpenCaseSecondCommand));
    }

    @Test
    public void execute_command_multipleCrimeCaseFound() {
        String expectedMessage = String.format(MESSAGE_CASES_LISTED_OVERVIEW, 5);
        FindOpenCaseCommand command = prepareCommand();
        assertCommandSuccess(command, expectedMessage, Arrays.asList(BRAVO, DELTA, ECHO, GOLF, FIVE));
    }

    /**
     * Prepare the FindOpenCaseCommand {@code FindOpenCaseCommand}.
     */
    private FindOpenCaseCommand prepareCommand() {
        FindOpenCaseCommand command = new FindOpenCaseCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<CrimeCase>} is equal to {@code expectedList}<br>
     *     - the {@code Investigapptor} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindOpenCaseCommand command, String expectedMessage,
                                      List<CrimeCase> expectedList) {
        Investigapptor expectedInvestigapptor = new Investigapptor(model.getInvestigapptor());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredCrimeCaseList());
        assertEquals(expectedInvestigapptor, model.getInvestigapptor());
    }
}
```
###### /java/seedu/investigapptor/logic/parser/FindCaseTagsCommandParserTest.java
``` java
public class FindCaseTagsCommandParserTest {

    private FindCaseTagsCommandParser parser = new FindCaseTagsCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCaseTagsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindInvestTagsCommand() {
        // no leading and trailing whitespaces. arguments are lowercase as comparison is lowercase based
        FindCaseTagsCommand expectedFindCommand =
                new FindCaseTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList("murder", "robbery")));
        assertParseSuccess(parser, "murder robbery", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n murder \n \t robbery  \t", expectedFindCommand);
    }

}
```
###### /java/seedu/investigapptor/logic/parser/FindInvestTagsCommandParserTest.java
``` java
public class FindInvestTagsCommandParserTest {

    private FindInvestTagsCommandParser parser = new FindInvestTagsCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindInvestTagsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindInvestTagsCommand() {
        // no leading and trailing whitespaces. arguments are lowercase as comparison is lowercase based
        FindInvestTagsCommand expectedFindCommand =
                new FindInvestTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList("teama", "new")));
        assertParseSuccess(parser, "teama new", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n teama \n \t new  \t", expectedFindCommand);
    }

}
```
