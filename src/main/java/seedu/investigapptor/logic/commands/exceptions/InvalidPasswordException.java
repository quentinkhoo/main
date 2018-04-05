package seedu.investigapptor.logic.commands.exceptions;

import seedu.investigapptor.commons.exceptions.IllegalValueException;

//@@author quentinkhoo
/**
 * disallows user from updating password due to invalid password type
 */
public class InvalidPasswordException extends IllegalValueException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
