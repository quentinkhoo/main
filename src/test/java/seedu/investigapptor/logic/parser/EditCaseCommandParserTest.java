package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.commands.CommandTestUtil.CASENAME_DESC_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.CASENAME_DESC_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.DESCRIPTION_DESC_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_CASENAME_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_INVESTIGATOR_INDEX_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_STARTDATE_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVESTIGATOR_DESC_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.STARTDATE_DESC_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.STARTDATE_DESC_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.TAG_DESC_FRAUD;
import static seedu.investigapptor.logic.commands.CommandTestUtil.TAG_DESC_MURDER;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_CASENAME_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_CASENAME_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_DESCRIPTION_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_STARTDATE_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_STARTDATE_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_FRAUD;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_MURDER;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.investigapptor.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_FIRST_CASE;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_SECOND_CASE;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_THIRD_CASE;

import org.junit.Test;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.logic.commands.EditCaseCommand;
import seedu.investigapptor.logic.commands.EditCaseCommand.EditCrimeCaseDescriptor;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.Date;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.tag.Tag;
import seedu.investigapptor.testutil.EditCrimeCaseDescriptorBuilder;

public class EditCaseCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCaseCommand.MESSAGE_USAGE);

    private EditCaseCommandParser parser = new EditCaseCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_CASENAME_APPLE, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCaseCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + CASENAME_DESC_APPLE, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + CASENAME_DESC_APPLE, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 k/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_CASENAME_DESC,
                CaseName.MESSAGE_CASE_NAME_CONSTRAINTS); // invalid case name
        assertParseFailure(parser, "1" + INVALID_DESCRIPTION_DESC,
                Description.MESSAGE_DESCRIPTION_CONSTRAINTS); // invalid description
        assertParseFailure(parser, "1" + INVALID_INVESTIGATOR_INDEX_DESC,
                MESSAGE_INVALID_INDEX); // invalid investigator index
        assertParseFailure(parser, "1" + INVALID_STARTDATE_DESC,
                Date.MESSAGE_DATE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, "1" + INVALID_TAG_DESC,
                Tag.MESSAGE_TAG_CONSTRAINTS); // invalid tag

        // invalid description followed by valid investigator index
        assertParseFailure(parser, "1" + INVALID_DESCRIPTION_DESC + INVESTIGATOR_DESC_APPLE,
                Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        // valid description followed by invalid description. The test case for invalid description followed by
        // valid description is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + DESCRIPTION_DESC_BANANA + INVALID_DESCRIPTION_DESC,
                Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code CrimeCase} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + TAG_DESC_FRAUD + TAG_DESC_MURDER
                + TAG_EMPTY, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_DESC_FRAUD + TAG_EMPTY
                + TAG_DESC_MURDER, Tag.MESSAGE_TAG_CONSTRAINTS);
        assertParseFailure(parser, "1" + TAG_EMPTY + TAG_DESC_FRAUD
                + TAG_DESC_MURDER, Tag.MESSAGE_TAG_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_CASENAME_DESC + INVALID_INVESTIGATOR_INDEX_DESC
                        + VALID_STARTDATE_APPLE + VALID_DESCRIPTION_APPLE,
                CaseName.MESSAGE_CASE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_CASE;
        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_BANANA + TAG_DESC_MURDER
                + STARTDATE_DESC_APPLE + CASENAME_DESC_APPLE + TAG_DESC_FRAUD;

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_APPLE)
                .withDescription(VALID_DESCRIPTION_BANANA)
                .withStartDate(VALID_STARTDATE_APPLE)
                .withTags(VALID_TAG_MURDER, VALID_TAG_FRAUD).build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_CASE;

        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_BANANA + STARTDATE_DESC_APPLE;

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_BANANA)
                .withStartDate(VALID_STARTDATE_APPLE).build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // case name
        Index targetIndex = INDEX_THIRD_CASE;
        String userInput = targetIndex.getOneBased() + CASENAME_DESC_APPLE;
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withCaseName(VALID_CASENAME_APPLE).build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // description
        userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_APPLE;
        descriptor = new EditCrimeCaseDescriptorBuilder().withDescription(VALID_DESCRIPTION_APPLE).build();
        expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // investigapptor
        userInput = targetIndex.getOneBased() + STARTDATE_DESC_APPLE;
        descriptor = new EditCrimeCaseDescriptorBuilder().withStartDate(VALID_STARTDATE_APPLE).build();
        expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_FRAUD;
        descriptor = new EditCrimeCaseDescriptorBuilder().withTags(VALID_TAG_FRAUD).build();
        expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_CASE;
        String userInput = targetIndex.getOneBased()  + DESCRIPTION_DESC_APPLE + STARTDATE_DESC_APPLE
                + TAG_DESC_FRAUD + DESCRIPTION_DESC_APPLE + STARTDATE_DESC_APPLE
                + TAG_DESC_FRAUD + DESCRIPTION_DESC_BANANA + STARTDATE_DESC_BANANA
                + TAG_DESC_MURDER;

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_BANANA)
                .withStartDate(VALID_STARTDATE_BANANA)
                .withTags(VALID_TAG_FRAUD, VALID_TAG_MURDER)
                .build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_CASE;
        String userInput = targetIndex.getOneBased() + INVALID_DESCRIPTION_DESC + DESCRIPTION_DESC_BANANA;
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_BANANA).build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + CASENAME_DESC_BANANA + INVALID_DESCRIPTION_DESC
                + STARTDATE_DESC_BANANA + DESCRIPTION_DESC_BANANA;
        descriptor = new EditCrimeCaseDescriptorBuilder().withDescription(VALID_DESCRIPTION_BANANA)
                .withCaseName(VALID_CASENAME_BANANA)
                .withStartDate(VALID_STARTDATE_BANANA).build();
        expectedCommand = new EditCaseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_CASE;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder().withTags().build();
        EditCaseCommand expectedCommand = new EditCaseCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
