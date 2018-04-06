# leowweiching
###### /java/seedu/investigapptor/logic/commands/EditCrimeCaseDescriptorTest.java
``` java
public class EditCrimeCaseDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditCrimeCaseDescriptor descriptorWithSameValues = new EditCrimeCaseDescriptor(DESC_APPLE);
        assertTrue(DESC_APPLE.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_APPLE.equals(DESC_APPLE));

        // null -> returns false
        assertFalse(DESC_APPLE.equals(null));

        // different types -> returns false
        assertFalse(DESC_APPLE.equals(5));

        // different values -> returns false
        assertFalse(DESC_APPLE.equals(DESC_BANANA));

        // different case name -> returns false
        EditCrimeCaseDescriptor editedApple = new EditCrimeCaseDescriptorBuilder(DESC_APPLE)
                .withCaseName(VALID_CASENAME_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different description -> returns false
        editedApple = new EditCrimeCaseDescriptorBuilder(DESC_APPLE)
                .withDescription(VALID_DESCRIPTION_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different investigator -> returns false
        editedApple = new EditCrimeCaseDescriptorBuilder(DESC_APPLE)
                .withInvestigator(VALID_INVESTIGATOR_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different start date -> returns false
        editedApple = new EditCrimeCaseDescriptorBuilder(DESC_APPLE)
                .withStartDate(VALID_STARTDATE_BANANA).build();
        assertFalse(DESC_APPLE.equals(editedApple));

        // different tags -> returns false
        editedApple = new EditCrimeCaseDescriptorBuilder(DESC_APPLE).withTags(VALID_TAG_MURDER).build();
        assertFalse(DESC_APPLE.equals(editedApple));
    }
}
```
###### /java/seedu/investigapptor/logic/parser/AddCaseCommandParserTest.java
``` java
public class AddCaseCommandParserTest {
    private AddCaseCommandParser parser = new AddCaseCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {

        Index targetIndex = INDEX_SECOND_PERSON;
        Set<Tag> tag = SampleDataUtil.getTagSet(VALID_TAG_FRAUD);
        Set<Tag> tagList = SampleDataUtil.getTagSet(VALID_TAG_FRAUD, VALID_TAG_MURDER);

        AddCaseCommand expectedCommand = new AddCaseCommand(new CaseName(VALID_CASENAME_BANANA),
                new Description(VALID_DESCRIPTION_BANANA), targetIndex,
                new Date(VALID_STARTDATE_BANANA), tag);

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
                new Date(VALID_STARTDATE_BANANA), tagList);
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
                new Date(VALID_STARTDATE_APPLE), tag);
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
                        + VALID_INVESTIGATOR_INDEX_BANANA + STARTDATE_DESC_BANANA,
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
                + TAG_DESC_MURDER + TAG_DESC_FRAUD, Date.MESSAGE_DATE_CONSTRAINTS);

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
```
###### /java/seedu/investigapptor/logic/parser/InvestigapptorParserTest.java
``` java
    @Test
    public void parseCommand_add() throws Exception {
        Investigator investigator = new InvestigatorBuilder().build();
        CrimeCase crimeCase = new CrimeCaseBuilder().withInvestigator(investigator).build();
        AddCaseCommand command = (AddCaseCommand)
                parser.parseCommand(CrimeCaseUtil.getAddCommand(crimeCase) + "i/" + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new AddCaseCommand(crimeCase.getCaseName(), crimeCase.getDescription(),
                INDEX_FIRST_PERSON, crimeCase.getStartDate(), crimeCase.getTags()), command);
    }

```
###### /java/seedu/investigapptor/logic/parser/InvestigapptorParserTest.java
``` java
    @Test
    public void parseCommand_addAlias() throws Exception {
        Investigator investigator = new InvestigatorBuilder().build();
        CrimeCase crimeCase = new CrimeCaseBuilder().withInvestigator(investigator).build();
        AddCaseCommand command = (AddCaseCommand)
                parser.parseCommand(CrimeCaseUtil.getAliasAddCommand(crimeCase)
                        + "i/" + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new AddCaseCommand(crimeCase.getCaseName(), crimeCase.getDescription(),
                INDEX_FIRST_PERSON, crimeCase.getStartDate(), crimeCase.getTags()), command);
    }

```
###### /java/seedu/investigapptor/storage/XmlAdaptedCrimeCaseTest.java
``` java
public class XmlAdaptedCrimeCaseTest {
    private static final String INVALID_NAME = "Project H@ppy";
    private static final String INVALID_DESCRIPTION = " ";
    private static final XmlAdaptedInvestigator INVALID_INVESTIGATOR =
            new XmlAdaptedInvestigator("R@chel", " ", " ", " ", " ",
                    BENSON.getCrimeCases(), BENSON.getTags().stream()
                    .map(XmlAdaptedTag::new)
                    .collect(Collectors.toList()));
    private static final String INVALID_DATE = "123/44/17";
    private static final String INVALID_STATUS = " ";
    private static final String INVALID_TAG = "#Corruption";

    private static final String VALID_NAME = ALFA.getCaseName().toString();
    private static final String VALID_DESCRIPTION = ALFA.getDescription().toString();
    private static final XmlAdaptedInvestigator VALID_INVESTIGATOR =
            new XmlAdaptedInvestigator(ALFA.getCurrentInvestigator());
    private static final String VALID_STARTDATE = ALFA.getStartDate().date;
    private static final String VALID_ENDDATE = ALFA.getEndDate().date;
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
                new XmlAdaptedCrimeCase(INVALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR,
                        VALID_STARTDATE, VALID_ENDDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = CaseName.MESSAGE_CASE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(null, VALID_DESCRIPTION, VALID_INVESTIGATOR,
                VALID_STARTDATE, VALID_ENDDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, CaseName.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, INVALID_DESCRIPTION, VALID_INVESTIGATOR, VALID_STARTDATE,
                        VALID_STATUS, VALID_ENDDATE, VALID_TAGS);
        String expectedMessage = Description.MESSAGE_DESCRIPTION_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, null, VALID_INVESTIGATOR,
                VALID_STARTDATE, VALID_ENDDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidInvestigator_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, INVALID_INVESTIGATOR, VALID_STARTDATE,
                        VALID_ENDDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullInvestigator_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, null,
                VALID_STARTDATE, VALID_ENDDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Person.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidStartDate_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR, INVALID_DATE,
                        VALID_ENDDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = Date.MESSAGE_DATE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidEndDate_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR, VALID_STARTDATE,
                        INVALID_DATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = Date.MESSAGE_DATE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullStartDate_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR,
                null, VALID_ENDDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullEndDate_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR,
                VALID_STARTDATE, null, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidStatus_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR, VALID_STARTDATE,
                        VALID_ENDDATE, INVALID_STATUS, VALID_TAGS);
        String expectedMessage = Status.MESSAGE_STATUS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullStatus_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR,
                VALID_STARTDATE, VALID_ENDDATE, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Status.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR, VALID_STARTDATE,
                        VALID_ENDDATE, VALID_STATUS, invalidTags);
        Assert.assertThrows(IllegalValueException.class, crimeCase::toModelType);
    }

}
```
