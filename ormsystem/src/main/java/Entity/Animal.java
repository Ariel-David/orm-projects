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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfLegs() {
        return numOfLegs;
    }

    public void setNumOfLegs(int numOfLegs) {
        this.numOfLegs = numOfLegs;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

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
