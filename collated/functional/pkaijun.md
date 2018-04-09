# pkaijun
###### \java\seedu\investigapptor\logic\commands\CloseCaseCommand.java
``` java
/**
 * Update the status of a case from open to close and update the EndDate field
 */
public class CloseCaseCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "close";
    public static final String COMMAND_ALIAS = "cl";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the status from open to close "
            + "and updates the end date accordingly.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_CLOSE_CASE_SUCCESS = "Case status updated: %1$s";
    public static final String MESSAGE_DUPLICATE_CASE = "This case already exists in investigapptor.";
    public static final String MESSAGE_CASE_ALREADY_CLOSE = "Case is already closed.";

    private final Index index;

    private CrimeCase caseToClose;
    private CrimeCase closedCase;

    /**
     * @param index of the crimecase in the filtered crimecase list to close
     */
    public CloseCaseCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateCrimeCase(caseToClose, closedCase);
        } catch (DuplicateCrimeCaseException dce) {
            throw new CommandException(MESSAGE_DUPLICATE_CASE);
        } catch (CrimeCaseNotFoundException cnfe) {
            throw new AssertionError("The target case cannot be missing");
        }
        model.updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        return new CommandResult(String.format(MESSAGE_CLOSE_CASE_SUCCESS, closedCase.getStatus()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        EventsCenter.getInstance().post(new SwapTabEvent(1));   // List results toggles to case tab

        List<CrimeCase> lastShownList = model.getFilteredCrimeCaseList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
        }

        caseToClose = lastShownList.get(index.getZeroBased());

        if (caseToClose.getStatus().toString().equals(CASE_CLOSE)) {
            throw new CommandException(MESSAGE_CASE_ALREADY_CLOSE);
        }

        closedCase = createEditedCase(caseToClose);
    }

    /**
     * Creates and returns a {@code CrimeCase} with the details of {@code caseToEdit}
     * Updates status to "close" with the other fields remaining the same
     */
    private static CrimeCase createEditedCase(CrimeCase caseToClose) {
        assert caseToClose != null;

        CaseName name = caseToClose.getCaseName();
        Description desc = caseToClose.getDescription();
        Date startDate = caseToClose.getStartDate();
        Date endDate = new Date(Date.getTodayDate());
        Set<Tag> tags = caseToClose.getTags();
        Investigator investigator = caseToClose.getCurrentInvestigator();
        Status status = caseToClose.getStatus();
        status.closeCase();    // Close case status only

        return new CrimeCase(name, desc, investigator, startDate, endDate, status, tags);
    }
}
```
###### \java\seedu\investigapptor\logic\commands\FindCaseTagsCommand.java
``` java
/**
 * Finds and lists all investigators in investigapptor whose tags contains any of the argument keywords.
 * Keyword matching is not case-sensitive.
 */
public class FindCaseTagsCommand extends Command {

    public static final String COMMAND_WORD = "findCaseTags";
    public static final String COMMAND_ALIAS = "fct";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds cases whose tags contain any of "
            + "the specified keywords and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " Homicide";

    private final TagContainsKeywordsPredicate predicate;

    public FindCaseTagsCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredCrimeCaseList(predicate);
        EventsCenter.getInstance().post(new SwapTabEvent(1));   // List results toggles to case tab
        return new CommandResult(getMessageForCrimeListShownSummary(model.getFilteredCrimeCaseList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCaseTagsCommand // instanceof handles nulls
                && this.predicate.equals(((FindCaseTagsCommand) other).predicate)); // state check
    }
}
```
###### \java\seedu\investigapptor\logic\commands\FindInvestTagsCommand.java
``` java
/**
 * Finds and lists all investigators in investigapptor whose tags contains any of the argument keywords.
 * Keyword matching is not case-sensitive.
 */
public class FindInvestTagsCommand extends Command {

    public static final String COMMAND_WORD = "findInvestTags";
    public static final String COMMAND_ALIAS = "fit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds investigators whose tags contain any of "
            + "the specified keywords and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " teamA";

    private final TagContainsKeywordsPredicate predicate;

    public FindInvestTagsCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(predicate);
        EventsCenter.getInstance().post(new SwapTabEvent(0));   // List results toggles to investigators tab
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindInvestTagsCommand // instanceof handles nulls
                && this.predicate.equals(((FindInvestTagsCommand) other).predicate)); // state check
    }
}
```
###### \java\seedu\investigapptor\logic\parser\CloseCaseCommandParser.java
``` java
package seedu.investigapptor.logic.parser;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.logic.commands.CloseCaseCommand;
import seedu.investigapptor.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new CloseCaseCommandParser object
 */
public class CloseCaseCommandParser implements Parser<CloseCaseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CloseCaseCommand
     * and returns an CloseCaseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CloseCaseCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new CloseCaseCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    CloseCaseCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\investigapptor\logic\parser\FindCaseTagsCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindInvestTagsCommand object
 */
public class FindCaseTagsCommandParser implements Parser<FindCaseTagsCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindInvestTagsCommandParser
     * and returns an FindInvestTagsCommandParser object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCaseTagsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCaseTagsCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.toLowerCase().split("\\s+");

        return new FindCaseTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
```
###### \java\seedu\investigapptor\logic\parser\FindInvestTagsCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindInvestTagsCommand object
 */
public class FindInvestTagsCommandParser implements Parser<FindInvestTagsCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindInvestTagsCommandParser
     * and returns an FindInvestTagsCommandParser object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindInvestTagsCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindInvestTagsCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.toLowerCase().split("\\s+");

        return new FindInvestTagsCommand(new TagContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
    }
}
```
###### \java\seedu\investigapptor\model\crimecase\CrimeCase.java
``` java
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
```
###### \java\seedu\investigapptor\model\crimecase\TagContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Person}'s {@code Tags} matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<CrimeCase> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /* Returns true if keywords matches with any element in the set of tags of a person
     */
    @Override
    public boolean test(CrimeCase crimecase) {
        return keywords.stream()
                .anyMatch(crimecase.getTagsRaw()::contains);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }
}
```
###### \java\seedu\investigapptor\model\Investigapptor.java
``` java
    /**
     * Replaces the given case {@code target} in the list with {@code editedCase}.
     * {@code Investigapptor}'s tag list will be updated with the tags of {@code editedCase}.
     *
     * @throws DuplicateCrimeCaseException if updating the crimeCase's details causes the crimeCase to be equivalent to
     *                                  another existing crimeCase in the list.
     * @throws CrimeCaseNotFoundException  if {@code target} could not be found in the list.
     * @see #syncWithMasterTagList(CrimeCase)
     */
    public void updateCrimeCase(CrimeCase target, CrimeCase editedCase)
            throws DuplicateCrimeCaseException, CrimeCaseNotFoundException {
        requireNonNull(editedCase);

        CrimeCase syncedEditedCrimeCase = syncWithMasterTagList(editedCase);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the crimeCase list.
        removeCrimeCaseFromInvestigator(target);
        cases.setCrimeCase(target, syncedEditedCrimeCase);
        addCrimeCaseToInvestigator(syncedEditedCrimeCase);
    }
```
###### \java\seedu\investigapptor\model\person\investigator\TagContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Person}'s {@code Tags} matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /* Returns true if keywords matches with any element in the set of tags of a person
     */
    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(person.getTagsRaw()::contains);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }
}
```
###### \java\seedu\investigapptor\ui\CalendarPanel.java
``` java
/**
 * The CalendarPanel of the Application which displays an overview of the duration of all the cass
 */
public class CalendarPanel extends UiPart<Region> {
    private static final String FXML = "CalendarPanel.fxml";
    private static final Style[] ALL_STYLES = { Style.STYLE1, Style.STYLE2, Style.STYLE3, Style.STYLE4,
        Style.STYLE5, Style.STYLE6, Style.STYLE7 };
    private static final String CLOSED_CASE_CALENDAR = "Closed Cases";
    private static final String OPENED_CASE_CALENDAR = "Opened Cases";
    private static final String CALENDAR_SOURCE = "All Cases";

    private Calendar caseCloseCalendar;
    private Calendar caseOpenCalendar;
    private CalendarSource caseCalendarSource;

    private ObservableList<CrimeCase> crimeList;

    @FXML
    private CalendarView calendarPanel;

    public CalendarPanel(ObservableList<CrimeCase> crimeList) {
        super(FXML);

        this.crimeList = crimeList;

        /** Constructing the new various calendar objects **/
        calendarPanel = new CalendarView();
        caseCalendarSource = new CalendarSource(CALENDAR_SOURCE);  // Contains calendars
        caseCloseCalendar = new Calendar(CLOSED_CASE_CALENDAR);  // Contains entries of cases that are closed
        caseOpenCalendar = new Calendar(OPENED_CASE_CALENDAR);  // Contains entries of cases that are opened

        /** Setting the defaults **/
        setCalendarView();
        setCalendar();

        /** Creating the calendar entries **/
        createCalendarEntries();

        /** Creating the calendar entries and adding it to the calendar **/
        addToCalendar();

        registerAsAnEventHandler(this);
    }

    public CalendarView getViewPanel() {
        return this.calendarPanel;
    }

    /**
     * Create canlendar entries for all the cases in the crime list
     */
    private void createCalendarEntries() {
        Date endDate;
        Date startDate;
        String caseName;
        String status;

        for (CrimeCase crimecase : crimeList) {
            status = crimecase.getStatus().toString();
            startDate = crimecase.getStartDate();
            endDate = crimecase.getEndDate();
            caseName = crimecase.getCaseName().toString();

            setEntry(startDate, endDate, status, caseName);
        }
    }

    /**
     * Creates an entry and adds it to the respective calendar according to its status
     * @param startDate
     * @param endDate
     * @param status
     * @param caseName
     */
    private void setEntry(StartDate startDate, EndDate endDate, String status, String caseName) {
        Entry<String> caseEntry = new Entry<>(caseName);
        caseEntry.changeStartDate(LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDay()));
        caseEntry.setFullDay(true);

        if (status.equals(CASE_OPEN)) {
            caseEntry.changeEndDate(caseEntry.getStartDate());
            caseOpenCalendar.addEntry(caseEntry);
        } else if (status.equals(CASE_CLOSE)) {
            caseEntry.changeEndDate(LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDay()));
            caseCloseCalendar.addEntry(caseEntry);
        } else {
            assert(false);  // Should not reach here
        }
    }

    /**
     * Configure the view of the calendar
     */
    private void setCalendarView() {
        calendarPanel.setShowAddCalendarButton(false);
        calendarPanel.setShowSearchField(false);
        calendarPanel.setShowSearchResultsTray(false);
        calendarPanel.setShowPrintButton(false);
        calendarPanel.showMonthPage();
        calendarPanel.setShowAddCalendarButton(false);
        calendarPanel.setShowToday(true);
    }

    /**
     * Configure the settings of the calendars
     */
    private void setCalendar() {
        caseCloseCalendar.setReadOnly(true);
        caseCloseCalendar.setStyle(ALL_STYLES[0]);

        caseOpenCalendar.setReadOnly(true);
        caseOpenCalendar.setStyle(ALL_STYLES[4]);
    }

    /**
     * Add the entries to the calendar view and source
     */
    private void addToCalendar() {
        caseCalendarSource.getCalendars().add(caseCloseCalendar);
        caseCalendarSource.getCalendars().add(caseOpenCalendar);
        calendarPanel.getCalendarSources().addAll(caseCalendarSource);
    }

    @Subscribe
    private void handleNewCaseEvent(InvestigapptorChangedEvent event) {
        crimeList = event.data.getCrimeCaseList();
        Platform.runLater(this::updateCalendar);
    }

    /**
     * Updates the calendar whenever there is a change made to the crimecaselist
     */
    private void updateCalendar() {
        caseCloseCalendar.clear();
        caseOpenCalendar.clear();
        createCalendarEntries();
    }
}
```
###### \java\seedu\investigapptor\ui\PersonCard.java
``` java
    /**
     *
     * Creates tag labels for person
     */
    private void colorTag(Person person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(getTagColorStyle(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

    /**
     *
     * @param tagName
     * @return Colour in the array
     */
    private String getTagColorStyle(String tagName) {
        // Hash the tag name to get the corresponding colour
        return LABEL_COLOR[Math.abs(tagName.hashCode()) % LABEL_COLOR.length];
    }
```
###### \resources\view\DarkTheme.css
``` css
#tags .red {
    -fx-text-fill: black;
    -fx-background-color: #FF0000;
}

#tags .yellow {
    -fx-text-fill: black;
    -fx-background-color: #FFFF00;
}

#tags .blue {
    -fx-text-fill: black;
    -fx-background-color: #76D3E2;
}

#tags .orange {
    -fx-text-fill: black;
    -fx-background-color: #F9C815;
}

#tags .pink {
    -fx-text-fill: black;
    -fx-background-color: #F915EE;
}

#tags .olive {
    -fx-text-fill: black;
    -fx-background-color: #72B07C;
}

#tags .black {
    -fx-text-fill: white;
    -fx-background-color: #000000;
}

#tags .brown {
    -fx-text-fill: black;
    -fx-background-color: #CD853F;
}

#tags .gray {
    -fx-text-fill: black;
    -fx-background-color: #b6b6b6;
}

#tags .green {
    -fx-text-fill: black;
    -fx-background-color: #00ff06;
}

#tags .beige {
    -fx-text-fill: black;
    -fx-background-color: #FFE4C4;
}

#tags .lightblue {
    -fx-text-fill: black;
    -fx-background-color: #27fffa;
}

#tags .golden {
    -fx-text-fill: black;
    -fx-background-color: #DAA520;
}

#tags .purple {
    -fx-text-fill: black;
    -fx-background-color: #bc99da;
}


```
###### \resources\view\MainWindow.fxml
``` fxml
          <StackPane fx:id="calendarPanelPlaceholder" prefWidth="340" >
            <padding>
              <Insets top="10" right="10" bottom="10" left="10" />
            </padding>
          </StackPane>
```
