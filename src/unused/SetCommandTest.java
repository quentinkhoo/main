package seedu.investigapptor.logic.commands;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.investigapptor.logic.commands.exceptions.CommandException;

public class SetCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final SetCommand setCommand = new SetCommand("p@ssword p/password");

    @Test
    public void executeUndoableCommand_invalidAlias_throwsCommandException()
            throws CommandException {
        thrown.expect(CommandException.class);
        setCommand.executeUndoableCommand();
    }
}
