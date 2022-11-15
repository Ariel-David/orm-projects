package Sql;

import Utils.ConnectionUtilities;
import Utils.ExceptionMessage;
import Utils.QueryBuilder;
import Utils.SqlConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlDatabase {

    private static Logger logger = LogManager.getLogger(MysqlDatabase.class.getName());

    public <T> void createOne(T object) {
        logger.info("createOne"+" - "+"Create one");
        String query = new QueryBuilder.Builder().insert(object).insertValues(object).build().toString();
        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            boolean hasBeenSuccessfullyInserted = ConnectionUtilities.TableConnectionWithInsertQuery(connection, query);
        } catch (SQLException e) {
            logger.fatal("createOne"+"Cannot connect the database");
            throw new IllegalStateException("Cannot connect the database!", e);
            throw new IllegalStateException(ExceptionMessage.SQL_CONNECTION.getMessage(), e);
        }
    }

    public <T> void createMany(List<T> objects) {
        logger.info("createMany"+" - "+"Create many");
        String query = "";
        // check if the list is empty!
        String query = new QueryBuilder.Builder().insert(objects.get(0)).build().toString();
        for (T object : objects)
            query += new QueryBuilder.Builder().insertValues(object).build().toString() + ", ";
        query = query.substring(0, query.length() - 2);
        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            boolean hasBeenSuccessfullyInserted = ConnectionUtilities.TableConnectionWithInsertQuery(connection, query);
        } catch (SQLException e) {
            throw new IllegalStateException(ExceptionMessage.SQL_CONNECTION.getMessage(), e);
        }
    }

    public <T> List<?> updateEntireEntity(T object) throws SQLException {
        logger.info("<"+getClass().getName()+"> " + ": " + "<"+"updateEntireEntity"+" >"+" - "+"Update entire entity with object: "
                + object.toString());
        Class<?> clz = object.getClass();
        Field[] declaredFields = clz.getDeclaredFields();
        Object index = null;
        T item;
        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (field.getName() == "id") {
                    index = field.get(object);
                    List<?> list = findOne(clz, "id", index); //TODO:handle empty list exception
                    item = (T) list.get(0);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(ExceptionMessage.FIELDS_OF_OBJECT.getMessage());
        }

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            String query = new QueryBuilder.Builder().update(clz).set().build().toString();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                query += new QueryBuilder.Builder().setValue(field.getName(), field.get(object)).build().toString();
            }
            query = query.substring(0, query.length() - 2) + " ";
            query += new QueryBuilder.Builder().where("id", index).build().toString();
            System.out.println(query);
            int indexChanged = ConnectionUtilities.TableConnectionWithUpdateQuery(connection, query);
            return findOne(clz, "id", index);
        } catch (SQLException e) {
            throw new IllegalStateException(ExceptionMessage.SQL_CONNECTION.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(ExceptionMessage.FIELDS_OF_OBJECT.getMessage());
        }
        return null;
    }

    public <T, V, K> List<T> update(Class<T> clz, String whereKey, K whereValue, String field, V value) {
        logger.info("update"+" - "+"Update the "+ field+" for "+"'"+value+"'"+" to item with " +whereKey+" of value " + whereValue);
        String query = new QueryBuilder.Builder()
                .update(clz)
                .set()
                .setValue(field, value)
                .where(whereKey, whereValue)
                .build().toString();
        try (Connection connection = DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword())) {
            int indexChanged = ConnectionUtilities.TableConnectionWithUpdateQuery(connection, query);
            return findOne(clz, "id", indexChanged);
        } catch (SQLException e) {
            throw new IllegalStateException(ExceptionMessage.SQL_CONNECTION.getMessage(), e);
        }
    }

    public <T> List<T> findAll(Class<T> clz) {
        logger.info("findAll"+" - "+"Find all items");
        String query = new QueryBuilder.Builder()
                .select("*")
                .from(clz)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithSelectQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException(ExceptionMessage.SQL_CONNECTION.getMessage(), e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T, V> List<T> findOne(Class<T> clz, String field, V value) {
        logger.info("findOne"+" - "+"Get one item with field: " +" '"+ field +"' "+ " and value: " + "'"+value.toString()+"'");
        String query = new QueryBuilder.Builder()
                .select("*")
                .from(clz)
                .where(field, value)
                .limit(1)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithSelectQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException(ExceptionMessage.SQL_CONNECTION.getMessage(), e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T, V> List<T> findAny(Class<T> clz, String field, V value) {
        logger.info("findAny"+" - "+"Get all items with field: " +" '"+ field +"' "+ " and value: " + "'"+value.toString()+"'");
        String query = new QueryBuilder.Builder()
                .select("*")
                .from(clz)
                .where(field, value)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithSelectQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException(ExceptionMessage.SQL_CONNECTION.getMessage(), e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T, V> Boolean deleteOne(Class<T> clz, String field, V value) {
        logger.info("deleteOne"+" - "+"Delete one item with field: " +" '"+ field +"' "+ " and value: " + "'"+value.toString()+"'");
        String query = new QueryBuilder.Builder()
                .delete(clz)
                .where(field, value)
                .limit(1)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithDeleteQuery(connection, query) > 0;

        } catch (SQLException e) {
            throw new IllegalStateException(ExceptionMessage.SQL_CONNECTION.getMessage(), e);
        }
        return null;
    }

    public <T, V> Boolean deleteAny(Class<T> clz, String field, V value) {
        logger.info("deleteAny"+" - "+"delete any item with field: " +" '"+ field +"' "+ " and value: " + "'"+value.toString()+"'");
        String query = new QueryBuilder.Builder()
                .delete(clz)
                .where(field, value)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithDeleteQuery(connection, query) > 0;
        } catch (SQLException e) {
            throw new IllegalStateException(ExceptionMessage.SQL_CONNECTION.getMessage(), e);
        }
        return null;
    }

    public <T, V> Boolean deleteEntireTable(Class<T> clz) {
        logger.info("deleteEntireTable"+" - "+"delete the entire table");
        String query = new QueryBuilder.Builder()
                .truncate(clz)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithDeleteQuery(connection, query) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(ExceptionMessage.TRUNCATE.getMessage());
        }
    }

    public <T> boolean createTable(Class<T> clz) {
        logger.info("createTable"+" - "+"Create table");
        String query = new QueryBuilder.Builder().createTable(clz).build().toString();
        System.out.println(query);
        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithCreateTableQuery(connection, query) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(ExceptionMessage.CREATE_TABLE.getMessage());
        }
    }

    private <T> List<T> readFromDB(ResultSet rs, Class<T> clz) throws
            SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<T> results = new ArrayList<>();
        logger.info("readFromDB"+" - "+"Read from the database");
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
