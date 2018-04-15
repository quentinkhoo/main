# leowweiching-reused
###### \main\java\seedu\investigapptor\logic\commands\DeleteCaseCommand.java
``` java
/**
 * Deletes a case identified using it's last displayed index from the investigapptor book.
 */
public class DeleteCaseCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deletecase";
    public static final String COMMAND_ALIAS = "dc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the case identified by the index number used in the last listing of cases.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_CASE_SUCCESS = "Deleted Case: %1$s";

    private final Index targetIndex;

    private CrimeCase caseToDelete;

    public DeleteCaseCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(caseToDelete);
        try {
            model.deleteCrimeCase(caseToDelete);
            EventsCenter.getInstance().post(new SwapTabEvent(1));
        } catch (CrimeCaseNotFoundException pnfe) {
            throw new AssertionError("The target case cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_CASE_SUCCESS, caseToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<CrimeCase> lastShownList = model.getFilteredCrimeCaseList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
        }

        caseToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCaseCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteCaseCommand) other).targetIndex) // state check
                && Objects.equals(this.caseToDelete, ((DeleteCaseCommand) other).caseToDelete));
    }
}
```
###### \main\java\seedu\investigapptor\logic\commands\FindCaseCommand.java
``` java
/**
 * Finds and lists all cases in investigapptor book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCaseCommand extends Command {

    public static final String COMMAND_WORD = "findcases";
    public static final String COMMAND_ALIAS = "fc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all cases whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alpha bravo charlie";

    private final CaseNameContainsKeywordsPredicate predicate;

    public FindCaseCommand(CaseNameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredCrimeCaseList(predicate);
        EventsCenter.getInstance().post(new SwapTabEvent(1));
        return new CommandResult(getMessageForCrimeCaseListShownSummary(model.getFilteredCrimeCaseList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCaseCommand // instanceof handles nulls
                && this.predicate.equals(((FindCaseCommand) other).predicate)); // state check
    }
}
```
###### \main\java\seedu\investigapptor\model\crimecase\CaseName.java
``` java
import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.commons.util.AppUtil.checkArgument;

/**
 * Represents a CrimeCase's name in the Investigapptor.
 * Guarantees: immutable; is valid as declared in {@link #isValidCaseName(String)}
 */
public class CaseName {

    public static final String MESSAGE_CASE_NAME_CONSTRAINTS =
            "Crime case names should be alphanumeric, and not be blank";

    public static final String CASE_NAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String crimeCaseName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public CaseName(String name) {
        requireNonNull(name);
        checkArgument(isValidCaseName(name), MESSAGE_CASE_NAME_CONSTRAINTS);
        this.crimeCaseName = name;
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidCaseName(String test) {
        return test.matches(CASE_NAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return crimeCaseName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CaseName // instanceof handles nulls
                && this.crimeCaseName.equals(((CaseName) other).crimeCaseName)); // state check
    }

    @Override
    public int hashCode() {
        return crimeCaseName.hashCode();
    }
}
```
###### \main\java\seedu\investigapptor\model\crimecase\CrimeCase.java
``` java
/**
 * Represents a Crime Case in the Investigapptor.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class CrimeCase {

    private final CaseName name;
    private final Description description;
    private final StartDate startDate;
    private final EndDate endDate;
    private final Investigator currentInvestigator;
    private final Status status;

    private final UniqueTagList tags;

    /**
     * Every field must be present and not null
     */
    public CrimeCase(CaseName name, Description description, Investigator currentInvestigator,
                     StartDate startDate, EndDate endDate, Status status, Set<Tag> tags) {
        this.name = name;
        this.description = description;
        this.currentInvestigator = new Investigator(currentInvestigator.getName(), currentInvestigator.getPhone(),
        currentInvestigator.getEmail(), currentInvestigator.getAddress(), currentInvestigator.getRank(),
                currentInvestigator.getTags());
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.tags = new UniqueTagList(tags);
    }

    public CaseName getCaseName() {
        return name;
    }

    public Description getDescription() {
        return description;
    }

    public Investigator getCurrentInvestigator() {
        return currentInvestigator;
    }

    public StartDate getStartDate() {
        return startDate;
    }

    public EndDate getEndDate() {
        return endDate;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

```
###### \main\java\seedu\investigapptor\model\crimecase\Description.java
``` java
/**
 * Represents a Crime Case's description in the Investigator Application.
 * Guarantees: immutable; is valid as declared in {@link #isValidDescription(String)}
 */
public class Description {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Crime case descriptions can take any values, and it should not be blank";

    public static final String CASE_NAME_VALIDATION_REGEX = "[\\p{Graph}][\\p{Graph}]*";

    public final String description;

    /**
     * Constructs a {@code Description}.
     *
     * @param description A valid description.
     */
    public Description(String description) {
        requireNonNull(description);
        checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);
        this.description = description;
    }

    /**
     * Returns true if a given string is a valid case description.
     */
    public static boolean isValidDescription(String test) {
        return test.trim() != null && !test.trim().isEmpty();
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                && this.description.equals(((Description) other).description)); // state check
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
```
###### \main\java\seedu\investigapptor\model\crimecase\NameContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code CrimeCase}'s {@code CaseName} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate implements Predicate<CrimeCase> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(CrimeCase crimeCase) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(crimeCase.getCaseName().crimeCaseName, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((NameContainsKeywordsPredicate) other).keywords)); // state check
    }

}
```
###### \main\java\seedu\investigapptor\model\Investigapptor.java
``` java
    public void setCrimeCases(List<CrimeCase> cases) throws DuplicateCrimeCaseException {
        this.cases.setCrimeCases(cases);
    }

```
###### \main\java\seedu\investigapptor\model\Investigapptor.java
``` java
    /**
     * Adds a case to the investigapptor book.
     * Also checks the new case's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #tags}.
     *
     * @throws DuplicateCrimeCaseException if an equivalent case already exists.
     */
    public void addCrimeCase(CrimeCase c) throws DuplicateCrimeCaseException {
        CrimeCase crimeCase = syncWithMasterTagList(c);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any case
        // in the case list.
        if (cases.add(crimeCase)) {
            addCrimeCaseToInvestigator(crimeCase);
        }
    }

```
###### \main\java\seedu\investigapptor\model\Investigapptor.java
``` java
    /**
     * Removes {@code key} from this {@code Investigapptor}.
     *
     * @throws CrimeCaseNotFoundException if the {@code key} is not in this {@code Investigapptor}.
     */
    public boolean removeCrimeCase(CrimeCase key) throws CrimeCaseNotFoundException {
        if (cases.remove(key)) {
            removeCrimeCaseFromInvestigator(key);
            return true;
        } else {
            throw new CrimeCaseNotFoundException();
        }
    }

```
###### \main\java\seedu\investigapptor\model\Investigapptor.java
``` java
    @Override
    public ObservableList<CrimeCase> getCrimeCaseList() {
        return cases.asObservableList();
    }

```
###### \main\java\seedu\investigapptor\model\Model.java
``` java
    /** {@code Predicate} that always evaluate to true */
    Predicate<CrimeCase> PREDICATE_SHOW_ALL_CASES = unused -> true;

```
###### \main\java\seedu\investigapptor\model\Model.java
``` java
    /** Deletes the given case. */
    void deleteCrimeCase(CrimeCase target) throws CrimeCaseNotFoundException;
    /** Adds the given case */
    void addCrimeCase(CrimeCase crimecase) throws DuplicateCrimeCaseException;
    /**
     * Replaces the given case {@code target} with {@code editedCase}.
     *
     * @throws DuplicateCrimeCaseException if updating the crimecase's details causes the crimecase to be equivalent to
     *      another existing crimecase in the list.
     * @throws CrimeCaseNotFoundException if {@code target} could not be found in the list.
     */
    void updateCrimeCase(CrimeCase target, CrimeCase editedCrimeCase)
            throws DuplicateCrimeCaseException, CrimeCaseNotFoundException;

```
###### \main\java\seedu\investigapptor\model\Model.java
``` java
    /** Returns an unmodifiable view of the filtered case list */
    ObservableList<CrimeCase> getFilteredCrimeCaseList();

```
###### \main\java\seedu\investigapptor\model\Model.java
``` java
    /**
     * Updates the filter of the filtered case list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredCrimeCaseList(Predicate<CrimeCase> predicate);

```
###### \main\java\seedu\investigapptor\model\ModelManager.java
``` java
    @Override
    public synchronized void deleteCrimeCase(CrimeCase target) throws CrimeCaseNotFoundException {
        investigapptor.removeCrimeCase(target);
        indicateInvestigapptorChanged();
    }

```
###### \main\java\seedu\investigapptor\model\ModelManager.java
``` java
    @Override
    public synchronized void addCrimeCase(CrimeCase crimecase) throws DuplicateCrimeCaseException {
        investigapptor.addCrimeCase(crimecase);
        updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        indicateInvestigapptorChanged();
    }

```
###### \main\java\seedu\investigapptor\model\ModelManager.java
``` java
    @Override
    public void updateCrimeCase(CrimeCase target, CrimeCase editedCase)
            throws DuplicateCrimeCaseException, CrimeCaseNotFoundException {
        requireAllNonNull(target, editedCase);

        investigapptor.updateCrimeCase(target, editedCase);
        indicateInvestigapptorChanged();
    }

```
###### \main\java\seedu\investigapptor\model\ModelManager.java
``` java
    /**
     * Returns an unmodifiable view of the list of {@code CrimeCase} backed by the internal list of
     * {@code investigapptor}
     */
    @Override
    public ObservableList<CrimeCase> getFilteredCrimeCaseList() {
        return FXCollections.unmodifiableObservableList(filteredCrimeCases);
    }

    @Override
    public void updateFilteredCrimeCaseList(Predicate<CrimeCase> predicate) {
        requireNonNull(predicate);
        filteredCrimeCases.setPredicate(predicate);
        indicateFilteredCrimeCaseListChanged();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return investigapptor.equals(other.investigapptor)
                && filteredPersons.equals(other.filteredPersons)
                && filteredCrimeCases.equals(other.filteredCrimeCases);
    }

}
```
###### \main\java\seedu\investigapptor\storage\XmlSerializableInvestigapptor.java
``` java
/**
 * An Immutable Investigapptor that is serializable to XML format
 */
@XmlRootElement(name = "investigapptor")
public class XmlSerializableInvestigapptor {

    @XmlElement
    private List<XmlAdaptedCrimeCase> cases;
    @XmlElement
    private List<XmlAdaptedPerson> persons;
    @XmlElement
    private List<XmlAdaptedTag> tags;
    @XmlElement
    private List<XmlAdaptedInvestigator> investigators;
    @XmlElement
    private XmlAdaptedPassword password;

    /**
     * Creates an empty XmlSerializableInvestigapptor.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableInvestigapptor() {
        cases = new ArrayList<>();
        persons = new ArrayList<>();
        investigators = new ArrayList<>();
        tags = new ArrayList<>();
        password = new XmlAdaptedPassword();
    }

    /**
     * Conversion
     */
    public XmlSerializableInvestigapptor(ReadOnlyInvestigapptor src) {
        this();
        cases.addAll(src.getCrimeCaseList().stream().map(XmlAdaptedCrimeCase::new).collect(Collectors.toList()));
        persons.addAll(src.getPersonOnlyList().stream().map(XmlAdaptedPerson::new).collect(Collectors.toList()));
        investigators.addAll(src.getInvestigatorList().stream()
                .map(XmlAdaptedInvestigator::new).collect(Collectors.toList()));
        tags.addAll(src.getTagList().stream().map(XmlAdaptedTag::new).collect(Collectors.toList()));
        password = new XmlAdaptedPassword(src.getPassword());
    }


    /**
     * Converts this investigapptor into the model's {@code Investigapptor} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     *                               {@code XmlAdaptedCrimeCase}, {@code XmlAdaptedPerson} or {@code XmlAdaptedTag}.
     */
    public Investigapptor toModelType() throws IllegalValueException {
        Investigapptor investigapptor = new Investigapptor();
        for (XmlAdaptedTag t : tags) {
            investigapptor.addTag(t.toModelType());
        }
        for (XmlAdaptedCrimeCase c : cases) {
            investigapptor.addCrimeCase(c.toModelType());
        }
        for (XmlAdaptedPerson p : persons) {
            investigapptor.addPerson(p.toModelType());
        }
        for (XmlAdaptedInvestigator i : investigators) {
            Investigator investigator = i.toModelType();
            investigapptor.addPerson(investigator);

        }
        investigapptor.setPassword(password.toModelType());
        return investigapptor;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableInvestigapptor)) {
            return false;
        }

        XmlSerializableInvestigapptor otherAb = (XmlSerializableInvestigapptor) other;
        return cases.equals(otherAb.cases) && persons.equals(otherAb.persons) && tags.equals(otherAb.tags)
                && password.equals(otherAb.password);
    }
}
```
###### \test\java\seedu\investigapptor\logic\commands\AddCaseCommandIntegrationTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code AddCaseCommand}.
 */
public class AddCaseCommandIntegrationTest {

    private static Model model;

    @BeforeClass
    public static void setUp() {
        model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
    }

    @Test
    public void execute_newCrimeCase_success() throws Exception {
        CrimeCase validCrimeCase = new CrimeCaseBuilder().build();

        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.addCrimeCase(validCrimeCase);

        assertCommandSuccess(prepareCommand(new CrimeCaseBuilder().build(), model), model,
                String.format(AddCaseCommand.MESSAGE_SUCCESS, validCrimeCase), expectedModel);
    }

    @Test
    public void execute_duplicateCrimeCase_throwsCommandException() {
        CrimeCase crimeCaseInList = model.getInvestigapptor().getCrimeCaseList().get(0);
        assertCommandFailure(prepareCommand(crimeCaseInList, model), model,
                AddCaseCommand.MESSAGE_DUPLICATE_CASE);
    }

    /**
     * Generates a new {@code AddCaseCommand} which upon execution,
     * adds {@code crimeCase} into the {@code model}.
     */
    private AddCaseCommand prepareCommand(CrimeCase crimeCase, Model model) {
        AddCaseCommand command = new AddCaseCommand(crimeCase);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \test\java\seedu\investigapptor\logic\commands\AddCaseCommandTest.java
``` java
public class AddCaseCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullCrimeCase_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCaseCommand(null);
    }

    @Test
    public void execute_crimeCaseAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingCrimeCaseAdded modelStub = new ModelStubAcceptingCrimeCaseAdded();
        CrimeCase validCrimeCase = new CrimeCaseBuilder().build();

        CommandResult commandResult = getAddCommandForCrimeCase(validCrimeCase, modelStub).execute();

        assertEquals(String.format(AddCaseCommand.MESSAGE_SUCCESS, validCrimeCase),
                commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validCrimeCase), modelStub.crimeCasesAdded);
    }

    @Test
    public void execute_duplicateCrimeCase_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateCrimeCaseException();
        CrimeCase validCrimeCase = new CrimeCaseBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCaseCommand.MESSAGE_DUPLICATE_CASE);

        getAddCommandForCrimeCase(validCrimeCase, modelStub).execute();
    }

    @Test
    public void equals() {
        CrimeCase projHappy = new CrimeCaseBuilder().withName("Project Happy").build();
        CrimeCase projSad = new CrimeCaseBuilder().withName("Project Sad").build();
        AddCaseCommand addProjHappyCommand = new AddCaseCommand(projHappy);
        AddCaseCommand addProjSadCommand = new AddCaseCommand(projSad);

        // same object -> returns true
        assertTrue(addProjHappyCommand.equals(addProjHappyCommand));

        // same values -> returns true
        AddCaseCommand addProjHappyCommandCopy = new AddCaseCommand(projHappy);
        assertTrue(addProjHappyCommand.equals(addProjHappyCommandCopy));

        // different types -> returns false
        assertFalse(addProjHappyCommand.equals(1));

        // null -> returns false
        assertFalse(addProjHappyCommand.equals(null));

        // different case -> returns false
        assertFalse(addProjHappyCommand.equals(addProjSadCommand));
    }

    /**
     * Generates a new AddCaseCommand with the details of the given case.
     */
    private AddCaseCommand getAddCommandForCrimeCase(CrimeCase crimeCase, Model model) {
        AddCaseCommand command = new AddCaseCommand(crimeCase);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyInvestigapptor newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyInvestigapptor getInvestigapptor() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(Person target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void addCrimeCase(CrimeCase crimecase)
                throws DuplicateCrimeCaseException {
            fail("This method should not be called.");
        }

        @Override
        public void updateCrimeCase(CrimeCase target, CrimeCase editedCase)
                throws DuplicateCrimeCaseException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteCrimeCase(CrimeCase target)
            throws CrimeCaseNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteTag(Tag toDelete)
                throws TagNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void backUpInvestigapptor(String fileName) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<CrimeCase> getFilteredCrimeCaseList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredCrimeCaseList(Predicate<CrimeCase> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updatePassword(Password password) {
            fail("This method should not be called");
        }

        @Override
        public void removePassword() {
            fail("This method should not be called");
        }
    }

    /**
     * A Model stub that always throw a DuplicateCrimeCaseException when trying to add a case.
     */
    private class ModelStubThrowingDuplicateCrimeCaseException extends ModelStub {
        @Override
        public void addCrimeCase(CrimeCase crimeCase) throws DuplicateCrimeCaseException {
            throw new DuplicateCrimeCaseException();
        }

        @Override
        public ReadOnlyInvestigapptor getInvestigapptor() {
            return new Investigapptor();
        }
    }

    /**
     * A Model stub that always accept the case being added.
     */
    private class ModelStubAcceptingCrimeCaseAdded extends ModelStub {
        final ArrayList<CrimeCase> crimeCasesAdded = new ArrayList<>();

        @Override
        public void addCrimeCase(CrimeCase crimeCase) throws DuplicateCrimeCaseException {
            requireNonNull(crimeCase);
            crimeCasesAdded.add(crimeCase);
        }

        @Override
        public ReadOnlyInvestigapptor getInvestigapptor() {
            return new Investigapptor();
        }
    }

}
```
###### \test\java\seedu\investigapptor\logic\commands\DeleteCaseCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCaseCommand}.
 */
public class DeleteCaseCommandTest {

    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);

        String expectedMessage = String.format(DeleteCaseCommand.MESSAGE_DELETE_CASE_SUCCESS, crimeCaseToDelete);

        ModelManager expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.deleteCrimeCase(crimeCaseToDelete);

        assertCommandSuccess(deleteCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        DeleteCaseCommand deleteCaseCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);

        String expectedMessage = String.format(DeleteCaseCommand.MESSAGE_DELETE_CASE_SUCCESS, crimeCaseToDelete);

        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());
        expectedModel.deleteCrimeCase(crimeCaseToDelete);
        showNoCrimeCase(expectedModel);

        assertCommandSuccess(deleteCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        Index outOfBoundIndex = INDEX_SECOND_CASE;
        // ensures that outOfBoundIndex is still in bounds of investigapptor book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getInvestigapptor().getCrimeCaseList().size());

        DeleteCaseCommand deleteCaseCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);
        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());

        // delete -> first crimeCase deleted
        deleteCaseCommand.execute();
        undoRedoStack.push(deleteCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first crimeCase deleted again
        expectedModel.deleteCrimeCase(crimeCaseToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        DeleteCaseCommand deleteCaseCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteCaseCommand not pushed into undoRedoStack
        assertCommandFailure(deleteCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code CrimeCase} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted crimeCase in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the crimeCase object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameCrimeCaseDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        DeleteCaseCommand deleteCaseCommand = prepareCommand(INDEX_FIRST_CASE);
        Model expectedModel = new ModelManager(model.getInvestigapptor(), new UserPrefs());

        showCrimeCaseAtIndex(model, INDEX_SECOND_CASE);
        CrimeCase crimeCaseToDelete = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        // delete -> deletes second crimeCase in unfiltered crimeCase list / first crimeCase in filtered crimeCase list
        deleteCaseCommand.execute();
        undoRedoStack.push(deleteCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.deleteCrimeCase(crimeCaseToDelete);
        assertNotEquals(crimeCaseToDelete, model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased()));
        // redo -> deletes same second crimeCase in unfiltered crimeCase list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        DeleteCaseCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_CASE);
        DeleteCaseCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_CASE);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCaseCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_CASE);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different crimeCase -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteCaseCommand} with the parameter {@code index}.
     */
    private DeleteCaseCommand prepareCommand(Index index) {
        DeleteCaseCommand deleteCaseCommand = new DeleteCaseCommand(index);
        deleteCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCaseCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoCrimeCase(Model model) {
        model.updateFilteredCrimeCaseList(p -> false);

        assertTrue(model.getFilteredCrimeCaseList().isEmpty());
    }
}
```
###### \test\java\seedu\investigapptor\logic\commands\EditCaseCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for EditCaseCommand.
 */
public class EditCaseCommandTest {

    private Model model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        CrimeCase editedCrimeCase = new CrimeCaseBuilder().build();
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(editedCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_SECOND_CASE, descriptor);

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(model.getFilteredCrimeCaseList().get(1), editedCrimeCase);

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastCrimeCase = Index.fromOneBased(model.getFilteredCrimeCaseList().size());
        CrimeCase lastCrimeCase = model.getFilteredCrimeCaseList().get(indexLastCrimeCase.getZeroBased());

        CrimeCaseBuilder crimeCaseInList = new CrimeCaseBuilder(lastCrimeCase);
        CrimeCase editedCrimeCase = crimeCaseInList.withName(VALID_CASENAME_BANANA)
                .withDescription(VALID_DESCRIPTION_BANANA)
                .withTags(VALID_TAG_MURDER).build();

        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_BANANA)
                .withDescription(VALID_DESCRIPTION_BANANA).withTags(VALID_TAG_MURDER).build();
        EditCaseCommand editCaseCommand = prepareCommand(indexLastCrimeCase, descriptor);

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(lastCrimeCase, editedCrimeCase);

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE,
                new EditCrimeCaseDescriptor());
        CrimeCase editedCrimeCase = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        CrimeCase crimeCaseInFilteredList = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        CrimeCase editedCrimeCase = new CrimeCaseBuilder(crimeCaseInFilteredList)
                .withName(VALID_CASENAME_BANANA).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE,
                new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_BANANA).build());

        String expectedMessage = String.format(EditCaseCommand.MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase);

        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());
        expectedModel.updateCrimeCase(model.getFilteredCrimeCaseList().get(0), editedCrimeCase);

        assertCommandSuccess(editCaseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateCrimeCaseUnfilteredList_failure() {
        CrimeCase firstCrimeCase = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(firstCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_SECOND_CASE, descriptor);

        assertCommandFailure(editCaseCommand, model, EditCaseCommand.MESSAGE_DUPLICATE_CASE);
    }

    @Test
    public void execute_duplicateCrimeCaseFilteredList_failure() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        // edit crimeCase in filtered list into a duplicate in investigapptor book
        CrimeCase crimeCaseInList = model.getInvestigapptor().getCrimeCaseList().get(INDEX_SECOND_CASE.getZeroBased());
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE,
                new EditCrimeCaseDescriptorBuilder(crimeCaseInList).build());

        assertCommandFailure(editCaseCommand, model, EditCaseCommand.MESSAGE_DUPLICATE_CASE);
    }

    @Test
    public void execute_invalidCrimeCaseIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withCaseName(VALID_CASENAME_BANANA).build();
        EditCaseCommand editCaseCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of investigapptor book
     */
    @Test
    public void execute_invalidCrimeCaseIndexFilteredList_failure() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);
        Index outOfBoundIndex = INDEX_SECOND_CASE;
        // ensures that outOfBoundIndex is still in bounds of investigapptor book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getInvestigapptor().getCrimeCaseList().size());

        EditCaseCommand editCaseCommand = prepareCommand(outOfBoundIndex,
                new EditCrimeCaseDescriptorBuilder().withCaseName(VALID_CASENAME_BANANA).build());

        assertCommandFailure(editCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CrimeCase editedCrimeCase = new CrimeCaseBuilder().build();
        CrimeCase crimeCaseToEdit = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(editedCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE, descriptor);
        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        // edit -> first crimeCase edited
        editCaseCommand.execute();
        undoRedoStack.push(editCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first crimeCase edited again
        expectedModel.updateCrimeCase(crimeCaseToEdit, editedCrimeCase);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder()
                .withCaseName(VALID_CASENAME_BANANA).build();
        EditCaseCommand editCaseCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> editCaseCommand not pushed into undoRedoStack
        assertCommandFailure(editCaseCommand, model, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code CrimeCase} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited crimeCase in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the crimeCase object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameCrimeCaseEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        CrimeCase editedCrimeCase = new CrimeCaseBuilder().build();
        EditCrimeCaseDescriptor descriptor = new EditCrimeCaseDescriptorBuilder(editedCrimeCase).build();
        EditCaseCommand editCaseCommand = prepareCommand(INDEX_FIRST_CASE, descriptor);
        Model expectedModel = new ModelManager(new Investigapptor(model.getInvestigapptor()), new UserPrefs());

        showCrimeCaseAtIndex(model, INDEX_SECOND_CASE);
        CrimeCase crimeCaseToEdit = model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased());
        // edit -> edits second crimeCase in unfiltered crimeCase list / first crimeCase in filtered crimeCase list
        editCaseCommand.execute();
        undoRedoStack.push(editCaseCommand);

        // undo -> reverts investigapptor back to previous state and filtered crimeCase list to show all crimeCases
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateCrimeCase(crimeCaseToEdit, editedCrimeCase);
        assertNotEquals(model.getFilteredCrimeCaseList().get(INDEX_FIRST_CASE.getZeroBased()), crimeCaseToEdit);
        // redo -> edits same second crimeCase in unfiltered crimeCase list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        final EditCaseCommand standardCommand = prepareCommand(INDEX_FIRST_CASE, DESC_APPLE);

        // same values -> returns true
        EditCrimeCaseDescriptor copyDescriptor = new EditCrimeCaseDescriptor(DESC_APPLE);
        EditCaseCommand commandWithSameValues = prepareCommand(INDEX_FIRST_CASE, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCaseCommand(INDEX_SECOND_CASE, DESC_APPLE)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCaseCommand(INDEX_FIRST_CASE, DESC_BANANA)));
    }

    /**
     * Returns an {@code EditCaseCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCaseCommand prepareCommand(Index index, EditCrimeCaseDescriptor descriptor) {
        EditCaseCommand editCaseCommand = new EditCaseCommand(index, descriptor);
        editCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editCaseCommand;
    }
}
```
###### \test\java\seedu\investigapptor\logic\commands\FindCaseCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code FindCaseCommand}.
 */
public class FindCaseCommandTest {
    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
    }

    @Test
    public void equals() {
        CaseNameContainsKeywordsPredicate firstPredicate =
                new CaseNameContainsKeywordsPredicate(Collections.singletonList("first"));
        CaseNameContainsKeywordsPredicate secondPredicate =
                new CaseNameContainsKeywordsPredicate(Collections.singletonList("second"));

        FindCaseCommand findFirstCommand = new FindCaseCommand(firstPredicate);
        FindCaseCommand findSecondCommand = new FindCaseCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCaseCommand findFirstCommandCopy = new FindCaseCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different case -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noCrimeCaseFound() {
        String expectedMessage = String.format(MESSAGE_CASES_LISTED_OVERVIEW, 0);
        FindCaseCommand command = prepareCommand(" ");
        assertCommandSuccess(command, expectedMessage, Collections.emptyList());
    }

    @Test
    public void execute_multipleKeywords_multipleCrimeCasesFound() {
        String expectedMessage = String.format(MESSAGE_CASES_LISTED_OVERVIEW, 3);
        FindCaseCommand command = prepareCommand("Charlie Echo Foxtrot");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CHARLIE, ECHO, FOXTROT));
    }

    /**
     * Parses {@code userInput} into a {@code FindCaseCommand}.
     */
    private FindCaseCommand prepareCommand(String userInput) {
        FindCaseCommand command =
                new FindCaseCommand(new CaseNameContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+"))));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<CrimeCase>} is equal to {@code expectedList}<br>
     *     - the {@code Investigapptor} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindCaseCommand command, String expectedMessage, List<CrimeCase> expectedList) {
        Investigapptor expectedInvestigapptor = new Investigapptor(model.getInvestigapptor());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredCrimeCaseList());
        assertEquals(expectedInvestigapptor, model.getInvestigapptor());
    }
}
```
###### \test\java\seedu\investigapptor\logic\parser\DeleteCaseCommandParserTest.java
``` java
/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCaseCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCaseCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCaseCommandParserTest {

    private DeleteCaseCommandParser parser = new DeleteCaseCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCaseCommand(INDEX_FIRST_CASE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteCaseCommand.MESSAGE_USAGE));
    }
}
```
###### \test\java\seedu\investigapptor\logic\parser\EditCaseCommandParserTest.java
``` java
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
                StartDate.MESSAGE_DATE_CONSTRAINTS); // invalid address
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
```
###### \test\java\seedu\investigapptor\logic\parser\InvestigapptorParserTest.java
``` java
    @Test
    public void parseCommand_findCase() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCaseCommand command = (FindCaseCommand) parser.parseCommand(
                FindCaseCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCaseCommand(new CaseNameContainsKeywordsPredicate(keywords)), command);
    }

```
###### \test\java\seedu\investigapptor\logic\parser\InvestigapptorParserTest.java
``` java
    @Test
    public void parseCommand_findCaseAlias() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCaseCommand command = (FindCaseCommand) parser.parseCommand(
                FindCaseCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCaseCommand(new CaseNameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findInvestigator() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindInvestigatorCommand command = (FindInvestigatorCommand) parser.parseCommand(
                FindInvestigatorCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindInvestigatorCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findInvestigatorAlias() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindInvestigatorCommand command = (FindInvestigatorCommand) parser.parseCommand(
                FindInvestigatorCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindInvestigatorCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_helpAlias() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_ALIAS) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_ALIAS + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_historyAlias() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_listInvestigators() throws Exception {
        assertTrue(parser.parseCommand("listinvestigators") instanceof ListInvestigatorCommand);
        assertTrue(parser.parseCommand(ListInvestigatorCommand.COMMAND_WORD) instanceof ListInvestigatorCommand);
    }

    @Test
    public void parseCommand_listInvestigatorsAlias() throws Exception {
        assertTrue(parser.parseCommand("li") instanceof ListInvestigatorCommand);
        assertTrue(parser.parseCommand(ListInvestigatorCommand.COMMAND_ALIAS) instanceof ListInvestigatorCommand);
    }

    @Test
    public void parseCommand_listCases() throws Exception {
        assertTrue(parser.parseCommand("listcases") instanceof ListCaseCommand);
        assertTrue(parser.parseCommand(ListCaseCommand.COMMAND_WORD) instanceof ListCaseCommand);
    }

    @Test
    public void parseCommand_listCasesAlias() throws Exception {
        assertTrue(parser.parseCommand("lc") instanceof ListCaseCommand);
        assertTrue(parser.parseCommand(ListCaseCommand.COMMAND_ALIAS) instanceof ListCaseCommand);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectInvestigatorCommand command = (SelectInvestigatorCommand) parser.parseCommand(
                SelectInvestigatorCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectInvestigatorCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_selectAlias() throws Exception {
        SelectInvestigatorCommand command = (SelectInvestigatorCommand) parser.parseCommand(
                SelectInvestigatorCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectInvestigatorCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_redoCommandAlias_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_undoCommandAlias_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_ALIAS) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }

```
###### \test\java\seedu\investigapptor\storage\XmlSerializableInvestigapptorTest.java
``` java
    @Test
    public void toModelType_invalidCrimeCaseFile_throwsIllegalValueException() throws Exception {
        XmlSerializableInvestigapptor dataFromFile = XmlUtil.getDataFromFile(INVALID_CRIMECASE_FILE,
                XmlSerializableInvestigapptor.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

```
