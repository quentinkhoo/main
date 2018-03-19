package seedu.investigapptor.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.investigapptor.model.Investigapptor;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.crimecase.exceptions.DuplicateCrimeCaseException;

/**
 * A utility class containing a list of {@code CrimeCase} objects to be used in tests.
 */
public class TypicalCrimeCases {

    public static final CrimeCase ALFA = new CrimeCaseBuilder().withName("Project Alfa")
            .withDescription("Murder on the orient express").withStartDate("10/09/2015")
            .toggleStatus()
            .withTags("Murder").build();
    public static final CrimeCase BRAVO = new CrimeCaseBuilder().withName("Project Bravo")
            .withDescription("Crooked house")
            .withStartDate("12/03/2016")
            .withTags("Kidnap", "Homicide").build();
    public static final CrimeCase CHARLIE = new CrimeCaseBuilder().withName("Project Charlie").toggleStatus()
            .withStartDate("18/01/2012").withDescription("ABC murders").build();
    public static final CrimeCase DELTA = new CrimeCaseBuilder().withName("Project Delta")
            .withStartDate("27/11/1999").withDescription("Peril at End House").build();
    public static final CrimeCase ECHO = new CrimeCaseBuilder().withName("Project Echo")
            .withStartDate("23/11/1965").withDescription("A study in scarlet").build();
    public static final CrimeCase FOXTROT = new CrimeCaseBuilder().withName("Project Foxtrot").toggleStatus()
            .withStartDate("09/07/2017").withDescription("The sign of the four").build();
    public static final CrimeCase GOLF = new CrimeCaseBuilder().withName("Project Golf")
            .withStartDate("02/08/2017").withDescription("The hound of the baskervilles").build();

    // Manually added
    public static final CrimeCase BLUE = new CrimeCaseBuilder().withName("Project Blue").toggleStatus()
            .withStartDate("15/06/2014").withDescription("Reichenbach fall").build();
    public static final CrimeCase YELLOW = new CrimeCaseBuilder().withName("Project Yellow")
            .withStartDate("19/12/2012").withDescription("Scandal in Belgravia").build();

    private TypicalCrimeCases() {} // prevents instantiation

    /**
     * Returns an {@code Investigapptor} with all the typical cases.
     */
    public static Investigapptor getTypicalInvestigapptor() {
        Investigapptor ia = new Investigapptor();
        for (CrimeCase crimeCase : getTypicalCrimeCases()) {
            try {
                ia.addCrimeCase(crimeCase);
            } catch (DuplicateCrimeCaseException e) {
                throw new AssertionError("not possible");
            }
        }
        return ia;
    }

    public static List<CrimeCase> getTypicalCrimeCases() {
        return new ArrayList<>(Arrays.asList(ALFA, BRAVO, CHARLIE, DELTA, ECHO, FOXTROT, GOLF));
    }
}
