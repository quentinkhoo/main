package seedu.investigapptor.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.investigapptor.commons.events.model.InvestigapptorBackupEvent;
import seedu.investigapptor.commons.events.model.InvestigapptorChangedEvent;
import seedu.investigapptor.commons.events.storage.DataSavingExceptionEvent;
import seedu.investigapptor.commons.exceptions.DataConversionException;
import seedu.investigapptor.commons.exceptions.WrongPasswordException;
import seedu.investigapptor.model.Password;
import seedu.investigapptor.model.ReadOnlyInvestigapptor;
import seedu.investigapptor.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends InvestigapptorStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getInvestigapptorFilePath();

    @Override
    Optional<ReadOnlyInvestigapptor> readInvestigapptor()
            throws DataConversionException, IOException, WrongPasswordException;

    @Override
    Optional<ReadOnlyInvestigapptor> readInvestigapptor(Password password)
            throws DataConversionException, IOException, WrongPasswordException;

    @Override
    void saveInvestigapptor(ReadOnlyInvestigapptor investigapptor) throws IOException;

    @Override
    void backupInvestigapptor(ReadOnlyInvestigapptor investigapptor, String fileName) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleInvestigapptorChangedEvent(InvestigapptorChangedEvent abce);

    void handleInvestigapptorBackupEvent(InvestigapptorBackupEvent abce);
}
