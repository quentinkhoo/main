package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_INVESTIGATOR;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.logic.commands.AddCaseCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.StartDate;
import seedu.investigapptor.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCaseCommandParser implements Parser<AddCaseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCaseCommand
     * and returns an AddCaseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCaseCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_INVESTIGATOR,
                        PREFIX_START_DATE, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_INVESTIGATOR, PREFIX_START_DATE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCaseCommand.MESSAGE_USAGE));
        }

        try {
            CaseName name = ParserUtil.parseCaseName(argMultimap.getValue(PREFIX_NAME)).get();
            Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION)).get();
            Index investigatorIndex = ParserUtil.parseIndex(argMultimap.getOnlyValue(PREFIX_INVESTIGATOR));
            StartDate startDate = ParserUtil.parseStartDate(argMultimap.getValue(PREFIX_START_DATE)).get();
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            return new AddCaseCommand(name, description, investigatorIndex, startDate, tagList);

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
