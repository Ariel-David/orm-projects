package Entity;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnimalTests {

    // ---------- Throws Exceptions ---------- //
    @Test
    void animal_setNegativeNumberOfLettersInName_ExceptIllegalArgumentException() {
        Animal animal = Animal.createRandomAnimalInfo();
        assertThrows(IllegalArgumentException.class, () -> animal.setName(""), "Animal's name must be a at least 2 letters");
    }


    @Test
    void animal_setNumberOfLegsToNegative_ExpectIllegalArgumentException() {
        Animal animal = Animal.createRandomAnimalInfo();
        int numOfLegs = -1;
        assertThrows(IllegalArgumentException.class, () -> animal.setNumOfLegs(numOfLegs), "An animal can't have a negative number of legs");
    }

    @Test
    void animal_setSoundTooShort_ExpectIllegalArgumentException() {
        Animal animal = Animal.createRandomAnimalInfo();
        String sound = "k";
        assertThrows(IllegalArgumentException.class, () -> animal.setSound(sound), "A sound that an animal makes must be longer than letter");
    }

    @Test
    void animal_setIdNegativeNumber_ExpectIllegalArgumentException() {
        Animal animal = Animal.createRandomAnimalInfo();
        int id = -5;
        assertThrows(IllegalArgumentException.class, () -> animal.setId(id), "A sound that an animal makes must be longer than letter");
    }

    // ---------- Assert Equals ---------- //

    @Test
    void animal_setNameToAnimal_ExpectTheSame() {
        Animal animal = Animal.createRandomAnimalInfo();
        String name = "Yossi";
        animal.setName(name);
        assertEquals(name, animal.getName());
    }

    @Test
    void animal_setLegsToAnimal_ExpectTheSame() {
        Animal animal = Animal.createRandomAnimalInfo();
        int legs = 5;
        animal.setNumOfLegs(legs);
        assertEquals(legs, animal.getNumOfLegs());
    }

    @Test
    void animal_setIdToAnimal_ExpectTheSame() {
        Animal animal = Animal.createRandomAnimalInfo();
        int id = 5;
        animal.setId(id);
        assertEquals(id, animal.getId());
    }

}
