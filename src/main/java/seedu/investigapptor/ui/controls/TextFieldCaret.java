package seedu.investigapptor.ui.controls;

import com.sun.javafx.scene.control.skin.TextFieldSkin;

import javafx.scene.control.TextField;

/**
 * Self defined method for creating text skin
 */
public class TextFieldCaret extends TextFieldSkin {

    private TextField textField;

    public TextFieldCaret(TextField textField) {
        super(textField);
        setCaretProperties();
    }

    /**
     * Update caret properties
     */
    private void setCaretProperties() {
        setCaretAnimating(true);
    }
}
