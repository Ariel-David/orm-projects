package Sql;

import Annotation.NotNull;
import Annotation.PrimaryKey;
import Annotation.Unique;
import Entity.Animal;
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

    public <T> List<?> updateEntireEntity(T object) {

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
        } catch (Exception e) {
            System.out.println("fa");
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
        } catch (Exception e) {
            System.out.println("catchhh");
        }
        return null;
    }

    public <T, V, K> List<T> updateProperty(Class<T> clz, String whereKey, K whereValue, String filed, V value) {
        String query = new QueryBuilder.Builder().update(clz).set().setValue(filed, value).build().toString();
        query = query.substring(0, query.length() - 2) + " ";
        query += new QueryBuilder.Builder().where(whereKey, whereValue).build().toString();
        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            try{
                if (ConnectionUtilities.TableConnectionWithUpdateQuery(connection, query) > 0) {
                    return findAny(clz, whereKey, whereValue);
                }
            }catch (SQLSyntaxErrorException err){
                System.out.println(err.getMessage());
                if (err.getMessage().contains("Table")){
                        createTable(clz);
                }
            }
//            throw new SQLException("false");
//            System.out.println("false");
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    public <T> List<T> findAll(Class<T> clz) {
        String query = new QueryBuilder.Builder()
                .select("*")
                .from(clz)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithSelectQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            return null;
//            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            return null;
//            throw new RuntimeException(e);
        }
    }

    public <T, V> List<T> findOne(Class<T> clz, String field, V value) {
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

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            ResultSet rs = ConnectionUtilities.TableConnectionWithSelectQuery(connection, query);
            return readFromDB(rs, clz);

        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T, V> Boolean deleteOne(Class<T> clz, String field, V value) {
        String query = new QueryBuilder.Builder()
                .delete(clz)
                .where(field, value)
                .limit(1)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithDeleteQuery(connection, query) > 0;

        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public <T, V> Boolean deleteAny(Class<T> clz, String field, V value) {
        String query = new QueryBuilder.Builder()
                .delete(clz)
                .where(field, value)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithDeleteQuery(connection, query) > 0;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return null;
    }

    public <T, V> Boolean deleteEntireTable(Class<T> clz) {
        String query = new QueryBuilder.Builder()
                .truncate(clz)
                .build().toString();

        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithDeleteQuery(connection, query) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't truncate this table");
        }
    }

    public <T> boolean createTable(Class<T> clz) {
        String query = new QueryBuilder.Builder().createTable(clz).build().toString();
        System.out.println(query);
        try (Connection connection = ConnectionUtilities.getConnectionInstance()) {
            return ConnectionUtilities.TableConnectionWithCreateTableQuery(connection, query) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't create the table");
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
