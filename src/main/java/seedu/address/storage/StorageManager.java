package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.InvestigapptorChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyInvestigapptor;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of Investigapptor data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private InvestigapptorStorage investigapptorStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(InvestigapptorStorage investigapptorStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.investigapptorStorage = investigapptorStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public String getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ Investigapptor methods ==============================

    @Override
    public String getInvestigapptorFilePath() {
        return investigapptorStorage.getInvestigapptorFilePath();
    }

    @Override
    public Optional<ReadOnlyInvestigapptor> readInvestigapptor() throws DataConversionException, IOException {
        return readInvestigapptor(investigapptorStorage.getInvestigapptorFilePath());
    }

    @Override
    public Optional<ReadOnlyInvestigapptor> readInvestigapptor(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return investigapptorStorage.readInvestigapptor(filePath);
    }

    @Override
    public void saveInvestigapptor(ReadOnlyInvestigapptor investigapptor) throws IOException {
        saveInvestigapptor(investigapptor, investigapptorStorage.getInvestigapptorFilePath());
    }

    @Override
    public void saveInvestigapptor(ReadOnlyInvestigapptor investigapptor, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        investigapptorStorage.saveInvestigapptor(investigapptor, filePath);
    }

    @Override
    public void backupInvestigapptor(ReadOnlyInvestigapptor investigapptor) throws IOException {
        investigapptorStorage.backupInvestigapptor(investigapptor);
    }


    @Override
    @Subscribe
    public void handleInvestigapptorChangedEvent(InvestigapptorChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveInvestigapptor(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
