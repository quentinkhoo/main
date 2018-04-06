# Marcus-cxc
###### \java\seedu\investigapptor\commons\events\model\InvestigapptorBackupEvent.java
``` java
/** Indicates the Investigapptor in the model has changed*/
public class InvestigapptorBackupEvent extends BaseEvent {

    public final ReadOnlyInvestigapptor data;
    public final String fileName;

    public InvestigapptorBackupEvent(ReadOnlyInvestigapptor data, String fileName) {
        this.data = data;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getPersonList().size() + ", number of tags " + data.getTagList().size();
    }
}
```
###### \java\seedu\investigapptor\logic\commands\BackupCommand.java
``` java
/**
 * Creates a xml copy of the current state of the investigapptor
 * The name of the xml is given by the user
 */
public class BackupCommand extends Command {

    public static final String COMMAND_WORD = "backup";
    public static final String COMMAND_ALIAS = "bu";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Create a backup of the current state "
            + "The backup will be saved as the given name\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " February";

    public static final String MESSAGE_SUCCESS = " backup has been created";
    private final String fileName;

    public BackupCommand(String fileName) {
        requireNonNull(fileName);
        this.fileName = fileName;
    }

    @Override
    public CommandResult execute() {
        requireNonNull(fileName);
        model.backUpInvestigapptor(fileName);
        return new CommandResult(fileName + MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof BackupCommand // instanceof handles nulls
                && this.fileName.equals(((BackupCommand) other).fileName)); // state check
    }
}
```
###### \java\seedu\investigapptor\logic\commands\ListInvestigatorCaseCommand.java
``` java
/**
 * Lists all investigators in the investigapptor book to the user.
 */
public class ListInvestigatorCaseCommand extends Command {


    public static final String COMMAND_WORD = "listinvestigatorcases";
    public static final String COMMAND_ALIAS = "lic";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all cases of the selected investigator "
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Listed all %1$s";
    private final Index targetIndex;
    private Investigator investigator;

    private CaseContainsInvestigatorPredicate predicate;

    public ListInvestigatorCaseCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (targetIndex != null) {
            List<Person> lastShownList = model.getFilteredPersonList();

            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
            }
            investigator = (Investigator) lastShownList.get(targetIndex.getZeroBased());
            requireNonNull(investigator);
            predicate = new CaseContainsInvestigatorPredicate(investigator.hashCode());
        }
        model.updateFilteredCrimeCaseList(predicate);
        EventsCenter.getInstance().post(new SwapTabEvent(1));
        return new CommandResult(getMessageForCrimeCaseListShownSummary(model.getFilteredCrimeCaseList().size()));
    }



}
```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    /**
     * Converts {@code key} hashcode list of cases into CrimeCase object
     *
     *
     */
    public void convertHashToCases(Investigator key) {
        requireNonNull(key.getCaseListHashed());
        if (key.getCaseListHashed() != null) {
            for (Integer i : key.getCaseListHashed()) {
                for (CrimeCase c : cases) {
                    if (c.hashCode() == i) {
                        try {
                            key.addCrimeCase(c);
                        } catch (DuplicateCrimeCaseException e) {
                            throw new AssertionError("Not possible, duplicate case while retrieving from xml");
                        }
                    }
                }
            }
        }
    }
    //// case-level operations
```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    /**
     * Deletes {@code Investigapptor} from all person and UniqueTagList
     */
    public void deleteTag(Tag toDelete) throws TagNotFoundException {
        if (tags.contains(toDelete)) {
            tags.delete(toDelete);
            persons.deleteTagFromPersons(toDelete);
            cases.deleteTagFromCrimeCases(toDelete);
        } else {
            throw new TagNotFoundException();
        }
    }

    /**
     * Updates the master tag list to include tags in {@code person} that are not in the list.
     *
     * @return a copy of this {@code person} such that every tag in this person points to a Tag object in the master
     * list.
     */
    private Person syncWithMasterTagList(Person person) {
        final UniqueTagList personTags = new UniqueTagList(person.getTags());
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        if (person instanceof Investigator) {
            Investigator inv = new Investigator(person.getName(), person.getPhone(), person.getEmail(),
                    person.getAddress(), ((Investigator) person).getRank(),
                    correctTagReferences, ((Investigator) person).getCaseListHashed());
            convertHashToCases(inv);
            return inv;
        }
        return new Person(
                person.getName(), person.getPhone(), person.getEmail(), person.getAddress(), correctTagReferences);
    }
```
###### \java\seedu\investigapptor\model\person\investigator\Investigator.java
``` java
/**
 * Represents a Investigator in the investigapptor book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Investigator extends Person {

    private UniqueCrimeCaseList crimeCases;
    private Rank rank;
    private ArrayList<Integer> caseListHashed;
    /**
     * Every field must be present and not null.
     */
    public Investigator(Name name, Phone phone, Email email, Address address, Rank rank, Set<Tag> tags) {
        super(name, phone, email, address, tags);
        this.rank = rank;
        crimeCases = new UniqueCrimeCaseList();
        caseListHashed = new ArrayList<>();
    }
    public Investigator(Name name, Phone phone, Email email, Address address, Rank rank,
                        Set<CrimeCase> cases, Set<Tag> tags) {
        super(name, phone, email, address, tags);
        this.rank = rank;
        crimeCases = new UniqueCrimeCaseList(cases);
        caseListHashed = new ArrayList<>();
        for (CrimeCase c : cases) {
            caseListHashed.add(c.hashCode());
        }
    }
    public Investigator(Name name, Phone phone, Email email, Address address, Rank rank,
                        Set<Tag> tags, ArrayList<Integer> caseListHashed) {
        super(name, phone, email, address, tags);
        this.rank = rank;
        crimeCases = new UniqueCrimeCaseList();
        this.caseListHashed = new ArrayList<>(caseListHashed);
    }
    /**
     * Add CrimeCase to list
     * Add the CrimeCase hashcode as well
     */
    public void addCrimeCase(CrimeCase caseToAdd) throws DuplicateCrimeCaseException {
        crimeCases.add(caseToAdd);
        if (!caseListHashed.contains(caseToAdd.hashCode())) {
            caseListHashed.add(caseToAdd.hashCode());
        }
    }
    /**
     * Returns an immutable CrimeCase set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<CrimeCase> getCaseAsSet() {
        return Collections.unmodifiableSet(crimeCases.toSet());
    }
    /**
     * Increase the investigator rank by one
     */
    public void promote() throws Exception {
        rank.promote();
    }
    /**
     * Decrease the investigator rank by one
     */
    public void demote() throws Exception {
        rank.demote();
    }
    /**
     * Returns rank in string
     */
    public Rank getRank() {
        return rank;
    }

    @Override
    public boolean isInvestigator() {
        return true;
    }
    /**
     * Returns an immutable crime case set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public ObservableList<CrimeCase> getCrimeCases() {
        return crimeCases.asObservableList();
    }

    /**
     * Returns true if empty
     * else if not empty
     */
    public boolean emptyList() {
        if (getCrimeCases().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Remove CrimeCase from list
     * Remove the CrimeCase hashcode as well
     */
    public void removeCrimeCase(CrimeCase caseToRemove) throws CrimeCaseNotFoundException {
        crimeCases.remove(caseToRemove);
        caseListHashed.remove((Integer) caseToRemove.hashCode());
    }

    public void clearCaseList() {
        crimeCases.removeAll();
    }

    public ArrayList<Integer> getCaseListHashed() {
        return caseListHashed;
    }
    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, rank, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Address: ")
                .append(getAddress())
                .append(" Rank: ")
                .append(getRank())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        builder.append(" CrimeCases: ");
        getCrimeCases().forEach(builder::append);
        return builder.toString();
    }
}
```
###### \java\seedu\investigapptor\model\person\investigator\Rank.java
``` java
/**
 * Represents an Investigator's rank in the investigapptor book.
 * Guarantees: immutable; is valid as declared in {@link #isValidRank(String)}
 */
public class Rank {


    public static final String MESSAGE_RANK_CONSTRAINTS =
            "Rank can only contain numbers which present their rank as show below\n"
                    + "Constable = 1\n"
                    + "Sergeant = 2\n"
                    + "Inspector = 3\n"
                    + "Detective = 4\n"
                    + "Captain = 5\n";
    public static final String RANK_VALIDATION_REGEX = "\\b[1-5]\\b";
    private int value;

    /**
     * Constructs a {@code Rank}.
     *
     * @param rank a value representing their rank
     */
    public Rank(String rank) {
        requireNonNull(rank);
        checkArgument(isValidRank(rank), MESSAGE_RANK_CONSTRAINTS);
        this.value = Integer.parseInt(rank);
    }

    /**
     * Returns true if a given string is a valid person phone number.
     */
    public static boolean isValidRank(String test) {
        return test.matches(RANK_VALIDATION_REGEX);
    }
    /**
     * Increase value by one
     * Throws @PromotionExceedException() if investigator is already at maximum rank
     */
    public void promote() throws Exception {
        if (value >= 5) {
            throw new PromoteExceedException();
        } else {
            value++;
        }
    }
    /**
     * Increase value by one
     * Throws @PromotionExceedException() if investigator is already at maximum rank
     */
    public void demote() throws Exception {
        if (value <= 1) {
            throw new DemoteExceedException();
        } else {
            value--;
        }
    }

    /**
     * Returns rank's value in string
     * @return
     */
    public String getValue() {
        return String.valueOf(value);
    }
    @Override
    public String toString() {
        switch (value) {
        case 1: return "Constable\n";
        case 2: return "Sergeant\n";
        case 3: return "Inspector\n";
        case 4: return "Detective\n";
        case 5: return "Captain\n";
        default: return "Error\n";
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Rank // instanceof handles nulls
                && this.value == ((Rank) other).value); // state check
    }

    @Override
    public int hashCode() {
        return value;
    }

}
```
###### \java\seedu\investigapptor\storage\StorageManager.java
``` java
    @Override
    @Subscribe
    public void handleInvestigapptorBackupEvent(InvestigapptorBackupEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            backupInvestigapptor(event.data, event.fileName);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
```
