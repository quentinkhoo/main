package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_ALIAS;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.logic.parser.SetPasswordCommandParser;
import seedu.investigapptor.logic.parser.exceptions.ParseException;

/**
 * Sets a specific settings for the application
 */
public class SetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "set";
    public static final String COMMAND_ALIAS = "s";
    public static final String[] TYPE_PASSWORD_ALIASES = {"password", "pass", "pw", "p"};
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets the specified type\n"
            + "Parameters: TYPE (must be password)\n"
            + "Example: " + COMMAND_WORD + " cases";
    public static final String MESSAGE_ALIASES =
            "password: Can either be password, pass, pw or p\n"
                    + "Example: " + COMMAND_WORD + " pw\n";

    public static final String MESSAGE_SUCCESS = "Succesfully set %1$s";

    private final String setType;
    private String args;
    private String[] argsArray;

    public SetCommand(String args) {
        this.args = args.trim();
        argsArray = args.split("\\s+");
        this.setType = argsArray[0];
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        if (isValidPasswordAlias(setType)) {
            try {
                if (args.contains(" ")) {
                    args = args.substring(args.indexOf(" "));
                    return new SetPasswordCommandParser().parse(args).executeUndoableCommand();
                } else {
                    return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            SetPasswordCommand.MESSAGE_USAGE));
                }
            } catch (ParseException pe) {
                return new CommandResult(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        SetPasswordCommand.MESSAGE_USAGE));
            }
        } else {
            throw new CommandException(MESSAGE_INVALID_COMMAND_ALIAS);
        }
    }

    /**
     * Checks and returns an input listing type is a valid investigator alias
     * @param type
     */
    public static boolean isValidPasswordAlias(String type) {
        for (String alias : TYPE_PASSWORD_ALIASES) {
            if (type.equals(alias)) {
                return true;
            }
        }
        return false;
    }

}
