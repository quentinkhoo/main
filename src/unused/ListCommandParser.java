package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_ALIAS;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.logic.commands.ListCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;

//@@author quentinkhoo
/**
 + * Parses input arguments and creates a new ListCommand object
 + */
public class ListCommandParser implements Parser<ListCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns an ListCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */

    public ListCommand parse(String args) throws ParseException {
        try {
            String type = ParserUtil.parseListType(args);
            return new ListCommand(type);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_ALIAS, ListCommand.MESSAGE_ALIASES));
        }
    }
}
//@@author
