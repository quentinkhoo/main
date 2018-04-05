# quentinkhoo
###### \java\seedu\investigapptor\commons\events\ui\SwapTabEvent.java
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
###### \java\seedu\investigapptor\commons\events\ui\ValidPasswordEvent.java
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
###### \java\seedu\investigapptor\commons\exceptions\WrongPasswordException.java
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
###### \java\seedu\investigapptor\logic\commands\exceptions\InvalidPasswordException.java
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
###### \java\seedu\investigapptor\logic\commands\exceptions\NoPasswordException.java
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
###### \java\seedu\investigapptor\logic\commands\RemovePasswordCommand.java
``` java
/**
 * Removes the password from the investigapptor application
 */
public class RemovePasswordCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removepassword";
    public static final String COMMAND_ALIAS = "rp";
    public static final String MESSAGE_SUCCESS =  "Password successfully removed!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the password from the Investigapptor."
            + " Requires input of current password"
            + "Parameters: " + PREFIX_PASSWORD + "currentPassword";

    private String inputPassword;


    public RemovePasswordCommand(String inputPassword) {
        requireNonNull(inputPassword);
        this.inputPassword = inputPassword;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        try {
            checkInputPassword(inputPassword);
        } catch (NoPasswordException npe) {
            return new CommandResult(npe.getMessage());
        } catch (WrongPasswordException wpe) {
            return new CommandResult(wpe.getMessage());
        }
        model.removePassword();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Checks whether the input password matches the current investigapptor password
     * @param inputPassword
     * @throws WrongPasswordException if password is invalid or there is no password in the application
     */
    private void checkInputPassword(String inputPassword) throws WrongPasswordException, NoPasswordException {
        String inputPasswordHash = Password.generatePasswordHash(inputPassword);
        try {
            String currentPasswordHash = model.getInvestigapptor().getPassword().getPassword();
            if (!currentPasswordHash.equals(inputPasswordHash)) {
                throw new WrongPasswordException("Password cannot be removed. Invalid password has been entered.");
            }
        } catch (NullPointerException npe) {
            throw new NoPasswordException("Investigapptor currently has no password!");
        }
    }
}
```
###### \java\seedu\investigapptor\logic\commands\SetPasswordCommand.java
``` java
/**
 * Adds a password to the investigapptor book.
 */
public class SetPasswordCommand extends UndoableCommand {

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
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.updatePassword(password);
            logger.info("Password has been updated!");
            return new CommandResult(String.format(MESSAGE_SUCCESS));
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
###### \java\seedu\investigapptor\logic\parser\ParserUtil.java
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
###### \java\seedu\investigapptor\logic\parser\RemovePasswordCommandParser.java
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
        return new RemovePasswordCommand(inputPassword);
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
###### \java\seedu\investigapptor\logic\parser\SetPasswordCommandParser.java
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
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    public Investigapptor() {
        this.password = new Password();
    }

    public Investigapptor(String password) {
        this.password = new Password(password);
    }
```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    public void setPassword(String password) {
        this.password = new Password(password);
    }

    public void setPassword(Password oldPassword) {
        this.password = oldPassword;
    }
```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    ///password level operations

    /**
     * Updates the password of this {@code Investigapptor}.
     * @param newPassword  will be the new password.
     */
    public void updatePassword(Password newPassword) {
        password.updatePassword(newPassword);
    }

    /**
     * Removes the password of this {@code Investigapptor}
     */
    public void removePassword() {
        this.password = null;
    }
```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    @Override
    public Password getPassword() {
        return password;
    }
```
###### \java\seedu\investigapptor\model\Model.java
``` java
    /**
     * Updates the password with the given password.
     */
    void updatePassword(Password password) throws InvalidPasswordException;

    /**
     * Removes the existing password
     */
    void removePassword();
```
###### \java\seedu\investigapptor\model\ModelManager.java
``` java
    @Override
    public void updatePassword(Password password) throws InvalidPasswordException {
        investigapptor.updatePassword(password);
        indicateInvestigapptorChanged();
    }

    @Override
    public void removePassword() {
        investigapptor.removePassword();
        indicateInvestigapptorChanged();
    }
```
###### \java\seedu\investigapptor\model\Password.java
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
     * @param password
     */
    public Password(String password) {
        this.passwordHash = password;
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
###### \java\seedu\investigapptor\model\ReadOnlyInvestigapptor.java
``` java
    /**
     * Returns the hashed password
     */
    Password getPassword();
```
###### \java\seedu\investigapptor\storage\InvestigapptorStorage.java
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
###### \java\seedu\investigapptor\storage\StorageManager.java
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
###### \java\seedu\investigapptor\storage\XmlAdaptedPassword.java
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
###### \java\seedu\investigapptor\storage\XmlInvestigapptorStorage.java
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
###### \java\seedu\investigapptor\ui\CommandBox.java
``` java

    /**
     * Clears the command box input field
     */
    private void clearScreenText() {
        commandTextField.setText("");
        commandTextDisplay.setText("");
    }

    /**
     *  Toggles between hiding the password and revealing the password field to the user
     */
    private void togglePasswordHide() {
        if (hideEnabled) {
            commandTextField.setOpacity(1);
            commandTextDisplay.setOpacity(0);
            hideEnabled = false;
        } else {
            commandTextField.setOpacity(0);
            commandTextDisplay.setOpacity(1);
            hideEnabled = true;
        }
    }
    /**
     * Hides password string
     * @param inputText
     * @return
     */
    private String hidePasswordText(String inputText) {
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
###### \java\seedu\investigapptor\ui\MainWindow.java
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
###### \java\seedu\investigapptor\ui\PasswordBox.java
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
###### \java\seedu\investigapptor\ui\PasswordManager.java
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
###### \java\seedu\investigapptor\ui\PasswordWindow.java
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

    /**
     * Opens the help window.
     */
    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    void releaseResources() {
        browserPanel.freeResources();
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        handleHelp();
    }
}
```
###### \resources\view\PasswordBox.fxml
``` fxml
<StackPane styleClass="anchor-pane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <PasswordField fx:id="passwordField" onAction="#handlePasswordInput" onKeyPressed="#handleKeyPress" promptText="Enter password here..."/>
</StackPane>

```
###### \resources\view\PasswordWindow.fxml
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
