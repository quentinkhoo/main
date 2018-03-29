package seedu.investigapptor.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.investigapptor.testutil.Assert;

public class PasswordTest {

    @Test
    public void isValidPassword() {
        // null investigapptor
        Assert.assertThrows(NullPointerException.class, () -> Password.isValidPassword(null));

        // invalid password
        assertFalse(Password.isValidPassword("")); // empty string
        assertFalse(Password.isValidPassword(" ")); // spaces only
        assertFalse(Password.isValidPassword("!@#$% !@#$%")); // with spaces
        assertFalse(Password.isValidPassword("bugou")); // less than 8 characters

        // valid addresses
        assertTrue(Password.isValidPassword("peterjack")); // alphabets only
        assertTrue(Password.isValidPassword("12345678")); // numbers only
        assertTrue(Password.isValidPassword("peterthe2nd")); // alphanumeric characters
        assertTrue(Password.isValidPassword("C@p1talTAn")); // with capital letters, numbers and symbols
    }
}
