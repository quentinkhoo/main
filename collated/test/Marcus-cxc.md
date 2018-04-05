# Marcus-cxc
###### \java\seedu\investigapptor\logic\commands\ListInvestigatorCaseCommandTest.java
``` java
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
```
