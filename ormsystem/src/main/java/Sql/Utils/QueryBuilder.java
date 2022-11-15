package Sql.Utils;

import Annotation.AutoIncrement;
import Annotation.NotNull;
import Annotation.PrimaryKey;
import Annotation.Unique;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class QueryBuilder {
    private final String query;

    public String toString() {
        return query;
    }

    public static class Builder {
        private String query;

        public Builder() {
            this.query = "";
        }

        public Builder select(String select) {
            this.query += "SELECT " + select + " ";
            return this;
        }

        public <T> Builder from(Class<T> from) {
            this.query += "FROM " + from.getSimpleName().toLowerCase() + " ";
            return this;
        }

        public <T> Builder where(String key, T value) {
            if (!this.query.contains("WHERE")) this.query += "WHERE ";
            this.query += key + "=" + decideInstance(value);
            return this;
        }

        public Builder and() {
            this.query += "AND ";
            return this;
        }

        public Builder or() {
            this.query += "OR ";
            return this;
        }

        public Builder limit(int amount) {
            this.query += "LIMIT " + amount;
            return this;
        }

        public <T> Builder delete(Class<T> clz) {
            this.query += "DELETE FROM " + clz.getSimpleName().toLowerCase() + " ";
            return this;
        }

        public <T> Builder truncate(Class<T> clz) {
            this.query += "TRUNCATE TABLE " + clz.getSimpleName().toLowerCase() + " ";
            return this;
        }

        public <T> Builder insertValues(T object) {
            this.query += "(";
            Field[] declaredFields = object.getClass().getDeclaredFields();
            Arrays.stream(declaredFields).forEach(field -> {
                field.setAccessible(true);
                try{
                    if(field.isAnnotationPresent(NotNull.class) && field.get(object) == null){
                        throw new IllegalArgumentException(ExceptionMessage.EMPTY_NOTNULL_FIELD.getMessage());
                    }
                    if (!field.isAnnotationPresent(AutoIncrement.class)) {
                        Object value = field.get(object);
                        this.query += decideInstance(value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(ExceptionMessage.FIELDS_OF_OBJECT.getMessage());
                }
            });

            this.query = this.query.substring(0, this.query.length() - 2);
            this.query += ") ";

            return this;
        }

        public <T> Builder insert(T object) {
            this.query += "INSERT INTO " + object.getClass().getSimpleName().toLowerCase() + " ( ";

            Field[] declaredFields = object.getClass().getDeclaredFields();
            Arrays.stream(declaredFields).forEach(field -> {
                if (!field.isAnnotationPresent(AutoIncrement.class)) {
                    this.query += field.getName() + ", ";
                }
            });

            this.query = this.query.substring(0, this.query.length() - 2);
            this.query += ") VALUES ";

            return this;
        }



        public <T> Builder createTable(Class<T> clz) {
            this.query += "CREATE TABLE IF NOT EXISTS " + clz.getSimpleName().toLowerCase() + "(";

            AtomicInteger primaryKeyCounter = new AtomicInteger();
            AtomicInteger autoIncrementCounter = new AtomicInteger();

            Field[] declaredFields = clz.getDeclaredFields();
            Arrays.stream(declaredFields).forEach(field -> {
                this.query += "" + field.getName() + " ";
                Type type = field.getAnnotatedType().getType();

                if (String.class.equals(type)) this.query += "varchar(255) ";
                else if (int.class.equals(type)) this.query += "int ";
                else if (boolean.class.equals(type)) this.query += "BOOLEAN ";
                else System.out.println("Unsupported class");


                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    if (primaryKeyCounter.incrementAndGet() > 1)
                        throw new IllegalArgumentException(ExceptionMessage.MULTIPLE_PRIMARY_KEY.getMessage());
                    this.query += "PRIMARY KEY ";
                }
                if (field.isAnnotationPresent(Unique.class)) this.query += "UNIQUE ";
                if (field.isAnnotationPresent(NotNull.class)) this.query += "NOT NULL ";
                if (field.isAnnotationPresent(AutoIncrement.class)) {
                    if (autoIncrementCounter.incrementAndGet() > 1)
                        throw new IllegalArgumentException(ExceptionMessage.MULTIPLE_AUTO_INCREMENT.getMessage());
                    this.query += "AUTO_INCREMENT ";
                }

                this.query = this.query.trim();
                this.query += ", ";
            });

            this.query = this.query.substring(0, this.query.length() - 2);
            this.query += ")";

            return this;
        }

        public <V> Builder values(Map<String, V> map) {
            this.query += "(";

            for (String key : map.keySet()) this.query += key + ", ";

            this.query = this.query.substring(0, this.query.length() - 2);
            this.query += ") VALUES (";

            for (V value : map.values()) this.query += decideInstance(value);


            this.query = this.query.substring(0, this.query.length() - 2);
            this.query += ") ";

            return this;
        }


        public <T> Builder update(Class<T> clz) {
            this.query += "UPDATE " + clz.getSimpleName().toLowerCase() + " ";
            return this;
        }

        public Builder set() {
            if (!this.query.contains("SET")) this.query += "SET ";
            return this;
        }

        public <T> Builder setValue(String key, T value) {
            this.query += key + "=" + decideInstance(value);
            return this;
        }

        public QueryBuilder build() {
            return new QueryBuilder(this);
        }

        // --------- Additional Builder Private Functions ---------- //
        private <V> String decideInstance(V value) {
            if (value == null)
                return "NULL ";
            if (value instanceof Integer || value instanceof Double || value instanceof Float || value instanceof Short || value instanceof Long || value instanceof Byte)
                return value + ", ";
            if (value instanceof String || value instanceof Character)
                return "'" + value + "', ";
            if (value instanceof Boolean)
                return (Boolean) value ? "1, " : "0, ";
            return value.toString() + ", ";
        }
    }

    private QueryBuilder(Builder builder) {
        this.query = builder.query;
    }
}
