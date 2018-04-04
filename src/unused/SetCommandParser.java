package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_ALIAS;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.logic.commands.Command;
import seedu.investigapptor.logic.commands.SetCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;


/**
 + * Parses input arguments and creates a new ListCommand object
 + */
public class SetCommandParser implements Parser<Command> {

    private String inputType;
    private String actualType;

    /**
     * Parses the given {@code String} of arguments in the context of the SetCommand
     * and returns an SetCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parse(String args) throws ParseException {
        args = args.trim();
        String[] argsArray = args.split("\\s+");
        try {
            inputType = argsArray[0];
            actualType = ParserUtil.parseSetType(inputType);
            return new SetCommand(args);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_ALIAS, SetCommand.MESSAGE_ALIASES));
        }
    }
}
