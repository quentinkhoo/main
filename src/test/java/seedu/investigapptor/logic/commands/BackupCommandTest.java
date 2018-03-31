package seedu.investigapptor.logic.commands;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BackupCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullCrimeCase_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new BackupCommand(null);
    }

}
