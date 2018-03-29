package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.model.Model;

/**
 * Lists all investigators in the investigapptor book to the user.
 */
public class ListInvestigatorCommand extends ListCommand {

    private static final String listType = "investigators";
    private static Model model;

    public ListInvestigatorCommand(Model model) {
        super(listType);
        this.model = model;
    }
    /**
     * Swap tabs to listing all investigators in the investigapptor book to the user.
     */
    public CommandResult executeListInvestigators() {

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        EventsCenter.getInstance().post(new SwapTabEvent(0));

        return new CommandResult(String.format(MESSAGE_SUCCESS, listType));
    }


}
