package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.model.Model;

/**
 * Lists all investigators in the investigapptor book to the user.
 */
public class ListInvestigatorCommand extends Command {

    public static final String COMMAND_WORD = "listinvestigators";
    public static final String COMMAND_ALIAS = "li";
    public static final String MESSAGE_SUCCESS = "Listed all investigators";

    /**
     * Swap tabs to listing all investigators in the investigapptor book to the user.
     */
    public CommandResult execute() {

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        EventsCenter.getInstance().post(new SwapTabEvent(0));

        return new CommandResult(MESSAGE_SUCCESS);
    }


}
