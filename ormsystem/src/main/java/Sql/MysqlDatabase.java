package Sql;

import Utils.ConnectionUtilities;
import Utils.ExceptionMessage;
import Utils.QueryBuilder;
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
        logger.info("createOne" + " - " + "Create one");
        String query = new QueryBuilder.Builder().insert(object).insertValues(object).build().toString();
        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            boolean hasBeenSuccessfullyInserted = ConnectionUtilities.TableConnectionWithInsertQuery(connection, query);
        } catch (SQLException e) {
            logger.fatal("createOne" + ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage());
            throw new IllegalStateException(ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage(), e);
        }
    }

    public <T> void createMany(List<T> objects) {
        logger.info("createMany" + " - " + "Create many");
        String query = "";
        // check if the list is empty!
        query = new QueryBuilder.Builder().insert(objects.get(0)).build().toString();
        for (T object : objects)
            query += new QueryBuilder.Builder().insertValues(object).build().toString() + ", ";
        query = query.substring(0, query.length() - 2);
        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            boolean hasBeenSuccessfullyInserted = ConnectionUtilities.TableConnectionWithInsertQuery(connection, query);
            logger.info("createMany" + " - " + "has Been Success fully Inserted: " + hasBeenSuccessfullyInserted);
            if (hasBeenSuccessfullyInserted == false) {
                logger.error("createMany " + "Failed to insert");
            }
        } catch (SQLException e) {
            logger.fatal("createMany" + ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage());
            throw new IllegalStateException(ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage(), e);
        }
    }

    public <T> List<?> updateEntireEntity(T object) {
        logger.info("<updateEntireEntity> - Update entire entity with object: " + object.toString());

        Class<?> clz = object.getClass();
        Field[] declaredFields = clz.getDeclaredFields();
        Object index = null;
        T item;
        try {
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (field.getName().equals("id")) {
                    index = field.get(object);
                    List<?> list = findOne(clz, "id", index); //TODO:handle empty list exception
                    item = (T) list.get(0);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            logger.fatal("updateEntireEntity" + ExceptionMessage.FIELDS_OF_OBJECT.getMessage());
            throw new RuntimeException(ExceptionMessage.FIELDS_OF_OBJECT.getMessage());
        }

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            String query = new QueryBuilder.Builder().update(clz).set().build().toString();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                query += new QueryBuilder.Builder().setValue(field.getName(), field.get(object)).build().toString() + ", ";
            }
            query = query.substring(0, query.length() - 2) + " ";
            query += new QueryBuilder.Builder().where("id", index).build().toString();
            System.out.println(query);
            int indexChanged = ConnectionUtilities.TableConnectionWithUpdateQuery(connection, query);
            return findOne(clz, "id", index);
        } catch (SQLException e) {
            logger.fatal("updateEntireEntity " + ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage());
            throw new IllegalStateException(ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.fatal("updateEntireEntity " + ExceptionMessage.FIELDS_OF_OBJECT.getMessage());
            throw new RuntimeException(ExceptionMessage.FIELDS_OF_OBJECT.getMessage());
        }
    }

    public <T, V, K> int updateProperty(Class<T> clz, String whereKey, K whereValue, String field, V value) {
        logger.info("update" + " - " + "Update the " + field + " for " + "'" + value + "'" + " to item with " + whereKey + " of value " + whereValue);
        String query = new QueryBuilder.Builder()
                .update(clz)
                .set()
                .setValue(field, value)
                .where(whereKey, whereValue)
                .build().toString();
        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithIntegerResponse(connection, query);
        } catch (SQLException e) {
            logger.fatal("update" + ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage());
            throw new IllegalStateException("update" + ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage(), e);
        }
    }

    public <T> List<T> findAll(Class<T> clz) {
        logger.info("findAll" + " - " + "Find all items");
        String query = new QueryBuilder.Builder()
                .select("*")
                .from(clz)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            System.out.println(query);
            ResultSet rs = ConnectionUtilities.TableConnectionWithSelectQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            logger.fatal("findAll" + ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage());
            throw new IllegalStateException(ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage(), e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            logger.fatal("findAll" + ExceptionMessage.RUNTIME.getMessage());
            throw new RuntimeException(e);
        }
    }

    public <T, V> List<T> findOne(Class<T> clz, String field, V value) {
        logger.info("findOne" + " - " + "Get one item with field: " + " '" + field + "' " + " and value: " + "'" + value.toString() + "'");
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
            logger.fatal("findOne" + ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage());
            throw new IllegalStateException(ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage(), e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            logger.fatal("findOne" + ExceptionMessage.RUNTIME.getMessage());
            throw new RuntimeException(e);
        }
    }

    public <T, V> List<T> findAny(Class<T> clz, String field, V value) {
        logger.info("findAny" + " - " + "Get all items with field: " + " '" + field + "' " + " and value: " + "'" + value.toString() + "'");
        String query = new QueryBuilder.Builder()
                .select("*")
                .from(clz)
                .where(field, value)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithSelectQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            logger.fatal("findAny" + ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage());
            throw new IllegalStateException(ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage(), e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            logger.fatal("findAny" + ExceptionMessage.RUNTIME.getMessage());
            throw new RuntimeException(e);
        }
    }

    public <T, V> Boolean deleteOne(Class<T> clz, String field, V value) {
        logger.info("deleteOne" + " - " + "Delete one item with field: " + " '" + field + "' " + " and value: " + "'" + value.toString() + "'");
        String query = new QueryBuilder.Builder()
                .delete(clz)
                .where(field, value)
                .limit(1)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithDeleteQuery(connection, query) > 0;

        } catch (SQLException e) {
            logger.fatal("deleteOne" + ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage());
            throw new IllegalStateException(ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage(), e);
        }
    }

    public <T, V> Boolean deleteAny(Class<T> clz, String field, V value) {
        logger.info("deleteAny" + " - " + "delete any item with field: " + " '" + field + "' " + " and value: " + "'" + value.toString() + "'");
        String query = new QueryBuilder.Builder()
                .delete(clz)
                .where(field, value)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithDeleteQuery(connection, query) > 0;
        } catch (SQLException e) {
            logger.fatal("deleteAny" + ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage());
            throw new IllegalStateException(ExceptionMessage.ILLEGAL_SQL_QUERY.getMessage(), e);
        }
    }

    public <T, V> Boolean deleteEntireTable(Class<T> clz) {
        logger.info("deleteEntireTable" + " - " + "delete the entire table");
        String query = new QueryBuilder.Builder()
                .truncate(clz)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithDeleteQuery(connection, query) > 0;
        } catch (SQLException e) {
            logger.fatal("deleteEntireTable" + ExceptionMessage.TRUNCATE.getMessage());
            throw new RuntimeException(ExceptionMessage.TRUNCATE.getMessage());
        }
    }

    public <T> boolean createTable(Class<T> clz) {
        logger.info("createTable" + " - " + "Create table");
        String query = new QueryBuilder.Builder().createTable(clz).build().toString();
        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithCreateTableQuery(connection, query) > 0;
        } catch (SQLException e) {
            logger.fatal("createTable" + ExceptionMessage.CREATE_TABLE.getMessage());
            throw new RuntimeException(ExceptionMessage.CREATE_TABLE.getMessage());
        }
    }

    private <T> List<T> readFromDB(ResultSet rs, Class<T> clz) throws
            SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        List<T> results = new ArrayList<>();

        logger.info("readFromDB" + " - " + "Reading all entities from the database: " + clz.getSimpleName());

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

        System.out.println(results);
        return results;
    }
}
