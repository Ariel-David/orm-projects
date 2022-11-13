package Client;

import Entity.Animal;
import Sql.MysqlDatabase;
import Utils.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    public static void main(String[] args) {


        MysqlDatabase table = new MysqlDatabase();

        String findAllQuery = new QueryBuilder.Builder()
                .select("*")
                .from(Animal.class)
                .build().getQuery();
        System.out.println(table.findAll(Animal.class, findAllQuery));

        String findOneQuery = new QueryBuilder.Builder()
                .select("*")
                .from(Animal.class)
                .where("id", 1)
                .build().getQuery();
        System.out.println(table.findOne(Animal.class, findOneQuery));

        String findAnyQuery = new QueryBuilder.Builder()
                .select("*")
                .from(Animal.class)
                .where("name", "charizard")
                .or()
                .where("numOfLegs", 2)
                .build().getQuery();
        System.out.println(table.findAny(Animal.class, findAnyQuery));
    }
}

enum SqlMethods {SELECT, FROM, WHERE}
