package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.investigapptor.logic.commands.FindInvestTagsCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.person.investigator.TagContainsKeywordsPredicate;

//@@author pkaijun
/**
 * Parses input arguments and creates a new FindInvestTagsCommand object
 */
public class FindInvestTagsCommandParser implements Parser<FindInvestTagsCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindInvestTagsCommandParser
     * and returns an FindInvestTagsCommandParser object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindInvestTagsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindInvestTagsCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.toLowerCase().split("\\s+");

        return new FindInvestTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
