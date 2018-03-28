package seedu.investigapptor.logic.commands.exceptions;

/**
 * disallows user from updating password due to invalid password type
 */
public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
