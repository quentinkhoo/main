package seedu.address.storage;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalInvestigapptor;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.events.model.InvestigapptorChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.model.Investigapptor;
import seedu.address.model.ReadOnlyInvestigapptor;
import seedu.address.model.UserPrefs;
import seedu.address.ui.testutil.EventsCollectorRule;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlInvestigapptorStorage investigapptorStorage = new XmlInvestigapptorStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(investigapptorStorage, userPrefsStorage);
    }

    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void investigapptorReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlInvestigapptorStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlInvestigapptorStorageTest} class.
         */
        Investigapptor original = getTypicalInvestigapptor();
        storageManager.saveInvestigapptor(original);
        ReadOnlyInvestigapptor retrieved = storageManager.readInvestigapptor().get();
        assertEquals(original, new Investigapptor(retrieved));
    }

    @Test
    public void getInvestigapptorFilePath() {
        assertNotNull(storageManager.getInvestigapptorFilePath());
    }

    @Test
    public void handleInvestigapptorChangedEvent_exceptionThrown_eventRaised() {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlInvestigapptorStorageExceptionThrowingStub("dummy"),
                                             new JsonUserPrefsStorage("dummy"));
        storage.handleInvestigapptorChangedEvent(new InvestigapptorChangedEvent(new Investigapptor()));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlInvestigapptorStorageExceptionThrowingStub extends XmlInvestigapptorStorage {

        public XmlInvestigapptorStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveInvestigapptor(ReadOnlyInvestigapptor investigapptor, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
