package seedu.investigapptor.ui;

import static seedu.investigapptor.model.crimecase.Status.CASE_CLOSE;
import static seedu.investigapptor.model.crimecase.Status.CASE_OPEN;

import java.time.LocalDate;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import seedu.investigapptor.commons.events.model.InvestigapptorChangedEvent;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Date;

//@@author pkaijun
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
