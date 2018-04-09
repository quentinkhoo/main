package seedu.investigapptor.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import guitests.guihandles.PasswordBoxHandle;
import javafx.scene.input.KeyCode;
import seedu.investigapptor.commons.events.ui.ValidPasswordEvent;
import seedu.investigapptor.commons.util.FileUtil;
import seedu.investigapptor.storage.JsonUserPrefsStorage;
import seedu.investigapptor.storage.Storage;
import seedu.investigapptor.storage.StorageManager;
import seedu.investigapptor.storage.XmlInvestigapptorStorage;
import seedu.investigapptor.ui.testutil.EventsCollectorRule;


//@@author quentinkhoo
public class PasswordBoxTest extends GuiUnitTest {
    private static final String CORRECT_PASSWORD = "password";
    private static final String WRONG_PASSWORD = "p@ssword";
    private static final String TEST_DATA_FOLDER = FileUtil.getPath(
            "src/test/data/XmlSerializableInvestigapptorTest/");

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private ArrayList<String> defaultStyleOfPasswordBox;
    private ArrayList<String> errorStyleOfPasswordBox;
    private PasswordBoxHandle passwordBoxHandle;


    @Before
    public void setUp() throws Exception {

        Storage storageManager = setUpStorage();

        PasswordBox passwordBox = new PasswordBox(storageManager);
        passwordBoxHandle = new PasswordBoxHandle(getChildNode(passwordBox.getRoot(),
                PasswordBoxHandle.PASSWORD_INPUT_FIELD_ID));
        uiPartRule.setUiPart(passwordBox);

        defaultStyleOfPasswordBox = new ArrayList<>(passwordBoxHandle.getStyleClass());

        errorStyleOfPasswordBox = new ArrayList<>(defaultStyleOfPasswordBox);
        errorStyleOfPasswordBox.add(CommandBox.ERROR_STYLE_CLASS);
    }

    private String getTestFilePath(String fileName) {
        return TEST_DATA_FOLDER + fileName;
    }
    private Storage setUpStorage() {
        XmlInvestigapptorStorage addressBookStorage = new XmlInvestigapptorStorage(getTestFilePath(
                "typicalPasswordInvestigapptor.xml"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTestFilePath("prefs"));
        return new StorageManager(addressBookStorage, userPrefsStorage);
    }

    @Test
    public void passwordBox_startingWithWrongPassword() {
        assertBehaviorForWrongPassword();
    }

    @Test
    public void passwordBox_startingWithCorrectPassword() {
        assertBehaviorForCorrectPassword();
    }

    @Test
    public void passwordBox_handleKeyPress() {
        passwordBoxHandle.run(WRONG_PASSWORD);
        assertEquals(errorStyleOfPasswordBox, passwordBoxHandle.getStyleClass());

        guiRobot.push(KeyCode.ESCAPE);
        assertEquals("", passwordBoxHandle.getInput());

        guiRobot.push(KeyCode.A);
        assertEquals(defaultStyleOfPasswordBox, passwordBoxHandle.getStyleClass());
    }

    /**
     * Input a wrong password, then verify that <br>
     *      - the text remains resets <br>
     *      - the command box's style is the same as {@code errorStyleOfCommandBox}.
     */
    private void assertBehaviorForWrongPassword() {
        passwordBoxHandle.run(WRONG_PASSWORD);
        assertEquals("", passwordBoxHandle.getInput());
        assertEquals(errorStyleOfPasswordBox, passwordBoxHandle.getStyleClass());
    }

    /**
     * Input the correct password, then verifies that
     *      - the event {@code ValidPasswordEvent} is raised.
     */
    private void assertBehaviorForCorrectPassword() {
        passwordBoxHandle.run(CORRECT_PASSWORD);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ValidPasswordEvent);
    }
}
