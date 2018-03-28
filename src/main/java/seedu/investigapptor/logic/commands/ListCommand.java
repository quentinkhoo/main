package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.commons.core.Messages.MESSAGE_INVALID_COMMAND_ALIAS;

import seedu.investigapptor.logic.commands.exceptions.CommandException;

/**
 * Lists all persons in the investigapptor book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "l";
    public static final String[] TYPE_INVESTIGATOR_ALIASES = {"investigators", "investigator", "invest", "inv", "i"};
    public static final String[] TYPE_COMMAND_CASE_ALIASES = {"cases", "case", "cas", "c"};
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists the specified type\n"
            + "Parameters: TYPE (must be either investigators or cases)\n"
            + "Example: " + COMMAND_WORD + " cases";
    public static final String MESSAGE_ALIASES =
            "investigators: Can either be investigators, investigator, invest, inv or i\n"
            + "Example: " + COMMAND_WORD + " invest\n"
            + "cases: Can either be cases, case, cas or c\n"
            + "Example: " + COMMAND_WORD + " cas";

    public static final String MESSAGE_SUCCESS = "Listed all %1$s";

    private final String listType;

    public ListCommand(String listType) {
        this.listType = listType;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (isValidInvestigatorAlias(listType)) {
            return new ListInvestigatorCommand(model).executeListInvestigators();

        } else if (isValidCaseAlias(listType)) {
            return new ListCaseCommand(model).executeListCases();

        } else {
            throw new CommandException(MESSAGE_INVALID_COMMAND_ALIAS);
        }

    }

    /**
     * Checks and returns an input listing type is a valid investigator alias
     * @param type
     */
    public static boolean isValidInvestigatorAlias(String type) {
        for (String alias : TYPE_INVESTIGATOR_ALIASES) {
            if (type.equals(alias)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks and returns an input listing type is a valid case alias
     * @param type
     */
    public static boolean isValidCaseAlias(String type) {
        for (String alias : TYPE_COMMAND_CASE_ALIASES) {
            if (type.equals(alias)) {
                return true;
            }
        }
        return false;
    }
}
