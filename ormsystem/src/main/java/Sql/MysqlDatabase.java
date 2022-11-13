package Sql;

import Utils.ConnectionUtilities;
import Utils.SqlConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlDatabase {

    public <T> List<T> getAll(Class<T> clz) {

        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("select * from %s", clz.getSimpleName().toLowerCase()));
            return readAllFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> List<T> findOne(Class<T> clz){

        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("select * from %s", clz.getSimpleName().toLowerCase()));
            return readAllFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private <T> List<T> readAllFromDB(ResultSet rs, Class<T> clz) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<T> results = new ArrayList<>();

        while (rs.next()) {
            Constructor<T> constructor = clz.getConstructor(null);
            T item = constructor.newInstance();
            Field[] declaredFields = clz.getDeclaredFields();

            for (Field field : declaredFields) {
                field.setAccessible(true);
                field.set(item, rs.getObject(field.getName()));
            }

            results.add(item);
        }

        return results;
    }
}
