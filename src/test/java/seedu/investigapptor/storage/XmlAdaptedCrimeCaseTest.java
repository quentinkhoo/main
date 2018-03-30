package seedu.investigapptor.storage;

import static org.junit.Assert.assertEquals;
import static seedu.investigapptor.storage.XmlAdaptedCrimeCase.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.investigapptor.testutil.TypicalCrimeCases.ALFA;
import static seedu.investigapptor.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.Date;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.Status;
import seedu.investigapptor.model.person.Name;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.testutil.Assert;

public class XmlAdaptedCrimeCaseTest {
    private static final String INVALID_NAME = "Project H@ppy";
    private static final String INVALID_DESCRIPTION = " ";
    private static final XmlAdaptedPerson INVALID_INVESTIGATOR =
            new XmlAdaptedPerson("R@chel", " ", " ", " ",
                    BENSON.getTags().stream()
                            .map(XmlAdaptedTag::new)
                            .collect(Collectors.toList()));
    private static final String INVALID_STARTDATE = "123/44/17";
    private static final String INVALID_STATUS = " ";
    private static final String INVALID_TAG = "#Corruption";

    private static final String VALID_NAME = ALFA.getCaseName().toString();
    private static final String VALID_DESCRIPTION = ALFA.getDescription().toString();
    private static final XmlAdaptedPerson VALID_INVESTIGATOR =
            new XmlAdaptedPerson(ALFA.getCurrentInvestigator());
    private static final String VALID_STARTDATE = ALFA.getStartDate().date;
    private static final String VALID_STATUS = ALFA.getStatus().toString();
    private static final List<XmlAdaptedTag> VALID_TAGS = ALFA.getTags().stream()
            .map(XmlAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validCrimeCaseDetails_returnsPerson() throws Exception {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(ALFA);
        assertEquals(ALFA, crimeCase.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(INVALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR, VALID_STARTDATE,
                        VALID_STATUS, VALID_TAGS);
        String expectedMessage = CaseName.MESSAGE_CASE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(null, VALID_DESCRIPTION, VALID_INVESTIGATOR,
                VALID_STARTDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, CaseName.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, INVALID_DESCRIPTION, VALID_INVESTIGATOR, VALID_STARTDATE,
                        VALID_STATUS, VALID_TAGS);
        String expectedMessage = Description.MESSAGE_DESCRIPTION_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, null, VALID_INVESTIGATOR,
                VALID_STARTDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidInvestigator_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, INVALID_INVESTIGATOR, VALID_STARTDATE,
                        VALID_STATUS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullInvestigator_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, null,
                VALID_STARTDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Person.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidStartDate_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR, INVALID_STARTDATE,
                        VALID_STATUS, VALID_TAGS);
        String expectedMessage = Date.MESSAGE_DATE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullStartDate_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR,
                null, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidStatus_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR, VALID_STARTDATE,
                        INVALID_STATUS, VALID_TAGS);
        String expectedMessage = Status.MESSAGE_STATUS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullStatus_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR,
                VALID_STARTDATE, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Status.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR, VALID_STARTDATE,
                        VALID_STATUS, invalidTags);
        Assert.assertThrows(IllegalValueException.class, crimeCase::toModelType);
    }

}
