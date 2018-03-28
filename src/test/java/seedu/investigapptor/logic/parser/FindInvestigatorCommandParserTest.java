package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.Test;

import seedu.investigapptor.logic.commands.FindInvestigatorCommand;
import seedu.investigapptor.model.person.NameContainsKeywordsPredicate;

public class FindInvestigatorCommandParserTest {

    private FindInvestigatorCommandParser parser = new FindInvestigatorCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindInvestigatorCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindInvestigatorCommand() {
        // no leading and trailing whitespaces
        FindInvestigatorCommand expectedFindInvestigatorCommand =
                new FindInvestigatorCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindInvestigatorCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindInvestigatorCommand);
    }

}
