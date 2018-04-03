package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_PASSWORD;

import java.util.stream.Stream;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.logic.commands.RemovePasswordCommand;
import seedu.investigapptor.logic.commands.SetPasswordCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.Password;

/**
 * Parses input arguments and creates a new PasswordCommand object
 */
public class RemovePasswordCommandParser implements Parser<RemovePasswordCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the PasswordCommand
     * and returns an PasswordCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemovePasswordCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PASSWORD);

        if (!arePrefixesPresent(argMultimap, PREFIX_PASSWORD)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemovePasswordCommand.MESSAGE_USAGE));
        }

        String inputPassword = args.substring(4);
        return new RemovePasswordCommand(inputPassword);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
