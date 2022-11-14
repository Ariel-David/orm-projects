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

//        System.out.println(table.findAll(Animal.class));
//        System.out.println(table.findOne(Animal.class, "numOfLegs", 4));
//        System.out.println(table.findAny(Animal.class, "id", 2));

//        Map<String, Object> map = new HashMap<>();
//        map.put("numOfLegs", 1);
//        map.put("name", "ramini");
//        map.put("sound", "riri!");
//
//        String query = new QueryBuilder.Builder().insert(Animal.class).values(map).build().toString();
//        System.out.println(query);

//        System.out.println(table.deleteAny(Animal.class, "numOfLegs", 5));
        System.out.println(table.deleteEntireTable(Animal.class));
        System.out.println(table.findAll(Animal.class));
    }
}

