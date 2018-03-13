package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.events.model.InvestigapptorChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyInvestigapptor;
import seedu.address.model.UserPrefs;

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
    Optional<ReadOnlyInvestigapptor> readInvestigapptor() throws DataConversionException, IOException;

    @Override
    void saveInvestigapptor(ReadOnlyInvestigapptor investigapptor) throws IOException;

    @Override
    void backupInvestigapptor(ReadOnlyInvestigapptor investigapptor) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleInvestigapptorChangedEvent(InvestigapptorChangedEvent abce);
}
