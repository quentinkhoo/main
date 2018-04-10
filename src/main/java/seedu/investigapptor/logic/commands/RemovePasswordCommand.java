package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.investigapptor.logic.commands.exceptions.NoPasswordException;

//@@author quentinkhoo
/**
 * Removes the password from the investigapptor application
 */
public class RemovePasswordCommand extends Command {

    public static final String COMMAND_WORD = "removepassword";
    public static final String COMMAND_ALIAS = "rp";
    public static final String MESSAGE_SUCCESS =  "Password successfully removed!";
    public static final String MESSAGE_NO_PASSWORD = "No password to remove!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the password from the Investigapptor.";

    @Override
    public CommandResult execute() {
        requireNonNull(model);
        try {
            model.removePassword();
            return new CommandResult(MESSAGE_SUCCESS);
        } catch (NoPasswordException npe) {
            return new CommandResult(MESSAGE_NO_PASSWORD);
        }
    }

}
