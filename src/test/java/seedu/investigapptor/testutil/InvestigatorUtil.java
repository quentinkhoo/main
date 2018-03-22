package seedu.investigapptor.testutil;

import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_RANK;
import static seedu.investigapptor.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.investigapptor.logic.commands.RegisterInvestigatorCommand;
import seedu.investigapptor.model.person.investigator.Investigator;

/**
 * A utility class for Investigator.
 */
public class InvestigatorUtil {

    /**
     * Returns an reg command string for adding the {@code investigator}.
     */
    public static String getRegCommand(Investigator investigator) {
        return RegisterInvestigatorCommand.COMMAND_WORD + " " + getPersonDetails(investigator);
    }

    /**
     * Returns an reg command string for adding the {@code investigator}.
     */
    public static String getAliasRegCommand(Investigator investigator) {
        return RegisterInvestigatorCommand.COMMAND_ALIAS + " " + getPersonDetails(investigator);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Investigator investigator) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + investigator.getName().fullName + " ");
        sb.append(PREFIX_PHONE + investigator.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + investigator.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + investigator.getAddress().value + " ");
        sb.append(PREFIX_RANK + investigator.getRank().getValue() + " ");
        investigator.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
