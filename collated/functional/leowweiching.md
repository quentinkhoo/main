# leowweiching
###### /java/seedu/investigapptor/logic/commands/AddCaseCommand.java
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
            + PREFIX_INVESTIGATOR + "INDEX (must be a positive integer) "
            + PREFIX_STARTDATE + "START DATE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Project Magic "
            + PREFIX_DESCRIPTION + "Kidnapping of 6 year-old John Doe "
            + PREFIX_INVESTIGATOR + "1 "
            + PREFIX_STARTDATE + "25/12/2017 "
            + PREFIX_TAG + "Homicide "
            + PREFIX_TAG + "Missing Persons";

    public static final String MESSAGE_SUCCESS = "New case added: %1$s";
    public static final String MESSAGE_DUPLICATE_CASE = "This case already exists in the investigapptor book";

    private CaseName name;
    private Description description;
    private Index investigatorIndex;
    private Date startDate;
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
                          Date startDate, Set<Tag> tagList) {
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
                this.startDate, new Date(LARGEST_DATE), new Status(), this.tagList);
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
###### /java/seedu/investigapptor/logic/commands/EditCaseCommand.java
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
        Date updatedStartDate = editCrimeCaseDescriptor.getStartDate().orElse(crimeCaseToEdit.getStartDate());
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
        private Date startDate;
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

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Optional<Date> getStartDate() {
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
###### /java/seedu/investigapptor/model/crimecase/Date.java
``` java
/**
 * Represents a CrimeCase's Start date in the Investigapptor.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Input date must follow DD/MM/YYYY or D/M/YYYY format, and it should not be blank";

    public static final String DATE_VALIDATION_REGEX = "([0-9]*)/([0-9]*)/([0-9]*)";
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
    public Date(String date) {
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
    public static boolean isValidDate(String test) {
        if (isEmptyDate(test)) {
            return false;
        }

        if (!test.matches(DATE_VALIDATION_REGEX)) {
            return false;
        }

        String[] dateProperties = test.split("/");
        if (hasDateMonthYear((test))) {
            int testDay = Integer.parseInt(dateProperties[DOB_DAY_INDEX]);
            int testMonth = Integer.parseInt(dateProperties[DOB_MONTH_INDEX]);
            int testYear = Integer.parseInt(dateProperties[DOB_YEAR_INDEX]);

            try {
                LocalDate.of(testYear, testMonth, testDay);
                return true;
            } catch (DateTimeException dte) {
                return false;
            }
        } else {
            return false;
        }

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
                || (other instanceof Date
                && this.date.equals(((Date) other).date));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.day)
                .append(" ")
                .append(this.month)
                .append(" ")
                .append(this.year);
        return builder.toString();
    }
}
```
###### /java/seedu/investigapptor/model/crimecase/Status.java
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
     * Close the case by updating the case status to close
     */
    public void closeCase() {
        this.status = CASE_CLOSE;
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
###### /java/seedu/investigapptor/model/Investigapptor.java
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
                if (key.getCurrentInvestigator().getName().equals(person.getName())) {
                    Investigator investigator = (Investigator) person;
                    investigator.addCrimeCase(key);
                    break;
                }
            }
        }
    }

```
###### /java/seedu/investigapptor/model/Investigapptor.java
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
                if (key.getCurrentInvestigator().getName().equals(person.getName())) {
                    Investigator investigator = (Investigator) person;
                    investigator.removeCrimeCase(key);
                    break;
                }
            }
        }
    }

    //// tag-level operations

```
###### /java/seedu/investigapptor/storage/XmlAdaptedCrimeCase.java
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
                    Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(this.startDate)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        final Date startDate = new Date(this.startDate);

        if (this.endDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(this.endDate)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        final Date endDate = new Date(this.endDate);

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
