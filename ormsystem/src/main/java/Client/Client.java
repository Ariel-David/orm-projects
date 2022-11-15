package Client;

import Entity.Animal;
import Sql.MysqlDatabase;

import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) {

        MysqlDatabase table = new MysqlDatabase();

//        table.createTable(Animal.class);
//        table.createMany(createListOfEntity());

    }

    public static void testSequence(){
        // create element
        Animal random = Animal.createRandomAnimalInfo();
        // create table of that same element's type
        // add 1 element to the table above
        // add 3 elements to the table above
        // get all elements from the table above
        // update 1 element's field from above
        // update entire element from above
        // get 1 element from the table above
        // delete one element from above
        // delete 2 elements from above
        // delete all elements from table
    }

    public static <T> List<T> createListOfEntity() {
        Animal cat = Animal.createRandomAnimalInfo();
        Animal dog = Animal.createRandomAnimalInfo();
        Animal pig = Animal.createRandomAnimalInfo();

        List<Animal> animalsList = new ArrayList<>();
        animalsList.add(cat);
        animalsList.add(dog);
        animalsList.add(pig);

        return (List<T>) animalsList;
    }
}

