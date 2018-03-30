package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.investigapptor.model.Password.MESSAGE_PASSWORD_CONSTRAINTS;

import java.util.Objects;
import java.util.logging.Logger;

import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.logic.commands.exceptions.InvalidPasswordException;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.Password;

/**
 * Adds a password to the investigapptor book.
 */
public class PasswordCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "password";
    public static final String COMMAND_ALIAS = "pw";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates/Updates password for the Investigapptor."
            + "Parameters: " + PREFIX_PASSWORD + " password";

    public static final String MESSAGE_SUCCESS = "Password updated";

    private final Logger logger = LogsCenter.getLogger(PasswordCommand.class);

    private Password password;

    /**
     * Creates an PasswordCommand to add the specified {@code CrimeCase}
     */
    public PasswordCommand(Password password, Model model) {
        requireNonNull(password);
        this.password = password;
        this.model = model;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.updatePassword(password);
            logger.info("Password has been updated!");
            return new CommandResult(String.format(MESSAGE_SUCCESS));
        } catch (InvalidPasswordException ipe) {
            throw new CommandException(MESSAGE_PASSWORD_CONSTRAINTS);
        }

    }

    @Override
    public boolean equals(Object other) {

        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PasswordCommand)) {
            return false;
        }

        // state check
        return Objects.equals(password, ((PasswordCommand) other).password);
    }
}
