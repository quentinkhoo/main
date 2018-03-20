package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_CASES;
import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.investigapptor.commons.core.EventsCenter;
import seedu.investigapptor.commons.core.Messages;
import seedu.investigapptor.commons.events.ui.SwapTabEvent;
import seedu.investigapptor.logic.commands.exceptions.CommandException;

/**
 * Lists all persons in the investigapptor book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "l";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists the specified type\n"
            + "Parameters: TYPE (must be either investigator or cases)\n"
            + "Example: " + COMMAND_WORD + " cases";

    public static final String MESSAGE_SUCCESS = "Listed all %1$s";

    private final String listType;

    public ListCommand(String listType) {
        this.listType = listType;
    }

    @Override
    public CommandResult execute() throws CommandException {
        switch (listType) {
        case "cases":
            model.updateFilteredCrimeCaseList(PREDICATE_SHOW_ALL_CASES);
            EventsCenter.getInstance().post(new SwapTabEvent(1));
            break;

        case "investigators":
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            EventsCenter.getInstance().post(new SwapTabEvent(0));
            break;

        default:
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, listType));
    }
}
