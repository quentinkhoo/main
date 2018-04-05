# quentinkhoo
###### \java\seedu\investigapptor\storage\XmlAdaptedPasswordTest.java
``` java
public class XmlAdaptedPasswordTest {

    @Test
    public void toModelType_validPassword_returnsPassword() throws Exception {
        Password password = new Password("password");
        XmlAdaptedPassword storedPassword = new XmlAdaptedPassword(password);
        assertEquals(password, storedPassword.toModelType());
    }
}
```
###### \java\seedu\investigapptor\testutil\InvestigapptorBuilder.java
``` java
    /**
     * Parses {@code password} into a {@code Password} and adds it to the {@code Investigapptor} that we are building.
     */
    public InvestigapptorBuilder withPassword(String password) {
        investigapptor.updatePassword(new Password(password));
        return this;
    }
```
