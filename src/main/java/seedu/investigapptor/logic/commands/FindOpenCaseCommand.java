package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.model.crimecase.Status.CASE_OPEN;

//@@author pkaijun
/**
 * Finds and lists all cases in investigapptor whose status are opened
 */
public class FindOpenCaseCommand extends FindByStatusCommand {
    public static final String COMMAND_WORD = "findopencases";
    public static final String COMMAND_ALIAS = "foc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds cases whose status is "
            + CASE_OPEN + ".\n"
            + "Example: " + COMMAND_WORD;

    public FindOpenCaseCommand() {
        super(CASE_OPEN);
    }
}
