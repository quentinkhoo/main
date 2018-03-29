package seedu.investigapptor.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.investigapptor.commons.exceptions.DataConversionException;
import seedu.investigapptor.commons.exceptions.WrongPasswordException;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Password;
import seedu.investigapptor.model.ReadOnlyInvestigapptor;

/**
 * Represents a storage for {@link Investigapptor}.
 */
public interface InvestigapptorStorage {

    /**
     * Returns the file path of the data file.
     */
    String getInvestigapptorFilePath();

    /**
     * Returns Investigapptor data as a {@link ReadOnlyInvestigapptor}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyInvestigapptor> readInvestigapptor()
            throws DataConversionException, IOException, WrongPasswordException;

    /**
     * @see #getInvestigapptorFilePath()
     */
    Optional<ReadOnlyInvestigapptor> readInvestigapptor(String filePath)
            throws DataConversionException, IOException, WrongPasswordException;

    /**
     * @see #getInvestigapptorFilePath()
     */
    Optional<ReadOnlyInvestigapptor> readInvestigapptorWithPassword(Password password)
            throws DataConversionException, IOException, WrongPasswordException;

    /**
     * @see #getInvestigapptorFilePath()
     */
    Optional<ReadOnlyInvestigapptor> checkInvestigapptorPassword(String filePath, Password password)
            throws DataConversionException, IOException, WrongPasswordException;

    /**
     * Saves the given {@link ReadOnlyInvestigapptor} to the storage.
     * @param investigapptor cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveInvestigapptor(ReadOnlyInvestigapptor investigapptor) throws IOException;

    /**
     * @see #saveInvestigapptor(ReadOnlyInvestigapptor)
     */
    void saveInvestigapptor(ReadOnlyInvestigapptor investigapptor, String filePath) throws IOException;

    /**
     * Creates a backup of the given {@link ReadOnlyInvestigapptor} to the storage.
     * @param investigapptor cannot be null.
     * @param fileName
     * @throws IOException if there was any problem writing to the file.
     */
    void backupInvestigapptor(ReadOnlyInvestigapptor investigapptor, String fileName) throws IOException;

}
