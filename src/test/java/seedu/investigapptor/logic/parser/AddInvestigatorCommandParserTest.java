package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.investigapptor.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_RANK_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.investigapptor.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.investigapptor.logic.commands.CommandTestUtil.RANK_DESC_CAP;
import static seedu.investigapptor.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.investigapptor.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_RANK_CAPTAIN;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.investigapptor.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.investigapptor.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.investigapptor.logic.commands.AddInvestigatorCommand;
import seedu.investigapptor.model.person.Address;
import seedu.investigapptor.model.person.Email;
import seedu.investigapptor.model.person.Name;
import seedu.investigapptor.model.person.Phone;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.person.investigator.Rank;
import seedu.investigapptor.model.tag.Tag;
import seedu.investigapptor.testutil.InvestigatorBuilder;

public class AddInvestigatorCommandParserTest {
    private AddInvestigatorCommandParser parser = new AddInvestigatorCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Investigator expectedInvestigator = new InvestigatorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withRank(VALID_RANK_CAPTAIN).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + RANK_DESC_CAP + TAG_DESC_FRIEND,
                new AddInvestigatorCommand(expectedInvestigator));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + RANK_DESC_CAP + TAG_DESC_FRIEND,
                new AddInvestigatorCommand(expectedInvestigator));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_AMY + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + RANK_DESC_CAP + TAG_DESC_FRIEND,
                new AddInvestigatorCommand(expectedInvestigator));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_AMY + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + RANK_DESC_CAP + TAG_DESC_FRIEND,
                new AddInvestigatorCommand(expectedInvestigator));

        // multiple addresses - last investigapptor accepted
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_AMY
                + ADDRESS_DESC_BOB + RANK_DESC_CAP + TAG_DESC_FRIEND,
                new AddInvestigatorCommand(expectedInvestigator));

        // multiple tags - all accepted
        Investigator expectedInvestigatorMultipleTags = new InvestigatorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withRank(VALID_RANK_CAPTAIN).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RANK_DESC_CAP + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddInvestigatorCommand(expectedInvestigatorMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Investigator expectedInvestigator = new InvestigatorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withRank(VALID_RANK_CAPTAIN).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + RANK_DESC_CAP, new AddInvestigatorCommand(expectedInvestigator));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddInvestigatorCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + RANK_DESC_CAP, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                        + RANK_DESC_CAP, expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB
                        + RANK_DESC_CAP, expectedMessage);

        // missing investigapptor prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB
                        + RANK_DESC_CAP, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB
                + RANK_DESC_CAP, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RANK_DESC_CAP + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RANK_DESC_CAP + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + RANK_DESC_CAP + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // invalid investigapptor
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + RANK_DESC_CAP + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RANK_DESC_CAP + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                        + RANK_DESC_CAP, Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + RANK_DESC_CAP + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddInvestigatorCommand.MESSAGE_USAGE));

        //invalid rank
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_RANK_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Rank.MESSAGE_RANK_CONSTRAINTS);
    }
}
