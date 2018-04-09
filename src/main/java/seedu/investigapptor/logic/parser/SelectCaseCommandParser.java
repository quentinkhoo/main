package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.logic.commands.SelectCaseCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
//@@author leowweiching
/**
 * Parses input arguments and creates a new SelectCaseCommand object
 */
public class SelectCaseCommandParser implements Parser<SelectCaseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectCaseCommand
     * and returns an SelectCaseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SelectCaseCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SelectCaseCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCaseCommand.MESSAGE_USAGE));
        }
    }
}
