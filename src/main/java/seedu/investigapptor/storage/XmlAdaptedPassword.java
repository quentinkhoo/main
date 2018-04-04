package seedu.investigapptor.storage;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import seedu.investigapptor.model.Password;

//@@author quentinkhoo
/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedPassword {

    @XmlElement(required = true)
    private String currentPassword;

    /**
     * Constructs an XmlAdaptedPassword.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPassword() {}

    /**
     * Constructs an {@code XmlAdaptedPassword} with the given password.
     */
    public XmlAdaptedPassword(Password password) {
        try {
            this.currentPassword = password.getPassword();
        } catch (NullPointerException npe) {
            this.currentPassword = null;
        }
    }

    /**
     * Converts this jaxb-friendly adapted password object into the model's Password object.
     *
     */
    public Password toModelType() {
        return new Password(currentPassword);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPassword)) {
            return false;
        }

        XmlAdaptedPassword otherPassword = (XmlAdaptedPassword) other;
        return Objects.equals(currentPassword, otherPassword.currentPassword);
    }
}
