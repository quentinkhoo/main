package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_PASSWORD;

import seedu.investigapptor.commons.exceptions.WrongPasswordException;
import seedu.investigapptor.logic.commands.exceptions.NoPasswordException;
import seedu.investigapptor.model.Password;

//@@author quentinkhoo
/**
 * Removes the password from the investigapptor application
 */
public class RemovePasswordCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removepassword";
    public static final String COMMAND_ALIAS = "rp";
    public static final String MESSAGE_SUCCESS =  "Password successfully removed!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the password from the Investigapptor."
            + " Requires input of current password"
            + "Parameters: " + PREFIX_PASSWORD + "currentPassword";

    private String inputPassword;


    public RemovePasswordCommand(String inputPassword) {
        requireNonNull(inputPassword);
        this.inputPassword = inputPassword;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        try {
            checkInputPassword(inputPassword);
        } catch (NoPasswordException npe) {
            return new CommandResult(npe.getMessage());
        } catch (WrongPasswordException wpe) {
            return new CommandResult(wpe.getMessage());
        }
        model.removePassword();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Checks whether the input password matches the current investigapptor password
     * @param inputPassword
     * @throws WrongPasswordException if password is invalid or there is no password in the application
     */
    private void checkInputPassword(String inputPassword) throws WrongPasswordException, NoPasswordException {
        String inputPasswordHash = Password.generatePasswordHash(inputPassword);
        try {
            String currentPasswordHash = model.getInvestigapptor().getPassword().getPassword();
            if (!currentPasswordHash.equals(inputPasswordHash)) {
                throw new WrongPasswordException("Password cannot be removed. Invalid password has been entered.");
            }
        } catch (NullPointerException npe) {
            throw new NoPasswordException("Investigapptor currently has no password!");
        }
    }
}
