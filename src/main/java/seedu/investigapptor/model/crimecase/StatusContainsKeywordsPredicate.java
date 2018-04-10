package seedu.investigapptor.model.crimecase;

import java.util.List;
import java.util.function.Predicate;

//@@author pkaijun
/**
 * Tests that a {@code CrimeCase}'s {@code Status} matches any of the keywords given.
 */
public class StatusContainsKeywordsPredicate implements Predicate<CrimeCase> {
    private final List<String> keywords;

    public StatusContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    /* Returns true if keywords matches with any element in the set of status of a crimecase
     */
    @Override
    public boolean test(CrimeCase crimecase) {
        return keywords.stream()
                .anyMatch(crimecase.getStatus().toString()::contains);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StatusContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((StatusContainsKeywordsPredicate) other).keywords)); // state check
    }
}
