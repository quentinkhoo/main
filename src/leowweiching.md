# leowweiching
###### \main\java\seedu\investigapptor\logic\commands\AddCaseCommand.java
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
###### \main\java\seedu\investigapptor\logic\commands\EditCaseCommand.java
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
###### \main\java\seedu\investigapptor\logic\commands\SelectCaseCommand.java
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
###### \main\java\seedu\investigapptor\logic\parser\SelectCaseCommandParser.java
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
###### \main\java\seedu\investigapptor\model\crimecase\EndDate.java
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
###### \main\java\seedu\investigapptor\model\crimecase\StartDate.java
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
###### \main\java\seedu\investigapptor\model\crimecase\Status.java
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
###### \main\java\seedu\investigapptor\model\Investigapptor.java
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
###### \main\java\seedu\investigapptor\model\Investigapptor.java
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
###### \main\java\seedu\investigapptor\storage\XmlAdaptedCrimeCase.java
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
###### \main\java\seedu\investigapptor\ui\BrowserPanel.java
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
###### \main\java\seedu\investigapptor\ui\BrowserPanel.java
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
###### \main\java\seedu\investigapptor\ui\BrowserPanel.java
``` java
    @Subscribe
    private void handleCrimeCasePanelSelectionChangedEvent(CrimeCasePanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadCrimeCasePage(event.getNewSelection().crimeCase);
    }
```
###### \main\resources\view\LightTheme.css
``` css

```
###### \test\java\seedu\investigapptor\logic\commands\EditCrimeCaseDescriptorTest.java
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
###### \test\java\seedu\investigapptor\logic\commands\SelectCaseCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code SelectCaseCommand}.
 */
public class SelectCaseCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalInvestigapptor(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastCrimeCaseIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size());

        assertExecutionSuccess(INDEX_FIRST_CASE);
        assertExecutionSuccess(INDEX_THIRD_CASE);
        assertExecutionSuccess(lastCrimeCaseIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredCrimeCaseList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        assertExecutionSuccess(INDEX_FIRST_CASE);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showCrimeCaseAtIndex(model, INDEX_FIRST_CASE);

        Index outOfBoundsIndex = INDEX_SECOND_CASE;
        // ensures that outOfBoundIndex is still in bounds of investigapptor book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getInvestigapptor().getCrimeCaseList().size());

        assertExecutionFailureWithEvent(outOfBoundsIndex, Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectCaseCommand selectFirstCommand = new SelectCaseCommand(INDEX_FIRST_CASE);
        SelectCaseCommand selectSecondCommand = new SelectCaseCommand(INDEX_SECOND_CASE);

        // same object -> returns true
        assertTrue(selectFirstCommand.equals(selectFirstCommand));

        // same values -> returns true
        SelectCaseCommand selectFirstCommandCopy = new SelectCaseCommand(INDEX_FIRST_CASE);
        assertTrue(selectFirstCommand.equals(selectFirstCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(selectFirstCommand.equals(selectSecondCommand));
    }

    /**
     * Executes a {@code SelectCaseCommand} with the given {@code index},
     * and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        SelectCaseCommand selectCaseCommand = prepareCommand(index);

        try {
            CommandResult commandResult = selectCaseCommand.execute();
            assertEquals(String.format(SelectCaseCommand.MESSAGE_SELECT_CASE_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        JumpToCrimeCaseListRequestEvent lastEvent =
                (JumpToCrimeCaseListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
    }

    /**
     * Executes a {@code SelectCaseCommand} with the given {@code index},
     * and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     *
     * This function checks that no events were raised
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SelectCaseCommand selectCaseCommand = prepareCommand(index);

        try {
            selectCaseCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Executes a {@code SelectCaseCommand} with the given {@code index},
     * and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     *
     * This function also checks that the event raised is FilteredCrimeCaseListChangedEvent
     */
    private void assertExecutionFailureWithEvent(Index index, String expectedMessage) {
        SelectCaseCommand selectCaseCommand = prepareCommand(index);

        try {
            selectCaseCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1
                    && eventsCollectorRule.eventsCollector.getMostRecent()
                    instanceof FilteredCrimeCaseListChangedEvent);
        }
    }

    /**
     * Returns a {@code SelectCaseCommand} with parameters {@code index}.
     */
    private SelectCaseCommand prepareCommand(Index index) {
        SelectCaseCommand selectCaseCommand = new SelectCaseCommand(index);
        selectCaseCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return selectCaseCommand;
    }
}
```
###### \test\java\seedu\investigapptor\logic\parser\AddCaseCommandParserTest.java
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
                new StartDate(VALID_STARTDATE_BANANA), tag);

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
                new StartDate(VALID_STARTDATE_BANANA), tagList);
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
                new StartDate(VALID_STARTDATE_APPLE), tag);
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
                + TAG_DESC_MURDER + TAG_DESC_FRAUD, StartDate.MESSAGE_DATE_CONSTRAINTS);

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
###### \test\java\seedu\investigapptor\logic\parser\InvestigapptorParserTest.java
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
###### \test\java\seedu\investigapptor\logic\parser\InvestigapptorParserTest.java
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
###### \test\java\seedu\investigapptor\logic\parser\SelectCaseCommandParserTest.java
``` java
/**
 * Test scope: similar to {@code DeleteCaseCommandParserTest}.
 * @see DeleteCaseCommandParserTest
 */
public class SelectCaseCommandParserTest {

    private SelectCaseCommandParser parser = new SelectCaseCommandParser();

    @Test
    public void parse_validArgs_returnsSelectCommand() {
        assertParseSuccess(parser, "1", new SelectCaseCommand(INDEX_FIRST_CASE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SelectCaseCommand.MESSAGE_USAGE));
    }
}
```
###### \test\java\seedu\investigapptor\storage\XmlAdaptedCrimeCaseTest.java
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
        String expectedMessage = StartDate.MESSAGE_DATE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_invalidEndDate_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase =
                new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR, VALID_STARTDATE,
                        INVALID_DATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = EndDate.MESSAGE_DATE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullStartDate_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR,
                null, VALID_ENDDATE, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, StartDate.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, crimeCase::toModelType);
    }

    @Test
    public void toModelType_nullEndDate_throwsIllegalValueException() {
        XmlAdaptedCrimeCase crimeCase = new XmlAdaptedCrimeCase(VALID_NAME, VALID_DESCRIPTION, VALID_INVESTIGATOR,
                VALID_STARTDATE, null, VALID_STATUS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, EndDate.class.getSimpleName());
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
