package seedu.investigapptor.storage;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.investigapptor.commons.exceptions.IllegalValueException;
import seedu.investigapptor.commons.util.FileUtil;
import seedu.investigapptor.commons.util.XmlUtil;
import seedu.investigapptor.model.Investigapptor;
//import seedu.investigapptor.testutil.TypicalCrimeCases;
import seedu.investigapptor.testutil.TypicalPersons;

public class XmlSerializableInvestigapptorTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("src/test/data/XmlSerializableInvestigapptorTest/");
    private static final File TYPICAL_CRIMECASES_FILE = new File(TEST_DATA_FOLDER
            + "typicalCrimeCasesInvestigapptor.xml");
    private static final File INVALID_CRIMECASE_FILE = new File(TEST_DATA_FOLDER
            + "invalidCrimeCaseInvestigapptor.xml");
    private static final File TYPICAL_PERSONS_FILE = new File(TEST_DATA_FOLDER + "typicalPersonsInvestigapptor.xml");
    private static final File INVALID_PERSON_FILE = new File(TEST_DATA_FOLDER + "invalidPersonInvestigapptor.xml");
    private static final File INVALID_TAG_FILE = new File(TEST_DATA_FOLDER + "invalidTagInvestigapptor.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /* TO REVIEW */
    /*
    @Test
    public void toModelType_typicalCrimeCasesFile_success() throws Exception {
        XmlSerializableInvestigapptor dataFromFile = XmlUtil.getDataFromFile(TYPICAL_CRIMECASES_FILE,
                XmlSerializableInvestigapptor.class);
        Investigapptor investigapptorFromFile = dataFromFile.toModelType();
        Investigapptor typicalCrimeCasesInvestigapptor = TypicalCrimeCases.getTypicalInvestigapptor();
        assertEquals(investigapptorFromFile, typicalCrimeCasesInvestigapptor);
    }
    */

    @Test
    public void toModelType_invalidCrimeCaseFile_throwsIllegalValueException() throws Exception {
        XmlSerializableInvestigapptor dataFromFile = XmlUtil.getDataFromFile(INVALID_CRIMECASE_FILE,
                XmlSerializableInvestigapptor.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        XmlSerializableInvestigapptor dataFromFile = XmlUtil.getDataFromFile(TYPICAL_PERSONS_FILE,
                XmlSerializableInvestigapptor.class);
        Investigapptor investigapptorFromFile = dataFromFile.toModelType();
        Investigapptor typicalPersonsInvestigapptor = TypicalPersons.getTypicalInvestigapptor();
        assertEquals(investigapptorFromFile, typicalPersonsInvestigapptor);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        XmlSerializableInvestigapptor dataFromFile = XmlUtil.getDataFromFile(INVALID_PERSON_FILE,
                XmlSerializableInvestigapptor.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }

    @Test
    public void toModelType_invalidTagFile_throwsIllegalValueException() throws Exception {
        XmlSerializableInvestigapptor dataFromFile = XmlUtil.getDataFromFile(INVALID_TAG_FILE,
                XmlSerializableInvestigapptor.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }
}
