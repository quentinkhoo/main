# leowweiching
###### \java\seedu\investigapptor\logic\commands\AddCaseCommand.java
``` java
/**
 * Adds a case to the investigapptor book.
 */
public class AddCaseCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addcase";
    public static final String COMMAND_ALIAS = "ac";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a case to the investigapptor book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_INVESTIGATOR + "INVESTIGATOR_INDEX (must be a positive integer) "
            + PREFIX_STARTDATE + "START DATE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Project Magic "
            + PREFIX_DESCRIPTION + "Kidnapping of 6 year-old John Doe "
            + PREFIX_INVESTIGATOR + "1 "
            + PREFIX_STARTDATE + "01/04/2018 "
            + PREFIX_TAG + "Homicide "
            + PREFIX_TAG + "Fraud";

    public static final String MESSAGE_SUCCESS = "New case added: %1$s";
    public static final String MESSAGE_DUPLICATE_CASE = "This case already exists in the investigapptor book";

    private CaseName name;
    private Description description;
    private Index investigatorIndex;
    private StartDate startDate;
    private Set<Tag> tagList;

    private CrimeCase toAdd;

    /**
     * Creates an AddCaseCommand to add the specified {@code CrimeCase}
     */
    public AddCaseCommand(CrimeCase crimeCase) {
        requireNonNull(crimeCase);
        toAdd = crimeCase;
    }

    /**
     * @param name of the case to be added
     * @param description of the case to be added
     * @param investigatorIndex of the investigator to be added
     * @param startDate of the case to be added
     * @param tagList of the case to be added
     */
    public AddCaseCommand(CaseName name, Description description, Index investigatorIndex,
                          StartDate startDate, Set<Tag> tagList) {
        requireNonNull(name);
        requireNonNull(description);
        requireNonNull(investigatorIndex);
        requireNonNull(startDate);

        this.name = name;
        this.description = description;
        this.investigatorIndex = investigatorIndex;
        this.startDate = startDate;
        this.tagList = tagList;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addCrimeCase(toAdd);
            EventsCenter.getInstance().post(new SwapTabEvent(1));
        } catch (DuplicateCrimeCaseException e) {
            throw new CommandException(MESSAGE_DUPLICATE_CASE);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        if (investigatorIndex != null) {
            List<Person> lastShownList = model.getFilteredPersonList();

            if (investigatorIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
            }
            Investigator investigatorToAdd = (Investigator) lastShownList.get(investigatorIndex.getZeroBased());
            toAdd = createCrimeCase(investigatorToAdd);
        }
    }

    /**
     * Creates and returns a {@code CrimeCase} with the details of {@code investigatorToAdd}
     */
    private CrimeCase createCrimeCase(Investigator investigatorToAdd) {
        assert investigatorToAdd != null;

        return new CrimeCase(this.name, this.description, investigatorToAdd,
                this.startDate, new EndDate(LARGEST_DATE), new Status(), this.tagList);
    }

    @Override
    public boolean equals(Object other) {

        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCaseCommand)) {
            return false;
        }

        // state check
        return Objects.equals(toAdd, ((AddCaseCommand) other).toAdd);
    }
}
```
###### \java\seedu\investigapptor\logic\commands\DeleteCaseCommand.java
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
###### \java\seedu\investigapptor\logic\commands\EditCaseCommand.java
``` java
/**
 * Edits the details of an existing case in the investigapptor book.
 */
public class EditCaseCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "editcase";
    public static final String COMMAND_ALIAS = "ec";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the case identified "
            + "by the index number used in the last listing of crimeCases. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_INVESTIGATOR + "INVESTIGATOR INDEX (must be a positive integer)] "
            + "[" + PREFIX_STARTDATE + "STARTDATE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DESCRIPTION + "91234567 "
            + PREFIX_INVESTIGATOR + "johndoe@example.com";

    public static final String MESSAGE_EDIT_CASE_SUCCESS = "Edited Case: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_CASE = "This case already exists in investigapptor.";

    private final Index caseIndex;
    private final EditCrimeCaseDescriptor editCrimeCaseDescriptor;

    private CrimeCase crimeCaseToEdit;
    private CrimeCase editedCrimeCase;

    /**
     * @param caseIndex of the case in the filtered case list to edit
     * @param editCrimeCaseDescriptor details to edit the case with
     */
    public EditCaseCommand(Index caseIndex, EditCrimeCaseDescriptor editCrimeCaseDescriptor) {
        requireNonNull(caseIndex);
        requireNonNull(editCrimeCaseDescriptor);

        this.caseIndex = caseIndex;
        this.editCrimeCaseDescriptor = new EditCrimeCaseDescriptor(editCrimeCaseDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateCrimeCase(crimeCaseToEdit, editedCrimeCase);
            EventsCenter.getInstance().post(new SwapTabEvent(1));
        } catch (DuplicateCrimeCaseException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_CASE);
        } catch (CrimeCaseNotFoundException pnfe) {
            throw new AssertionError("The target case cannot be missing");
        }
        model.updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        return new CommandResult(String.format(MESSAGE_EDIT_CASE_SUCCESS, editedCrimeCase));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<CrimeCase> lastShownCrimeCaseList = model.getFilteredCrimeCaseList();
        List<Person> lastShownPersonList = model.getFilteredPersonList();

        Index investigatorIndex = editCrimeCaseDescriptor.getCurrentInvestigatorIndex()
                .orElse(null);

        if (caseIndex.getZeroBased() >= lastShownCrimeCaseList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
        }

        crimeCaseToEdit = lastShownCrimeCaseList.get(caseIndex.getZeroBased());

        // An investigator index was parsed
        if (investigatorIndex != null) {
            if (investigatorIndex.getZeroBased() >= lastShownPersonList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_INVESTIGATOR_DISPLAYED_INDEX);
            }
            editCrimeCaseDescriptor
                    .setCurrentInvestigator((Investigator) lastShownPersonList
                            .get(investigatorIndex.getZeroBased()));
        } else {
            editCrimeCaseDescriptor.setCurrentInvestigator(crimeCaseToEdit.getCurrentInvestigator());
        }

        editedCrimeCase = createEditedCrimeCase(crimeCaseToEdit, editCrimeCaseDescriptor);
    }

    /**
     * Creates and returns a {@code CrimeCase} with the details of {@code crimeCaseToEdit}
     * edited with {@code editCrimeCaseDescriptor}.
     */
    private static CrimeCase createEditedCrimeCase(CrimeCase crimeCaseToEdit,
                                                   EditCrimeCaseDescriptor editCrimeCaseDescriptor) {
        assert crimeCaseToEdit != null;

        CaseName updatedCaseName = editCrimeCaseDescriptor.getCaseName().orElse(crimeCaseToEdit.getCaseName());
        Description updatedDescription = editCrimeCaseDescriptor.getDescription()
                .orElse(crimeCaseToEdit.getDescription());
        Investigator updatedInvestigator = editCrimeCaseDescriptor.getCurrentInvestigator()
                .orElse(crimeCaseToEdit.getCurrentInvestigator());
        StartDate updatedStartDate = editCrimeCaseDescriptor.getStartDate().orElse(crimeCaseToEdit.getStartDate());
        Set<Tag> updatedTags = editCrimeCaseDescriptor.getTags().orElse(crimeCaseToEdit.getTags());

        return new CrimeCase(updatedCaseName, updatedDescription, updatedInvestigator,
                updatedStartDate, crimeCaseToEdit.getEndDate(), crimeCaseToEdit.getStatus(), updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCaseCommand)) {
            return false;
        }

        // state check
        EditCaseCommand e = (EditCaseCommand) other;
        return caseIndex.equals(e.caseIndex)
                && editCrimeCaseDescriptor.equals(e.editCrimeCaseDescriptor)
                && Objects.equals(crimeCaseToEdit, e.crimeCaseToEdit);
    }

    /**
     * Stores the details to edit the case with. Each non-empty field value will replace the
     * corresponding field value of the crimeCase.
     */
    public static class EditCrimeCaseDescriptor {
        private CaseName name;
        private Description description;
        private Index currentInvestigatorIndex;
        private Investigator currentInvestigator;
        private StartDate startDate;
        private Set<Tag> tags;

        public EditCrimeCaseDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditCrimeCaseDescriptor(EditCrimeCaseDescriptor toCopy) {
            setCaseName(toCopy.name);
            setDescription(toCopy.description);
            setCurrentInvestigatorIndex(toCopy.currentInvestigatorIndex);
            setCurrentInvestigator(toCopy.currentInvestigator);
            setStartDate(toCopy.startDate);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.name, this.description, this.currentInvestigatorIndex,
                    this.startDate, this.tags);
        }

        public void setCaseName(CaseName name) {
            this.name = name;
        }

        public Optional<CaseName> getCaseName() {
            return Optional.ofNullable(name);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setCurrentInvestigatorIndex(Index currentInvestigatorIndex) {
            this.currentInvestigatorIndex = currentInvestigatorIndex;
        }

        public Optional<Index> getCurrentInvestigatorIndex() {
            return Optional.ofNullable(currentInvestigatorIndex);
        }

        public void setCurrentInvestigator(Investigator currentInvestigator) {
            this.currentInvestigator = currentInvestigator;
        }

        public Optional<Investigator> getCurrentInvestigator() {
            return Optional.ofNullable(currentInvestigator);
        }

        public void setStartDate(StartDate startDate) {
            this.startDate = startDate;
        }

        public Optional<StartDate> getStartDate() {
            return Optional.ofNullable(startDate);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditCrimeCaseDescriptor)) {
                return false;
            }

            // state check
            EditCrimeCaseDescriptor e = (EditCrimeCaseDescriptor) other;

            return getCaseName().equals(e.getCaseName())
                    && getDescription().equals(e.getDescription())
                    && getCurrentInvestigator().equals(e.getCurrentInvestigator())
                    && getStartDate().equals(e.getStartDate())
                    && getTags().equals(e.getTags());
        }
    }
}
```
###### \java\seedu\investigapptor\logic\commands\FindCaseCommand.java
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
###### \java\seedu\investigapptor\logic\commands\SelectCaseCommand.java
``` java
/**
 * Selects a person identified using it's last displayed index from the investigapptor book.
 */
public class SelectCaseCommand extends Command {

    public static final String COMMAND_WORD = "selectcase";
    public static final String COMMAND_ALIAS = "sc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the case identified by the index number used in the last listing of cases.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_CASE_SUCCESS = "Selected Case: %1$s";

    private final Index targetIndex;

    public SelectCaseCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<CrimeCase> lastShownList = model.getFilteredCrimeCaseList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToCrimeCaseListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_CASE_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectCaseCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectCaseCommand) other).targetIndex)); // state check
    }
}
```
###### \java\seedu\investigapptor\logic\parser\SelectCaseCommandParser.java
``` java
/**
 * Parses input arguments and creates a new SelectCaseCommand object
 */
public class SelectCaseCommandParser implements Parser<SelectCaseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectCaseCommand
     * and returns an SelectCaseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SelectCaseCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SelectCaseCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCaseCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\investigapptor\model\crimecase\CaseName.java
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
###### \java\seedu\investigapptor\model\crimecase\CrimeCase.java
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
###### \java\seedu\investigapptor\model\crimecase\Description.java
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
###### \java\seedu\investigapptor\model\crimecase\EndDate.java
``` java
/**
 * Represents a CrimeCase's End date in the Investigapptor.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class EndDate {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Input date must follow DD/MM/YYYY or D/M/YYYY format, and it should not be blank";

    public static final String DATE_VALIDATION_REGEX = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}";
    public static final String LARGEST_DATE = "12/12/3000";

    private static final int DOB_DAY_INDEX = 0;
    private static final int DOB_MONTH_INDEX = 1;
    private static final int DOB_YEAR_INDEX = 2;
    private static String[] dateProperties;

    public final String date;

    private int day;
    private int month;
    private int year;

    /**
     * Constructs a {@code date}.
     *
     * @param date A valid date.
     */
    public EndDate(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        this.date = date;
        setDateProperties(date);
    }

    /**
     * Using LocalDate to retrieve the current date according to the format dd/mm/yyyy
     * @return String todayDate
     */
    public static String getTodayDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String todayDate = now.format(formatter);

        return todayDate;
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String date) {
        if (isEmptyDate(date) || !date.matches(DATE_VALIDATION_REGEX) || !hasDateMonthYear(date)) {
            return false;
        }

        try {
            stringToDate(date);
            return true;
        } catch (DateTimeException dte) {
            return false;
        }
    }
    /**
     * Returns {@code LocalDate} from given {@code String} date
     */
    private static LocalDate stringToDate(String date) throws DateTimeException {
        String[] dateProperties = date.split("/");
        int testDay = Integer.parseInt(dateProperties[DOB_DAY_INDEX]);
        int testMonth = Integer.parseInt(dateProperties[DOB_MONTH_INDEX]);
        int testYear = Integer.parseInt(dateProperties[DOB_YEAR_INDEX]);

        return LocalDate.of(testYear, testMonth, testDay);
    }

    /**
     * Returns true if a given string is empty or has only whitespaces
     */
    public static boolean isEmptyDate(String str) {
        return str.trim().isEmpty();
    }

    /**
     * Returns true if a given string has a day, month, year input
     */
    public static boolean hasDateMonthYear(String date) {
        String[] dateProperties = date.split("/");
        return dateProperties.length == 3;
    }

    public void setDateProperties(String date) {
        this.dateProperties = date.split("/");
        this.day = Integer.parseInt(dateProperties[DOB_DAY_INDEX]);
        this.month = Integer.parseInt(dateProperties[DOB_MONTH_INDEX]);
        this.year = Integer.parseInt(dateProperties[DOB_YEAR_INDEX]);
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof EndDate
                && this.date.equals(((EndDate) other).date));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.day)
                .append("/")
                .append(this.month)
                .append("/")
                .append(this.year);
        return builder.toString();
    }
}
```
###### \java\seedu\investigapptor\model\crimecase\NameContainsKeywordsPredicate.java
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
###### \java\seedu\investigapptor\model\crimecase\StartDate.java
``` java
/**
 * Represents a CrimeCase's Start date in the Investigapptor.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class StartDate {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Input date must follow DD/MM/YYYY or D/M/YYYY format, cannot be greater than today's date,"
                    + " and should not be blank";

    public static final String DATE_VALIDATION_REGEX = "[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}";

    private static final int DOB_DAY_INDEX = 0;
    private static final int DOB_MONTH_INDEX = 1;
    private static final int DOB_YEAR_INDEX = 2;
    private static String[] dateProperties;

    public final String date;

    private int day;
    private int month;
    private int year;

    /**
     * Constructs a {@code date}.
     *
     * @param date A valid date.
     */
    public StartDate(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        this.date = stringToDate(date).format(formatter);
        setDateProperties(date);
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String date) {
        if (isEmptyDate(date) || !date.matches(DATE_VALIDATION_REGEX) || !hasDateMonthYear(date)) {
            return false;
        }

        try {
            // Check if input date exceeds today's date
            LocalDate inputDate = stringToDate(date);
            LocalDate now = LocalDate.now();
            return inputDate.isBefore(now);
        } catch (DateTimeException dte) {
            return false;
        }

    }
    /**
     * Returns {@code LocalDate} from given {@code String} date
     */
    private static LocalDate stringToDate(String date) throws DateTimeException {
        String[] dateProperties = date.split("/");
        int testDay = Integer.parseInt(dateProperties[DOB_DAY_INDEX]);
        int testMonth = Integer.parseInt(dateProperties[DOB_MONTH_INDEX]);
        int testYear = Integer.parseInt(dateProperties[DOB_YEAR_INDEX]);

        return LocalDate.of(testYear, testMonth, testDay);
    }

    /**
     * Returns true if a given string is empty or has only whitespaces
     */
    public static boolean isEmptyDate(String str) {
        return str.trim().isEmpty();
    }

    /**
     * Returns true if a given string has a day, month, year input
     */
    public static boolean hasDateMonthYear(String date) {
        String[] dateProperties = date.split("/");
        return dateProperties.length == 3;
    }

    public void setDateProperties(String date) {
        this.dateProperties = date.split("/");
        this.day = Integer.parseInt(dateProperties[DOB_DAY_INDEX]);
        this.month = Integer.parseInt(dateProperties[DOB_MONTH_INDEX]);
        this.year = Integer.parseInt(dateProperties[DOB_YEAR_INDEX]);
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof StartDate
                && this.date.equals(((StartDate) other).date));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.day)
                .append("/")
                .append(this.month)
                .append("/")
                .append(this.year);
        return builder.toString();
    }
}
```
###### \java\seedu\investigapptor\model\crimecase\Status.java
``` java
/**
 * Represents a CrimeCase's status in the Investigapptor.
 */
public class Status {

    public static final String MESSAGE_STATUS_CONSTRAINTS =
            "Crime case status should not be blank";

    public static final String CASE_CLOSE = "close";
    public static final String CASE_OPEN = "open";

    private String status;

    /**
     * Constructs a {@code Status}.
     *
     */
    public Status() {
        this.status = CASE_OPEN;
    }

    /**
     * Constructs a {@code Status}.
     *
     * @param status A valid status.
     */
    public Status(String status) {
        requireNonNull(status);
        checkArgument(isValidStatus(status), MESSAGE_STATUS_CONSTRAINTS);
        this.status = status;
    }

    /**
     * Toggles status depending on current status
     *
     */
    public void toggleCase() {
        if (this.status.equals(CASE_OPEN)) {
            this.status = CASE_CLOSE;
        } else {
            this.status = CASE_OPEN;
        }
    }

    /**
     * Returns true if a given string is a valid case status.
     */
    public static boolean isValidStatus(String test) {
        return !test.trim().isEmpty() && (test.trim().equals("open") || test.trim().equals("close"));
    }

    @Override
    public String toString() {
        return this.status;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Status // instanceof handles nulls
                && this.status.equals(((Status) other).status)); // state check
    }

    @Override
    public int hashCode() {
        return status.hashCode();
    }
}
```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    public void setCrimeCases(List<CrimeCase> cases) throws DuplicateCrimeCaseException {
        this.cases.setCrimeCases(cases);
    }

```
###### \java\seedu\investigapptor\model\Investigapptor.java
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
###### \java\seedu\investigapptor\model\Investigapptor.java
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
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    /**
     * Adds {@code crimeCase} to {@code Investigator}.
     *
     * @throws DuplicateCrimeCaseException if the {@code key} is not in this {@code Investigapptor}.
     */
    public void addCrimeCaseToInvestigator(CrimeCase key) throws DuplicateCrimeCaseException {
        if (key.getCurrentInvestigator() != null) {
            for (Person person : persons) {
                // Finds the independent Investigator object that was assigned under the case
                if (key.getCurrentInvestigator().equals(person)) {
                    Investigator investigator = (Investigator) person;
                    investigator.addCrimeCase(key);
                    break;
                }
            }
        }
    }

```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    /**
     * Removes {@code key} from {@code Investigator}.
     *
     * @throws CrimeCaseNotFoundException if the {@code key} is not in this {@code Investigapptor}.
     */
    public void removeCrimeCaseFromInvestigator(CrimeCase key) throws CrimeCaseNotFoundException {
        if (key.getCurrentInvestigator() != null) {
            for (Person person : persons) {
                // Finds the independent Investigator object that was assigned under the case
                if (key.getCurrentInvestigator().equals(person)) {
                    Investigator investigator = (Investigator) person;
                    investigator.removeCrimeCase(key);
                    break;
                }
            }
        }
    }

    //// tag-level operations

```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    @Override
    public ObservableList<CrimeCase> getCrimeCaseList() {
        return cases.asObservableList();
    }

```
###### \java\seedu\investigapptor\model\Model.java
``` java
    /** {@code Predicate} that always evaluate to true */
    Predicate<CrimeCase> PREDICATE_SHOW_ALL_CASES = unused -> true;

```
###### \java\seedu\investigapptor\model\Model.java
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
###### \java\seedu\investigapptor\model\Model.java
``` java
    /** Returns an unmodifiable view of the filtered case list */
    ObservableList<CrimeCase> getFilteredCrimeCaseList();

```
###### \java\seedu\investigapptor\model\Model.java
``` java
    /**
     * Updates the filter of the filtered case list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredCrimeCaseList(Predicate<CrimeCase> predicate);

```
###### \java\seedu\investigapptor\model\ModelManager.java
``` java
    @Override
    public synchronized void deleteCrimeCase(CrimeCase target) throws CrimeCaseNotFoundException {
        investigapptor.removeCrimeCase(target);
        indicateInvestigapptorChanged();
    }

```
###### \java\seedu\investigapptor\model\ModelManager.java
``` java
    @Override
    public synchronized void addCrimeCase(CrimeCase crimecase) throws DuplicateCrimeCaseException {
        investigapptor.addCrimeCase(crimecase);
        updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        indicateInvestigapptorChanged();
    }

```
###### \java\seedu\investigapptor\model\ModelManager.java
``` java
    @Override
    public void updateCrimeCase(CrimeCase target, CrimeCase editedCase)
            throws DuplicateCrimeCaseException, CrimeCaseNotFoundException {
        requireAllNonNull(target, editedCase);

        investigapptor.updateCrimeCase(target, editedCase);
        indicateInvestigapptorChanged();
    }

```
###### \java\seedu\investigapptor\model\ModelManager.java
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
###### \java\seedu\investigapptor\storage\XmlAdaptedCrimeCase.java
``` java
/**
 * JAXB-friendly version of the CrimeCase.
 */
public class XmlAdaptedCrimeCase {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Case's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private XmlAdaptedInvestigator investigator;
    @XmlElement(required = true)
    private String startDate;
    @XmlElement(required = true)
    private String endDate;
    @XmlElement(required = true)
    private String status;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedCrimeCase.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedCrimeCase() {}

    /**
     * Constructs an {@code XmlAdaptedCrimeCase} with the given case details.
     */
    public XmlAdaptedCrimeCase(String name, String description, XmlAdaptedInvestigator investigator, String startDate,
                               String endDate, String status, List<XmlAdaptedTag> tagged) {
        this.name = name;
        this.description = description;
        this.investigator = investigator;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given CrimeCase into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedCrimeCase
     */
    public XmlAdaptedCrimeCase(CrimeCase source) {
        name = source.getCaseName().crimeCaseName;
        description = source.getDescription().description;
        investigator = new XmlAdaptedInvestigator(source.getCurrentInvestigator());
        startDate = source.getStartDate().date;
        endDate = source.getEndDate().date;
        status = source.getStatus().toString();
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's CrimeCase object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted case
     */
    public CrimeCase toModelType() throws IllegalValueException {
        final List<Tag> crimeCaseTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            crimeCaseTags.add(tag.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    CaseName.class.getSimpleName()));
        }
        if (!CaseName.isValidCaseName(this.name)) {
            throw new IllegalValueException(CaseName.MESSAGE_CASE_NAME_CONSTRAINTS);
        }
        final CaseName name = new CaseName(this.name);

        if (this.description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(this.description)) {
            throw new IllegalValueException(Description.MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        final Description description = new Description(this.description);
        if (this.investigator == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Person.class.getSimpleName()));
        }
        final Investigator investigator = this.investigator.toModelType();

        if (this.startDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    StartDate.class.getSimpleName()));
        }
        if (!StartDate.isValidDate(this.startDate)) {
            throw new IllegalValueException(StartDate.MESSAGE_DATE_CONSTRAINTS);
        }
        final StartDate startDate = new StartDate(this.startDate);

        if (this.endDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    EndDate.class.getSimpleName()));
        }
        if (!EndDate.isValidDate(this.endDate)) {
            throw new IllegalValueException(EndDate.MESSAGE_DATE_CONSTRAINTS);
        }
        final EndDate endDate = new EndDate(this.endDate);

        if (this.status == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Status.class.getSimpleName()));
        }
        if (!Status.isValidStatus(this.status)) {
            throw new IllegalValueException(Status.MESSAGE_STATUS_CONSTRAINTS);
        }
        final Status status = new Status(this.status);

        final Set<Tag> tags = new HashSet<>(crimeCaseTags);
        return new CrimeCase(name, description, investigator, startDate, endDate, status, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedCrimeCase)) {
            return false;
        }

        XmlAdaptedCrimeCase otherCrimeCase = (XmlAdaptedCrimeCase) other;
        return Objects.equals(name, otherCrimeCase.name)
                && Objects.equals(description, otherCrimeCase.description)
                && Objects.equals(investigator, otherCrimeCase.investigator)
                && Objects.equals(startDate, otherCrimeCase.startDate)
                && Objects.equals(endDate, otherCrimeCase.endDate)
                && Objects.equals(status, otherCrimeCase.status)
                && tagged.equals(otherCrimeCase.tagged);
    }
}
```
###### \java\seedu\investigapptor\storage\XmlSerializableInvestigapptor.java
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
###### \java\seedu\investigapptor\ui\BrowserPanel.java
``` java
    /**
     * Loads a CrimeCase HTML file with details from {@code CrimeCase}.
     */
    private void loadCrimeCasePage(CrimeCase crimeCase) {
        loadCaseDetailsPage(crimeCase.getCaseName().crimeCaseName,
                crimeCase.getDescription().description,
                crimeCase.getCurrentInvestigator(),
                crimeCase.getStartDate().toString(),
                crimeCase.getEndDate().toString(),
                crimeCase.getStatus().toString(),
                getTagsSeparatedByComma(crimeCase.getTagsRaw()));
    }
```
###### \java\seedu\investigapptor\ui\BrowserPanel.java
``` java
    /**
     * Loads the case details HTML file with a background that matches the general theme.
     */
    private void loadCaseDetailsPage(String caseName, String description, Investigator currentInvestigator,
                                     String startDate, String endDate, String status, String tagList) {

        String encDescription = description;
        String encInvEmail = currentInvestigator.getEmail().value;
        String encInvAddress = currentInvestigator.getAddress().value;
        String encStartDate = startDate;
        String encEndDate = endDate;

        // Encodes emails and addresses to handle symbols such as '#'
        try {
            encDescription = URLEncoder.encode(description, "UTF-8");
            encInvEmail = URLEncoder.encode(currentInvestigator.getEmail().value, "UTF-8");
            encInvAddress = URLEncoder.encode(currentInvestigator.getAddress().value, "UTF-8");
            encStartDate = URLEncoder.encode(startDate, "UTF-8");
            encEndDate = URLEncoder.encode(endDate, "UTF-8");
        } catch (UnsupportedEncodingException usee) {
            usee.printStackTrace();
        }

        String caseDetailsPage = CASE_DETAILS_PAGE
                + "?caseName=" + caseName
                + "&description=" + encDescription
                + "&tags=" + tagList
                + "&invName=" + currentInvestigator.getName().fullName
                + "&invRank=" + currentInvestigator.getRank().toString()
                + "&invPhone=" + currentInvestigator.getPhone().value
                + "&invEmail=" + encInvEmail
                + "&invAddress=" + encInvAddress
                + "&startDate=" + encStartDate
                + "&endDate=" + encEndDate
                + "&status=" + status;

        loadPage(caseDetailsPage);
    }

    private String getTagsSeparatedByComma(Set<String> tags) {
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for (String tag: tags) {
            sb.append(sep);
            sb.append(tag);
            sep = ",";
        }
        return sb.toString();
    }

```
###### \java\seedu\investigapptor\ui\BrowserPanel.java
``` java
    @Subscribe
    private void handleCrimeCasePanelSelectionChangedEvent(CrimeCasePanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadCrimeCasePage(event.getNewSelection().crimeCase);
    }
```
###### \resources\docs\stylesheets\BrowserPanel.css
``` css

/* Reset */

html,
body,
div,
span,
applet,
object,
iframe,
h1,
h2,
h3,
h4,
h5,
h6,
p,
blockquote,
pre,
a,
abbr,
acronym,
address,
big,
cite,
code,
del,
dfn,
em,
img,
ins,
kbd,
q,
s,
samp,
small,
strike,
strong,
sub,
sup,
tt,
var,
b,
u,
i,
center,
dl,
dt,
dd,
ol,
ul,
li,
fieldset,
form,
label,
legend,
table,
caption,
tbody,
tfoot,
thead,
tr,
th,
td,
article,
aside,
canvas,
details,
embed,
figure,
figcaption,
footer,
header,
hgroup,
menu,
nav,
output,
ruby,
section,
summary,
time,
mark,
audio,
video {
    margin: 0;
    padding: 0;
    border: 0;
    font-size: 100%;
    font: inherit;
    vertical-align: baseline;
}

article,
aside,
details,
figcaption,
figure,
footer,
header,
hgroup,
menu,
nav,
section {
    display: block;
}

body {
    line-height: 1;
}

ol,
ul {
    list-style: none;
}

blockquote,
q {
    quotes: none;
}

blockquote:before,
blockquote:after,
q:before,
q:after {
    content: '';
    content: none;
}

table {
    border-collapse: collapse;
    border-spacing: 0;
}

body {
    -webkit-text-size-adjust: none;
}





/* Box Model */

*,
*:before,
*:after {
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
}





/* Containers */

.container {
    margin-left: auto;
    margin-right: auto;
}

.container.\31 25\25 {
    width: 100%;
    max-width: 100rem;
    min-width: 80rem;
}

.container.\37 5\25 {
    width: 60rem;
}

.container.\35 0\25 {
    width: 40rem;
}

.container.\32 5\25 {
    width: 20rem;
}

.container {
    width: 80rem;
}

@media screen and (max-width: 1680px) {

    .container.\31 25\25 {
        width: 100%;
        max-width: 100rem;
        min-width: 80rem;
    }

    .container.\37 5\25 {
        width: 60rem;
    }

    .container.\35 0\25 {
        width: 40rem;
    }

    .container.\32 5\25 {
        width: 20rem;
    }

    .container {
        width: 80rem;
    }
}

@media screen and (max-width: 1280px) {

    .container.\31 25\25 {
        width: 100%;
        max-width: 81.25rem;
        min-width: 65rem;
    }

    .container.\37 5\25 {
        width: 48.75rem;
    }

    .container.\35 0\25 {
        width: 32.5rem;
    }

    .container.\32 5\25 {
        width: 16.25rem;
    }

    .container {
        width: 65rem;
    }
}

@media screen and (max-width: 980px) {

    .container.\31 25\25 {
        width: 100%;
        max-width: 112.5%;
        min-width: 90%;
    }

    .container.\37 5\25 {
        width: 67.5%;
    }

    .container.\35 0\25 {
        width: 45%;
    }

    .container.\32 5\25 {
        width: 22.5%;
    }

    .container {
        width: 90%;
    }
}

@media screen and (max-width: 736px) {

    .container.\31 25\25 {
        width: 100%;
        max-width: 112.5%;
        min-width: 90%;
    }

    .container.\37 5\25 {
        width: 67.5%;
    }

    .container.\35 0\25 {
        width: 45%;
    }

    .container.\32 5\25 {
        width: 22.5%;
    }

    .container {
        width: 90% !important;
    }
}

@media screen and (max-width: 480px) {

    .container.\31 25\25 {
        width: 100%;
        max-width: 112.5%;
        min-width: 90%;
    }

    .container.\37 5\25 {
        width: 67.5%;
    }

    .container.\35 0\25 {
        width: 45%;
    }

    .container.\32 5\25 {
        width: 22.5%;
    }

    .container {
        width: 90% !important;
    }
}


/* Grid */

.row {
    border-bottom: solid 1px transparent;
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
}

.row>* {
    float: left;
    -moz-box-sizing: border-box;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
}

.row:after,
.row:before {
    content: '';
    display: block;
    clear: both;
    height: 0;
}

.row.uniform>*> :first-child {
    margin-top: 0;
}

.row.uniform>*> :last-child {
    margin-bottom: 0;
}

.row.\30 \25>* {
    padding: 0 0 0 0rem;
}

.row.\30 \25 {
    margin: 0 0 -1px 0rem;
}

.row.uniform.\30 \25>* {
    padding: 0rem 0 0 0rem;
}

.row.uniform.\30 \25 {
    margin: 0rem 0 -1px 0rem;
}

.row>* {
    padding: 0 0 0 2rem;
}

.row {
    margin: 0 0 -1px -2rem;
}

.row.uniform>* {
    padding: 2rem 0 0 2rem;
}

.row.uniform {
    margin: -2rem 0 -1px -2rem;
}

.row.\32 00\25>* {
    padding: 0 0 0 4rem;
}

.row.\32 00\25 {
    margin: 0 0 -1px -4rem;
}

.row.uniform.\32 00\25>* {
    padding: 4rem 0 0 4rem;
}

.row.uniform.\32 00\25 {
    margin: -4rem 0 -1px -4rem;
}

.row.\31 50\25>* {
    padding: 0 0 0 3rem;
}

.row.\31 50\25 {
    margin: 0 0 -1px -3rem;
}

.row.uniform.\31 50\25>* {
    padding: 3rem 0 0 3rem;
}

.row.uniform.\31 50\25 {
    margin: -3rem 0 -1px -3rem;
}

.row.\35 0\25>* {
    padding: 0 0 0 1rem;
}

.row.\35 0\25 {
    margin: 0 0 -1px -1rem;
}

.row.uniform.\35 0\25>* {
    padding: 1rem 0 0 1rem;
}

.row.uniform.\35 0\25 {
    margin: -1rem 0 -1px -1rem;
}

.row.\32 5\25>* {
    padding: 0 0 0 0.5rem;
}

.row.\32 5\25 {
    margin: 0 0 -1px -0.5rem;
}

.row.uniform.\32 5\25>* {
    padding: 0.5rem 0 0 0.5rem;
}

.row.uniform.\32 5\25 {
    margin: -0.5rem 0 -1px -0.5rem;
}

.\31 2u,
.\31 2u\24 {
    width: 100%;
    clear: none;
    margin-left: 0;
}

.\31 1u,
.\31 1u\24 {
    width: 91.6666666667%;
    clear: none;
    margin-left: 0;
}

.\31 0u,
.\31 0u\24 {
    width: 83.3333333333%;
    clear: none;
    margin-left: 0;
}

.\39 u,
.\39 u\24 {
    width: 75%;
    clear: none;
    margin-left: 0;
}

.\38 u,
.\38 u\24 {
    width: 66.6666666667%;
    clear: none;
    margin-left: 0;
}

.\37 u,
.\37 u\24 {
    width: 58.3333333333%;
    clear: none;
    margin-left: 0;
}

.\36 u,
.\36 u\24 {
    width: 50%;
    clear: none;
    margin-left: 0;
}

.\35 u,
.\35 u\24 {
    width: 41.6666666667%;
    clear: none;
    margin-left: 0;
}

.\34 u,
.\34 u\24 {
    width: 33.3333333333%;
    clear: none;
    margin-left: 0;
}

.\33 u,
.\33 u\24 {
    width: 25%;
    clear: none;
    margin-left: 0;
}

.\32 u,
.\32 u\24 {
    width: 16.6666666667%;
    clear: none;
    margin-left: 0;
}

.\31 u,
.\31 u\24 {
    width: 8.3333333333%;
    clear: none;
    margin-left: 0;
}

.\31 2u\24+*,
.\31 1u\24+*,
.\31 0u\24+*,
.\39 u\24+*,
.\38 u\24+*,
.\37 u\24+*,
.\36 u\24+*,
.\35 u\24+*,
.\34 u\24+*,
.\33 u\24+*,
.\32 u\24+*,
.\31 u\24+* {
    clear: left;
}

.\-11u {
    margin-left: 91.66667%;
}

.\-10u {
    margin-left: 83.33333%;
}

.\-9u {
    margin-left: 75%;
}

.\-8u {
    margin-left: 66.66667%;
}

.\-7u {
    margin-left: 58.33333%;
}

.\-6u {
    margin-left: 50%;
}

.\-5u {
    margin-left: 41.66667%;
}

.\-4u {
    margin-left: 33.33333%;
}

.\-3u {
    margin-left: 25%;
}

.\-2u {
    margin-left: 16.66667%;
}

.\-1u {
    margin-left: 8.33333%;
}

@media screen and (max-width: 1680px) {

    .row>* {
        padding: 0 0 0 2rem;
    }

    .row {
        margin: 0 0 -1px -2rem;
    }

    .row.uniform>* {
        padding: 2rem 0 0 2rem;
    }

    .row.uniform {
        margin: -2rem 0 -1px -2rem;
    }

    .row.\32 00\25>* {
        padding: 0 0 0 4rem;
    }

    .row.\32 00\25 {
        margin: 0 0 -1px -4rem;
    }

    .row.uniform.\32 00\25>* {
        padding: 4rem 0 0 4rem;
    }

    .row.uniform.\32 00\25 {
        margin: -4rem 0 -1px -4rem;
    }

    .row.\31 50\25>* {
        padding: 0 0 0 3rem;
    }

    .row.\31 50\25 {
        margin: 0 0 -1px -3rem;
    }

    .row.uniform.\31 50\25>* {
        padding: 3rem 0 0 3rem;
    }

    .row.uniform.\31 50\25 {
        margin: -3rem 0 -1px -3rem;
    }

    .row.\35 0\25>* {
        padding: 0 0 0 1rem;
    }

    .row.\35 0\25 {
        margin: 0 0 -1px -1rem;
    }

    .row.uniform.\35 0\25>* {
        padding: 1rem 0 0 1rem;
    }

    .row.uniform.\35 0\25 {
        margin: -1rem 0 -1px -1rem;
    }

    .row.\32 5\25>* {
        padding: 0 0 0 0.5rem;
    }

    .row.\32 5\25 {
        margin: 0 0 -1px -0.5rem;
    }

    .row.uniform.\32 5\25>* {
        padding: 0.5rem 0 0 0.5rem;
    }

    .row.uniform.\32 5\25 {
        margin: -0.5rem 0 -1px -0.5rem;
    }

    .\31 2u\28xlarge\29,
    .\31 2u\24\28xlarge\29 {
        width: 100%;
        clear: none;
        margin-left: 0;
    }

    .\31 1u\28xlarge\29,
    .\31 1u\24\28xlarge\29 {
        width: 91.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\31 0u\28xlarge\29,
    .\31 0u\24\28xlarge\29 {
        width: 83.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\39 u\28xlarge\29,
    .\39 u\24\28xlarge\29 {
        width: 75%;
        clear: none;
        margin-left: 0;
    }

    .\38 u\28xlarge\29,
    .\38 u\24\28xlarge\29 {
        width: 66.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\37 u\28xlarge\29,
    .\37 u\24\28xlarge\29 {
        width: 58.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\36 u\28xlarge\29,
    .\36 u\24\28xlarge\29 {
        width: 50%;
        clear: none;
        margin-left: 0;
    }

    .\35 u\28xlarge\29,
    .\35 u\24\28xlarge\29 {
        width: 41.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\34 u\28xlarge\29,
    .\34 u\24\28xlarge\29 {
        width: 33.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\33 u\28xlarge\29,
    .\33 u\24\28xlarge\29 {
        width: 25%;
        clear: none;
        margin-left: 0;
    }

    .\32 u\28xlarge\29,
    .\32 u\24\28xlarge\29 {
        width: 16.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\31 u\28xlarge\29,
    .\31 u\24\28xlarge\29 {
        width: 8.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\31 2u\24\28xlarge\29+*,
    .\31 1u\24\28xlarge\29+*,
    .\31 0u\24\28xlarge\29+*,
    .\39 u\24\28xlarge\29+*,
    .\38 u\24\28xlarge\29+*,
    .\37 u\24\28xlarge\29+*,
    .\36 u\24\28xlarge\29+*,
    .\35 u\24\28xlarge\29+*,
    .\34 u\24\28xlarge\29+*,
    .\33 u\24\28xlarge\29+*,
    .\32 u\24\28xlarge\29+*,
    .\31 u\24\28xlarge\29+* {
        clear: left;
    }

    .\-11u\28xlarge\29 {
        margin-left: 91.66667%;
    }

    .\-10u\28xlarge\29 {
        margin-left: 83.33333%;
    }

    .\-9u\28xlarge\29 {
        margin-left: 75%;
    }

    .\-8u\28xlarge\29 {
        margin-left: 66.66667%;
    }

    .\-7u\28xlarge\29 {
        margin-left: 58.33333%;
    }

    .\-6u\28xlarge\29 {
        margin-left: 50%;
    }

    .\-5u\28xlarge\29 {
        margin-left: 41.66667%;
    }

    .\-4u\28xlarge\29 {
        margin-left: 33.33333%;
    }

    .\-3u\28xlarge\29 {
        margin-left: 25%;
    }

    .\-2u\28xlarge\29 {
        margin-left: 16.66667%;
    }

    .\-1u\28xlarge\29 {
        margin-left: 8.33333%;
    }
}

@media screen and (max-width: 1280px) {

    .row>* {
        padding: 0 0 0 1.5rem;
    }

    .row {
        margin: 0 0 -1px -1.5rem;
    }

    .row.uniform>* {
        padding: 1.5rem 0 0 1.5rem;
    }

    .row.uniform {
        margin: -1.5rem 0 -1px -1.5rem;
    }

    .row.\32 00\25>* {
        padding: 0 0 0 3rem;
    }

    .row.\32 00\25 {
        margin: 0 0 -1px -3rem;
    }

    .row.uniform.\32 00\25>* {
        padding: 3rem 0 0 3rem;
    }

    .row.uniform.\32 00\25 {
        margin: -3rem 0 -1px -3rem;
    }

    .row.\31 50\25>* {
        padding: 0 0 0 2.25rem;
    }

    .row.\31 50\25 {
        margin: 0 0 -1px -2.25rem;
    }

    .row.uniform.\31 50\25>* {
        padding: 2.25rem 0 0 2.25rem;
    }

    .row.uniform.\31 50\25 {
        margin: -2.25rem 0 -1px -2.25rem;
    }

    .row.\35 0\25>* {
        padding: 0 0 0 0.75rem;
    }

    .row.\35 0\25 {
        margin: 0 0 -1px -0.75rem;
    }

    .row.uniform.\35 0\25>* {
        padding: 0.75rem 0 0 0.75rem;
    }

    .row.uniform.\35 0\25 {
        margin: -0.75rem 0 -1px -0.75rem;
    }

    .row.\32 5\25>* {
        padding: 0 0 0 0.375rem;
    }

    .row.\32 5\25 {
        margin: 0 0 -1px -0.375rem;
    }

    .row.uniform.\32 5\25>* {
        padding: 0.375rem 0 0 0.375rem;
    }

    .row.uniform.\32 5\25 {
        margin: -0.375rem 0 -1px -0.375rem;
    }

    .\31 2u\28large\29,
    .\31 2u\24\28large\29 {
        width: 100%;
        clear: none;
        margin-left: 0;
    }

    .\31 1u\28large\29,
    .\31 1u\24\28large\29 {
        width: 91.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\31 0u\28large\29,
    .\31 0u\24\28large\29 {
        width: 83.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\39 u\28large\29,
    .\39 u\24\28large\29 {
        width: 75%;
        clear: none;
        margin-left: 0;
    }

    .\38 u\28large\29,
    .\38 u\24\28large\29 {
        width: 66.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\37 u\28large\29,
    .\37 u\24\28large\29 {
        width: 58.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\36 u\28large\29,
    .\36 u\24\28large\29 {
        width: 50%;
        clear: none;
        margin-left: 0;
    }

    .\35 u\28large\29,
    .\35 u\24\28large\29 {
        width: 41.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\34 u\28large\29,
    .\34 u\24\28large\29 {
        width: 33.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\33 u\28large\29,
    .\33 u\24\28large\29 {
        width: 25%;
        clear: none;
        margin-left: 0;
    }

    .\32 u\28large\29,
    .\32 u\24\28large\29 {
        width: 16.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\31 u\28large\29,
    .\31 u\24\28large\29 {
        width: 8.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\31 2u\24\28large\29+*,
    .\31 1u\24\28large\29+*,
    .\31 0u\24\28large\29+*,
    .\39 u\24\28large\29+*,
    .\38 u\24\28large\29+*,
    .\37 u\24\28large\29+*,
    .\36 u\24\28large\29+*,
    .\35 u\24\28large\29+*,
    .\34 u\24\28large\29+*,
    .\33 u\24\28large\29+*,
    .\32 u\24\28large\29+*,
    .\31 u\24\28large\29+* {
        clear: left;
    }

    .\-11u\28large\29 {
        margin-left: 91.66667%;
    }

    .\-10u\28large\29 {
        margin-left: 83.33333%;
    }

    .\-9u\28large\29 {
        margin-left: 75%;
    }

    .\-8u\28large\29 {
        margin-left: 66.66667%;
    }

    .\-7u\28large\29 {
        margin-left: 58.33333%;
    }

    .\-6u\28large\29 {
        margin-left: 50%;
    }

    .\-5u\28large\29 {
        margin-left: 41.66667%;
    }

    .\-4u\28large\29 {
        margin-left: 33.33333%;
    }

    .\-3u\28large\29 {
        margin-left: 25%;
    }

    .\-2u\28large\29 {
        margin-left: 16.66667%;
    }

    .\-1u\28large\29 {
        margin-left: 8.33333%;
    }
}

@media screen and (max-width: 980px) {

    .row>* {
        padding: 0 0 0 1.5rem;
    }

    .row {
        margin: 0 0 -1px -1.5rem;
    }

    .row.uniform>* {
        padding: 1.5rem 0 0 1.5rem;
    }

    .row.uniform {
        margin: -1.5rem 0 -1px -1.5rem;
    }

    .row.\32 00\25>* {
        padding: 0 0 0 3rem;
    }

    .row.\32 00\25 {
        margin: 0 0 -1px -3rem;
    }

    .row.uniform.\32 00\25>* {
        padding: 3rem 0 0 3rem;
    }

    .row.uniform.\32 00\25 {
        margin: -3rem 0 -1px -3rem;
    }

    .row.\31 50\25>* {
        padding: 0 0 0 2.25rem;
    }

    .row.\31 50\25 {
        margin: 0 0 -1px -2.25rem;
    }

    .row.uniform.\31 50\25>* {
        padding: 2.25rem 0 0 2.25rem;
    }

    .row.uniform.\31 50\25 {
        margin: -2.25rem 0 -1px -2.25rem;
    }

    .row.\35 0\25>* {
        padding: 0 0 0 0.75rem;
    }

    .row.\35 0\25 {
        margin: 0 0 -1px -0.75rem;
    }

    .row.uniform.\35 0\25>* {
        padding: 0.75rem 0 0 0.75rem;
    }

    .row.uniform.\35 0\25 {
        margin: -0.75rem 0 -1px -0.75rem;
    }

    .row.\32 5\25>* {
        padding: 0 0 0 0.375rem;
    }

    .row.\32 5\25 {
        margin: 0 0 -1px -0.375rem;
    }

    .row.uniform.\32 5\25>* {
        padding: 0.375rem 0 0 0.375rem;
    }

    .row.uniform.\32 5\25 {
        margin: -0.375rem 0 -1px -0.375rem;
    }

    .\31 2u\28medium\29,
    .\31 2u\24\28medium\29 {
        width: 100%;
        clear: none;
        margin-left: 0;
    }

    .\31 1u\28medium\29,
    .\31 1u\24\28medium\29 {
        width: 91.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\31 0u\28medium\29,
    .\31 0u\24\28medium\29 {
        width: 83.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\39 u\28medium\29,
    .\39 u\24\28medium\29 {
        width: 75%;
        clear: none;
        margin-left: 0;
    }

    .\38 u\28medium\29,
    .\38 u\24\28medium\29 {
        width: 66.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\37 u\28medium\29,
    .\37 u\24\28medium\29 {
        width: 58.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\36 u\28medium\29,
    .\36 u\24\28medium\29 {
        width: 50%;
        clear: none;
        margin-left: 0;
    }

    .\35 u\28medium\29,
    .\35 u\24\28medium\29 {
        width: 41.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\34 u\28medium\29,
    .\34 u\24\28medium\29 {
        width: 33.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\33 u\28medium\29,
    .\33 u\24\28medium\29 {
        width: 25%;
        clear: none;
        margin-left: 0;
    }

    .\32 u\28medium\29,
    .\32 u\24\28medium\29 {
        width: 16.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\31 u\28medium\29,
    .\31 u\24\28medium\29 {
        width: 8.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\31 2u\24\28medium\29+*,
    .\31 1u\24\28medium\29+*,
    .\31 0u\24\28medium\29+*,
    .\39 u\24\28medium\29+*,
    .\38 u\24\28medium\29+*,
    .\37 u\24\28medium\29+*,
    .\36 u\24\28medium\29+*,
    .\35 u\24\28medium\29+*,
    .\34 u\24\28medium\29+*,
    .\33 u\24\28medium\29+*,
    .\32 u\24\28medium\29+*,
    .\31 u\24\28medium\29+* {
        clear: left;
    }

    .\-11u\28medium\29 {
        margin-left: 91.66667%;
    }

    .\-10u\28medium\29 {
        margin-left: 83.33333%;
    }

    .\-9u\28medium\29 {
        margin-left: 75%;
    }

    .\-8u\28medium\29 {
        margin-left: 66.66667%;
    }

    .\-7u\28medium\29 {
        margin-left: 58.33333%;
    }

    .\-6u\28medium\29 {
        margin-left: 50%;
    }

    .\-5u\28medium\29 {
        margin-left: 41.66667%;
    }

    .\-4u\28medium\29 {
        margin-left: 33.33333%;
    }

    .\-3u\28medium\29 {
        margin-left: 25%;
    }

    .\-2u\28medium\29 {
        margin-left: 16.66667%;
    }

    .\-1u\28medium\29 {
        margin-left: 8.33333%;
    }
}

@media screen and (max-width: 736px) {

    .row>* {
        padding: 0 0 0 1.25rem;
    }

    .row {
        margin: 0 0 -1px -1.25rem;
    }

    .row.uniform>* {
        padding: 1.25rem 0 0 1.25rem;
    }

    .row.uniform {
        margin: -1.25rem 0 -1px -1.25rem;
    }

    .row.\32 00\25>* {
        padding: 0 0 0 2.5rem;
    }

    .row.\32 00\25 {
        margin: 0 0 -1px -2.5rem;
    }

    .row.uniform.\32 00\25>* {
        padding: 2.5rem 0 0 2.5rem;
    }

    .row.uniform.\32 00\25 {
        margin: -2.5rem 0 -1px -2.5rem;
    }

    .row.\31 50\25>* {
        padding: 0 0 0 1.875rem;
    }

    .row.\31 50\25 {
        margin: 0 0 -1px -1.875rem;
    }

    .row.uniform.\31 50\25>* {
        padding: 1.875rem 0 0 1.875rem;
    }

    .row.uniform.\31 50\25 {
        margin: -1.875rem 0 -1px -1.875rem;
    }

    .row.\35 0\25>* {
        padding: 0 0 0 0.625rem;
    }

    .row.\35 0\25 {
        margin: 0 0 -1px -0.625rem;
    }

    .row.uniform.\35 0\25>* {
        padding: 0.625rem 0 0 0.625rem;
    }

    .row.uniform.\35 0\25 {
        margin: -0.625rem 0 -1px -0.625rem;
    }

    .row.\32 5\25>* {
        padding: 0 0 0 0.3125rem;
    }

    .row.\32 5\25 {
        margin: 0 0 -1px -0.3125rem;
    }

    .row.uniform.\32 5\25>* {
        padding: 0.3125rem 0 0 0.3125rem;
    }

    .row.uniform.\32 5\25 {
        margin: -0.3125rem 0 -1px -0.3125rem;
    }

    .\31 2u\28small\29,
    .\31 2u\24\28small\29 {
        width: 100%;
        clear: none;
        margin-left: 0;
    }

    .\31 1u\28small\29,
    .\31 1u\24\28small\29 {
        width: 91.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\31 0u\28small\29,
    .\31 0u\24\28small\29 {
        width: 83.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\39 u\28small\29,
    .\39 u\24\28small\29 {
        width: 75%;
        clear: none;
        margin-left: 0;
    }

    .\38 u\28small\29,
    .\38 u\24\28small\29 {
        width: 66.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\37 u\28small\29,
    .\37 u\24\28small\29 {
        width: 58.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\36 u\28small\29,
    .\36 u\24\28small\29 {
        width: 50%;
        clear: none;
        margin-left: 0;
    }

    .\35 u\28small\29,
    .\35 u\24\28small\29 {
        width: 41.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\34 u\28small\29,
    .\34 u\24\28small\29 {
        width: 33.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\33 u\28small\29,
    .\33 u\24\28small\29 {
        width: 25%;
        clear: none;
        margin-left: 0;
    }

    .\32 u\28small\29,
    .\32 u\24\28small\29 {
        width: 16.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\31 u\28small\29,
    .\31 u\24\28small\29 {
        width: 8.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\31 2u\24\28small\29+*,
    .\31 1u\24\28small\29+*,
    .\31 0u\24\28small\29+*,
    .\39 u\24\28small\29+*,
    .\38 u\24\28small\29+*,
    .\37 u\24\28small\29+*,
    .\36 u\24\28small\29+*,
    .\35 u\24\28small\29+*,
    .\34 u\24\28small\29+*,
    .\33 u\24\28small\29+*,
    .\32 u\24\28small\29+*,
    .\31 u\24\28small\29+* {
        clear: left;
    }

    .\-11u\28small\29 {
        margin-left: 91.66667%;
    }

    .\-10u\28small\29 {
        margin-left: 83.33333%;
    }

    .\-9u\28small\29 {
        margin-left: 75%;
    }

    .\-8u\28small\29 {
        margin-left: 66.66667%;
    }

    .\-7u\28small\29 {
        margin-left: 58.33333%;
    }

    .\-6u\28small\29 {
        margin-left: 50%;
    }

    .\-5u\28small\29 {
        margin-left: 41.66667%;
    }

    .\-4u\28small\29 {
        margin-left: 33.33333%;
    }

    .\-3u\28small\29 {
        margin-left: 25%;
    }

    .\-2u\28small\29 {
        margin-left: 16.66667%;
    }

    .\-1u\28small\29 {
        margin-left: 8.33333%;
    }
}

@media screen and (max-width: 480px) {

    .row>* {
        padding: 0 0 0 1.25rem;
    }

    .row {
        margin: 0 0 -1px -1.25rem;
    }

    .row.uniform>* {
        padding: 1.25rem 0 0 1.25rem;
    }

    .row.uniform {
        margin: -1.25rem 0 -1px -1.25rem;
    }

    .row.\32 00\25>* {
        padding: 0 0 0 2.5rem;
    }

    .row.\32 00\25 {
        margin: 0 0 -1px -2.5rem;
    }

    .row.uniform.\32 00\25>* {
        padding: 2.5rem 0 0 2.5rem;
    }

    .row.uniform.\32 00\25 {
        margin: -2.5rem 0 -1px -2.5rem;
    }

    .row.\31 50\25>* {
        padding: 0 0 0 1.875rem;
    }

    .row.\31 50\25 {
        margin: 0 0 -1px -1.875rem;
    }

    .row.uniform.\31 50\25>* {
        padding: 1.875rem 0 0 1.875rem;
    }

    .row.uniform.\31 50\25 {
        margin: -1.875rem 0 -1px -1.875rem;
    }

    .row.\35 0\25>* {
        padding: 0 0 0 0.625rem;
    }

    .row.\35 0\25 {
        margin: 0 0 -1px -0.625rem;
    }

    .row.uniform.\35 0\25>* {
        padding: 0.625rem 0 0 0.625rem;
    }

    .row.uniform.\35 0\25 {
        margin: -0.625rem 0 -1px -0.625rem;
    }

    .row.\32 5\25>* {
        padding: 0 0 0 0.3125rem;
    }

    .row.\32 5\25 {
        margin: 0 0 -1px -0.3125rem;
    }

    .row.uniform.\32 5\25>* {
        padding: 0.3125rem 0 0 0.3125rem;
    }

    .row.uniform.\32 5\25 {
        margin: -0.3125rem 0 -1px -0.3125rem;
    }

    .\31 2u\28xsmall\29,
    .\31 2u\24\28xsmall\29 {
        width: 100%;
        clear: none;
        margin-left: 0;
    }

    .\31 1u\28xsmall\29,
    .\31 1u\24\28xsmall\29 {
        width: 91.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\31 0u\28xsmall\29,
    .\31 0u\24\28xsmall\29 {
        width: 83.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\39 u\28xsmall\29,
    .\39 u\24\28xsmall\29 {
        width: 75%;
        clear: none;
        margin-left: 0;
    }

    .\38 u\28xsmall\29,
    .\38 u\24\28xsmall\29 {
        width: 66.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\37 u\28xsmall\29,
    .\37 u\24\28xsmall\29 {
        width: 58.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\36 u\28xsmall\29,
    .\36 u\24\28xsmall\29 {
        width: 50%;
        clear: none;
        margin-left: 0;
    }

    .\35 u\28xsmall\29,
    .\35 u\24\28xsmall\29 {
        width: 41.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\34 u\28xsmall\29,
    .\34 u\24\28xsmall\29 {
        width: 33.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\33 u\28xsmall\29,
    .\33 u\24\28xsmall\29 {
        width: 25%;
        clear: none;
        margin-left: 0;
    }

    .\32 u\28xsmall\29,
    .\32 u\24\28xsmall\29 {
        width: 16.6666666667%;
        clear: none;
        margin-left: 0;
    }

    .\31 u\28xsmall\29,
    .\31 u\24\28xsmall\29 {
        width: 8.3333333333%;
        clear: none;
        margin-left: 0;
    }

    .\31 2u\24\28xsmall\29+*,
    .\31 1u\24\28xsmall\29+*,
    .\31 0u\24\28xsmall\29+*,
    .\39 u\24\28xsmall\29+*,
    .\38 u\24\28xsmall\29+*,
    .\37 u\24\28xsmall\29+*,
    .\36 u\24\28xsmall\29+*,
    .\35 u\24\28xsmall\29+*,
    .\34 u\24\28xsmall\29+*,
    .\33 u\24\28xsmall\29+*,
    .\32 u\24\28xsmall\29+*,
    .\31 u\24\28xsmall\29+* {
        clear: left;
    }

    .\-11u\28xsmall\29 {
        margin-left: 91.66667%;
    }

    .\-10u\28xsmall\29 {
        margin-left: 83.33333%;
    }

    .\-9u\28xsmall\29 {
        margin-left: 75%;
    }

    .\-8u\28xsmall\29 {
        margin-left: 66.66667%;
    }

    .\-7u\28xsmall\29 {
        margin-left: 58.33333%;
    }

    .\-6u\28xsmall\29 {
        margin-left: 50%;
    }

    .\-5u\28xsmall\29 {
        margin-left: 41.66667%;
    }

    .\-4u\28xsmall\29 {
        margin-left: 33.33333%;
    }

    .\-3u\28xsmall\29 {
        margin-left: 25%;
    }

    .\-2u\28xsmall\29 {
        margin-left: 16.66667%;
    }

    .\-1u\28xsmall\29 {
        margin-left: 8.33333%;
    }
}





/* Basic */

@-ms-viewport {
    width: device-width;
}

body {
    -ms-overflow-style: scrollbar;
}

@media screen and (max-width: 480px) {

    html,
    body {
        min-width: 320px;
    }
}

body {
    background: #fff;
}

body.is-loading *,
body.is-loading *:before,
body.is-loading *:after {
    -moz-animation: none !important;
    -webkit-animation: none !important;
    -ms-animation: none !important;
    animation: none !important;
    -moz-transition: none !important;
    -webkit-transition: none !important;
    -ms-transition: none !important;
    transition: none !important;
}





/* Type */

html {
    font-size: 13pt;
}

@media screen and (max-width: 1680px) {

    html {
        font-size: 11pt;
    }
}

@media screen and (max-width: 1280px) {

    html {
        font-size: 11pt;
    }
}

@media screen and (max-width: 980px) {

    html {
        font-size: 12pt;
    }
}

@media screen and (max-width: 736px) {

    html {
        font-size: 12pt;
    }
}

@media screen and (max-width: 480px) {

    html {
        font-size: 12pt;
    }
}

body {
    background-color: #fff;
    color: rgba(0, 0, 0, 0.5);
}

body,
input,
select,
textarea {
    font-family: "Poppins", sans-serif;
    font-weight: 300;
    font-size: 1rem;
    line-height: 1.65;
}

a {
    text-decoration: underline;
}

a:hover {
    text-decoration: none;
}

strong,
b {
    font-weight: 600;
}

em,
i {
    font-style: italic;
}

p {
    margin: 0 0 2rem 0;
}

p:last-child {
    margin: 0;
}

p.special {
    text-transform: uppercase;
    font-size: .75rem;
    font-weight: 300;
    margin: 0 0 .5rem 0;
    padding: 0 0 1rem 0;
    letter-spacing: .25rem;
}

p.special:after {
    content: '';
    position: absolute;
    margin: auto;
    right: 0;
    bottom: 0;
    left: 0;
    width: 50%;
    height: 1px;
    background-color: rgba(0, 0, 0, 0.125);
}

h1,
h2,
h3,
h4,
h5,
h6 {
    font-weight: 700;
    line-height: 1.5;
    margin: 0 0 1rem 0;
}

h1 a,
h2 a,
h3 a,
h4 a,
h5 a,
h6 a {
    color: inherit;
    text-decoration: none;
}

h2 {
    font-size: 1.75rem;
}

h3 {
    font-size: 1.35rem;
}

h4 {
    font-size: 1.1rem;
}

h5 {
    font-size: 0.9rem;
}

h6 {
    font-size: 0.7rem;
}

sub {
    font-size: 0.8rem;
    position: relative;
    top: 0.5rem;
}

sup {
    font-size: 0.8rem;
    position: relative;
    top: -0.5rem;
}

blockquote {
    border-left: solid 4px;
    font-style: italic;
    margin: 0 0 2rem 0;
    padding: 0.5rem 0 0.5rem 2rem;
}

code {
    border-radius: 4px;
    border: solid 1px;
    font-family: "Courier New", monospace;
    font-size: 0.9rem;
    margin: 0 0.25rem;
    padding: 0.25rem 0.65rem;
}

pre {
    -webkit-overflow-scrolling: touch;
    font-family: "Courier New", monospace;
    font-size: 0.9rem;
    margin: 0 0 2rem 0;
}

pre code {
    display: block;
    line-height: 1.75;
    padding: 1rem 1.5rem;
    overflow-x: auto;
}

hr {
    border: 0;
    border-bottom: solid 1px;
    margin: 2rem 0;
}

hr.major {
    margin: 3rem 0;
}

.align-left {
    text-align: left;
}

.align-center {
    text-align: center;
}

.align-right {
    text-align: right;
}

input,
select,
textarea {
    color: #1e1f23;
}

a {
    color: #54a354;
}

strong,
b {
    color: #1e1f23;
}

h1,
h2,
h3,
h4,
h5,
h6 {
    color: #1e1f23;
}

blockquote {
    border-left-color: rgba(144, 144, 144, 0.25);
}

code {
    background: rgba(144, 144, 144, 0.075);
    border-color: rgba(144, 144, 144, 0.25);
}

hr {
    border-bottom-color: rgba(144, 144, 144, 0.25);
}


/* Box */

.box {
    /*margin-bottom: 2rem;*/
    background: rgba(255, 255, 255, 0.95);
    border-radius: 4px;
}

.box .image.fit {
    margin: 0;
    border-radius: 0;
}

.box .image.fit img {
    border-radius: 0;
}

.box header h4 {
    margin-bottom: 1rem;
    position: relative;
    padding-top: 1rem;
    padding-bottom: 1rem;
    font-weight: 700;
    text-align: center;
}

.box header h4:after {
    content: '';
    position: absolute;
    margin: auto;
    right: 0;
    bottom: 0;
    left: 0;
    width: 10%;
    height: 1px;
    background-color: rgba(0, 0, 0, 0.125);
}

.box header p {
    text-transform: uppercase;
    font-size: .75rem;
    font-weight: 300;
    margin: 1rem 0;
    padding: 0;
    letter-spacing: .25rem;
}

.box .content {
    padding: 3rem;
}

.box> :last-child,
.box> :last-child> :last-child,
.box> :last-child> :last-child> :last-child {
    margin-bottom: 0;
}

.box.alt {
    border: 0;
    border-radius: 0;
    padding: 0;
}

@media screen and (max-width: 736px) {

    .box .content {
        padding: 1rem;
    }
}

.box {
    border-color: rgba(144, 144, 144, 0.25);
}


/* Button */

input[type="submit"],
input[type="reset"],
input[type="button"],
button,
.button {
    -moz-appearance: none;
    -webkit-appearance: none;
    -ms-appearance: none;
    appearance: none;
    -moz-transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out;
    -webkit-transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out;
    -ms-transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out;
    transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out;
    border-radius: 4px;
    border: 0;
    cursor: pointer;
    display: inline-block;
    font-weight: 300;
    height: 2.85rem;
    line-height: 2.95rem;
    padding: 0 1.5rem;
    text-align: center;
    text-decoration: none;
    text-transform: uppercase;
    white-space: nowrap;
}

input[type="submit"].icon,
input[type="reset"].icon,
input[type="button"].icon,
button.icon,
.button.icon {
    padding-left: 1.35rem;
}

input[type="submit"].icon:before,
input[type="reset"].icon:before,
input[type="button"].icon:before,
button.icon:before,
.button.icon:before {
    margin-right: 0.5rem;
}

input[type="submit"].fit,
input[type="reset"].fit,
input[type="button"].fit,
button.fit,
.button.fit {
    display: block;
    margin: 0 0 1rem 0;
    width: 100%;
}

input[type="submit"].small,
input[type="reset"].small,
input[type="button"].small,
button.small,
.button.small {
    font-size: 0.8rem;
}

input[type="submit"].big,
input[type="reset"].big,
input[type="button"].big,
button.big,
.button.big {
    font-size: 1.35rem;
}

input[type="submit"].disabled,
input[type="submit"]:disabled,
input[type="reset"].disabled,
input[type="reset"]:disabled,
input[type="button"].disabled,
input[type="button"]:disabled,
button.disabled,
button:disabled,
.button.disabled,
.button:disabled {
    -moz-pointer-events: none;
    -webkit-pointer-events: none;
    -ms-pointer-events: none;
    pointer-events: none;
    opacity: 0.25;
}

@media screen and (max-width: 480px) {

    input[type="submit"],
    input[type="reset"],
    input[type="button"],
    button,
    .button {
        padding: 0;
        width: 100%;
    }
}

input[type="submit"],
input[type="reset"],
input[type="button"],
button,
.button {
    background-color: #f2f2f2;
    color: #000 !important;
}

input[type="submit"]:hover,
input[type="reset"]:hover,
input[type="button"]:hover,
button:hover,
.button:hover {
    background-color: white;
}

input[type="submit"]:active,
input[type="reset"]:active,
input[type="button"]:active,
button:active,
.button:active {
    background-color: #e5e5e5;
}

input[type="submit"].alt,
input[type="reset"].alt,
input[type="button"].alt,
button.alt,
.button.alt {
    background-color: transparent;
    box-shadow: inset 0 0 0 2px rgba(144, 144, 144, 0.25);
    color: #1e1f23 !important;
}

input[type="submit"].alt:hover,
input[type="reset"].alt:hover,
input[type="button"].alt:hover,
button.alt:hover,
.button.alt:hover {
    background-color: rgba(144, 144, 144, 0.075);
}

input[type="submit"].alt:active,
input[type="reset"].alt:active,
input[type="button"].alt:active,
button.alt:active,
.button.alt:active {
    background-color: rgba(144, 144, 144, 0.2);
}

input[type="submit"].alt.icon:before,
input[type="reset"].alt.icon:before,
input[type="button"].alt.icon:before,
button.alt.icon:before,
.button.alt.icon:before {
    color: #bbb;
}

input[type="submit"].special,
input[type="reset"].special,
input[type="button"].special,
button.special,
.button.special {
    background-color: #54a354;
    color: #ffffff !important;
}

input[type="submit"].special:hover,
input[type="reset"].special:hover,
input[type="button"].special:hover,
button.special:hover,
.button.special:hover {
    background-color: #62ae62;
}

input[type="submit"].special:active,
input[type="reset"].special:active,
input[type="button"].special:active,
button.special:active,
.button.special:active {
    background-color: #4b924b;
}





/* Form */

form {
    margin: 0 0 2rem 0;
}

label {
    display: block;
    font-size: 0.9rem;
    font-weight: 700;
    margin: 0 0 1rem 0;
}

input[type="text"],
input[type="password"],
input[type="email"],
select,
textarea {
    -moz-appearance: none;
    -webkit-appearance: none;
    -ms-appearance: none;
    appearance: none;
    border-radius: 4px;
    border: none;
    border: solid 1px;
    color: inherit;
    display: block;
    outline: 0;
    padding: 0 1rem;
    text-decoration: none;
    width: 100%;
}

input[type="text"]:invalid,
input[type="password"]:invalid,
input[type="email"]:invalid,
select:invalid,
textarea:invalid {
    box-shadow: none;
}

.select-wrapper {
    text-decoration: none;
    display: block;
    position: relative;
}

.select-wrapper:before {
    -moz-osx-font-smoothing: grayscale;
    -webkit-font-smoothing: antialiased;
    font-family: FontAwesome;
    font-style: normal;
    font-weight: normal;
    text-transform: none !important;
}

.select-wrapper:before {
    content: '\f078';
    display: block;
    height: 2.75rem;
    line-height: 2.75rem;
    pointer-events: none;
    position: absolute;
    right: 0;
    text-align: center;
    top: 0;
    width: 2.75rem;
}

.select-wrapper select::-ms-expand {
    display: none;
}

input[type="text"],
input[type="password"],
input[type="email"],
select {
    height: 2.75rem;
}

textarea {
    padding: 0.75rem 1rem;
}

input[type="checkbox"],
input[type="radio"] {
    -moz-appearance: none;
    -webkit-appearance: none;
    -ms-appearance: none;
    appearance: none;
    display: block;
    float: left;
    margin-right: -2rem;
    opacity: 0;
    width: 1rem;
    z-index: -1;
}

input[type="checkbox"]+label,
input[type="radio"]+label {
    text-decoration: none;
    cursor: pointer;
    display: inline-block;
    font-size: 1rem;
    font-weight: 300;
    padding-left: 2.4rem;
    padding-right: 0.75rem;
    position: relative;
}

input[type="checkbox"]+label:before,
input[type="radio"]+label:before {
    -moz-osx-font-smoothing: grayscale;
    -webkit-font-smoothing: antialiased;
    font-family: FontAwesome;
    font-style: normal;
    font-weight: normal;
    text-transform: none !important;
}

input[type="checkbox"]+label:before,
input[type="radio"]+label:before {
    border-radius: 4px;
    border: solid 1px;
    content: '';
    display: inline-block;
    height: 1.65rem;
    left: 0;
    line-height: 1.58125rem;
    position: absolute;
    text-align: center;
    top: 0;
    width: 1.65rem;
}

input[type="checkbox"]:checked+label:before,
input[type="radio"]:checked+label:before {
    content: '\f00c';
}

input[type="checkbox"]+label:before {
    border-radius: 4px;
}

input[type="radio"]+label:before {
    border-radius: 100%;
}

::-webkit-input-placeholder {
    opacity: 1.0;
}

:-moz-placeholder {
    opacity: 1.0;
}

::-moz-placeholder {
    opacity: 1.0;
}

:-ms-input-placeholder {
    opacity: 1.0;
}

.formerize-placeholder {
    opacity: 1.0;
}

label {
    color: #1e1f23;
}

input[type="text"],
input[type="password"],
input[type="email"],
select,
textarea {
    background: rgba(144, 144, 144, 0.075);
    border-color: rgba(144, 144, 144, 0.25);
}

input[type="text"]:focus,
input[type="password"]:focus,
input[type="email"]:focus,
select:focus,
textarea:focus {
    border-color: #54a354;
    box-shadow: 0 0 0 1px #54a354;
}

.select-wrapper:before {
    color: rgba(144, 144, 144, 0.25);
}

input[type="checkbox"]+label,
input[type="radio"]+label {
    color: rgba(0, 0, 0, 0.5);
}

input[type="checkbox"]+label:before,
input[type="radio"]+label:before {
    background: rgba(144, 144, 144, 0.075);
    border-color: rgba(144, 144, 144, 0.25);
}

input[type="checkbox"]:checked+label:before,
input[type="radio"]:checked+label:before {
    background-color: #54a354;
    border-color: #54a354;
    color: #ffffff;
}

input[type="checkbox"]:focus+label:before,
input[type="radio"]:focus+label:before {
    border-color: #54a354;
    box-shadow: 0 0 0 1px #54a354;
}

::-webkit-input-placeholder {
    color: #bbb !important;
}

:-moz-placeholder {
    color: #bbb !important;
}

::-moz-placeholder {
    color: #bbb !important;
}

:-ms-input-placeholder {
    color: #bbb !important;
}

.formerize-placeholder {
    color: #bbb !important;
}





/* Gallery */

.gallery {
    width: 100%;
    margin: 0;
    display: -moz-flex;
    display: -webkit-flex;
    display: -ms-flex;
    display: flex;
    -moz-flex-wrap: wrap;
    -webkit-flex-wrap: wrap;
    -ms-flex-wrap: wrap;
    flex-wrap: wrap;
    -moz-align-items: stretch;
    -webkit-align-items: stretch;
    -ms-align-items: stretch;
    align-items: stretch;
}

.gallery .image.fit {
    margin: 0;
}

.gallery>* {
    -moz-flex-shrink: 1;
    -webkit-flex-shrink: 1;
    -ms-flex-shrink: 1;
    flex-shrink: 1;
    -moz-flex-grow: 0;
    -webkit-flex-grow: 0;
    -ms-flex-grow: 0;
    flex-grow: 0;
}

.gallery>* {
    width: 50%;
}

@media screen and (max-width: 980px) {

    .gallery>* {
        width: 100%;
    }
}





/* Icon */

.icon {
    text-decoration: none;
    border-bottom: none;
    position: relative;
}

.icon:before {
    -moz-osx-font-smoothing: grayscale;
    -webkit-font-smoothing: antialiased;
    font-family: FontAwesome;
    font-style: normal;
    font-weight: normal;
    text-transform: none !important;
}

.icon>.label {
    display: none;
}





/* Image */

.image {
    display: inline-block;
    position: relative;
}

.image.flush {
    margin: 0 !important;
}

.image img {
    display: block;
}

.image.left,
.image.right {
    max-width: 50%;
}

.image.left img,
.image.right img {
    width: 100%;
}

.image.left {
    float: left;
    margin: 0 1rem 1rem 0;
    top: 0.25rem;
}

.image.right {
    float: right;
    margin: 0 0 1rem 1rem;
    top: 0.25rem;
}

.image.fit {
    display: block;
    margin: 0 0 2rem 0;
    width: 100%;
}

.image.fit img {
    width: 100%;
}

.image.main {
    display: block;
    margin: 0 0 3rem 0;
    width: 100%;
}

.image.main img {
    width: 100%;
}





/* List */

ol {
    list-style: decimal;
    margin: 0 0 2rem 0;
    padding-left: 1.25rem;
}

ol li {
    padding-left: 0.25rem;
}

ul {
    /*list-style: disc;*/
    margin: 0 0 2rem 0;
    padding-left: 1rem;
}

ul li {
    padding-left: 0.5rem;
}

ul.alt {
    list-style: none;
    padding-left: 0;
}

ul.alt li {
    border-top: solid 1px;
    padding: 0.5rem 0;
}

ul.alt li:first-child {
    border-top: 0;
    padding-top: 0;
}

ul.icons {
    cursor: default;
    list-style: none;
    padding-left: 0;
}

ul.icons li {
    display: inline-block;
    padding: 0 1rem 0 0;
}

ul.icons li:last-child {
    padding-right: 0;
}

ul.icons li .icon:before {
    font-size: 2rem;
}

ul.actions {
    cursor: default;
    list-style: none;
    padding-left: 0;
}

ul.actions li {
    display: inline-block;
    padding: 0 1rem 0 0;
    vertical-align: middle;
}

ul.actions li:last-child {
    padding-right: 0;
}

ul.actions.small li {
    padding: 0 0.5rem 0 0;
}

ul.actions.vertical li {
    display: block;
    padding: 1rem 0 0 0;
}

ul.actions.vertical li:first-child {
    padding-top: 0;
}

ul.actions.vertical li>* {
    margin-bottom: 0;
}

ul.actions.vertical.small li {
    padding: 0.5rem 0 0 0;
}

ul.actions.vertical.small li:first-child {
    padding-top: 0;
}

ul.actions.fit {
    display: table;
    margin-left: -1rem;
    padding: 0;
    table-layout: fixed;
    width: calc(100% + 1rem);
}

ul.actions.fit li {
    display: table-cell;
    padding: 0 0 0 1rem;
}

ul.actions.fit li>* {
    margin-bottom: 0;
}

ul.actions.fit.small {
    margin-left: -0.5rem;
    width: calc(100% + 0.5rem);
}

ul.actions.fit.small li {
    padding: 0 0 0 0.5rem;
}

@media screen and (max-width: 480px) {

    ul.actions {
        margin: 0 0 2rem 0;
    }

    ul.actions li {
        padding: 1rem 0 0 0;
        display: block;
        text-align: center;
        width: 100%;
    }

    ul.actions li:first-child {
        padding-top: 0;
    }

    ul.actions li>* {
        width: 100%;
        margin: 0 !important;
    }

    ul.actions li>*.icon:before {
        margin-left: -2rem;
    }

    ul.actions.small li {
        padding: 0.5rem 0 0 0;
    }

    ul.actions.small li:first-child {
        padding-top: 0;
    }
}

dl {
    margin: 0 0 2rem 0;
}

dl dt {
    display: block;
    font-weight: 700;
    margin: 0 0 1rem 0;
}

dl dd {
    margin-left: 2rem;
}

ul.alt li {
    border-top-color: rgba(144, 144, 144, 0.25);
}





/* Section/Article */

section.special,
article.special {
    text-align: center;
}

header p {
    position: relative;
    margin: 0 0 1.5rem 0;
}

header h2+p {
    font-size: 1.25rem;
    margin-top: -1rem;
}

header h3+p {
    font-size: 1.1rem;
    margin-top: -0.8rem;
}

header h4+p,
header h5+p,
header h6+p {
    font-size: 0.9rem;
    margin-top: -0.6rem;
}

header p {
    color: #bbb;
}





/*Spotlight */

.spotlight {
    display: -moz-flex;
    display: -webkit-flex;
    display: -ms-flex;
    display: flex;
    -moz-flex-wrap: wrap;
    -webkit-flex-wrap: wrap;
    -ms-flex-wrap: wrap;
    flex-wrap: wrap;
    -moz-align-items: center;
    -webkit-align-items: center;
    -ms-align-items: center;
    align-items: center;
}

.spotlight>* {
    -moz-flex-shrink: 1;
    -webkit-flex-shrink: 1;
    -ms-flex-shrink: 1;
    flex-shrink: 1;
    -moz-flex-grow: 0;
    -webkit-flex-grow: 0;
    -ms-flex-grow: 0;
    flex-grow: 0;
}

.spotlight>* {
    width: 50%;
}

.spotlight p:last-child {
    margin: 0;
}

.spotlight .inner {
    padding: 6rem;
}

.spotlight .image {
    margin: 0;
}

.spotlight .image img {
    width: 100%;
}

.spotlight.alt {
    background: #1e1f23;
    color: rgba(255, 255, 255, 0.25);
    display: -moz-flex;
    display: -webkit-flex;
    display: -ms-flex;
    display: flex;
    -moz-flex-wrap: wrap;
    -webkit-flex-wrap: wrap;
    -ms-flex-wrap: wrap;
    flex-wrap: wrap;
    -moz-align-items: center;
    -webkit-align-items: center;
    -ms-align-items: center;
    align-items: center;
    text-align: right;
}

.spotlight.alt>* {
    -moz-flex-shrink: 1;
    -webkit-flex-shrink: 1;
    -ms-flex-shrink: 1;
    flex-shrink: 1;
    -moz-flex-grow: 0;
    -webkit-flex-grow: 0;
    -ms-flex-grow: 0;
    flex-grow: 0;
}

.spotlight.alt h3 {
    color: #FFF;
}

.spotlight.alt .image {
    -moz-order: 2;
    -webkit-order: 2;
    -ms-order: 2;
    order: 2;
}

@media screen and (max-width: 1280px) {

    .spotlight .inner {
        padding: 4rem;
    }
}

@media screen and (max-width: 980px) {

    .spotlight {
        display: -moz-flex;
        display: -webkit-flex;
        display: -ms-flex;
        display: flex;
        -moz-flex-wrap: wrap;
        -webkit-flex-wrap: wrap;
        -ms-flex-wrap: wrap;
        flex-wrap: wrap;
        -moz-align-items: center;
        -webkit-align-items: center;
        -ms-align-items: center;
        align-items: center;
        text-align: center;
    }

    .spotlight>* {
        -moz-flex-shrink: 1;
        -webkit-flex-shrink: 1;
        -ms-flex-shrink: 1;
        flex-shrink: 1;
        -moz-flex-grow: 0;
        -webkit-flex-grow: 0;
        -ms-flex-grow: 0;
        flex-grow: 0;
    }

    .spotlight>* {
        width: 100%;
    }

    .spotlight.alt {
        display: -moz-flex;
        display: -webkit-flex;
        display: -ms-flex;
        display: flex;
        -moz-flex-wrap: wrap;
        -webkit-flex-wrap: wrap;
        -ms-flex-wrap: wrap;
        flex-wrap: wrap;
        -moz-align-items: center;
        -webkit-align-items: center;
        -ms-align-items: center;
        align-items: center;
        text-align: center;
    }

    .spotlight.alt>* {
        -moz-flex-shrink: 1;
        -webkit-flex-shrink: 1;
        -ms-flex-shrink: 1;
        flex-shrink: 1;
        -moz-flex-grow: 0;
        -webkit-flex-grow: 0;
        -ms-flex-grow: 0;
        flex-grow: 0;
    }

    .spotlight.alt .image {
        -moz-order: 0;
        -webkit-order: 0;
        -ms-order: 0;
        order: 0;
    }
}

@media screen and (max-width: 736px) {

    .spotlight .inner {
        padding: 2rem;
    }
}





/* Table */

.table-wrapper {
    -webkit-overflow-scrolling: touch;
    overflow-x: auto;
}

table {
    margin: 0 0 2rem 0;
    width: 100%;
}

table tbody tr {
    border: solid 1px;
    border-left: 0;
    border-right: 0;
}

table td {
    padding: 0.75rem 0.75rem;
}

table th {
    font-size: 0.9rem;
    font-weight: 700;
    padding: 0 0.75rem 0.75rem 0.75rem;
    text-align: left;
}

table thead {
    border-bottom: solid 2px;
}

table tfoot {
    border-top: solid 2px;
}

table.alt {
    border-collapse: separate;
}

table.alt tbody tr td {
    border: solid 1px;
    border-left-width: 0;
    border-top-width: 0;
}

table.alt tbody tr td:first-child {
    border-left-width: 1px;
}

table.alt tbody tr:first-child td {
    border-top-width: 1px;
}

table.alt thead {
    border-bottom: 0;
}

table.alt tfoot {
    border-top: 0;
}

table tbody tr {
    border-color: rgba(144, 144, 144, 0.25);
}

table tbody tr:nth-child(2n+1) {
    background-color: rgba(144, 144, 144, 0.075);
}

table th {
    color: #1e1f23;
}

table thead {
    border-bottom-color: rgba(144, 144, 144, 0.25);
}

table tfoot {
    border-top-color: rgba(144, 144, 144, 0.25);
}

table.alt tbody tr td {
    border-color: rgba(144, 144, 144, 0.25);
}





/* Header */

body {
    background-color: #1e1f23;
    /*padding-top: 8rem;*/
}

body.is-loading:after {
    visibility: hidden;
    opacity: 0;
}

body:after {
    -moz-pointer-events: none;
    -webkit-pointer-events: none;
    -ms-pointer-events: none;
    pointer-events: none;
    -moz-transition: opacity 1.5s ease-in-out, visibility 1.5s;
    -webkit-transition: opacity 1.5s ease-in-out, visibility 1.5s;
    -ms-transition: opacity 1.5s ease-in-out, visibility 1.5s;
    transition: opacity 1.5s ease-in-out, visibility 1.5s;
    content: '';
    background-image: url(../../images/bg.jpg);
    background-attachment: fixed;
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;
    position: fixed;
    display: block;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    z-index: -1;
    visibility: visible;
    opacity: 1;
}

@media screen and (max-width: 980px) {

    body:after {
        background-attachment: scroll;
        background-position: top;
        background-size: 100%;
    }
}

#header {
    color: #a6a6a6;
    cursor: default;
    position: relative;
    text-align: center;
    z-index: 10001;
}

#header>.logo {
    margin: 0 0 4rem 0;
    padding: 0;
}

#header>.logo a {
    font-size: 4rem;
    font-weight: 700;
    color: #FFF;
    text-decoration: none;
    line-height: 1rem;
}

#header>.logo span {
    font-weight: 300;
    font-size: 1rem;
    display: block;
    color: rgba(255, 255, 255, 0.65);
}

@media screen and (max-width: 1280px) {

    body {
        /*padding-top: 3rem;*/
        background-attachment: scroll;
        background-size: auto;
        background-position: top;
    }
}

@media screen and (max-width: 736px) {

    #header>.logo {
        margin: 0 0 2rem 0;
    }

    #header>.logo a {
        font-size: 3rem;
    }
}





/* Main */

#main>.inner {
    /*margin: 0 auto;
		width: 80rem;
		max-width: 90%;
		margin-bottom: 2rem;*/
    background: rgba(255, 255, 255, 0.95);
    -webkit-box-shadow: 0px 0px 15px 0px rgba(0, 0, 0, 0.75);
    -moz-box-shadow: 0px 0px 15px 0px rgba(0, 0, 0, 0.75);
    box-shadow: 0px 0px 15px 0px rgba(0, 0, 0, 0.75);
    border-radius: 4px;
}

@media screen and (max-width: 980px) {

    #main .image {
        width: 100%;
        min-width: 100%;
        float: none;
        margin: 0 0 2rem 0;
    }

    #main .image img {
        width: 100%;
    }
}

.wrapper>header {
    text-align: center;
    background: #FFF;
    padding: 2rem;
    margin: 0;
}

.wrapper>header h2 {
    position: relative;
    padding-bottom: .75rem;
}

.wrapper>header h2:after {
    content: '';
    position: absolute;
    margin: auto;
    right: 0;
    bottom: 0;
    left: 0;
    width: 10%;
    height: 1px;
    background-color: rgba(0, 0, 0, 0.125);
}

.wrapper>header p {
    text-transform: uppercase;
    font-weight: 300;
    font-size: .8rem;
    letter-spacing: .25rem;
    margin: 0;
}

.wrapper>header.special {
    padding: 4rem 2rem;
}

@media screen and (max-width: 980px) {

    .wrapper>header.special {
        padding: 3rem 2rem;
    }
}

@media screen and (max-width: 736px) {

    .wrapper>header {
        padding: 1.5rem;
    }

    .wrapper>header h2 {
        font-size: 1.5rem;
    }

    .wrapper>header h2:after {
        width: 50%;
    }
}

.wrapper.style1 .content {
    padding: 6rem;
}

@media screen and (max-width: 1280px) {

    .wrapper.style1 .content {
        padding: 1rem;
    }
}

@media screen and (max-width: 736px) {

    .wrapper.style1 .content {
        padding: 1rem;
    }
}

.wrapper.style2 {
    background: #1e1f23;
    padding: 6rem;
}

.wrapper.style2 header {
    background: #121315;
    border-radius: 4px 4px 0 0;
}

.wrapper.style2 header h2 {
    color: #FFF;
}

.wrapper.style2 header h2:after {
    background-color: rgba(255, 255, 255, 0.125);
}

.wrapper.style2 header p {
    color: rgba(255, 255, 255, 0.25);
}

@media screen and (max-width: 1280px) {

    .wrapper.style2 {
        padding: 4rem;
    }
}

@media screen and (max-width: 736px) {

    .wrapper.style2 {
        padding: 2rem;
    }
}





/* Footer */

#footer {
    padding: 2rem 0 0.1rem 0;
    text-align: center;
}

#footer a {
    color: rgba(255, 255, 255, 0.75);
}

#footer a:hover {
    color: #FFF;
}

#footer .copyright {
    color: #bbb;
    font-size: 0.9rem;
    margin: 0 0 2rem 0;
    padding: 0 1rem;
    text-align: center;
}

@media screen and (max-width: 736px) {

    #footer {
        padding: 1rem 0 0.1rem 0;
    }
}


/* Tags */

.special div.tag-area {
    text-align: center;
    padding-top: 10px;
}

.tags{
  margin: 0px;
  padding: 0px;
  list-style: none;
  display:inline-table;
}
.tags li {
  position: relative;
  width: auto;
  height: 30px;
  padding: 0 10px;
  line-height: 32px;
  background: #53aac3;
  float: left;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-size: 14px;
  font-weight: 600;
  color: #fff;
  margin: 0 5px 5px 0;
  border-radius: 4px;
  overflow: hidden;
}
```
###### \resources\view\LightTheme.css
``` css

```
