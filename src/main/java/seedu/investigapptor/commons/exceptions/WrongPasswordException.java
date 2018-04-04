package seedu.investigapptor.commons.exceptions;

//@@author quentinkhoo
/**
 * Represents an error during decryption
 */
public class WrongPasswordException extends Exception {
    public WrongPasswordException(String message) {
        super(message);
    }
}

