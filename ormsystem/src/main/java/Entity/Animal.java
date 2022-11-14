package Entity;

import Annotation.AutoIncrement;
import Annotation.NotNull;
import Annotation.PrimaryKey;
import Annotation.Unique;

public class Animal {

    @AutoIncrement
    @PrimaryKey
    private int id;
    private String name;
    @NotNull
    private int numOfLegs;
    @Unique
    private String sound;

    public Animal() {
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numOfLegs=" + numOfLegs +
                ", sound='" + sound + '\'' +
                '}';
    }
}
