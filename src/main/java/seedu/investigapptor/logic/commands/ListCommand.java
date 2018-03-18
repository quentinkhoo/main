package seedu.investigapptor.logic.commands;

import static seedu.investigapptor.model.Model.PREDICATE_SHOW_ALL_INVESTIGATORS;

/**
 * Lists all persons in the investigapptor book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "l";

    public static final String MESSAGE_SUCCESS = "Listed all persons";


    @Override
    public CommandResult execute() {
        model.updateFilteredInvestigatorList(PREDICATE_SHOW_ALL_INVESTIGATORS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
