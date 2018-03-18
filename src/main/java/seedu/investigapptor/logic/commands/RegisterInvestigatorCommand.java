package seedu.investigapptor.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.investigapptor.logic.commands.exceptions.CommandException;
import seedu.investigapptor.model.person.exceptions.DuplicatePersonException;
import seedu.investigapptor.model.person.investigator.Investigator;

/**
 * Adds a person to the investigapptor book.
 */
public class RegisterInvestigatorCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "registerInvestigator";
    public static final String COMMAND_ALIAS = "regInvest";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Registers an investigator to investigapptor. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "teamA "
            + PREFIX_TAG + "new";

    public static final String MESSAGE_SUCCESS = "New investigator registered: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "Investigator already exists in the investigapptor";

    private final Investigator toAdd;

    /**
     * Creates an RegisterInvestigatorCommand to add the specified {@code Person}
     */
    public RegisterInvestigatorCommand(Investigator investigator) {
        requireNonNull(investigator);
        toAdd = investigator;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addInvestigator(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicatePersonException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RegisterInvestigatorCommand // instanceof handles nulls
                && toAdd.equals(((RegisterInvestigatorCommand) other).toAdd));
    }
}
