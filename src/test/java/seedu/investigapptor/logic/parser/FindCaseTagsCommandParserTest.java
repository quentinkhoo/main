package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.investigapptor.logic.commands.FindCaseTagsCommand;
import seedu.investigapptor.model.crimecase.TagContainsKeywordsPredicate;

//@@author pkaijun
public class FindCaseTagsCommandParserTest {

    private FindCaseTagsCommandParser parser = new FindCaseTagsCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCaseTagsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindInvestTagsCommand() {
        // no leading and trailing whitespaces. arguments are lowercase as comparison is lowercase based
        FindCaseTagsCommand expectedFindCommand =
                new FindCaseTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList("murder", "robbery")));
        assertParseSuccess(parser, "murder robbery", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n murder \n \t robbery  \t", expectedFindCommand);
    }

}
