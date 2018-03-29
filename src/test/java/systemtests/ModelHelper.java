package systemtests;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.investigapptor.model.Model;
import seedu.investigapptor.model.crimecase.CrimeCase;
import seedu.investigapptor.model.person.Person;

/**
 * Contains helper methods to set up {@code Model} for testing.
 */
public class ModelHelper {
    private static final Predicate<CrimeCase> PREDICATE_MATCHING_NO_CASES = unused -> false;
    private static final Predicate<Person> PREDICATE_MATCHING_NO_PERSONS = unused -> false;
    private static final Predicate<CrimeCase> PREDICATE_MATCHING_NO_CRIMECASE = unused -> false;

    /** PERSON PORTION **/

    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredCrimeCaseList(Model model, List<CrimeCase> toDisplay) {
        Optional<Predicate<CrimeCase>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredCrimeCaseList(predicate.orElse(PREDICATE_MATCHING_NO_CASES));
    }

    /**
     * @see ModelHelper#setFilteredCrimeCaseList(Model, List)
     */
    public static void setFilteredCrimeCaseList(Model model, CrimeCase... toDisplay) {
        setFilteredCrimeCaseList(model, Arrays.asList(toDisplay));
    }

    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredPersonList(Model model, List<Person> toDisplay) {
        Optional<Predicate<Person>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatching).reduce(Predicate::or);
        model.updateFilteredPersonList(predicate.orElse(PREDICATE_MATCHING_NO_PERSONS));
    }

    /**
     * @see ModelHelper#setFilteredPersonList(Model, List)
     */
    public static void setFilteredPersonList(Model model, Person... toDisplay) {
        setFilteredPersonList(model, Arrays.asList(toDisplay));
    }

    /**
     * Returns a predicate that evaluates to true if this {@code CrimeCase} equals to {@code other}.
     */
    private static Predicate<CrimeCase> getPredicateMatching(CrimeCase other) {
        return person -> person.equals(other);
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Person} equals to {@code other}.
     */
    private static Predicate<Person> getPredicateMatching(Person other) {
        return person -> person.equals(other);
    }

    /*** CRIME CASE PORTION ***/

    /**
     * Updates {@code model}'s crime case filtered list to display only {@code toDisplay}.
     */
    public static void setCrimeCaseFilteredList(Model model, List<CrimeCase> toDisplay) {
        Optional<Predicate<CrimeCase>> predicate =
                toDisplay.stream().map(ModelHelper::getPredicateMatchingCrimeCase).reduce(Predicate::or);
        model.updateFilteredCrimeCaseList(predicate.orElse(PREDICATE_MATCHING_NO_CRIMECASE));
    }

    /**
     * @see ModelHelper#setCrimeCaseFilteredList(Model, List)
     */
    public static void setCrimeCaseFilteredList(Model model, CrimeCase... toDisplay) {
        setCrimeCaseFilteredList(model, Arrays.asList(toDisplay));
    }

    /**
     * Returns a predicate that evaluates to true if this {@code CrimeCase} equals to {@code other}.
     */
    private static Predicate<CrimeCase> getPredicateMatchingCrimeCase(CrimeCase other) {
        return crimeCase -> crimeCase.equals(other);
    }
}
