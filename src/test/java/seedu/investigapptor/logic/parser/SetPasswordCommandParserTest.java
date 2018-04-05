package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Before;
import org.junit.Test;

import seedu.investigapptor.logic.commands.SetPasswordCommand;
import seedu.investigapptor.model.Password;

/**
 * Note that these test cases cover only input in set password <input>
 * the testing for the prior part is done in SetCommandParser
 */
public class SetPasswordCommandParserTest {

    private SetPasswordCommandParser parser;

    @Before
    public void setUp() {
        this.parser = new SetPasswordCommandParser();
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {

        //no space before prefix
        assertParseFailure(parser, "pw/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SetPasswordCommand.MESSAGE_USAGE));

        //empty password
        assertParseFailure(parser, " pw/", Password.MESSAGE_PASSWORD_CONSTRAINTS);

        //presence of space password
        assertParseFailure(parser, " pw/ password", Password.MESSAGE_PASSWORD_CONSTRAINTS);

        //insufficient length password
        assertParseFailure(parser, " pw/passwor", Password.MESSAGE_PASSWORD_CONSTRAINTS);
    }

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, " pw/password", new SetPasswordCommand(new Password("password")));
    }
}
