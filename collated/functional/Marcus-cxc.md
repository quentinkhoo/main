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
###### \java\seedu\investigapptor\logic\commands\DeleteInvestigatorCommand.java
``` java
/**
 * Deletes a person identified using it's last displayed index from the investigapptor book.
 */
public class DeleteInvestigatorCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteinvestigator";
    public static final String COMMAND_ALIAS = "di";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the investigator identified by the index number used in the last listing of investigators.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Investigator: %1$s";
    public static final String MESSAGE_ACTIVE_INVESTIGATOR = "Investigator is currently in charge of a case.\n"
            + "Please reassign the cases to another investigator to delete the selected investigator";
    private final Index targetIndex;

    private Person personToDelete;

    public DeleteInvestigatorCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    /**
    * Try to call the model to delete Person (@personToDelete)
    *
    */
    private void deletePerson() {
        try {
            model.deletePerson(personToDelete);
            EventsCenter.getInstance().post(new SwapTabEvent(0));
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target investigator cannot be missing");
        }
    }
    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(personToDelete);
        if (personToDelete instanceof Investigator && !((Investigator) personToDelete).isCaseListEmpty()) {
            throw new CommandException(MESSAGE_ACTIVE_INVESTIGATOR);
        } else {
            deletePerson();
        }
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, personToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
        }

        personToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteInvestigatorCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteInvestigatorCommand) other).targetIndex) // state check
                && Objects.equals(this.personToDelete, ((DeleteInvestigatorCommand) other).personToDelete));
    }
}
```
###### \java\seedu\investigapptor\logic\commands\EditInvestigatorCommand.java
``` java
/**
 * Edits the details of an existing person in the investigapptor book.
 */
public class EditInvestigatorCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "editinvestigator";
    public static final String COMMAND_ALIAS = "ei";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the investigator identified "
            + "by the index number used in the last listing of investigators. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_RANK + "RANK] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Investigator: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This investigator already exists in investigapptor.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditInvestigatorCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }
```
###### \java\seedu\investigapptor\logic\commands\EditInvestigatorCommand.java
``` java
    /**
     * Creates investigator and returns as {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());

        if (personToEdit instanceof Investigator) {
            Rank updatedRank = editPersonDescriptor.getRank().orElse((((Investigator) personToEdit).getRank()));
            return new Investigator(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedRank, updatedTags, (
                    (Investigator) personToEdit).getCaseListHashed());
        }

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
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

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListInvestigatorCaseCommand // instanceof handles nulls
                && this.predicate.equals(((ListInvestigatorCaseCommand) other).predicate)); // state check
    }

}
```
###### \java\seedu\investigapptor\logic\parser\AddInvestigatorCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddInvestigatorCommand object
 */
public class AddInvestigatorCommandParser implements Parser<AddInvestigatorCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddInvestigatorCommand
     * and returns an AddInvestigatorCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddInvestigatorCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_RANK, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE,
                PREFIX_EMAIL, PREFIX_RANK) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddInvestigatorCommand.MESSAGE_USAGE));
        }

        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).get();
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).get();
            Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).get();
            Rank rank = ParserUtil.parseRank(argMultimap.getValue(PREFIX_RANK)).get();
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            Investigator investigator = new Investigator(name, phone, email, address, rank, tagList);
            return new AddInvestigatorCommand(investigator);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
```
###### \java\seedu\investigapptor\logic\parser\EditInvestigatorCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditInvestigatorCommand object
 */
public class EditInvestigatorCommandParser implements Parser<EditInvestigatorCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditInvestigatorCommand
     * and returns an EditInvestigatorCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditInvestigatorCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_RANK,
                        PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditInvestigatorCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        try {
            ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).ifPresent(editPersonDescriptor::setName);
            ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).ifPresent(editPersonDescriptor::setPhone);
            ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).ifPresent(editPersonDescriptor::setEmail);
            ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).ifPresent(editPersonDescriptor::setAddress);
            ParserUtil.parseRank(argMultimap.getValue(PREFIX_RANK)).ifPresent(editPersonDescriptor::setRank);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditInvestigatorCommand.MESSAGE_NOT_EDITED);
        }

        return new EditInvestigatorCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
```
###### \java\seedu\investigapptor\model\crimecase\CaseContainsInvestigatorPredicate.java
``` java
/**
 * Tests that a {@code CrimeCase}'s {@code CaseName} matches any of the keywords given.
 */
public class CaseContainsInvestigatorPredicate implements Predicate<CrimeCase> {
    private final Integer hashcode;

    public CaseContainsInvestigatorPredicate(Integer hashcode) {
        this.hashcode = hashcode;
    }

    @Override
    public boolean test(CrimeCase crimeCase) {
        return hashcode == crimeCase.getCurrentInvestigator().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CaseContainsInvestigatorPredicate // instanceof handles nulls
                && this.hashcode.equals(((CaseContainsInvestigatorPredicate) other).hashcode)); // state check
    }

}
```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code Investigapptor}'s tag list will be updated with the tags of {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *                                  another existing person in the list.
     * @throws PersonNotFoundException  if {@code target} could not be found in the list.
     * @see #syncWithMasterTagList(Person)
     */
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedPerson);
        if (target instanceof Investigator) {
            for (CrimeCase c : ((Investigator) target).getCrimeCases()) {
                recreateCasesForInvestigator((Investigator) editedPerson, c);
            }
        }
        Person syncedEditedPerson = syncWithMasterTagList(editedPerson);
        persons.setPerson(target, syncedEditedPerson);
    }
    /**
     * Converts {@code key} hashcode list of cases into CrimeCase object
     * Throws AssertionError when duplicate case occur
     */
    private void recreateCasesForInvestigator(Investigator inv, CrimeCase c) {
        CrimeCase newCase = syncWithMasterTagList(new CrimeCase(c.getCaseName(), c.getDescription(), inv,
                c.getStartDate(), c.getEndDate(), c.getStatus(), c.getTags()));
        try {
            cases.setCrimeCase(c, newCase);
        } catch (DuplicateCrimeCaseException e) {
            throw new AssertionError("Duplicate Case when editing investigator");
        } catch (CrimeCaseNotFoundException e) {
            throw new AssertionError("Case not found when editing investigator");
        }
    }
    /**
     * Removes {@code key} from this {@code Investigapptor}.
     *
     * @throws PersonNotFoundException if the {@code key} is not in this {@code Investigapptor}.
     */
    public boolean removePerson(Person key) throws PersonNotFoundException {
        if (persons.remove(key)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }
```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    /**
     * Converts {@code key} hashcode list of cases into CrimeCase object
     * Throws AssertionError when duplicate case occur
     */
    private void convertHashToCases(Investigator key) {
        requireNonNull(key.getCaseListHashed());
        for (Integer i : key.getCaseListHashed()) {
            try {
                addCaseFromHash(key, i);
            } catch (DuplicateCrimeCaseException e) {
                throw new AssertionError("Not possible, duplicate case while retrieving from xml");
            }
        }
    }
    /**
     * Check {@code hash} if it matches to any CrimeCase in the cases list
     * if match, add the CrimeCase to the investigator {@code key}
     */
    private void addCaseFromHash (Investigator key, int hash) throws DuplicateCrimeCaseException {
        for (CrimeCase c : cases) {
            if (c.hashCode() == hash) {
                key.addCrimeCase(c);
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
    public boolean isCaseListEmpty() {
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
    private final int value;

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
    public void backupInvestigapptor(ReadOnlyInvestigapptor investigapptor, String fileName) throws IOException {
        logger.fine("Attempting to write to data file: " + "data/" + fileName + ".xml");
        investigapptorStorage.saveInvestigapptor(investigapptor, "data/" + fileName + ".xml");
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
###### \java\seedu\investigapptor\storage\XmlAdaptedInvestigator.java
``` java
/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedInvestigator {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Investigator's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private String rank;
    @XmlElement(required = true)
    private List<Integer> caseList = new ArrayList<>();

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedInvestigator() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedInvestigator(String name, String phone, String email, String address, String rank,
                                  List<CrimeCase> caseList, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.rank = rank;
        this.caseList = new ArrayList<>();
        if (caseList != null) {
            for (CrimeCase c : caseList) {
                this.caseList.add(c.hashCode());
            }
        }
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedInvestigator(Investigator source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        rank = source.getRank().getValue();
        caseList = new ArrayList<>();
        for (CrimeCase crimeCase : source.getCrimeCases()) {
            caseList.add(crimeCase.hashCode());
        }
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Investigator object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Investigator toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        final ArrayList<Integer> investigatorCases = new ArrayList<>();
        for (int crimeCase: caseList) {
            investigatorCases.add(crimeCase);
        }
        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        if (this.phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(this.phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone phone = new Phone(this.phone);

        if (this.email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(this.email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email email = new Email(this.email);

        if (this.address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(this.address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address address = new Address(this.address);

        if (this.rank == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Rank.class.getSimpleName()));
        }
        if (!Rank.isValidRank(this.rank)) {
            throw new IllegalValueException(Rank.MESSAGE_RANK_CONSTRAINTS);
        }
        final Rank rank = new Rank(this.rank);

        final Set<Tag> tags = new HashSet<>(personTags);

        return new Investigator(name, phone, email, address, rank, tags, investigatorCases);
    }

    /**
     * Compares if it is equal to another object
     * @param other
     * @return
     */
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedInvestigator)) {
            return false;
        }

        XmlAdaptedInvestigator otherPerson = (XmlAdaptedInvestigator) other;
        return Objects.equals(name, otherPerson.name)
                && Objects.equals(phone, otherPerson.phone)
                && Objects.equals(email, otherPerson.email)
                && Objects.equals(address, otherPerson.address)
                && Objects.equals(rank, otherPerson.rank)
                && tagged.equals(otherPerson.tagged)
                && caseList.equals(otherPerson.caseList);
    }
}
```
###### \java\seedu\investigapptor\storage\XmlInvestigapptorStorage.java
``` java
    @Override
    public void backupInvestigapptor(ReadOnlyInvestigapptor investigapptor, String fileName) throws IOException {
        saveInvestigapptor(investigapptor, filePath + ".backup");
    }
```
###### \java\seedu\investigapptor\ui\BrowserPanel.java
``` java
    /**
     * Loads a Investigator HTML file with details from {@code Investigator}.
     */
    private void loadPersonPage(Person person) {
        if (person instanceof Investigator) {
            loadInvestigatorDetailsPage((Investigator) person);
        } else {
            loadPage(SEARCH_PAGE_URL + person.getName().fullName);
        }
    }
    /**
     * Loads the case details HTML file with a background that matches the general theme.
     */
    private void loadInvestigatorDetailsPage(Investigator investigator) {
        StringBuilder  url = new StringBuilder();
        try {
            String investigatorDetails = INVESTIGATOR_DETAILS_PAGE
                    + "?invName=" + investigator.getName().fullName
                    + "&rank=" + investigator.getRank().toString()
                    + "&phone=" + investigator.getPhone().value
                    + "&email=" + URLEncoder.encode(investigator.getEmail().value, "UTF-8")
                    + "&address=" + URLEncoder.encode(investigator.getAddress().value, "UTF-8")
                    + "&tags=" + getTagsSeparatedByComma(investigator.getTagsRaw())
                    + "&case=";
            url.append(investigatorDetails);
            for (CrimeCase c : investigator.getCrimeCases()) {
                url.append(getBasicCaseDetails(c));
                url.append(",");
            }
            loadPage(url.toString());
        } catch (Exception e) {
            throw new AssertionError("Encoder Error");
        }
    }
    /**
     * Loads the case details HTML file with a background that matches the general theme.
     */
    private String getBasicCaseDetails(CrimeCase crimeCase) {
        String caseDetail = crimeCase.getCaseName().toString()
                + "!" + crimeCase.getStatus();

        return caseDetail;
    }
    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    private void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }

```
