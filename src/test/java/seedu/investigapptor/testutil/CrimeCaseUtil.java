package seedu.investigapptor.testutil;

import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.investigapptor.logic.commands.AddCaseCommand;
import seedu.investigapptor.model.crimecase.CrimeCase;

/**
 * A utility class for CrimeCase.
 */
public class CrimeCaseUtil {

    /**
     * Returns an add command string for adding the {@code crimeCase}.
     */
    public static String getAddCommand(CrimeCase crimeCase) {
        return AddCaseCommand.COMMAND_WORD + " " + getCrimeCaseDetails(crimeCase);
    }

    /**
     * Returns an add command string for adding the {@code crimeCase}.
     */
    public static String getAliasAddCommand(CrimeCase crimeCase) {
        return AddCaseCommand.COMMAND_ALIAS + " " + getCrimeCaseDetails(crimeCase);
    }

    /**
     * Returns the part of command string for the given {@code crimeCase}'s details.
     */
    public static String getCrimeCaseDetails(CrimeCase crimeCase) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + crimeCase.getCaseName().crimeCaseName + " ");
        sb.append(PREFIX_DESCRIPTION + crimeCase.getDescription().description + " ");
        sb.append(PREFIX_STARTDATE + crimeCase.getStartDate().date + " ");
        crimeCase.getTags().stream().forEach(s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
