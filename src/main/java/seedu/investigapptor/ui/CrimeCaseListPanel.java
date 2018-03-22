package seedu.investigapptor.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.events.ui.CrimeCasePanelSelectionChangedEvent;
import seedu.investigapptor.commons.events.ui.JumpToListRequestEvent;
import seedu.investigapptor.model.crimecase.CrimeCase;

/**
 * Panel containing the list of crimecase.
 */
public class CrimeCaseListPanel extends UiPart<Region> {
    private static final String FXML = "CrimeCaseListPanel.fxml";
    private static final String panelHeader = "Crime Cases";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<CrimeCaseCard> crimeCaseListView;

    public CrimeCaseListPanel(ObservableList<CrimeCase> caseList) {
        super(FXML);
        setConnections(caseList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<CrimeCase> caseList) {
        ObservableList<CrimeCaseCard> mappedList = EasyBind.map(
                caseList, (crimeCase) -> new CrimeCaseCard(crimeCase, caseList.indexOf(crimeCase) + 1));
        crimeCaseListView.setItems(mappedList);
        crimeCaseListView.setCellFactory(listView -> new CrimeCaseListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        crimeCaseListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in crime case list panel changed to : '" + newValue + "'");
                        raise(new CrimeCasePanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            crimeCaseListView.scrollTo(index);
            crimeCaseListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PersonCard}.
     */
    class CrimeCaseListViewCell extends ListCell<CrimeCaseCard> {

        @Override
        protected void updateItem(CrimeCaseCard caseCard, boolean empty) {
            super.updateItem(caseCard, empty);

            if (empty || caseCard == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(caseCard.getRoot());
            }
        }
    }

}
