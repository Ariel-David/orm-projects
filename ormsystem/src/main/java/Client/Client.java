package Client;

import Entity.Animal;
import Sql.MysqlDatabase;
import Utils.QueryBuilder;

import java.util.HashMap;
import java.util.Map;

public class Client {
    public static void main(String[] args) {

        MysqlDatabase table = new MysqlDatabase();

        System.out.println(table.findAll(Animal.class));
//        System.out.println(table.findOne(Animal.class, "numOfLegs", 4));
//        System.out.println(table.findAny(Animal.class, "id", 2));

//        Map<String, Object> map = new HashMap<>();
//        map.put("numOfLegs", 1);
//        map.put("name", "ramini");
//        map.put("sound", "riri!");
//
//        String query = new QueryBuilder.Builder().insert(Animal.class).values(map).build().toString();

//        String query = new QueryBuilder.Builder().createTable(Animal.class).build().toString();

//        String query = new QueryBuilder.Builder().where("isOk", true).and().where("ok", false).build().toString();

//        System.out.println(query);
    }
}

