package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.investigapptor.logic.commands.SelectInvestigatorCommand;

/**
 * Test scope: similar to {@code DeleteInvestigatorCommandParserTest}.
 * @see DeleteInvestigatorCommandParserTest
 */
public class SelectInvestigatorCommandParserTest {

    private SelectInvestigatorCommandParser parser = new SelectInvestigatorCommandParser();

    @Test
    public void parse_validArgs_returnsSelectCommand() {
        assertParseSuccess(parser, "1", new SelectInvestigatorCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SelectInvestigatorCommand.MESSAGE_USAGE));
    }
}
