package seedu.investigapptor.logic.commands;


/**
 * Creates a xml copy of the current state of the investigapptor
 * The name of the xml is given by the user
 */
public class BackupCommand extends Command {

    public static final String COMMAND_WORD = "backup";
    public static final String COMMAND_ALIAS = "bu";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Create a backup of the current state "
            + "The backup will be saved as the given name\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " February";

    public static final String MESSAGE_SUCCESS = " backup has been created";
    private final String fileName;

    public BackupCommand(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public CommandResult execute() {
        model.backUpInvestigapptor(fileName);
        return new CommandResult(fileName + MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof BackupCommand // instanceof handles nulls
                && this.fileName.equals(((BackupCommand) other).fileName)); // state check
    }
}
