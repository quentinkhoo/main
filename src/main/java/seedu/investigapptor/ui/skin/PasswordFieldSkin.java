package seedu.investigapptor.ui.skin;

import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_PASSWORD;

import com.sun.javafx.scene.control.behavior.PasswordFieldBehavior;
import com.sun.javafx.scene.control.skin.TextFieldSkin;

import javafx.scene.control.PasswordField;

//@@author quentinkhoo
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
//@@author
