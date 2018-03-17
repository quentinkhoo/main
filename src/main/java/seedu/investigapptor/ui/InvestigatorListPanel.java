package seedu.investigapptor.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.events.ui.InvestigatorPanelSelectionChangedEvent;
import seedu.investigapptor.commons.events.ui.JumpToListRequestEvent;
import seedu.investigapptor.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.investigapptor.model.person.Person;
import seedu.investigapptor.model.person.investigator.Investigator;

/**
 * Panel containing the list of persons.
 */
public class InvestigatorListPanel extends UiPart<Region> {
    private static final String FXML = "InvestigatorListPanel.fxml";
    private static final String PANEL_HEADER = "Investigators";
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);

    @FXML
    private ListView<InvestigatorCard> investigatorListView;

    @FXML
    private Label investigatorListPanelHeader;

    public InvestigatorListPanel(ObservableList<Investigator> investigatorList) {
        super(FXML);
        investigatorListPanelHeader.setText(PANEL_HEADER);
        setConnections(investigatorList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Investigator> investigatorList) {
        ObservableList<InvestigatorCard> mappedList = EasyBind.map(
                investigatorList, (investigator) -> new InvestigatorCard(investigator, investigatorList.indexOf(investigator) + 1));
        investigatorListView.setItems(mappedList);
        investigatorListView.setCellFactory(listView -> new InvestigatorListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        investigatorListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in person list panel changed to : '" + newValue + "'");
                        raise(new InvestigatorPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            investigatorListView.scrollTo(index);
            investigatorListView.getSelectionModel().clearAndSelect(index);
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
    class InvestigatorListViewCell extends ListCell<InvestigatorCard> {

        @Override
        protected void updateItem(InvestigatorCard investigator, boolean empty) {
            super.updateItem(investigator, empty);

            if (empty || investigator == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(investigator.getRoot());
            }
        }
    }

}
