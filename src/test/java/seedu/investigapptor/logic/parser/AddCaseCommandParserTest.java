package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.commands.CommandTestUtil.CASENAME_DESC_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.CASENAME_DESC_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.DESCRIPTION_DESC_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.DESCRIPTION_DESC_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_CASENAME_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_STARTDATE_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVESTIGATOR_DESC_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.STARTDATE_DESC_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.STARTDATE_DESC_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.TAG_DESC_FRAUD;
import static seedu.investigapptor.logic.commands.CommandTestUtil.TAG_DESC_MURDER;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_CASENAME_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_CASENAME_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_DESCRIPTION_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_DESCRIPTION_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_INVESTIGATOR_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_STARTDATE_APPLE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_STARTDATE_BANANA;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_FRAUD;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_MURDER;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.investigapptor.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.Set;

import org.junit.Test;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.logic.commands.AddCaseCommand;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.StartDate;
import seedu.investigapptor.model.tag.Tag;
import seedu.investigapptor.model.util.SampleDataUtil;

public class AddCaseCommandParserTest {
    private AddCaseCommandParser parser = new AddCaseCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {

        Index targetIndex = INDEX_SECOND_PERSON;
        Set<Tag> tag = SampleDataUtil.getTagSet(VALID_TAG_FRAUD);
        Set<Tag> tagList = SampleDataUtil.getTagSet(VALID_TAG_FRAUD, VALID_TAG_MURDER);

        AddCaseCommand expectedCommand = new AddCaseCommand(new CaseName(VALID_CASENAME_BANANA),
                new Description(VALID_DESCRIPTION_BANANA), targetIndex,
                new StartDate(VALID_STARTDATE_BANANA), tag);

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + CASENAME_DESC_BANANA + DESCRIPTION_DESC_BANANA
                + " i/" + targetIndex.getOneBased() + STARTDATE_DESC_BANANA
                + TAG_DESC_FRAUD, expectedCommand);

        // multiple case names - last case name accepted
        assertParseSuccess(parser, CASENAME_DESC_APPLE + CASENAME_DESC_BANANA + DESCRIPTION_DESC_BANANA
                + " i/" + targetIndex.getOneBased() + STARTDATE_DESC_BANANA
                + TAG_DESC_FRAUD, expectedCommand);

        // multiple descriptions - last description accepted
        assertParseSuccess(parser, CASENAME_DESC_BANANA + DESCRIPTION_DESC_APPLE + DESCRIPTION_DESC_BANANA
                + " i/" + targetIndex.getOneBased() + STARTDATE_DESC_BANANA
                + TAG_DESC_FRAUD, expectedCommand);

        // multiple start dates - last start date accepted
        assertParseSuccess(parser, CASENAME_DESC_BANANA + DESCRIPTION_DESC_BANANA
                + " i/" + targetIndex.getOneBased() + STARTDATE_DESC_APPLE + STARTDATE_DESC_BANANA
                + TAG_DESC_FRAUD, expectedCommand);

        // multiple tags - all accepted
        AddCaseCommand expectedCommandMultipleTags = new AddCaseCommand(new CaseName(VALID_CASENAME_BANANA),
                new Description(VALID_DESCRIPTION_BANANA), targetIndex,
                new StartDate(VALID_STARTDATE_BANANA), tagList);
        assertParseSuccess(parser, CASENAME_DESC_BANANA + DESCRIPTION_DESC_BANANA
                + " i/" + targetIndex.getOneBased()
                + STARTDATE_DESC_BANANA + TAG_DESC_MURDER
                + TAG_DESC_FRAUD, expectedCommandMultipleTags);
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Index targetIndex = INDEX_SECOND_PERSON;
        Set<Tag> tag = SampleDataUtil.getTagSet();

        AddCaseCommand expectedCommand = new AddCaseCommand(new CaseName(VALID_CASENAME_APPLE),
                new Description(VALID_DESCRIPTION_APPLE), targetIndex,
                new StartDate(VALID_STARTDATE_APPLE), tag);
        assertParseSuccess(parser, CASENAME_DESC_APPLE + DESCRIPTION_DESC_APPLE
                        + " i/" + targetIndex.getOneBased() + STARTDATE_DESC_APPLE,
                expectedCommand);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddCaseCommand.MESSAGE_USAGE);

        // missing case name prefix
        assertParseFailure(parser, VALID_CASENAME_BANANA + DESCRIPTION_DESC_BANANA
                        + STARTDATE_DESC_BANANA,
                expectedMessage);

        // missing description prefix
        assertParseFailure(parser, CASENAME_DESC_BANANA + VALID_DESCRIPTION_BANANA
                        + STARTDATE_DESC_BANANA,
                expectedMessage);

        // missing investigator prefix
        assertParseFailure(parser, CASENAME_DESC_BANANA + DESCRIPTION_DESC_BANANA
                        + VALID_INVESTIGATOR_BANANA + STARTDATE_DESC_BANANA,
                expectedMessage);

        // missing start date prefix
        assertParseFailure(parser, CASENAME_DESC_BANANA + DESCRIPTION_DESC_BANANA
                        + VALID_STARTDATE_BANANA,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_CASENAME_BANANA + VALID_DESCRIPTION_BANANA
                        + VALID_STARTDATE_BANANA,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {

        // invalid case name
        assertParseFailure(parser, INVALID_CASENAME_DESC + DESCRIPTION_DESC_BANANA
                + INVESTIGATOR_DESC_BANANA + STARTDATE_DESC_BANANA
                + TAG_DESC_MURDER + TAG_DESC_FRAUD, CaseName.MESSAGE_CASE_NAME_CONSTRAINTS);

        // invalid description
        assertParseFailure(parser, CASENAME_DESC_BANANA + INVALID_DESCRIPTION_DESC
                + INVESTIGATOR_DESC_BANANA + STARTDATE_DESC_BANANA
                + TAG_DESC_MURDER + TAG_DESC_FRAUD, Description.MESSAGE_DESCRIPTION_CONSTRAINTS);

        // invalid start date
        assertParseFailure(parser, CASENAME_DESC_BANANA + DESCRIPTION_DESC_BANANA
                + INVESTIGATOR_DESC_BANANA + INVALID_STARTDATE_DESC
                + TAG_DESC_MURDER + TAG_DESC_FRAUD, StartDate.MESSAGE_DATE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, CASENAME_DESC_BANANA + DESCRIPTION_DESC_BANANA
                + INVESTIGATOR_DESC_BANANA + STARTDATE_DESC_BANANA
                + INVALID_TAG_DESC + VALID_TAG_FRAUD, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_CASENAME_DESC + DESCRIPTION_DESC_BANANA
                        + INVESTIGATOR_DESC_BANANA + INVALID_STARTDATE_DESC,
                CaseName.MESSAGE_CASE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + CASENAME_DESC_BANANA
                        + DESCRIPTION_DESC_BANANA + STARTDATE_DESC_BANANA + TAG_DESC_MURDER + TAG_DESC_FRAUD,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCaseCommand.MESSAGE_USAGE));
    }
}
