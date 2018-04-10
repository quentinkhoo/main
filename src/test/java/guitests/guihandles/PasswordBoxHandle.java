package guitests.guihandles;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import seedu.investigapptor.ui.PasswordBox;

//@@author quentinkhoo
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
//@@author
