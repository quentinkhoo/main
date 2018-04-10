package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.model.crimecase.Status.CASE_CLOSE;

//@@author pkaijun
/**
 * Finds and lists all cases in investigapptor whose status are closed
 */
public class FindCloseCaseCommand extends FindByStatusCommand {
    public static final String COMMAND_WORD = "findclosecases";
    public static final String COMMAND_ALIAS = "fcc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds cases whose status is "
            + CASE_CLOSE + ".\n"
            + "Example: " + COMMAND_WORD;

    public FindCloseCaseCommand() {
        super(CASE_CLOSE);
    }
}
