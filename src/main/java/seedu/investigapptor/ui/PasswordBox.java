package seedu.investigapptor.ui;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.events.ui.InvalidFileFormatEvent;
import seedu.investigapptor.commons.events.ui.NewResultAvailableEvent;
import seedu.investigapptor.commons.events.ui.ValidPasswordEvent;
import seedu.investigapptor.commons.exceptions.DataConversionException;
import seedu.investigapptor.commons.exceptions.WrongPasswordException;
import seedu.investigapptor.logic.commands.CommandResult;
import seedu.investigapptor.model.Password;
import seedu.investigapptor.storage.Storage;

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
