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

    public <T> List<?> updateEntireEntity(T object) throws SQLException, IllegalAccessException {
        Class<?> clz = object.getClass();
        Field[] declaredFields = clz.getDeclaredFields();
        Object index = null;
        T item;

        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.getName() == "id") {
                index = field.get(object);
                List<?> list = findOne(clz, "id", index);
                item = (T) list.get(0);
                break;
            }
        }

        Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword());
        String query = new QueryBuilder.Builder().update(clz).build().toString();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            query += new QueryBuilder.Builder().set(field.getName(), field.get(object)).build().toString();
        }
        query += new QueryBuilder.Builder().where("id", index).build().toString();
        System.out.println(query);
        int indexChanged = ConnectionUtilities.TableConnectionWithUpdateQuery(connection, query);
        return findOne(clz, "id", index);
    }

    public <T, V, K> List<T> update(Class<T> clz, String whereKey, K whereValue, String filed, V value) {
        String query = new QueryBuilder.Builder()
                .update(clz)
                .set(filed, value)
                .where(whereKey, whereValue)
                .build().toString();
        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {
            int indexChanged = ConnectionUtilities.TableConnectionWithUpdateQuery(connection, query);
            return findOne(clz, "id", indexChanged);
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public <T> List<T> findAll(Class<T> clz) {
        String query = new QueryBuilder.Builder()
                .select("*")
                .from(clz)
                .build().toString();

        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithSelectQuery(connection, query);
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
                .build().toString();

        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithSelectQuery(connection, query);
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
                .build().toString();

        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithSelectQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {
            String query = "delete from animal where id = 1";
            ConnectionUtilities.TableConnectionWithDeleteQuery(connection, query);
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

    private <T> List<T> readFromDB(ResultSet rs, Class<T> clz) throws
            SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
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
