package seedu.investigapptor.model.crimecase;

/**
 * Represents a CrimeCase's status in the Investigapptor.
 */
public class Status {

    private String status;

    /**
     * Constructs a {@code Status}.
     *
     */
    public Status() {
        this.status = "open";
    }

    /**
     * Toggles status depending on current status
     *
     */
    public void toggleCase() {
        if (this.status.equals("open")) {
            this.status = "close";
        } else {
            this.status = "open";
        }
    }

    @Override
    public String toString() {
        return this.status;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Status // instanceof handles nulls
                && this.status.equals(((Status) other).status)); // state check
    }

    @Override
    public int hashCode() {
        return status.hashCode();
    }
}
