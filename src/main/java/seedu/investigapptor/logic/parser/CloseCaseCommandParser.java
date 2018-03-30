//@@author pkaijun
package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.logic.commands.CloseCaseCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new CloseCaseCommandParser object
 */
public class CloseCaseCommandParser implements Parser<CloseCaseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CloseCaseCommand
     * and returns an CloseCaseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CloseCaseCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new CloseCaseCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    CloseCaseCommand.MESSAGE_USAGE));
        }
    }
}
