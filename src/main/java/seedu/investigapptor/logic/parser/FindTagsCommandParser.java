package seedu.investigapptor.logic.parser;

import seedu.investigapptor.logic.commands.FindTagsCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.person.investigator.TagContainsKeywordsPredicate;

import java.util.Arrays;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class FindTagsCommandParser implements Parser<FindTagsCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindTagsCommandParser
     * and returns an FindTagsCommandParser object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindTagsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagsCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.toLowerCase().split("\\s+");

        return new FindTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
