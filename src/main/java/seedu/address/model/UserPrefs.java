package seedu.address.model;

import java.util.Objects;

import seedu.address.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private GuiSettings guiSettings;
    private String investigapptorFilePath = "data/investigapptor.xml";
    private String investigapptorName = "MyInvestigapptor";

    public UserPrefs() {
        this.setGuiSettings(500, 500, 0, 0);
    }

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }

    public String getInvestigapptorFilePath() {
        return investigapptorFilePath;
    }

    public void setInvestigapptorFilePath(String investigapptorFilePath) {
        this.investigapptorFilePath = investigapptorFilePath;
    }

    public String getInvestigapptorName() {
        return investigapptorName;
    }

    public void setInvestigapptorName(String investigapptorName) {
        this.investigapptorName = investigapptorName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(guiSettings, o.guiSettings)
                && Objects.equals(investigapptorFilePath, o.investigapptorFilePath)
                && Objects.equals(investigapptorName, o.investigapptorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, investigapptorFilePath, investigapptorName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal data file location : " + investigapptorFilePath);
        sb.append("\nInvestigapptor name : " + investigapptorName);
        return sb.toString();
    }

}
