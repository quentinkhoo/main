# leowweiching-reused
###### /java/seedu/investigapptor/logic/commands/DeleteCaseCommand.java
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
###### /java/seedu/investigapptor/logic/commands/FindCaseCommand.java
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
###### /java/seedu/investigapptor/model/crimecase/CaseName.java
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
###### /java/seedu/investigapptor/model/crimecase/CrimeCase.java
``` java
/**
 * Represents a Crime Case in the Investigapptor.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class CrimeCase {

    private final CaseName name;
    private final Description description;
    private final Date startDate;
    private final Date endDate;
    private final Investigator currentInvestigator;
    private final Status status;

    private final UniqueTagList tags;

    /**
     * Every field must be present and not null
     */
    public CrimeCase(CaseName name, Description description, Investigator currentInvestigator,
                     Date startDate, Date endDate, Status status, Set<Tag> tags) {
        this.name = name;
        this.description = description;
        this.currentInvestigator = currentInvestigator;
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
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

    /**
     * Returns an immutable tag set of type String
     */
    public Set<String> getTagsRaw() {
        Set<String> rawTags = new HashSet<>();
        for (Tag s : tags) {
            rawTags.add(s.getRawString().toLowerCase());
        }

        return rawTags;
    }

    /**
     * Deletes (@code toDelete) tag
     */
    public void deleteTag(Tag toDelete) {
        tags.delete(toDelete);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof CrimeCase)) {
            return false;
        }

        CrimeCase otherCrimeCase = (CrimeCase) other;
        return otherCrimeCase.getCaseName().equals(this.getCaseName())
                && otherCrimeCase.getDescription().equals(this.getDescription())
                && otherCrimeCase.getCurrentInvestigator().equals(this.getCurrentInvestigator())
                && otherCrimeCase.getStartDate().equals(this.getStartDate());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, startDate, status, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getCaseName())
                .append(" Description: ")
                .append(getDescription())
                .append(" Current Investigator: ")
                .append(getCurrentInvestigator().getName())
                .append(" Status: ")
                .append(getStatus())
                .append(" Start Date: ")
                .append(getStartDate());

        if (getStatus().toString().equals(CASE_CLOSE)) {
            builder.append(" End Date: ")
                    .append(getEndDate());
        }

        builder.append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
```
###### /java/seedu/investigapptor/model/crimecase/Description.java
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
###### /java/seedu/investigapptor/model/crimecase/NameContainsKeywordsPredicate.java
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
###### /java/seedu/investigapptor/model/Investigapptor.java
``` java
    public void setCrimeCases(List<CrimeCase> cases) throws DuplicateCrimeCaseException {
        this.cases.setCrimeCases(cases);
    }

```
###### /java/seedu/investigapptor/model/Investigapptor.java
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
###### /java/seedu/investigapptor/model/Investigapptor.java
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
###### /java/seedu/investigapptor/model/Investigapptor.java
``` java
    @Override
    public ObservableList<CrimeCase> getCrimeCaseList() {
        return cases.asObservableList();
    }

```
###### /java/seedu/investigapptor/model/Model.java
``` java
    /** {@code Predicate} that always evaluate to true */
    Predicate<CrimeCase> PREDICATE_SHOW_ALL_CASES = unused -> true;

```
###### /java/seedu/investigapptor/model/Model.java
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
###### /java/seedu/investigapptor/model/Model.java
``` java
    /** Returns an unmodifiable view of the filtered case list */
    ObservableList<CrimeCase> getFilteredCrimeCaseList();

```
###### /java/seedu/investigapptor/model/Model.java
``` java
    /**
     * Updates the filter of the filtered case list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredCrimeCaseList(Predicate<CrimeCase> predicate);

```
###### /java/seedu/investigapptor/model/ModelManager.java
``` java
    @Override
    public synchronized void deleteCrimeCase(CrimeCase target) throws CrimeCaseNotFoundException {
        investigapptor.removeCrimeCase(target);
        indicateInvestigapptorChanged();
    }

```
###### /java/seedu/investigapptor/model/ModelManager.java
``` java
    @Override
    public synchronized void addCrimeCase(CrimeCase crimecase) throws DuplicateCrimeCaseException {
        investigapptor.addCrimeCase(crimecase);
        updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        indicateInvestigapptorChanged();
    }

```
###### /java/seedu/investigapptor/model/ModelManager.java
``` java
    @Override
    public void updateCrimeCase(CrimeCase target, CrimeCase editedCase)
            throws DuplicateCrimeCaseException, CrimeCaseNotFoundException {
        requireAllNonNull(target, editedCase);

        investigapptor.updateCrimeCase(target, editedCase);
        indicateInvestigapptorChanged();
    }

```
###### /java/seedu/investigapptor/model/ModelManager.java
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
###### /java/seedu/investigapptor/storage/XmlSerializableInvestigapptor.java
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
