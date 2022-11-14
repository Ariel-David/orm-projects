package Sql;

import Utils.ConnectionUtilities;
import Utils.QueryBuilder;
import Utils.SqlConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlDatabase {

    public <T, V, K> List<T> update(Class<T> clz, String whereKey, K whereValue, String filed, V value) {
//        String testQuery = new QueryBuilder.Builder()
//                .select("*")
//                .from(clz)
//                .where(whereKey, whereValue)
//                .build().getQuery();
//        System.out.println(testQuery);

        String query = "UPDATE " + clz.getSimpleName().toLowerCase() + " SET " + filed + "=" + value + " WHERE " + whereKey + "=" + whereValue;
        System.out.println(query);
        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public <T> List<T> findAll(Class<T> clz) {
        String query = new QueryBuilder.Builder()
                .select("*")
                .from(clz)
                .build().getQuery();

        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T, V> List<T> findOne(Class<T> clz, String field, V value) {
        String query = new QueryBuilder.Builder()
                .select("*")
                .from(clz)
                .where(field, value)
                .limit(1)
                .build().getQuery();

        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T, V> List<T> findAny(Class<T> clz, String field, V value) {
        String query = new QueryBuilder.Builder()
                .select("*")
                .from(clz)
                .where(field, value)
                .build().getQuery();

        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> readFromDB(ResultSet rs, Class<T> clz) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
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
