//@@author pkaijun
package seedu.investigapptor.ui;

import com.calendarfx.model.Entry;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.layout.Region;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;

import seedu.investigapptor.commons.events.ui.CrimeCasePanelSelectionChangedEvent;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.Date;
import seedu.investigapptor.model.crimecase.Status;

import java.time.LocalDate;
import java.util.Random;
import java.util.logging.Logger;

/**
 * The CalendarPanel of the Application which displays an overview of the duration of all the cass
 */
public class CalendarPanel extends UiPart<Region>{
    private static final String FXML = "CalendarPanel.fxml";
    private static final Style[] ALL_STYLES = { Style.STYLE1, Style.STYLE2, Style.STYLE3,
            Style.STYLE4, Style.STYLE5, Style.STYLE6, Style.STYLE7 };

    private static Calendar caseCalendar;
    private static CalendarSource caseCalendarSource;

    @FXML
    private CalendarView calendarPanel;

    public CalendarPanel(ObservableList<CrimeCase> crimeList) {
        super(FXML);

        /** Constructing the new various calendar objects **/
        calendarPanel = new CalendarView();
        caseCalendarSource = new CalendarSource();  // Contains calendars

        /** Setting the defaults **/
        setCalendarView();

        /** Creating the calendar entries **/
        createCalendarEntries(crimeList);

        registerAsAnEventHandler(this);
    }


    public CalendarView getViewPanel() {
        return this.calendarPanel;
    }

    /**
     * Create canlendar entries for all the cases in the crime list
     * @param crimeList
     */
    private void createCalendarEntries(ObservableList<CrimeCase> crimeList) {
        Date endDate;
        Date startDate;
        String caseName;
        String status;

        for (CrimeCase crimecase : crimeList) {
            status = crimecase.getStatus().toString();
            startDate = crimecase.getStartDate();
            switch(status) {
                case Status.CASE_OPEN:
            }
            endDate = crimecase.getEndDate();
            caseName = crimecase.getCaseName().toString();

            createCalendar(caseName);
            setEntry(startDate, endDate, caseName);

            /** Creating the calendar entries and adding it to the calendar **/
            addToCalendar();
        }
    }

    /**
     * Create a calendar for each entry
     */
    private void createCalendar(String caseName) {
        caseCalendar = new Calendar(caseName);  // Contains entry
        setCalendar();
    }

    /**
     * Creates an entry and adds it to the calendar
     * @param startDate
     * @param endDate
     * @param caseName
     */
    private void setEntry(Date startDate, Date endDate, String caseName) {
        Entry<String> caseSpan = new Entry<>(caseName);
        caseSpan.changeStartDate(LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDay()));
        caseSpan.changeEndDate(LocalDate.of(endDate.getYear(), endDate.getMonth(), endDate.getDay()));
        caseSpan.setFullDay(true);

        caseCalendar.addEntry(caseSpan);
    }


    /**
     * Configure the view of the calendar
     */
    private void setCalendarView() {
        calendarPanel.setShowToday(true);
        calendarPanel.showMonthPage();
        calendarPanel.setShowSearchField(false);
        //calendarPanel.setMouseTransparent(true);    // Prevents user from clicking on the calendar
        calendarPanel.setShowPageSwitcher(false);
        calendarPanel.setShowPrintButton(true);
        calendarPanel.setShowToolBar(false);
        calendarPanel.setShowAddCalendarButton(false);
        calendarPanel.setShowPageToolBarControls(false);
        calendarPanel.setShowSearchField(false);
    }

    /**
     * Configure the settings of the calendar
     */
    private void setCalendar() {
        caseCalendar.setReadOnly(true);
        caseCalendar.setStyle(randomGenerateStyle());
    }

    private Style randomGenerateStyle() {
        Random random = new Random();
        int num = random.nextInt(6 - 0 + 1) + 0;
        return ALL_STYLES[num];
    }

    /**
     * Add the entries to the calendar view and source
     */
    private void addToCalendar() {
        caseCalendarSource.getCalendars().add(caseCalendar);
        calendarPanel.getCalendarSources().addAll(caseCalendarSource);
    }

}
