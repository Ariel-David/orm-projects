package Client;

import Entity.Animal;
import Sql.MysqlDatabase;

import java.sql.SQLException;

public class Client {
    public static void main(String[] args) throws SQLException, IllegalAccessException {

        MysqlDatabase table = new MysqlDatabase();

       // System.out.println(table.findAll(Animal.class));

        //System.out.println(table.findOne(Animal.class, "numOfLegs", 4));
        //System.out.println(table.findAny(Animal.class, "id", 2));
       // System.out.println(table.update(Animal.class,"id",1,"name","Efi"));
        Animal a = new Animal();
        a.setName("Rani");
        a.setSound("mooooo");
        a.setId(1);
       table.updateEntireEntity(a);
    }
}

