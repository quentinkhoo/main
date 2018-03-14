package seedu.investigapptor.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import seedu.investigapptor.model.crimecase.UniqueCrimeCaseList;

public class UniqueCrimeCaseListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueCrimeCaseList uniqueCrimeCaseList = new UniqueCrimeCaseList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueCrimeCaseList.asObservableList().remove(0);
    }
}
