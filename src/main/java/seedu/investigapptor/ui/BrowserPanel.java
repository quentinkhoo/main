package seedu.investigapptor.ui;

import java.net.URL;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.investigapptor.MainApp;
import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.events.ui.CrimeCasePanelSelectionChangedEvent;
import seedu.investigapptor.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String CASE_DETAILS_PAGE = "CaseDetailsPage.html";
    public static final String SEARCH_PAGE_URL =
            "https://se-edu.github.io/addressbook-level4/DummySearchPage.html?name=";

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    @FXML
    private WebView browser;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    //@@author leowweiching
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
    //@@author
    private void loadPersonPage(Person person) {
        loadPage(SEARCH_PAGE_URL + person.getName().fullName);
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

    //@@author leowweiching
    /**
     * Loads the case details HTML file with a background that matches the general theme.
     */
    private void loadCaseDetailsPage(String caseName, String description, Investigator currentInvestigator,
                                     String startDate, String endDate, String status, String tagList) {
        URL caseDetailsPage = MainApp.class.getResource(FXML_FILE_FOLDER + CASE_DETAILS_PAGE);
        loadPage(caseDetailsPage.toExternalForm()
                + "?caseName=" + caseName
                + "&description=" + description
                + "&tags=" + tagList
                + "&invName=" + currentInvestigator.getName().fullName
                + "&invRank=" + currentInvestigator.getRank().toString()
                + "&invPhone=" + currentInvestigator.getPhone().value
                + "&invEmail=" + currentInvestigator.getEmail().value
                + "&invAddress=" + currentInvestigator.getAddress().value
                + "&startDate=" + startDate
                + "&endDate=" + endDate
                + "&status=" + status);
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

    //@@author
    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }

    //@@author leowweiching
    @Subscribe
    private void handleCrimeCasePanelSelectionChangedEvent(CrimeCasePanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadCrimeCasePage(event.getNewSelection().crimeCase);
    }
    //@@author
}
