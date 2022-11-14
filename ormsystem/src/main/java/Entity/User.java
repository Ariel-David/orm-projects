package Entity;

import Annotation.NotNull;
import Annotation.PrimaryKey;
import Annotation.Unique;

public class User {

    @PrimaryKey
    private int id;
    private String firstName;
    private String lastName;
    @Unique
    @NotNull
    private String email;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
