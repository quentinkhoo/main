package seedu.investigapptor.commons.exceptions;

/**
 * Represents an error during decryption
 */
public class WrongPasswordException extends Exception {
    public WrongPasswordException(String message) {
        super(message);
    }
}

