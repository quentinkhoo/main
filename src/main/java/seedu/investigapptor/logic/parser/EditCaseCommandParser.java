package seedu.investigapptor.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_INVESTIGATOR;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.logic.commands.EditCaseCommand;
import seedu.investigapptor.logic.commands.EditCaseCommand.EditCrimeCaseDescriptor;
import seedu.investigapptor.logic.parser.exceptions.ParseException;
import seedu.investigapptor.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCrimeCaseCommand object
 */
public class EditCaseCommandParser implements Parser<EditCaseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCrimeCaseCommand
     * and returns an EditCrimeCaseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCaseCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_INVESTIGATOR,
                        PREFIX_STARTDATE, PREFIX_TAG);

        Index caseIndex;

        try {
            caseIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditCaseCommand.MESSAGE_USAGE));
        }

        EditCrimeCaseDescriptor editCrimeCaseDescriptor = new EditCrimeCaseDescriptor();
        try {
            ParserUtil.parseCaseName(argMultimap.getValue(PREFIX_NAME))
                    .ifPresent(editCrimeCaseDescriptor::setCaseName);
            ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION))
                    .ifPresent(editCrimeCaseDescriptor::setDescription);
            ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INVESTIGATOR))
                    .ifPresent(editCrimeCaseDescriptor::setCurrentInvestigatorIndex);
            ParserUtil.parseStartDate(argMultimap.getValue(PREFIX_STARTDATE))
                    .ifPresent(editCrimeCaseDescriptor::setStartDate);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editCrimeCaseDescriptor::setTags);

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editCrimeCaseDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCaseCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCaseCommand(caseIndex, editCrimeCaseDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
