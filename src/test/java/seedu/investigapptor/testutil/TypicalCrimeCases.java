package seedu.investigapptor.testutil;

import static seedu.investigapptor.testutil.TypicalPersons.FIONA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;


/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalCrimeCases {

    public static final CrimeCase ASSAULT = new CrimeCaseBuilder().withName("Assault")
            .withDescription("Assault at AYE").withStartDate("28/02/2012").withStatus("open")
            .withCurrentInvestigator(FIONA).build();


    private TypicalCrimeCases() {} // prevents instantiation

    /**
     * Returns an {@code Investigapptor} with all the typical persons.
     */
    public static Investigapptor getTypicalCrimeCaseInvestigapptor() {
        Investigapptor ia = new Investigapptor();
        for (CrimeCase crimeCases : getTypicalCrimeCases()) {
            try {
                ia.addCrimeCase(crimeCases);
            } catch (DuplicateCrimeCaseException e) {
                throw new AssertionError("not possible");
            }
        }
        return ia;
    }

    public static List<CrimeCase> getTypicalCrimeCases() {
        return new ArrayList<>(Arrays.asList(ASSAULT));
    }
}
