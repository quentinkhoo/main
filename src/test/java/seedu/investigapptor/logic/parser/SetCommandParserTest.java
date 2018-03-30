package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Before;
import org.junit.Test;

import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.logic.commands.SetCommand;

public class SetCommandParserTest {

    private SetCommandParser parser;

    @Before
    public void setUp() {
         parser = new SetCommandParser();
    }

    @Test
    public void parse_invalidAlias_failure() {
        assertParseFailure(parser, "set p@ssword", String.format(Messages.MESSAGE_INVALID_COMMAND_ALIAS,
                SetCommand.MESSAGE_ALIASES));
    }
}
