# quentinkhoo
###### \main\java\seedu\investigapptor\commons\events\ui\SwapTabEvent.java
``` java
/**
 * Represents swapping of tabs
 */
public class SwapTabEvent extends BaseEvent {

    public final int targetIndex;

    public SwapTabEvent(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \main\java\seedu\investigapptor\commons\events\ui\ValidPasswordEvent.java
``` java
/**
 * Indicates a request for Starting of Investigapptor
 */
public class ValidPasswordEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \main\java\seedu\investigapptor\commons\exceptions\WrongPasswordException.java
``` java
/**
 * Represents an error during decryption
 */
public class WrongPasswordException extends Exception {
    public WrongPasswordException(String message) {
        super(message);
    }
}

```
###### \main\java\seedu\investigapptor\logic\commands\exceptions\InvalidPasswordException.java
``` java
/**
 * disallows user from updating password due to invalid password type
 */
public class InvalidPasswordException extends IllegalValueException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
```
###### \main\java\seedu\investigapptor\logic\commands\exceptions\NoPasswordException.java
``` java
/**
 * disallows removing of password from application if there is no password
 */
public class NoPasswordException extends Exception {
    public NoPasswordException(String message) {
        super(message);
    }
}
```
###### \main\java\seedu\investigapptor\logic\commands\RemovePasswordCommand.java
``` java
/**
 * Removes the password from the investigapptor application
 */
public class RemovePasswordCommand extends Command {

    public static final String COMMAND_WORD = "removepassword";
    public static final String COMMAND_ALIAS = "rp";
    public static final String MESSAGE_SUCCESS =  "Password successfully removed!";
    public static final String MESSAGE_NO_PASSWORD = "No password to remove!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the password from the Investigapptor.";

    @Override
    public CommandResult execute() {
        requireNonNull(model);
        try {
            model.removePassword();
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (NoPasswordException npe) {
            return new CommandResult(MESSAGE_NO_PASSWORD);
        }
    }

}
```
###### \main\java\seedu\investigapptor\logic\commands\SetPasswordCommand.java
``` java
/**
 * Adds a password to the investigapptor book.
 */
public class SetPasswordCommand extends Command {

    public static final String COMMAND_WORD = "setpassword";
    public static final String COMMAND_ALIAS = "sp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates/Updates password for the Investigapptor."
            + "Parameters: " + PREFIX_PASSWORD + "password";

    public static final String MESSAGE_SUCCESS = "Password updated";

    private final Logger logger = LogsCenter.getLogger(SetPasswordCommand.class);

    private Password password;

    /**
     * Creates an PasswordCommand to add the specified {@code CrimeCase}
     */
    public SetPasswordCommand(Password password) {
        requireNonNull(password);
        this.password = password;
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);
        try {
            model.updatePassword(password);
            logger.info("Password has been updated!");
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (InvalidPasswordException ipe) {
            throw new CommandException(MESSAGE_PASSWORD_CONSTRAINTS);
        }

    }

    @Override
    public boolean equals(Object other) {

        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SetPasswordCommand)) {
            return false;
        }

        // state check
        return Objects.equals(password, ((SetPasswordCommand) other).password);
    }
}
```
###### \main\java\seedu\investigapptor\logic\LogicManager.java
``` java

    /**
     * Masks a password field
     * @param inputText
     * @return
     */
    private String maskPassword(String inputText) {
        StringBuilder sb = new StringBuilder(inputText);
        int prefixIndex = inputText.indexOf(PREFIX_PASSWORD.getPrefix());

        if (hasPasswordPrefix(inputText)) {
            for (int i = prefixIndex + 3; i < inputText.length(); i++) {
                sb.setCharAt(i, '*');
            }
        }
        return sb.toString();
    }

    /**
     * Checks for presence of password prefix
     * @param inputText
     * @return
     */
    private boolean hasPasswordPrefix(String inputText) {
        int passwordPrefixIndex = inputText.indexOf(PREFIX_PASSWORD.getPrefix());
        return passwordPrefixIndex != -1;
    }
```
###### \main\java\seedu\investigapptor\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code email} is invalid.
     */
    public static Password parsePassword(String password) throws InvalidPasswordException {
        requireNonNull(password);
        if (!Password.isValidPassword(password)) {
            throw new InvalidPasswordException(Password.MESSAGE_PASSWORD_CONSTRAINTS);
        }
        return new Password(password);
    }
```
###### \main\java\seedu\investigapptor\logic\parser\SetPasswordCommandParser.java
``` java
/**
 * Parses input arguments and creates a new PasswordCommand object
 */
public class SetPasswordCommandParser implements Parser<SetPasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PasswordCommand
     * and returns an PasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SetPasswordCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);

        if (!arePrefixesPresent(argMultimap, PREFIX_PASSWORD)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetPasswordCommand.MESSAGE_USAGE));
        }

        try {
            String inputPassword = args.substring(4);
            Password newPassword = ParserUtil.parsePassword(inputPassword);
            return new SetPasswordCommand(newPassword);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage());
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \main\java\seedu\investigapptor\model\Investigapptor.java
``` java
    public Investigapptor() {
        this.password = new Password();
    }

    public Investigapptor(String password) {
        this.password = new Password(password);
    }
```
###### \main\java\seedu\investigapptor\model\Investigapptor.java
``` java
    public void setPassword(String password) {
        String passwordHash = Password.generatePasswordHash(password);
        this.password = new Password(passwordHash);
    }

    public void setPassword(Password password) {
        this.password = password;
    }
```
###### \main\java\seedu\investigapptor\model\Investigapptor.java
``` java
    ///password level operations

    /**
     * Updates the password of this {@code Investigapptor}.
     * @param newPassword  will be the new password.
     */
    public void updatePassword(Password newPassword) {
        try {
            password.updatePassword(newPassword);
        } catch (NullPointerException npe) {
            setPassword(newPassword.getPassword());
        }
    }

    /**
     * Removes the password of this {@code Investigapptor}
     */
    public void removePassword () throws NoPasswordException {
        if (this.password == null || this.password.getPassword() == null) {
            throw new NoPasswordException("No password in investigapptor");
        }
        this.password = null;
    }
```
###### \main\java\seedu\investigapptor\model\Investigapptor.java
``` java
    @Override
    public Password getPassword() {
        return password;
    }
```
###### \main\java\seedu\investigapptor\model\Model.java
``` java
    /**
     * Updates the password with the given password.
     */
    void updatePassword(Password password) throws InvalidPasswordException;

    /**
     * Removes the existing password
     */
    void removePassword() throws NoPasswordException;
```
###### \main\java\seedu\investigapptor\model\ModelManager.java
``` java
    @Override
    public void updatePassword(Password password) throws InvalidPasswordException {
        investigapptor.updatePassword(password);
        indicateInvestigapptorChanged();
    }

    @Override
    public void removePassword() throws NoPasswordException {
        try {
            investigapptor.removePassword();
            indicateInvestigapptorChanged();
        } catch (NoPasswordException npe) {
            throw new NoPasswordException(npe.getMessage());
        }
    }
```
###### \main\java\seedu\investigapptor\model\Password.java
``` java
/**
 * Represents a Password in PartTimeManger
 * Store password as hashCode
 */
public class Password {

    public static final String MESSAGE_PASSWORD_CONSTRAINTS =
            "Password must be at least 8 character and must not contain any spaces.";

    public static final String INITIAL_VALUE = "IV";

    /**
     * accept all password that do not have whitespaces and at least 8 characters.
     */
    public static final String PASSWORD_VALIDATION_REGEX = "^(?=\\S+$).{8,}$";

    private String passwordHash;

    /**
     * constructor for default password
     */
    public Password() {

    }

    /**
     * use this if hashcode is known
     * @param passwordHash
     */
    public Password(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * @param test
     * @return true if password is of correct format
     */
    public static boolean isValidPassword(String test) {
        return test.matches(PASSWORD_VALIDATION_REGEX);
    }

    /**
     * updates an original password to a new password
     */
    public void updatePassword(Password newPassword) {
        requireNonNull(newPassword);
        this.passwordHash = generatePasswordHash(newPassword.getPassword());
    }

    public String getPassword() {
        return this.passwordHash;
    }

    /**
     * Generate password hash given a password string
     * @param password
     * @return encodedHash in String
     */
    public static String generatePasswordHash(String password) {
        String encodedHash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(INITIAL_VALUE.getBytes());
            if (!isNull(password)) {
                byte[] byteHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                encodedHash = Base64.getEncoder().encodeToString(byteHash);
            }
        } catch (NoSuchAlgorithmException noSuchAlgoException) {
            System.out.println("Cannot generate hash: MessageDigest.getInstance");
        }
        return encodedHash;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Password // instanceof handles nulls
                && this.passwordHash.equals(((Password) other).passwordHash)); // state check
    }

    @Override
    public int hashCode() {
        return passwordHash.hashCode();
    }
}
```
###### \main\java\seedu\investigapptor\model\ReadOnlyInvestigapptor.java
``` java
    /**
     * Returns the hashed password
     */
    Password getPassword();
```
###### \main\java\seedu\investigapptor\storage\InvestigapptorStorage.java
``` java
    /**
     * @see #getInvestigapptorFilePath()
     */
    void readInvestigapptorWithPassword(Password password)
            throws DataConversionException, IOException, WrongPasswordException;

    /**
     * @see #getInvestigapptorFilePath()
     */
    void checkInvestigapptorPassword(String filePath, Password password)
            throws DataConversionException, IOException, WrongPasswordException;

```
###### \main\java\seedu\investigapptor\storage\StorageManager.java
``` java
    @Override
    public void readInvestigapptorWithPassword(Password password)
            throws DataConversionException, IOException, WrongPasswordException {
        investigapptorStorage.checkInvestigapptorPassword(
                investigapptorStorage.getInvestigapptorFilePath(), password);
    }

    @Override
    public void checkInvestigapptorPassword(String filePath, Password password)
            throws DataConversionException, IOException, WrongPasswordException {
        logger.fine("Attempting to read data from file: " + filePath);
        investigapptorStorage.checkInvestigapptorPassword(filePath, password);
    }
```
###### \main\java\seedu\investigapptor\storage\XmlAdaptedPassword.java
``` java
/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedPassword {

    @XmlElement(required = true)
    private String currentPassword;

    /**
     * Constructs an XmlAdaptedPassword.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPassword() {}

    /**
     * Constructs an {@code XmlAdaptedPassword} with the given password.
     */
    public XmlAdaptedPassword(Password password) {
        try {
            this.currentPassword = password.getPassword();
        } catch (NullPointerException npe) {
            this.currentPassword = null;
        }
    }

    /**
     * Converts this jaxb-friendly adapted password object into the model's Password object.
     *
     */
    public Password toModelType() {
        return new Password(currentPassword);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPassword)) {
            return false;
        }

        XmlAdaptedPassword otherPassword = (XmlAdaptedPassword) other;
        return Objects.equals(currentPassword, otherPassword.currentPassword);
    }
}
```
###### \main\java\seedu\investigapptor\storage\XmlInvestigapptorStorage.java
``` java
    /**
     * Similar to {@link InvestigapptorStorage#readInvestigapptor()}
     *
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public void checkInvestigapptorPassword(String filePath, Password password)
            throws DataConversionException, IOException, WrongPasswordException {
        requireNonNull(filePath);
        requireNonNull(password);

        File investigapptorFile = new File(filePath);

        if (!investigapptorFile.exists()) {
            logger.info("Investigapptor file " + investigapptorFile + " not found");
        }
        XmlSerializableInvestigapptor xmlInvestigapptor = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
        try {
            String currentPassword = xmlInvestigapptor.toModelType().getPassword().getPassword();
            String inputPassword = Password.generatePasswordHash(password.getPassword());
            if (!isCorrectPassword(currentPassword, inputPassword)) {
                throw new WrongPasswordException("Invalid password entered! Please try again.");
            }
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + investigapptorFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    /**
     * Checks if an inputPassword is the currentPassword
     * @param currentPassword
     * @param inputPassword
     */
    private boolean isCorrectPassword(String currentPassword, String inputPassword) {
        return currentPassword.equals(inputPassword);
    }
```
###### \main\java\seedu\investigapptor\ui\CommandBox.java
``` java

    /**
     * Clears the command box input field
     */
    private void clearScreenText() {
        commandTextField.setText("");
    }



```
###### \main\java\seedu\investigapptor\ui\MainWindow.java
``` java
    /**
     * Changes to the {@code Tab} at the {@code index} and selects it.
     */
    private void changeTo(int index) {
        Platform.runLater(() -> {
            listPanel.getSelectionModel().select(index);
        });
    }
    @Subscribe
    private void handleSwapTabEvent(SwapTabEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        changeTo(event.targetIndex);
    }
```
###### \main\java\seedu\investigapptor\ui\PasswordBox.java
``` java
/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class PasswordBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "PasswordBox.fxml";

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Storage storage;

    @FXML
    private PasswordField passwordField;

    public PasswordBox(Storage storage) {
        super(FXML);
        this.storage = storage;

        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        passwordField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
    }

    /**
     * Handles the key press event, {@code keyEvent}.
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
        case ESCAPE:
            keyEvent.consume();
            passwordField.setText("");
            break;
        default:
            // let JavaFx handle the keypress
        }
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handlePasswordInput() {
        String input = passwordField.getText();
        try {
            storage.readInvestigapptorWithPassword(new Password(input));
            raise(new ValidPasswordEvent());
        } catch (WrongPasswordException wpe) {
            CommandResult passwordResult = new CommandResult("An invalid password has been entered");
            passwordField.setText("");
            logger.info("Result: " + passwordResult.feedbackToUser);
            raise(new NewResultAvailableEvent(passwordResult.feedbackToUser));
            setStyleToIndicateCommandFailure();
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty Investigapptor");
            raise(new InvalidFileFormatEvent());
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty Investigapptor");
            raise(new InvalidFileFormatEvent());
        }
    }

    /**
     * Sets the password box style to use the default style.
     */
    private void setStyleToDefault() {
        passwordField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the password box style to indicate a wrong password.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = passwordField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

}
```
###### \main\java\seedu\investigapptor\ui\PasswordManager.java
``` java
/**
 * The manager of the UI component.
 */
public class PasswordManager extends ComponentManager implements Ui {

    public static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";

    private static final Logger logger = LogsCenter.getLogger(UiManager.class);

    private Storage storage;
    private Ui ui;

    private PasswordWindow passwordWindow;
    private Stage primaryStage;

    public PasswordManager(Storage storage, Ui ui) {
        super();
        this.storage = storage;
        this.ui = ui;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Password UI...");
        this.primaryStage = primaryStage;
        try {
            PasswordWindow pw = new PasswordWindow(primaryStage, storage);
            pw.show();
            pw.fillInnerParts();
        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown("Fatal error during initializing", e);
        }
    }

    /**
     * Shows an error alert dialog with {@code title} and error message, {@code e},
     * and exits the application after the user has closed the alert dialog.
     */
    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }

    void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(passwordWindow.getPrimaryStage(), type, title, headerText, contentText);
    }

    /**
     * Shows an alert dialog on {@code owner} with the given parameters.
     * This method only returns after the user has closed the alert dialog.
     */
    private static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
                                               String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        passwordWindow.hide();
        passwordWindow.releaseResources();
    }
    @Subscribe
    private void handlePasswordCorrectEvent(ValidPasswordEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        ui.start(primaryStage);
    }

    @Subscribe
    private void handleInvalidStorageFileEvent(InvalidFileFormatEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        ui.start(primaryStage);
    }

}
```
###### \main\java\seedu\investigapptor\ui\PasswordWindow.java
``` java
/**
 * The Password Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class PasswordWindow extends UiPart<Stage> {

    private static final String FXML = "PasswordWindow.fxml";
    private static final String TITLE = "Please Enter Password";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private Stage primaryStage;
    private Storage storage;

    // Independent Ui parts residing in this Ui container
    private BrowserPanel browserPanel;

    @FXML
    private StackPane browserPlaceholder;

    @FXML
    private StackPane passwordBoxPlacedHolder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    public PasswordWindow(Stage primaryStage, Storage storage) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.storage = storage;

        setTitle(TITLE);
        registerAsAnEventHandler(this);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        ResultDisplay resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        PasswordBox passwordBox = new PasswordBox(storage);
        passwordBoxPlacedHolder.getChildren().add(passwordBox.getRoot());
    }

    void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }


    void show() {
        primaryStage.show();
    }

    void releaseResources() {
        browserPanel.freeResources();
    }
}
```
###### \main\java\seedu\investigapptor\ui\skin\PasswordFieldSkin.java
``` java
/**
 * The PassworldFieldSkin class is responsible for masking the password input yet displaying the other commands
 */
public class PasswordFieldSkin extends TextFieldSkin {

    public PasswordFieldSkin(PasswordField passwordField) {
        super(passwordField, new PasswordFieldBehavior(passwordField));
    }

    @Override
    protected String maskText(String inputText) {
        StringBuilder sb = new StringBuilder(inputText);
        int prefixIndex = inputText.indexOf(PREFIX_PASSWORD.getPrefix());

        if (hasPasswordPrefix(inputText)) {
            for (int i = prefixIndex + 3; i < inputText.length(); i++) {
                sb.setCharAt(i, '*');
            }
        }
        return sb.toString();
    }

    /**
     * Checks for presence of password prefix
     * @param inputText
     * @return
     */
    private boolean hasPasswordPrefix(String inputText) {
        int passwordPrefixIndex = inputText.indexOf(PREFIX_PASSWORD.getPrefix());
        return passwordPrefixIndex != -1;
    }
}
```
###### \main\resources\view\PasswordBox.fxml
``` fxml
<StackPane styleClass="anchor-pane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <PasswordField fx:id="passwordField" onAction="#handlePasswordInput" onKeyPressed="#handleKeyPress" promptText="Enter password here..."/>
</StackPane>

```
###### \main\resources\view\PasswordWindow.fxml
``` fxml
<fx:root type="javafx.stage.Stage" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1"
         minWidth="450" minHeight="205">
    <icons>
        <Image url="@/images/address_book_32.png" />
    </icons>
    <scene>
        <Scene>
            <stylesheets>
                <URL value="@DarkTheme.css" />
                <URL value="@Extensions.css" />
            </stylesheets>

            <VBox>
                <StackPane VBox.vgrow="NEVER" fx:id="passwordBoxPlacedHolder" styleClass="pane-with-border">
                    <padding>
                        <Insets top="5" right="10" bottom="5" left="10" />
                    </padding>
                </StackPane>

                <StackPane VBox.vgrow="NEVER" fx:id="resultDisplayPlaceholder" styleClass="pane-with-border"
                           minHeight="120" prefHeight="120" maxHeight="120">
                    <padding>
                        <Insets top="5" right="10" bottom="5" left="10" />
                    </padding>
                </StackPane>

                <StackPane fx:id="statusbarPlaceholder" VBox.vgrow="NEVER" />
            </VBox>
        </Scene>
    </scene>
</fx:root>
```
###### \test\java\guitests\guihandles\PasswordBoxHandle.java
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
###### \test\java\seedu\investigapptor\logic\commands\RemovePasswordCommandTest.java
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
###### \test\java\seedu\investigapptor\logic\commands\SetPasswordCommandTest.java
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
###### \test\java\seedu\investigapptor\logic\parser\InvestigapptorParserTest.java
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
###### \test\java\seedu\investigapptor\model\InvestigapptorTest.java
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
###### \test\java\seedu\investigapptor\storage\StorageManagerTest.java
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
###### \test\java\seedu\investigapptor\storage\XmlAdaptedPasswordTest.java
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
###### \test\java\seedu\investigapptor\storage\XmlInvestigapptorStorageTest.java
``` java
    private void readInvestigapptorWithPassword(String filePath, Password password) throws Exception {
        new XmlInvestigapptorStorage(filePath)
                .checkInvestigapptorPassword(addToTestDataPathIfNotNull(filePath), password);
    }
```
###### \test\java\seedu\investigapptor\storage\XmlInvestigapptorStorageTest.java
``` java
    @Test
    public void readInvestigapptorWithPassword_invalidAndValidPersonInvestigapptor_throwDataConversionException()
            throws Exception {
        thrown.expect(DataConversionException.class);
        readInvestigapptorWithPassword("invalidPasswordInvestigapptor.xml",
                new Password("password"));
    }
```
###### \test\java\seedu\investigapptor\testutil\InvestigapptorBuilder.java
``` java
    /**
     * Parses {@code password} into a {@code Password} and adds it to the {@code Investigapptor} that we are building.
     */
    public InvestigapptorBuilder withPassword(String password) {
        investigapptor.updatePassword(new Password(password));
        return this;
    }
```
###### \test\java\seedu\investigapptor\ui\CommandBoxTest.java
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
###### \test\java\seedu\investigapptor\ui\PasswordBoxTest.java
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
###### \unused\RemovePasswordCommandParser.java
``` java
/**
 * Parses input arguments and creates a new PasswordCommand object
 */
public class RemovePasswordCommandParser implements Parser<RemovePasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PasswordCommand
     * and returns an PasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemovePasswordCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);

        if (!arePrefixesPresent(argMultimap, PREFIX_PASSWORD)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemovePasswordCommand.MESSAGE_USAGE));
        }

        String inputPassword = args.substring(4);
        return new RemovePasswordCommand();
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \unused\SetCommand.java
``` java
/**
 * Sets a specific settings for the application
 */
public class SetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "set";
    public static final String COMMAND_ALIAS = "s";
    public static final String[] TYPE_PASSWORD_ALIASES = {"password", "pass", "pw", "p"};
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the specified type\n"
            + "Parameters: TYPE (must be password)\n"
            + "Example: " + COMMAND_WORD + " cases";
    public static final String MESSAGE_ALIASES =
            "password: Can either be password, pass, pw or p\n"
                    + "Example: " + COMMAND_WORD + " pw\n";

    public static final String MESSAGE_SUCCESS = "Succesfully set %1$s";

    private final String setType;
    private String args;
    private String[] argsArray;

    public SetCommand(String args) {
        this.args = args.trim();
        argsArray = args.split("\\s+");
        this.setType = argsArray[0];
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (isValidPasswordAlias(setType)) {
            try {
                if (args.contains(" ")) {
                    args = args.substring(args.indexOf(" "));
                    return new SetPasswordCommandParser().parse(args).executeUndoableCommand();
                } else {
                    return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            SetPasswordCommand.MESSAGE_USAGE));
                }
            } catch (ParseException pe) {
                return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        SetPasswordCommand.MESSAGE_USAGE));
            }
        } else {
            throw new CommandException(MESSAGE_INVALID_COMMAND_ALIAS);
        }
    }

    /**
     * Checks and returns an input listing type is a valid investigator alias
     * @param type
     */
    public static boolean isValidPasswordAlias(String type) {
        for (String alias : TYPE_PASSWORD_ALIASES) {
            if (type.equals(alias)) {
                return true;
            }
        }
        return false;
    }

}
```
