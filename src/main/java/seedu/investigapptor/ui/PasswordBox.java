package seedu.investigapptor.ui;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.events.ui.NewResultAvailableEvent;
import seedu.investigapptor.commons.events.ui.ValidPasswordEvent;
import seedu.investigapptor.commons.exceptions.DataConversionException;
import seedu.investigapptor.commons.exceptions.WrongPasswordException;
import seedu.investigapptor.logic.ListElementPointer;
import seedu.investigapptor.logic.Logic;
import seedu.investigapptor.logic.commands.CommandResult;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.Password;
import seedu.investigapptor.model.ReadOnlyInvestigapptor;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.util.SampleDataUtil;
import seedu.investigapptor.storage.Storage;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class PasswordBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "PasswordBox.fxml";

    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Storage storage;
    private final Model model;

    @FXML
    private TextField commandTextField;

    public PasswordBox(Storage storage, Model model) {
        super(FXML);

        this.storage = storage;
        this.model = model;

        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
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
        Optional<ReadOnlyInvestigapptor> investigapptorOptional;
        ReadOnlyInvestigapptor initialData;
        String input = commandTextField.getText();
        try {
            investigapptorOptional = storage.readInvestigapptor(new Password(input));
            raise(new ValidPasswordEvent());
        } catch (WrongPasswordException wpe) {
            CommandResult passwordResult = new CommandResult("An invalid password has been entered");
            commandTextField.setText("");
            logger.info("Result: " + passwordResult.feedbackToUser);
            raise(new NewResultAvailableEvent(passwordResult.feedbackToUser));
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty AddressBook");
            initialData = new Investigapptor();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty AddressBook");
            initialData = new Investigapptor();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

}
