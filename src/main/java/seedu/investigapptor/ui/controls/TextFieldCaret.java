package seedu.investigapptor.ui.controls;

import com.sun.javafx.scene.control.skin.TextFieldSkin;

import javafx.scene.control.TextField;

public class TextFieldCaret extends TextFieldSkin {

    private TextField textField;

    public TextFieldCaret(TextField textField) {
        super(textField);
        setCaretProperties();
    }

    private void setCaretProperties() {
        setCaretAnimating(true);
    }
}
