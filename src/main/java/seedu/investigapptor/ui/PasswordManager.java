package seedu.investigapptor.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import seedu.investigapptor.commons.core.ComponentManager;
import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.events.ui.ValidPasswordEvent;
import seedu.investigapptor.commons.util.StringUtil;
import seedu.investigapptor.logic.Logic;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.storage.Storage;

/**
 * The manager of the UI component.
 */
public class PasswordManager extends ComponentManager implements Ui {

    public static final String ALERT_DIALOG_PANE_FIELD_ID = "alertDialogPane";

    public static final String FILE_OPS_ERROR_DIALOG_STAGE_TITLE = "File Op Error";
    public static final String FILE_OPS_ERROR_DIALOG_HEADER_MESSAGE = "Could not save data";
    public static final String FILE_OPS_ERROR_DIALOG_CONTENT_MESSAGE = "Could not save data to file";

    private static final Logger logger = LogsCenter.getLogger(UiManager.class);

    private Storage storage;
    private Model model;
    private Logic logic;
    private Ui ui;

    private PasswordWindow passwordWindow;
    private Stage primaryStage;

    public PasswordManager(Storage storage, Model model, Logic logic, Ui ui) {
        super();
        this.storage = storage;
        this.model = model;
        this.logic = logic;
        this.ui = ui;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting UI...");
        this.primaryStage = primaryStage;
        try {
            PasswordWindow pw = new PasswordWindow(primaryStage, model, logic, storage);
            pw.show();
            pw.fillInnerParts();
        } catch (Throwable e) {
            logger.severe(StringUtil.getDetails(e));
            showFatalErrorDialogAndShutdown("Fatal error during initializing", e);
        }
    }

    /**
     * Shows an error alert dialog with {@code title} and error message, {@code e},
     * and exits the application after the user has closed the alert dialog.
     */
    private void showFatalErrorDialogAndShutdown(String title, Throwable e) {
        logger.severe(title + " " + e.getMessage() + StringUtil.getDetails(e));
        showAlertDialogAndWait(Alert.AlertType.ERROR, title, e.getMessage(), e.toString());
        Platform.exit();
        System.exit(1);
    }

    void showAlertDialogAndWait(Alert.AlertType type, String title, String headerText, String contentText) {
        showAlertDialogAndWait(passwordWindow.getPrimaryStage(), type, title, headerText, contentText);
    }

    /**
     * Shows an alert dialog on {@code owner} with the given parameters.
     * This method only returns after the user has closed the alert dialog.
     */
    private static void showAlertDialogAndWait(Stage owner, AlertType type, String title, String headerText,
                                               String contentText) {
        final Alert alert = new Alert(type);
        alert.getDialogPane().getStylesheets().add("view/DarkTheme.css");
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.getDialogPane().setId(ALERT_DIALOG_PANE_FIELD_ID);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        passwordWindow.hide();
        passwordWindow.releaseResources();
    }
    @Subscribe
    private void handlePasswordCorrectEvent(ValidPasswordEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        ui.start(primaryStage);
    }
}
