package Client;

import Entity.Animal;
import Sql.MysqlDatabase;

import java.lang.reflect.Field;
import java.sql.*;

public class Client {
    public static void main(String[] args) {
        MysqlDatabase table = new MysqlDatabase();
        table.getAll(Animal.class);
    }
}
