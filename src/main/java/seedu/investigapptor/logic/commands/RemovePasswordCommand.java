package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_PASSWORD;

import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;

import seedu.investigapptor.commons.exceptions.WrongPasswordException;
import seedu.investigapptor.logic.commands.exceptions.InvalidPasswordException;
import seedu.investigapptor.model.Password;

public class RemovePasswordCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removepassword";
    public static final String COMMAND_ALIAS = "rp";
    public static final String MESSAGE_SUCCESS =  "Password successfully removed!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the password from the Investigapptor."
            + " Requires input of current password"
            + "Parameters: " + PREFIX_PASSWORD + "currentPassword";

    private String inputPasswordHash;


    public RemovePasswordCommand(String inputPassword) {
        requireNonNull(inputPassword);
        this.inputPasswordHash = Password.generatePasswordHash(inputPassword);
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        try {
            checkInputPassword(inputPasswordHash);
        } catch (WrongPasswordException wpe) {
            return new CommandResult(wpe.getMessage());
        }
        model.removePassword();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    private void checkInputPassword(String inputPasswordHash) throws WrongPasswordException {
        try {
            String currentPasswordHash = model.getInvestigapptor().getPassword().getPassword();
            if (!currentPasswordHash.equals(inputPasswordHash)) {
                throw new WrongPasswordException("Password cannot be removed. Invalid password has been entered.");
            }
        } catch (NullPointerException npe) {
            throw new WrongPasswordException("Investigapptor currently has no password!");
        }
    }
}
