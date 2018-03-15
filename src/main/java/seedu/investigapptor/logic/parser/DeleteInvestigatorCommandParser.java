package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.logic.commands.DeleteInvestigatorCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteInvestigatorCommand object
 */
public class DeleteInvestigatorCommandParser implements Parser<DeleteInvestigatorCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteInvestigatorCommand
     * and returns an DeleteInvestigatorCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteInvestigatorCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteInvestigatorCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteInvestigatorCommand.MESSAGE_USAGE));
        }
    }

}
