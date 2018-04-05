package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.investigapptor.logic.commands.FindCaseTagsCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.crimecase.TagContainsKeywordsPredicate;

//@@author pkaijun
/**
 * Parses input arguments and creates a new FindInvestTagsCommand object
 */
public class FindCaseTagsCommandParser implements Parser<FindCaseTagsCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindInvestTagsCommandParser
     * and returns an FindInvestTagsCommandParser object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCaseTagsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCaseTagsCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.toLowerCase().split("\\s+");

        return new FindCaseTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
