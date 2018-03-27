package seedu.investigapptor.model;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Represents a Password in PartTimeManger
 * Store password as hashCode
 */
public class Password {

    public static final String MESSAGE_PASSWORD_CONSTRAINTS =
            "Password should be at least 8 character and no spaces.";

    public static final String DEFAULT_PASSWORD =
            "password";

    /**
     * accept all password that do not have whitespaces and at least 8 characters.
     */
    public static final String PASSWORD_VALIDATION_REGEX = "^(?=\\S+$).{8,}$";

    private String password;
    private String passwordHash;
    private final String initialValue = "IV";

    /**
     * constructor for default password
     */
    public Password() {
        createPassword(DEFAULT_PASSWORD);
    }

    /**
     * use this if hashcode is known
     * @param password
     */
    public Password(String password) {
        this.password = password;
    }

    /**
     * @param test
     * @return true if password is of correct format
     */
    public static boolean isValidPassword(String test) {
        return test.matches(PASSWORD_VALIDATION_REGEX);
    }

    /**
     * check if passwordHash generated from the string is same as current passwordHash
     * @param inputPassword
     * @return true if same
     */
    public boolean isCorrectPassword(String inputPassword) {
        //return passwordHash.equals(generatePasswordHash(password));
        return this.password.equals(inputPassword);
    }

    /**
     * Change password given a password
     * @return true if password is changed
     */
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Change password given a password
     * @return true if password is changed
     */
    public void updatePassword(Password newPassword) {
        requireNonNull(newPassword);
        this.password = newPassword.getPassword();
    }

    /**
     * Create passwordHash when password is entered in plain text
     * @param password
     */
    public void createPassword(String password) {
        requireNonNull(password);
        this.password = password;
        //passwordHash = generatePasswordHash(password);
    }

    public String getPassword() {
        return this.password;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Generate passwordHash given a string password
     * @param password
     * @return passwordHash in String
     */
    private String generatePasswordHash(String password) {
        String encodedHash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(initialValue.getBytes());
            byte[] byteHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            encodedHash = Base64.getEncoder().encodeToString(byteHash);
        } catch (NoSuchAlgorithmException noSuchAlgoException) {
            System.out.println("cannot generate hash: MessageDigest.getInstance");
        }
        return encodedHash;
    }

    /**
     * Checks if a password is null
     * @param password
     */
    public static boolean isNullPassword(String password) {
        return password == null;
    }

    /**
     * Checks if an inputPassword is the currentPassword
     * @param currentPassword
     * @param inputPassword
     */
    public static boolean isCorrectPassword(String currentPassword, String inputPassword) {
        return currentPassword.equals(inputPassword);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Password // instanceof handles nulls
                && this.password.equals(((Password) other).password)); // state check
    }

    @Override
    public int hashCode() {
        return password.hashCode();
    }
}