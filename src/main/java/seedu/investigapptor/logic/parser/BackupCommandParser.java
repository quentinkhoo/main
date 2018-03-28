package seedu.investigapptor.logic.parser;

import static java.util.Objects.requireNonNull;

import seedu.investigapptor.logic.commands.BackupCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new BackupCommand command
 */
public class BackupCommandParser implements Parser<BackupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the BackupCommand
     * and returns an BackupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public BackupCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String fileName = args.trim();
        if (!fileName.matches("^[\\w-]*$")) {
            throw new ParseException("file name can only contain alphanumeric and underscore");
        }
        return new BackupCommand(fileName);
    }

}
