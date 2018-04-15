# quentinkhoo
###### /java/guitests/guihandles/PasswordBoxHandle.java
``` java
/**
 * A handle to the {@code PasswordBox} in the GUI.
 */
public class PasswordBoxHandle extends NodeHandle<TextField> {

    public static final String PASSWORD_WINDOW_TITLE = "Please Enter Password";

    public static final String PASSWORD_INPUT_FIELD_ID = "#passwordField";

    public PasswordBoxHandle(TextField passwordBoxNode) {
        super(passwordBoxNode);
    }

    /**
     * Returns the text in the password box.
     */
    public String getInput() {
        return getRootNode().getText();
    }

    /**
     * Enters the given command in the Command Box and presses enter.
     * @return true if the command succeeded, false otherwise.
     */
    public boolean run(String password) {
        click();
        guiRobot.interact(() -> getRootNode().setText(password));
        guiRobot.pauseForHuman();

        guiRobot.type(KeyCode.ENTER);

        return !getStyleClass().contains(PasswordBox.ERROR_STYLE_CLASS);
    }

    /**
     * Returns the list of style classes present in the password box.
     */
    public ObservableList<String> getStyleClass() {
        return getRootNode().getStyleClass();
    }
}
```
###### /java/seedu/investigapptor/logic/commands/RemovePasswordCommandTest.java
``` java
public class RemovePasswordCommandTest {
    private static final Password TEST_PASSWORD = new Password("password");
    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Before
    public void setUp() {
        try {
            model.updatePassword(TEST_PASSWORD);
        } catch (InvalidPasswordException ipe) {
            throw new AssertionError("Shouldn't reach here");
        }

    }
    @Test
    public void execute_removePassword_success() throws Exception {
        RemovePasswordCommand removepasswordCommand = prepareCommand();
        ModelManager expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.updatePassword(TEST_PASSWORD);

        String expectedMessageSuccess = RemovePasswordCommand.MESSAGE_SUCCESS;
        expectedModel.removePassword();
        assertCommandSuccess(removepasswordCommand, model, expectedMessageSuccess, expectedModel);
        assertEquals(model.getInvestigapptor().getPassword(), null);

        String expectedMessageNoPassword = RemovePasswordCommand.MESSAGE_NO_PASSWORD;
        assertCommandSuccess(removepasswordCommand, model, expectedMessageNoPassword, expectedModel);
        assertEquals(model.getInvestigapptor().getPassword(), null);

    }

    /**
     * Returns a {@code RemovePasswordCommand}.
     */
    private RemovePasswordCommand prepareCommand() {
        RemovePasswordCommand removepasswordCommand = new RemovePasswordCommand();
        removepasswordCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return removepasswordCommand;
    }
}
```
###### /java/seedu/investigapptor/logic/commands/SetPasswordCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code PasswordCommand}.
 */
public class SetPasswordCommandTest {

    private static final String DEFAULT_PASSWORD = "password";
    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager();
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        SetPasswordCommand passwordCommand = prepareCommand(DEFAULT_PASSWORD);

        String expectedMessage = String.format(SetPasswordCommand.MESSAGE_SUCCESS);

        ModelManager expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.updatePassword(new Password(generatePasswordHash(DEFAULT_PASSWORD)));

        assertCommandSuccess(passwordCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        SetPasswordCommand passwordFirstCommand = prepareCommand(DEFAULT_PASSWORD);
        SetPasswordCommand passwordSecondCommand = prepareCommand(DEFAULT_PASSWORD + "1");

        // same object -> returns true
        assertTrue(passwordFirstCommand.equals(passwordFirstCommand));

        // same values -> returns true
        SetPasswordCommand passwordFirstCommandCopy = prepareCommand(DEFAULT_PASSWORD);
        assertTrue(passwordFirstCommand.equals(passwordFirstCommandCopy));

        // empty string -> returnf alse
        assertFalse(passwordFirstCommand.equals(""));

        // null -> returns false
        assertFalse(passwordFirstCommand.equals(null));

        // different password -> returns false
        assertFalse(passwordFirstCommand.equals(passwordSecondCommand));
    }

    /**
     * Returns a {@code PasswordCommand} with the parameter {@code password}.
     */
    private SetPasswordCommand prepareCommand(String password) {
        SetPasswordCommand passwordCommand = new SetPasswordCommand(new Password(password));
        passwordCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return passwordCommand;
    }
}
```
###### /java/seedu/investigapptor/logic/parser/InvestigapptorParserTest.java
``` java
    @Test
    public void parseCommand_setPassword() throws Exception {
        SetPasswordCommand command = (SetPasswordCommand) parser
                .parseCommand(SetPasswordCommand.COMMAND_WORD + " pw/password");
        assertEquals(new SetPasswordCommand(new Password("password")), command);
    }

    @Test
    public void parserCommand_removePassword() throws Exception {
        assertTrue(parser.parseCommand(RemovePasswordCommand.COMMAND_WORD) instanceof RemovePasswordCommand);
    }
```
###### /java/seedu/investigapptor/model/InvestigapptorTest.java
``` java
    @Test
    public void setPassword_passwordAdded() throws Exception {
        Investigapptor investigapptorAddedPassword = new Investigapptor("password");
        Password expectedPassword = new Password("password");
        Password investigapptorPassword = investigapptorAddedPassword.getPassword();
        assertEquals(expectedPassword, investigapptorPassword);
    }

    @Test
    public void updatePassword_passwordChanged_passwordUpdated() throws Exception {
        Investigapptor investigapptorWithNewPassword = new InvestigapptorBuilder().withPassword("oldPassword").build();
        investigapptorWithNewPassword.updatePassword(new Password("newPassword"));
        Investigapptor expectedInvestigapptor = new InvestigapptorBuilder().withPassword("newPassword").build();
        assertEquals(investigapptorWithNewPassword, expectedInvestigapptor);
    }

```
###### /java/seedu/investigapptor/storage/StorageManagerTest.java
``` java
    @Test
    public void investigapptorReadWithWrongPassword() throws Exception {
        thrown.expect(WrongPasswordException.class);
        Investigapptor original = getTypicalInvestigapptor();
        Password password = new Password("password");
        original.updatePassword(password);
        storageManager.saveInvestigapptor(original);
        Password wrongPassword = new Password("p@ssword");
        storageManager.readInvestigapptorWithPassword(wrongPassword);
    }

```
###### /java/seedu/investigapptor/storage/XmlAdaptedPasswordTest.java
``` java
public class XmlAdaptedPasswordTest {

    @Test
    public void toModelType_validPassword_returnsPassword() throws Exception {
        Password password = new Password("password");
        XmlAdaptedPassword storedPassword = new XmlAdaptedPassword(password);
        assertEquals(password, storedPassword.toModelType());
    }
}
```
###### /java/seedu/investigapptor/storage/XmlInvestigapptorStorageTest.java
``` java
    private void readInvestigapptorWithPassword(String filePath, Password password) throws Exception {
        new XmlInvestigapptorStorage(filePath)
                .checkInvestigapptorPassword(addToTestDataPathIfNotNull(filePath), password);
    }
```
###### /java/seedu/investigapptor/storage/XmlInvestigapptorStorageTest.java
``` java
    @Test
    public void readInvestigapptorWithPassword_invalidAndValidPersonInvestigapptor_throwDataConversionException()
            throws Exception {
        thrown.expect(DataConversionException.class);
        readInvestigapptorWithPassword("invalidPasswordInvestigapptor.xml",
                new Password("password"));
    }
```
###### /java/seedu/investigapptor/testutil/InvestigapptorBuilder.java
``` java
    /**
     * Parses {@code password} into a {@code Password} and adds it to the {@code Investigapptor} that we are building.
     */
    public InvestigapptorBuilder withPassword(String password) {
        investigapptor.updatePassword(new Password(password));
        return this;
    }
```
###### /java/seedu/investigapptor/ui/CommandBoxTest.java
``` java
    @Test
    public void handleKeyPress_escape() {
        guiRobot.push(KeyCode.ESCAPE);
        assertTrue("".equals(commandBoxHandle.getInput()));

        guiRobot.write("some input");
        assertTrue("some input".equals(commandBoxHandle.getInput()));

        guiRobot.push(KeyCode.ESCAPE);
        assertFalse("some input".equals(commandBoxHandle.getInput()));
        assertTrue("".equals(commandBoxHandle.getInput()));
    }
```
###### /java/seedu/investigapptor/ui/PasswordBoxTest.java
``` java
public class PasswordBoxTest extends GuiUnitTest {
    private static final String CORRECT_PASSWORD = "password";
    private static final String WRONG_PASSWORD = "p@ssword";
    private static final String TEST_DATA_FOLDER = FileUtil.getPath(
            "src/test/data/XmlSerializableInvestigapptorTest/");

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private ArrayList<String> defaultStyleOfPasswordBox;
    private ArrayList<String> errorStyleOfPasswordBox;
    private PasswordBoxHandle passwordBoxHandle;


    @Before
    public void setUp() throws Exception {

        Storage storageManager = setUpStorage();

        PasswordBox passwordBox = new PasswordBox(storageManager);
        passwordBoxHandle = new PasswordBoxHandle(getChildNode(passwordBox.getRoot(),
                PasswordBoxHandle.PASSWORD_INPUT_FIELD_ID));
        uiPartRule.setUiPart(passwordBox);

        defaultStyleOfPasswordBox = new ArrayList<>(passwordBoxHandle.getStyleClass());

        errorStyleOfPasswordBox = new ArrayList<>(defaultStyleOfPasswordBox);
        errorStyleOfPasswordBox.add(CommandBox.ERROR_STYLE_CLASS);
    }

    private String getTestFilePath(String fileName) {
        return TEST_DATA_FOLDER + fileName;
    }
    private Storage setUpStorage() {
        XmlInvestigapptorStorage addressBookStorage = new XmlInvestigapptorStorage(getTestFilePath(
                "typicalPasswordInvestigapptor.xml"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTestFilePath("prefs"));
        return new StorageManager(addressBookStorage, userPrefsStorage);
    }

    @Test
    public void passwordBox_startingWithWrongPassword() {
        assertBehaviorForWrongPassword();
    }

    @Test
    public void passwordBox_startingWithCorrectPassword() {
        assertBehaviorForCorrectPassword();
    }

    @Test
    public void passwordBox_handleKeyPress() {
        passwordBoxHandle.run(WRONG_PASSWORD);
        assertEquals(errorStyleOfPasswordBox, passwordBoxHandle.getStyleClass());

        guiRobot.push(KeyCode.ESCAPE);
        assertEquals("", passwordBoxHandle.getInput());

        guiRobot.push(KeyCode.A);
        assertEquals(defaultStyleOfPasswordBox, passwordBoxHandle.getStyleClass());
    }

    /**
     * Input a wrong password, then verify that <br>
     *      - the text remains resets <br>
     *      - the command box's style is the same as {@code errorStyleOfCommandBox}.
     */
    private void assertBehaviorForWrongPassword() {
        passwordBoxHandle.run(WRONG_PASSWORD);
        assertEquals("", passwordBoxHandle.getInput());
        assertEquals(errorStyleOfPasswordBox, passwordBoxHandle.getStyleClass());
    }

    /**
     * Input the correct password, then verifies that
     *      - the event {@code ValidPasswordEvent} is raised.
     */
    private void assertBehaviorForCorrectPassword() {
        passwordBoxHandle.run(CORRECT_PASSWORD);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ValidPasswordEvent);
    }
}
```
