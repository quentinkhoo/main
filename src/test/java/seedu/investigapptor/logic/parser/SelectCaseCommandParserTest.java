package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_CASE;

import org.junit.Test;

import seedu.investigapptor.logic.commands.SelectCaseCommand;

/**
 * Test scope: similar to {@code DeleteCaseCommandParserTest}.
 * @see DeleteCaseCommandParserTest
 */
public class SelectCaseCommandParserTest {

    private SelectCaseCommandParser parser = new SelectCaseCommandParser();

    @Test
    public void parse_validArgs_returnsSelectCommand() {
        assertParseSuccess(parser, "1", new SelectCaseCommand(INDEX_FIRST_CASE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SelectCaseCommand.MESSAGE_USAGE));
    }
}
