package seedu.investigapptor;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.investigapptor.commons.core.Config;
import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.core.LogsCenter;
import seedu.investigapptor.commons.core.Version;
import seedu.investigapptor.commons.events.ui.ExitAppRequestEvent;
import seedu.investigapptor.commons.exceptions.DataConversionException;
import seedu.investigapptor.commons.exceptions.WrongPasswordException;
import seedu.investigapptor.commons.util.ConfigUtil;
import seedu.investigapptor.commons.util.StringUtil;
import seedu.investigapptor.logic.Logic;
import seedu.investigapptor.logic.LogicManager;
import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.ModelManager;
import seedu.investigapptor.model.ReadOnlyInvestigapptor;
import seedu.investigapptor.model.UserPrefs;
import seedu.investigapptor.model.util.SampleDataUtil;
import seedu.investigapptor.storage.InvestigapptorStorage;
import seedu.investigapptor.storage.JsonUserPrefsStorage;
import seedu.investigapptor.storage.Storage;
import seedu.investigapptor.storage.StorageManager;
import seedu.investigapptor.storage.UserPrefsStorage;
import seedu.investigapptor.storage.XmlInvestigapptorStorage;
import seedu.investigapptor.ui.PasswordManager;
import seedu.investigapptor.ui.Ui;
import seedu.investigapptor.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 3, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;
    private boolean hasPassword;


    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing Investigapptor ]===========================");
        super.init();

        config = initConfig(getApplicationParameter("config"));

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        userPrefs = initPrefs(userPrefsStorage);
        InvestigapptorStorage investigapptorStorage = new
                XmlInvestigapptorStorage(userPrefs.getInvestigapptorFilePath());
        storage = new StorageManager(investigapptorStorage, userPrefsStorage);

        initLogging(config);

        hasPassword = false;

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model);

        ui = new UiManager(logic, config, userPrefs);

        initEventsCenter();

    }

    private String getApplicationParameter(String parameterName) {
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s investigapptor and {@code userPrefs}. <br>
     * The data from the sample investigapptor will be used instead if {@code storage}'s investigapptor is not found,
     * or an empty investigapptor will be used instead if errors occur when reading {@code storage}'s investigapptor.
     */
    private Model initModelManager(Storage storage, UserPrefs userPrefs) {
        Optional<ReadOnlyInvestigapptor> investigapptorOptional;
        ReadOnlyInvestigapptor initialData;
        try {
            investigapptorOptional = storage.readInvestigapptor();
            if (!investigapptorOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a sample Investigapptor");
            }
            initialData = investigapptorOptional.orElseGet(SampleDataUtil::getSampleInvestigapptor);
            String currentPasswordHash = initialData.getPassword().getPassword();
            if (currentPasswordHash == null) {
                hasPassword = false;
            } else {
                hasPassword = true;
            }
        } catch (WrongPasswordException wpe) {
            initialData = new Investigapptor();
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty Investigapptor");
            initialData = new Investigapptor();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty Investigapptor");
            initialData = new Investigapptor();
        }

        return new ModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(String configFilePath) {
        Config initializedConfig;
        String configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        String prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty Investigapptor");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        if (hasPassword) {
            logger.info("Starting Password Prompt " + MainApp.VERSION);
            PasswordManager passwordManager = new PasswordManager(storage, ui);
            passwordManager.start(primaryStage);
        } else {
            logger.info("Starting Investigapptor " + MainApp.VERSION);
            ui.start(primaryStage);
        }
    }


    @Override
    public void stop() {
        logger.info("============================ [ Stopping Investigapptor ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
