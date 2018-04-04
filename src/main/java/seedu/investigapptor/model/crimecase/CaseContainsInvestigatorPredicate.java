package seedu.investigapptor.model.crimecase;

import java.util.function.Predicate;


/**
 * Tests that a {@code CrimeCase}'s {@code CaseName} matches any of the keywords given.
 */
public class CaseContainsInvestigatorPredicate implements Predicate<CrimeCase> {
    private final Integer hashcode;

    public CaseContainsInvestigatorPredicate(Integer hashcode) {
        this.hashcode = hashcode;
    }

    @Override
    public boolean test(CrimeCase crimeCase) {
        return hashcode == crimeCase.getCurrentInvestigator().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CaseContainsInvestigatorPredicate // instanceof handles nulls
                && this.hashcode.equals(((CaseContainsInvestigatorPredicate) other).hashcode)); // state check
    }

}
