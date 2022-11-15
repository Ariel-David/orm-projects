package Entity;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnimalTests {

//    @Test
//    void player_generateNegativeShirtNumberPlayer_ExpectIllegalArgumentException(){
//        Integer shirtNumber = -5;
//        int position = 1;
//        assertThrows(IllegalArgumentException.class, () -> Player.createNamelessPlayer(position, shirtNumber), "Player's shirt number must be a positive number larger than 0");
//    }

    @Test
    void animal_setNegativeNumberOfLettersInName_ExceptIllegalArgumentException(){
        // given
        Animal animal = Animal.createRandomAnimalInfo();
        // then
        assertThrows(IllegalArgumentException.class, () -> animal.setName(""), "Animal's name must be a at least 2 letters");
    }


    @Test
    void animal_setNameToAnimal_ExpectTheSame(){
        // given
        Animal animal = Animal.createRandomAnimalInfo();
        // when
        String name = "Yossi";
        animal.setName(name);
        // then
        assertEquals(name, animal.getName());
    }

    @Test
    void animal_setNumberOfLegsToNegative_ExpectIllegalArgumentException(){
        // given
        Animal animal = Animal.createRandomAnimalInfo();
        // when
        int numOfLegs = -1;
        // then
        assertThrows(IllegalArgumentException.class, () ->  animal.setNumOfLegs(numOfLegs));
    }


}
