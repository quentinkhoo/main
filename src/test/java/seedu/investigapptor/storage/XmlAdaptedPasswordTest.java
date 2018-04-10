package seedu.investigapptor.storage;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.investigapptor.model.Password;

//@@author quentinkhoo
public class XmlAdaptedPasswordTest {

    @Test
    public void toModelType_validPassword_returnsPassword() throws Exception {
        Password password = new Password("password");
        XmlAdaptedPassword storedPassword = new XmlAdaptedPassword(password);
        assertEquals(password, storedPassword.toModelType());
    }
}
//@@author
