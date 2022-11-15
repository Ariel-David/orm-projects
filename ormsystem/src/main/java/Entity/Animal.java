package Entity;

import Annotation.AutoIncrement;
import Annotation.NotNull;
import Annotation.PrimaryKey;
import Annotation.Unique;
import Utils.ExceptionMessage;
import Utils.RandomData;

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

    public static Animal createRandomAnimalInfo(){
        Animal animal = new Animal();
        animal.setNumOfLegs(RandomData.generateRandomNumber(0,5));
        animal.setId(RandomData.getRandomId());
        animal.setSound(RandomData.getRandomSound());
        animal.setName(RandomData.getRandomFirstName());
        return animal;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.length() < 2) throw new IllegalArgumentException("name: " + ExceptionMessage.TOO_SHORT_STRING.getMessage());
        this.name = name;
    }

    public int getNumOfLegs() {
        return numOfLegs;
    }

    public void setNumOfLegs(int numOfLegs) {
        if (numOfLegs < 0) throw new IllegalArgumentException("numOfLegs: " + ExceptionMessage.NEGATIVE_FIELD.getMessage());
        this.numOfLegs = numOfLegs;
    }

    public void setId(int id) {
        if (id < 1) throw new IllegalArgumentException("id: " + ExceptionMessage.NEGATIVE_FIELD.getMessage());
        this.id = id;
    }


    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        if(sound.length() < 2) throw new IllegalArgumentException("sound: " + ExceptionMessage.TOO_SHORT_STRING.getMessage());
        this.sound = sound;
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
