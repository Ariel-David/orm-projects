package Entity;

import Annotation.AutoIncrement;
import Annotation.NotNull;
import Annotation.PrimaryKey;
import Annotation.Unique;

public class User {

    @AutoIncrement
    @PrimaryKey
    private int id;
    @NotNull
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
        //
    }
}
