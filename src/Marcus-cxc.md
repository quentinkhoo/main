# Marcus-cxc
###### \main\java\seedu\investigapptor\commons\events\model\InvestigapptorBackupEvent.java
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
###### \main\java\seedu\investigapptor\logic\commands\BackupCommand.java
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
###### \main\java\seedu\investigapptor\logic\commands\DeleteInvestigatorCommand.java
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
###### \main\java\seedu\investigapptor\logic\commands\EditInvestigatorCommand.java
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
###### \main\java\seedu\investigapptor\logic\commands\EditInvestigatorCommand.java
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
###### \main\java\seedu\investigapptor\logic\commands\ListInvestigatorCaseCommand.java
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
###### \main\java\seedu\investigapptor\logic\parser\AddInvestigatorCommandParser.java
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
###### \main\java\seedu\investigapptor\logic\parser\EditInvestigatorCommandParser.java
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
###### \main\java\seedu\investigapptor\logic\parser\ListInvestigatorCaseCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeleteInvestigatorCommand object
 */
public class ListInvestigatorCaseCommandParser implements Parser<ListInvestigatorCaseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteInvestigatorCommand
     * and returns an ListInvestigatorCaseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListInvestigatorCaseCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ListInvestigatorCaseCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListInvestigatorCaseCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \main\java\seedu\investigapptor\model\crimecase\CaseContainsInvestigatorPredicate.java
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
###### \main\java\seedu\investigapptor\model\Investigapptor.java
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
###### \main\java\seedu\investigapptor\model\Investigapptor.java
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
###### \main\java\seedu\investigapptor\model\Investigapptor.java
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
###### \main\java\seedu\investigapptor\model\person\investigator\Investigator.java
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
###### \main\java\seedu\investigapptor\model\person\investigator\Rank.java
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
###### \main\java\seedu\investigapptor\storage\StorageManager.java
``` java
    @Override
    public void backupInvestigapptor(ReadOnlyInvestigapptor investigapptor, String fileName) throws IOException {
        logger.fine("Attempting to write to data file: " + "data/" + fileName + ".xml");
        investigapptorStorage.saveInvestigapptor(investigapptor, "data/" + fileName + ".xml");
    }
```
###### \main\java\seedu\investigapptor\storage\StorageManager.java
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
###### \main\java\seedu\investigapptor\storage\XmlAdaptedInvestigator.java
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
###### \main\java\seedu\investigapptor\storage\XmlInvestigapptorStorage.java
``` java
    @Override
    public void backupInvestigapptor(ReadOnlyInvestigapptor investigapptor, String fileName) throws IOException {
        saveInvestigapptor(investigapptor, filePath + ".backup");
    }
```
###### \main\java\seedu\investigapptor\ui\BrowserPanel.java
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
###### \main\resources\view\InvestigatorDetailsPage.html
``` html

<!DOCTYPE html>
<html>

<head>
    <link rel="stylesheet" href="BrowserPanel.css">
    <script type="text/javascript">
        function getJsonFromUrl() {
            var query = location.search.substr(1);
            var result = {};
            query.split("&").forEach(function(part) {
                var item = part.split("=");
                result[item[0]] = decodeURIComponent(item[1].replace(/\+/g, '%20'));
            });
            return result;
        }

        // Takes the array of tags and parses it into format suitable to display with HTML
        function tagsToHTML(tagsArr){
            if (tagsArr[0] !== '') {
                var printThis = '<ul class="tags">';
                for (var i = 0; i < tagsArr.length; i++) {
                    printThis += '<li>' + tagsArr[i] + '</li>';
                }
                printThis += '</ul>';
                return printThis;
            } else return '#'; // return dummy value
        }

        // Takes the array of cases and parses it into format suitable to display with HTML
        function casesToHTML(casesArr){
            if (casesArr[0] !== '') {
                var printThis = '<div class="content">'
                    + '<div class="box">'
                    + '<header><h4>Cases</h4></header>'
                    + '<div class="content">'
                    + '<table>';

                for (var i = 0; i < casesArr.length - 1; i++) {
                    var crimeCase = casesArr[i].split("!");
                    printThis += '<tr>' +
                    '<td><strong>' + crimeCase[0] + '</strong></td>' +
                    '<td><strong>' + crimeCase[1] + '</strong></td>' +
                    '</tr>'
                }
                printThis += '</table>\n' +
                    '</div>\n' +
                    '</div>\n' +
                    '</div>';
                return printThis;
            } else return '#'; // return dummy value
        }
        // Takes in status and endDate and creates HTML tags if status is closed
        function endDateToHTML(status, endDate){
            if (status === "close") {
                return '<td><strong>End Date:</strong></td><td>' + endDate + '</td>';
            } else return '#'; // return dummy value
        }

        // Parse url parameters to get JSON
        var json = getJsonFromUrl();

        // Preprocess tags and endDate
        var tagsHTML = tagsToHTML(json.tags.split(','));
        var caseHTML = casesToHTML(json.case.split(','));
        //var endDateHTML = endDateToHTML(json.status, json.endDate);

        document.addEventListener("DOMContentLoaded", function(event) {
            document.getElementById("bigName").innerHTML=json.invName;
            if (tagsHTML !== '#')
                document.getElementById("tag-area").innerHTML=tagsHTML;
            if (caseHTML !== '#')
                document.getElementById("case-area").innerHTML=caseHTML;
            document.getElementById("name").innerHTML=json.invName;
            document.getElementById("rank").innerHTML=json.rank;
            document.getElementById("phone").innerHTML=json.phone;
            document.getElementById("email").innerHTML=json.email;
            document.getElementById("address").innerHTML=json.address;
        });
    </script>
</head>
<body>
    <!-- Main -->
    <section id="main">
        <div class="inner">
            <section id="one" class="wrapper style1">
                <header class="special">
                    <h2 id="bigName"></h2>
                    <div id="tag-area" class="tag-area"></div>
                </header>
                <!-- Section: Investigator Assigned -->
                <div class="content">
                    <div class="box">
                        <header><h4>Details</h4></header>
                        <div class="content">
                            <table>
                                <tr>
                                    <td><strong>Name:</strong></td>
                                    <td><span id="name"></span></td>
                                </tr>
                                <tr>
                                    <td><strong>Rank:</strong></td>
                                    <td><span id="rank"></span></td>
                                </tr>
                                <tr>
                                    <td><strong>Phone:</strong></td>
                                    <td><span id="phone"></span></td>
                                </tr>
                                <tr>
                                    <td><strong>Email:</strong></td>
                                    <td><span id="email"></span></td>
                                </tr>
                                <tr>
                                    <td><strong>Address:</strong></td>
                                    <td><span id="address"></span></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <!-- Section: Case Information -->
                <div id="case-area" class="case-area"></div>
            </section>
        </div>
    </section>
</body>

</html>
```
###### \test\java\seedu\investigapptor\logic\commands\ListInvestigatorCaseCommandTest.java
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
###### \test\java\seedu\investigapptor\logic\parser\AddInvestigatorCommandParserTest.java
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
###### \test\java\seedu\investigapptor\model\person\RankTest.java
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
###### \test\java\seedu\investigapptor\storage\XmlAdaptedInvestigatorTest.java
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
###### \test\java\seedu\investigapptor\testutil\InvestigatorBuilder.java
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
###### \test\java\seedu\investigapptor\testutil\TypicalInvestigator.java
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
