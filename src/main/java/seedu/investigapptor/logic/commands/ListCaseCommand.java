package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_CASES;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;

/**
 * Lists all investigators in the investigapptor book to the user.
 */
public class ListCaseCommand extends Command {

    public static final String COMMAND_WORD = "listcases";
    public static final String COMMAND_ALIAS = "lc";
    public static final String MESSAGE_SUCCESS = "Listed all cases";

    /**
     * Swap tabs to listing all cases in the investigapptor book to the user.
     */
    public CommandResult execute() {
        model.updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        EventsCenter.getInstance().post(new SwapTabEvent(1));
        return new CommandResult(MESSAGE_SUCCESS);
    }


}
