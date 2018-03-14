package seedu.investigapptor.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.exceptions.DataConversionException;
import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.commons.util.FileUtil;
import seedu.investigapptor.model.ReadOnlyInvestigapptor;

/**
 * A class to access Investigapptor data stored as an xml file on the hard disk.
 */
public class XmlInvestigapptorStorage implements InvestigapptorStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlInvestigapptorStorage.class);

    private String filePath;

    public XmlInvestigapptorStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getInvestigapptorFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyInvestigapptor> readInvestigapptor() throws DataConversionException, IOException {
        return readInvestigapptor(filePath);
    }

    /**
     * Similar to {@link InvestigapptorStorage#readInvestigapptor()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyInvestigapptor> readInvestigapptor(String filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        requireNonNull(filePath);

        File investigapptorFile = new File(filePath);

        if (!investigapptorFile.exists()) {
            logger.info("Investigapptor file "  + investigapptorFile + " not found");
            return Optional.empty();
        }

        XmlSerializableInvestigapptor xmlInvestigapptor = XmlFileStorage.loadDataFromSaveFile(new File(filePath));
        try {
            return Optional.of(xmlInvestigapptor.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + investigapptorFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveInvestigapptor(ReadOnlyInvestigapptor investigapptor) throws IOException {
        saveInvestigapptor(investigapptor, filePath);
    }

    /**
     * Similar to {@link InvestigapptorStorage#saveInvestigapptor(ReadOnlyInvestigapptor)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveInvestigapptor(ReadOnlyInvestigapptor investigapptor, String filePath) throws IOException {
        requireNonNull(investigapptor);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableInvestigapptor(investigapptor));
    }

    @Override
    public void backupInvestigapptor(ReadOnlyInvestigapptor investigapptor) throws IOException {
        saveInvestigapptor(investigapptor, filePath + ".backup");
    }

}
