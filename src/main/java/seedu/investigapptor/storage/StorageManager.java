package seedu.investigapptor.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.investigapptor.commons.core.ComponentManager;
import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.events.model.InvestigapptorBackupEvent;
import seedu.investigapptor.commons.events.model.InvestigapptorChangedEvent;
import seedu.investigapptor.commons.events.storage.DataSavingExceptionEvent;
import seedu.investigapptor.commons.exceptions.DataConversionException;
import seedu.investigapptor.commons.exceptions.WrongPasswordException;
import seedu.investigapptor.model.Password;
import seedu.investigapptor.model.ReadOnlyInvestigapptor;
import seedu.investigapptor.model.UserPrefs;

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
    public Optional<ReadOnlyInvestigapptor> readInvestigapptor()
            throws DataConversionException, IOException, WrongPasswordException {
        return readInvestigapptor(investigapptorStorage.getInvestigapptorFilePath());
    }

    @Override
    public Optional<ReadOnlyInvestigapptor> readInvestigapptor(String filePath)
            throws DataConversionException, IOException, WrongPasswordException {
        logger.fine("Attempting to read data from file: " + filePath);
        return investigapptorStorage.readInvestigapptor(filePath);
    }

    //@@author quentinkhoo
    @Override
    public void readInvestigapptorWithPassword(Password password)
            throws DataConversionException, IOException, WrongPasswordException {
        investigapptorStorage.checkInvestigapptorPassword(
                investigapptorStorage.getInvestigapptorFilePath(), password);
    }

    @Override
    public void checkInvestigapptorPassword(String filePath, Password password)
            throws DataConversionException, IOException, WrongPasswordException {
        logger.fine("Attempting to read data from file: " + filePath);
        investigapptorStorage.checkInvestigapptorPassword(filePath, password);
    }
    //@@author

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
    public void backupInvestigapptor(ReadOnlyInvestigapptor investigapptor, String fileName) throws IOException {
        logger.fine("Attempting to write to data file: " + "data/" + fileName + ".xml");
        investigapptorStorage.saveInvestigapptor(investigapptor, "data/" + fileName + ".xml");
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
    @Override
    @Subscribe
    public void handleInvestigapptorBackupEvent(InvestigapptorBackupEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            backupInvestigapptor(event.data, event.fileName);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
