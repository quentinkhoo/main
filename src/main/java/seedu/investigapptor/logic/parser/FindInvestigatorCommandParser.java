package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.investigapptor.logic.commands.FindInvestigatorCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindInvestigatorCommand object
 */
public class FindInvestigatorCommandParser implements Parser<FindInvestigatorCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindInvestigatorCommand
     * and returns an FindInvestigatorCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindInvestigatorCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindInvestigatorCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindInvestigatorCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
