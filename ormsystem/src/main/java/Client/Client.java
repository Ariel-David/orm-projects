package Client;

import Entity.Animal;
import Sql.MysqlDatabase;
import Utils.QueryBuilder;

import java.util.HashMap;
import java.util.Map;

public class Client {
    public static void main(String[] args) {

        MysqlDatabase table = new MysqlDatabase();

//        System.out.println(table.findAll(Animal.class));
//        System.out.println(table.findOne(Animal.class, "numOfLegs", 4));
//        System.out.println(table.findAny(Animal.class, "id", 2));

        System.out.println(table.updateProperty(Animal.class, "numOfLegs", 5, "numOfLegs", 4));
//        System.out.println(table.findOne(Animal.class, "id", 2));

//        table.createTable(Animal.class);

        //        Animal gato = new Animal();
//        gato.setId(1);
//        gato.setName("kitty");
//        table.createOne(gato);
//
//        String query = new QueryBuilder.Builder().insert(gato).build().toString();
//        System.out.println(query);
    }
}

