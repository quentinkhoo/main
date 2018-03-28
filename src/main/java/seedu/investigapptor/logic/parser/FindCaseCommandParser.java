package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.investigapptor.logic.commands.FindCaseCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.crimecase.CaseNameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCaseCommand object
 */
public class FindCaseCommandParser implements Parser<FindCaseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCaseCommand
     * and returns an FindCaseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCaseCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCaseCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        return new FindCaseCommand(new CaseNameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }

}
