package seedu.investigapptor.logic.commands.exceptions;

//@@author quentinkhoo
/**
 * disallows removing of password from application if there is no password
 */
public class NoPasswordException extends Exception {
    public NoPasswordException(String message) {
        super(message);
    }
}
