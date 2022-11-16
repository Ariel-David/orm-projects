package Client;

import Entity.Animal;
import Sql.MysqlDatabase;

import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        testSequence();
    }

    public static void testSequence() {
        MysqlDatabase table = new MysqlDatabase();

        // create element
        Animal randomAnimal = Animal.createRandomAnimalInfo();

        // add 1 element to the table above
        table.createOne(randomAnimal);

        // create table of that same element's type
        table.createTable(Animal.class);

        // get size of table now
        int size = table.findAll(Animal.class).size();

        // add 3 elements to the table above
        table.createMany(createListOfEntity());

        // get all elements from the table above
        System.out.println("Size of table before creating new elements --> " + size);
        System.out.println("Size of table after creating new elements --> " + table.findAll(Animal.class).size());

        // update 1 element's field from above
        table.updateProperty(Animal.class, "id", 1, "numOfLegs", 5);

        // update entire element from above
        Animal updateToThisOne = Animal.createRandomAnimalInfo();
        updateToThisOne.setId(2);
        table.updateEntireEntity(updateToThisOne);

        // get 1 element from the table above
        System.out.println("---------------------> Entity: " + table.findOne(Animal.class, "id", 2));


        // delete one element from above
        int removedLine = table.deleteOne(Animal.class, "id", 1);
        System.out.println("---------------------> Number of removed lines should be 1 and got: " + removedLine);

        // delete many elements from above
        int linesRemoved = table.deleteAny(Animal.class, "numOfLegs", 3);
        System.out.println("---------------------> Number of removed lines: " + linesRemoved);

        // delete all elements from table
        table.deleteEntireTable(Animal.class);

        // get all elements from the table above
        System.out.println(table.findAll(Animal.class).size() == 0 ? "This table is empty!" : "This table is not empty!");
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

