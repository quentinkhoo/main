# Marcus-cxc
###### \java\seedu\investigapptor\logic\commands\ListInvestigatorCaseCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListInvestigatorCaseCommandTest {

    private Model model = new ModelManager(TypicalInvestigator.getTypicalInvestigapptor(), new UserPrefs());
    private ListInvestigatorCaseCommand listInvestigatorCaseCommand;

    @Test
    public void equals() {
        ListInvestigatorCaseCommand findFirstCommand = new ListInvestigatorCaseCommand(INDEX_FIRST_PERSON);
        ListInvestigatorCaseCommand findSecondCommand = new ListInvestigatorCaseCommand(INDEX_SECOND_PERSON);
        findFirstCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        findSecondCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        try {
            findFirstCommand.execute();
        } catch (CommandException e) {
            throw new AssertionError("First command execute fail");
        }
        try {
            findSecondCommand.execute();
        } catch (CommandException e) {
            throw new AssertionError("Second command execute fail");
        }

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        ListInvestigatorCaseCommand findFirstCommandCopy = new ListInvestigatorCaseCommand(INDEX_FIRST_PERSON);
        findFirstCommandCopy.setData(model, new CommandHistory(), new UndoRedoStack());
        try {
            findFirstCommandCopy.execute();
        } catch (CommandException e) {
            throw new AssertionError("findFirstCommandCopy execute fail");
        }

        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }
    @Test
    public void execute_listInvestigatorCrimeCases_showsSameList() {
        listInvestigatorCaseCommand = new ListInvestigatorCaseCommand(INDEX_FIRST_PERSON);
        listInvestigatorCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        List<CrimeCase> expected = new ArrayList<>();
        expected.add(new CrimeCaseBuilder().withName("Omega").withInvestigator(ALICE).build());
        expected.add(new CrimeCaseBuilder().withName("Stigma").withInvestigator(ALICE).build());
        try {
            assertCommandSuccess(listInvestigatorCaseCommand,
                    String.format(Messages.MESSAGE_CASES_LISTED_OVERVIEW,
                            2), expected);
        } catch (CommandException e) {
            throw new AssertionError("Error");
        }
    }

    @Test
    public void execute_listInvestigatorCrimeCases_emptyResult() {
        listInvestigatorCaseCommand = new ListInvestigatorCaseCommand(INDEX_THIRD_PERSON);
        listInvestigatorCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        List<CrimeCase> expected = new ArrayList<>();
        try {
            assertCommandSuccess(listInvestigatorCaseCommand,
                    String.format(Messages.MESSAGE_CASES_LISTED_OVERVIEW,
                            0), expected);
        } catch (CommandException e) {
            throw new AssertionError("Error");
        }
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     * - the command feedback is equal to {@code expectedMessage}<br>
     * - the {@code FilteredList<CrimeCase>} is equal to {@code expectedList}<br>
     * - the {@code Investigapptor} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(ListInvestigatorCaseCommand command, String expectedMessage,
                                      List<CrimeCase> expectedList) throws CommandException {
        Investigapptor expectedInvestigapptor = new Investigapptor(model.getInvestigapptor());

        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredCrimeCaseList());
        assertEquals(expectedInvestigapptor, model.getInvestigapptor());
    }
}
```
###### \java\seedu\investigapptor\logic\parser\AddInvestigatorCommandParserTest.java
``` java
public class AddInvestigatorCommandParserTest {
    private AddInvestigatorCommandParser parser = new AddInvestigatorCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Investigator expectedInvestigator = new InvestigatorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withRank(VALID_RANK_INSPECTOR).withTags(VALID_TAG_FRIEND).build();

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
                .withRank(VALID_RANK_INSPECTOR).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + RANK_DESC_CAP + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddInvestigatorCommand(expectedInvestigatorMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Investigator expectedInvestigator = new InvestigatorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withRank(VALID_RANK_INSPECTOR).withTags().build();
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
```
###### \java\seedu\investigapptor\model\person\RankTest.java
``` java
public class RankTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Rank(null));
    }

    @Test
    public void constructor_invalidRank_throwsIllegalArgumentException() {
        String invalidRank = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Rank(invalidRank));
    }

    @Test
    public void isValidRank() {
        // null rank
        Assert.assertThrows(NullPointerException.class, () -> Rank.isValidRank(null));

        // blank rank
        assertFalse(Rank.isValidRank("")); // empty string
        assertFalse(Rank.isValidRank(" ")); // spaces only

        // invalid parts
        assertFalse(Rank.isValidRank("-5")); // negative value
        assertFalse(Rank.isValidRank("6")); // greater than 5
        assertFalse(Rank.isValidRank("0")); // less than 1
        // valid rank
        assertTrue(Rank.isValidRank("1"));
        assertTrue(Rank.isValidRank("2"));
        assertTrue(Rank.isValidRank("3"));
        assertTrue(Rank.isValidRank("4"));
        assertTrue(Rank.isValidRank("5"));

    }
}
```
###### \java\seedu\investigapptor\storage\XmlAdaptedInvestigatorTest.java
``` java
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
```
###### \java\seedu\investigapptor\testutil\InvestigatorBuilder.java
``` java
/**
 * A utility class to help with building Investigator objects.
 */
public class InvestigatorBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_RANK = "3";
    public static final String DEFAULT_TAGS = "friends";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Rank rank;
    private UniqueCrimeCaseList caseList;
    private Set<Tag> tags;

    public InvestigatorBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        rank = new Rank(DEFAULT_RANK);
        tags = SampleDataUtil.getTagSet(DEFAULT_TAGS);
        caseList = new UniqueCrimeCaseList();
    }

    /**
     * Initializes the InvestigatorBuilder with the data of {@code personToCopy}.
     */
    public InvestigatorBuilder(Investigator investigatorToCopy) {
        name = investigatorToCopy.getName();
        phone = investigatorToCopy.getPhone();
        email = investigatorToCopy.getEmail();
        address = investigatorToCopy.getAddress();
        tags = new HashSet<>(investigatorToCopy.getTags());
        caseList = new UniqueCrimeCaseList(investigatorToCopy.getCaseAsSet());
    }

    /**
     * Sets the {@code Name} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder withRank(String rank) {
        this.rank = new Rank(rank);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Investigator} that we are building.
     */
    public InvestigatorBuilder addCase(CrimeCase crimeCase) {
        try {
            CrimeCase c = new CrimeCase(crimeCase.getCaseName(), crimeCase.getDescription(),
                    new Investigator(name, phone, email, address, rank, tags), crimeCase.getStartDate(),
                    crimeCase.getEndDate(), crimeCase.getStatus(), crimeCase.getTags());
            caseList.add(crimeCase);
        } catch (DuplicateCrimeCaseException e) {
            throw new AssertionError("not possible");
        }
        return this;
    }

    public Investigator build() {
        return new Investigator(name, phone, email, address, rank, caseList.toSet(), tags);
    }

}
```
###### \java\seedu\investigapptor\testutil\TypicalInvestigator.java
``` java
    private TypicalInvestigator() {
    } // prevents instantiation

    /**
     * Returns an {@code Investigapptor} with all the typical persons.
     */
    public static Investigapptor getTypicalInvestigapptor() {
        Investigapptor ia = new Investigapptor();
        for (CrimeCase c : getCrimeCase()) {
            try {
                ia.addCrimeCase(c);
            } catch (DuplicateCrimeCaseException e) {
                throw new AssertionError("not possible");
            }
        }
        for (Investigator investigator : getTypicalInvestigators()) {
            try {
                ia.addPerson(investigator);
            } catch (DuplicatePersonException e) {
                throw new AssertionError("not possible");
            }
        }
        return ia;
    }

    public static List<CrimeCase> getCrimeCase() {
        ArrayList<CrimeCase> list = new ArrayList<>();
        list.add(new CrimeCaseBuilder().withName("Omega").withInvestigator(ALICE).build());
        list.add(new CrimeCaseBuilder().withName("Stigma").withInvestigator(ALICE).build());
        return list;
    }

    public static List<Investigator> getTypicalInvestigators() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
```
