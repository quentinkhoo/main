package seedu.investigapptor.ui;

import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_PASSWORD;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.events.ui.NewResultAvailableEvent;
import seedu.investigapptor.logic.ListElementPointer;
import seedu.investigapptor.logic.Logic;
import seedu.investigapptor.logic.commands.CommandResult;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;
    private ListElementPointer historySnapshot;
    private boolean hideEnabled;

    @FXML
    private TextField commandTextField;

    @FXML
    private TextField commandTextDisplay;

    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextDisplay.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        commandTextField.setOpacity(0);

        // checks for password prefix
        commandTextField.textProperty()
                .addListener((observable, oldValue, newValue) -> commandTextDisplay
                        .setText(hidePasswordText(newValue)));
        //commandTextField.setSkin(new TextFieldCaret(commandTextField));

        hideEnabled = true;
        historySnapshot = logic.getHistorySnapshot();
    }

    /**
     * Handles the key press event, {@code keyEvent}.
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
        case UP:
            // As up and down buttons will alter the position of the caret,
            // consuming it causes the caret's position to remain unchanged
            keyEvent.consume();
            navigateToPreviousInput();
            break;
        case DOWN:
            keyEvent.consume();
            navigateToNextInput();
            break;
        case ESCAPE:
            keyEvent.consume();
            clearScreenText();
            break;
        case CONTROL:
            keyEvent.consume();
            togglePasswordHide();
            break;
        default:
                // let JavaFx handle the keypress
        }
    }

    /**
     * Updates the text field with the previous input in {@code historySnapshot},
     * if there exists a previous input in {@code historySnapshot}
     */
    private void navigateToPreviousInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasPrevious()) {
            return;
        }

        replaceText(historySnapshot.previous());
    }

    /**
     * Updates the text field with the next input in {@code historySnapshot},
     * if there exists a next input in {@code historySnapshot}
     */
    private void navigateToNextInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasNext()) {
            return;
        }

        replaceText(historySnapshot.next());
    }

    /**
     * Sets {@code CommandBox}'s text field with {@code text} and
     * positions the caret to the end of the {@code text}.
     */
    private void replaceText(String text) {
        commandTextField.setText(text);
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandInputChanged() {
        try {
            CommandResult commandResult = logic.execute(commandTextField.getText());
            initHistory();
            historySnapshot.next();
            // process result of the command
            commandTextField.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));

        } catch (CommandException | ParseException e) {
            initHistory();
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    //@@author quentinkhoo

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
    //@@author

    /**
     * Initializes the history snapshot.
     */
    private void initHistory() {
        historySnapshot = logic.getHistorySnapshot();
        // add an empty string to represent the most-recent end of historySnapshot, to be shown to
        // the user if she tries to navigate past the most-recent end of the historySnapshot.
        historySnapshot.add("");
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextDisplay.getStyleClass().remove(ERROR_STYLE_CLASS);
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClassDisplay = commandTextDisplay.getStyleClass();
        ObservableList<String> styleClassField = commandTextField.getStyleClass();

        if (styleClassDisplay.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClassDisplay.add(ERROR_STYLE_CLASS);

        if (styleClassField.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClassField.add(ERROR_STYLE_CLASS);
    }

}
