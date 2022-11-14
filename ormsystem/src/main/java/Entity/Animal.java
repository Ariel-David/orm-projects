package Entity;

import java.lang.annotation.Native;

public class Animal {

    @PrimaryKey
    private int id;
    private String name;
    private int numOfLegs;
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
