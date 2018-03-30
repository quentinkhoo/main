package seedu.investigapptor.logic.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Test;

import seedu.investigapptor.logic.commands.Command;
import seedu.investigapptor.logic.commands.PasswordCommand;
import seedu.investigapptor.logic.commands.exceptions.InvalidPasswordException;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.Password;
import seedu.investigapptor.model.UserPrefs;

/**
 * Note that these test cases cover only input in set password <input>
 * the testing for the prior part is done in SetCommandParser
 */
public class PasswordCommandParserTest {

    private PasswordCommandParser parser = new PasswordCommandParser();

    private final Model model = new ModelManager(new Investigapptor(), new UserPrefs());

    /**
     * Asserts that the parsing of {@code userInput} by {@code parser} is successful and the command created
     * equals to {@code expectedCommand}.
     */
    public void assertParsePasswordSuccess(PasswordCommandParser parser, String userInput, Command expectedCommand) {
        try {
            Command command = parser.parse(userInput, model);
            assertEquals(expectedCommand, command);
        } catch (InvalidPasswordException ipe) {
            throw new IllegalArgumentException("Invalid password.", ipe);
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Invalid userInput.", pe);
        }
    }

    /**
     * Asserts that the parsing of {@code userInput} by {@code parser} is unsuccessful and the error message
     * equals to {@code expectedMessage}.
     */
    public void assertParsePasswordFailure(PasswordCommandParser parser, String userInput, String expectedMessage) {
        try {
            parser.parse(userInput, model);
            fail("The expected ParseException was not thrown.");
        } catch (InvalidPasswordException ipe) {
            assertEquals(expectedMessage, ipe.getMessage());
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
        }
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // insufficient characters
        assertParsePasswordFailure(parser, " p/ ", Password.MESSAGE_PASSWORD_CONSTRAINTS);

        // presence of space in password
        assertParsePasswordFailure(parser, " p/pass word", Password.MESSAGE_PASSWORD_CONSTRAINTS);

        // invalid prefix
        assertParsePasswordFailure(parser, " pw/password", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PasswordCommand.MESSAGE_USAGE));

        //no prefix
        assertParsePasswordFailure(parser, " password", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                PasswordCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_success() {
        assertParsePasswordSuccess(parser, " p/password", new PasswordCommand(new Password("password"),
                model));
    }
}
