package Client;

import Entity.Animal;
import Sql.MysqlDatabase;

public class Client {
    public static void main(String[] args) {

        MysqlDatabase table = new MysqlDatabase();

//        System.out.println(table.findAll(Animal.class));
//        System.out.println(table.findOne(Animal.class, "numOfLegs", 4));
//        System.out.println(table.findAny(Animal.class, "id", 2));
//        System.out.println(table.findAll(Animal.class));
        System.out.println(table.deleteOne(Animal.class, "id", 2));
        System.out.println(table.findAll(Animal.class));
    }
}

