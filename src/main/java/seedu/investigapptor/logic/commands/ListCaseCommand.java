package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_CASES;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.Model;

/**
 * Lists all investigators in the investigapptor book to the user.
 */
public class ListCaseCommand extends ListCommand {

    private static final String listType = "cases";
    private static Model model;

    public ListCaseCommand(Model model) throws CommandException {
        super(listType);
        this.model = model;
    }

    /**
     * Swap tabs to listing all cases in the investigapptor book to the user.
     */
    public CommandResult executeListCases() {

        model.updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
        EventsCenter.getInstance().post(new SwapTabEvent(1));

        return new CommandResult(String.format(MESSAGE_SUCCESS, listType));
    }


}
