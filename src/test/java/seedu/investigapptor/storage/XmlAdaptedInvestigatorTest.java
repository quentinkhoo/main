package seedu.investigapptor.storage;

import static org.junit.Assert.assertEquals;
import static seedu.investigapptor.storage.XmlAdaptedInvestigator.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.investigapptor.testutil.TypicalInvestigator.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.person.Address;
import seedu.investigapptor.model.person.Email;
import seedu.investigapptor.model.person.Name;
import seedu.investigapptor.model.person.Phone;
import seedu.investigapptor.model.person.investigator.Rank;
import seedu.investigapptor.testutil.Assert;
import seedu.investigapptor.testutil.TypicalInvestigator;

public class XmlAdaptedInvestigatorTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_RANK = "0";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_RANK = "6";
    private static final List<CrimeCase> VALID_CASE = TypicalInvestigator.BENSON.getCrimeCases();
    private static final List<XmlAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validInvestigatorDetails_returnsInvestigator() throws Exception {
        XmlAdaptedInvestigator investigator = new XmlAdaptedInvestigator(BENSON);
        assertEquals(BENSON, investigator.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedInvestigator investigator =
                new XmlAdaptedInvestigator(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_RANK, VALID_CASE, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, investigator::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedInvestigator investigator = new XmlAdaptedInvestigator(null, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_RANK, VALID_CASE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, investigator::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        XmlAdaptedInvestigator investigator =
                new XmlAdaptedInvestigator(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_RANK, VALID_CASE, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, investigator::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlAdaptedInvestigator investigator = new XmlAdaptedInvestigator(VALID_NAME, null, VALID_EMAIL,
                VALID_ADDRESS, VALID_RANK, VALID_CASE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, investigator::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        XmlAdaptedInvestigator investigator =
                new XmlAdaptedInvestigator(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        VALID_RANK, VALID_CASE, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, investigator::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        XmlAdaptedInvestigator investigator = new XmlAdaptedInvestigator(VALID_NAME, VALID_PHONE, null,
                VALID_ADDRESS, VALID_RANK, VALID_CASE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, investigator::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        XmlAdaptedInvestigator investigator =
                new XmlAdaptedInvestigator(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                        VALID_RANK, VALID_CASE, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_ADDRESS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, investigator::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedInvestigator investigator = new XmlAdaptedInvestigator(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                null, VALID_RANK, VALID_CASE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, investigator::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedInvestigator investigator =
                new XmlAdaptedInvestigator(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_RANK, VALID_CASE, invalidTags);
        Assert.assertThrows(IllegalValueException.class, investigator::toModelType);
    }

    @Test
    public void toModelType_invalidRank_throwsIllegalValueException() {
        XmlAdaptedInvestigator investigator = new XmlAdaptedInvestigator(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, INVALID_RANK, VALID_CASE, VALID_TAGS);
        String expectedMessage = Rank.MESSAGE_RANK_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, investigator::toModelType);
    }

    @Test
    public void toModelType_nullRank_throwsIllegalValueException() {
        XmlAdaptedInvestigator investigator = new XmlAdaptedInvestigator(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, null, VALID_CASE, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Rank.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, investigator::toModelType);
    }

}
