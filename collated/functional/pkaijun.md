# pkaijun
###### \java\seedu\investigapptor\logic\commands\CloseCaseCommand.java
``` java
package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_CASES;
import static seedu.investigapptor.model.crimecase.Status.CASE_CLOSE;

import java.util.List;
import java.util.Set;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.core.index.Index;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.crimecase.CaseName;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Date;
import seedu.investigapptor.model.crimecase.Description;
import seedu.investigapptor.model.crimecase.Status;
import seedu.investigapptor.model.crimecase.exceptions.CrimeCaseNotFoundException;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;
import seedu.investigapptor.model.person.investigator.Investigator;
import seedu.investigapptor.model.tag.Tag;

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

    private CrimeCase caseToEdit;
    private CrimeCase editedCase;

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
            model.updateCrimeCase(caseToEdit, editedCase);
        } catch (DuplicateCrimeCaseException dce) {
            throw new CommandException(MESSAGE_DUPLICATE_CASE);
        } catch (CrimeCaseNotFoundException cnfe) {
            throw new AssertionError("The target case cannot be missing");
        }
        model.updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        return new CommandResult(String.format(MESSAGE_CLOSE_CASE_SUCCESS, editedCase.getStatus()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        EventsCenter.getInstance().post(new SwapTabEvent(1));   // List results toggles to case tab

        List<CrimeCase> lastShownList = model.getFilteredCrimeCaseList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CASE_DISPLAYED_INDEX);
        }

        caseToEdit = lastShownList.get(index.getZeroBased());

        if (caseToEdit.getStatus().toString().equals(CASE_CLOSE)) {
            throw new CommandException(MESSAGE_CASE_ALREADY_CLOSE);
        }

        editedCase = createEditedCase(caseToEdit);
    }

    /**
     * Creates and returns a {@code CrimeCase} with the details of {@code caseToEdit}
     * Updates status to "close" with the other fields remaining the same
     */
    private static CrimeCase createEditedCase(CrimeCase caseToEdit) {
        assert caseToEdit != null;

        CaseName name = caseToEdit.getCaseName();
        Description desc = caseToEdit.getDescription();
        Date startDate = caseToEdit.getStartDate();
        Date endDate = new Date(Date.getTodayDate());
        Set<Tag> tags = caseToEdit.getTags();
        Investigator investigator = caseToEdit.getCurrentInvestigator();
        Status status = caseToEdit.getStatus();
        status.closeCase();    // Close case status only

        return new CrimeCase(name, desc, investigator, startDate, endDate, status, tags);
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

    /**
     * Removes {@code key} from this {@code Investigapptor}.
     *
     * @throws CrimeCaseNotFoundException if the {@code key} is not in this {@code Investigapptor}.
     */
    public boolean removeCrimeCase(CrimeCase key) throws CrimeCaseNotFoundException {
        if (cases.remove(key)) {
            return true;
        } else {
            throw new CrimeCaseNotFoundException();
        }
    }

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

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

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

    /**
     * Updates the master tag list to include tags in {@code crimeCase} that are not in the list.
     *
     * @return a copy of this {@code crimeCase} such that every tag in this case points to a Tag object in the master
     * list.
     */
    private CrimeCase syncWithMasterTagList(CrimeCase crimeCase) {
        final UniqueTagList crimeCaseTags = new UniqueTagList(crimeCase.getTags());
        tags.mergeFrom(crimeCaseTags);

        // Create map with values = tag object references in the master list
        // used for checking case tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of case tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        crimeCaseTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        Investigator investigator = (Investigator) syncWithMasterTagList(crimeCase.getCurrentInvestigator());
        investigator.clearCaseList(); // Fix for undo/redo: Clears investigator case list
        return new CrimeCase(
                crimeCase.getCaseName(), crimeCase.getDescription(), investigator,
                crimeCase.getStartDate(), crimeCase.getEndDate(), crimeCase.getStatus(), correctTagReferences);
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
    private void setEntry(Date startDate, Date endDate, String status, String caseName) {
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
