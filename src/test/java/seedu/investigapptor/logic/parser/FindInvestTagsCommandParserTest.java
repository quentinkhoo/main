package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.investigapptor.logic.commands.FindInvestTagsCommand;
import seedu.investigapptor.model.person.investigator.TagContainsKeywordsPredicate;

public class FindInvestTagsCommandParserTest {

    private FindInvestTagsCommandParser parser = new FindInvestTagsCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindInvestTagsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindInvestTagsCommand() {
        // no leading and trailing whitespaces. arguments are lowercase as comparison is lowercase based
        FindInvestTagsCommand expectedFindCommand =
                new FindInvestTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList("teama", "new")));
        assertParseSuccess(parser, "teama new", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n teama \n \t new  \t", expectedFindCommand);
    }

}
