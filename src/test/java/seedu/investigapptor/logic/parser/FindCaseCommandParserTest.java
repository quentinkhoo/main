package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.investigapptor.logic.commands.FindCaseCommand;
import seedu.investigapptor.model.crimecase.CaseNameContainsKeywordsPredicate;

public class FindCaseCommandParserTest {

    private FindCaseCommandParser parser = new FindCaseCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCaseCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCaseCommand() {
        // no leading and trailing whitespaces
        FindCaseCommand expectedFindCaseCommand =
                new FindCaseCommand(new CaseNameContainsKeywordsPredicate(Arrays.asList("Alpha", "Bravo")));
        assertParseSuccess(parser, "Alpha Bravo", expectedFindCaseCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alpha \n \t Bravo  \t", expectedFindCaseCommand);
    }

}
