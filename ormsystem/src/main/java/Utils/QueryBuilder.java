package Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

public class QueryBuilder {
    private String query;

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

        public Builder from(Class from) {
            this.query += "FROM " + from.getSimpleName().toLowerCase() + " ";
            return this;
        }

        public <T> Builder where(String key, T value) {
            if (!this.query.contains("WHERE")) this.query += "WHERE ";
            this.query += key + "=";
            if (value instanceof Integer) {
                this.query += value + " ";
            } else if (value instanceof String) {
                this.query += "'" + value + "' ";
            } else {
                throw new IllegalArgumentException("Type of object must be an integer or a string");
            }
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

        public Builder delete(String tableName) {
            this.query += "DELETE FROM " + tableName + " ";
            return this;
        }

        public Builder truncate(String tableName) {
            this.query += "TRUNCATE TABLE " + tableName + " ";
            return this;
        }

        public <T> Builder insert(Class<T> clz) {
            this.query += "INSERT INTO " + clz.getSimpleName().toLowerCase() + " ";
            return this;
        }

        public <T> Builder createTable(Class<T> clz) {
            this.query += "CREATE TABLE IF NOT EXISTS " + clz.getSimpleName().toLowerCase() + "(";
            Field[] declaredFields = clz.getDeclaredFields();
            Arrays.stream(declaredFields).forEach(field -> {
                this.query += field.getName() + " ";
                Type type = field.getAnnotatedType().getType();

                if (String.class.equals(type)) {
                    this.query += "varchar(50), ";
                } else if (int.class.equals(type)) {
                    this.query += "int, ";
                } else {
                    System.out.println("Unsupported class");
                }
            });

            this.query = this.query.substring(0, this.query.length() - 2);
            this.query += ")";

            return this;
        }

        public <V> Builder values(Map<String, V> map) {
            this.query += "(";

            for (String key : map.keySet())
                this.query += key + ", ";

            this.query = this.query.substring(0, this.query.length() - 2);
            this.query += ") VALUES (";

            for (V value : map.values()) {
                if (value instanceof Integer) {
                    this.query += value + ", ";
                } else if (value instanceof String) {
                    this.query += "'" + value + "', ";
                } else {
                    throw new IllegalArgumentException("Type of value object must be an integer or a string");
                }
            }

            this.query = this.query.substring(0, this.query.length() - 2);
            this.query += ") ";

            return this;
        }

        public <T> Builder update(Class<T> clz) {
            this.query += "UPDATE " + clz.getSimpleName().toLowerCase() + " ";
            return this;
        }

        public <T> Builder set(String key, T value) {
            if (!this.query.contains("SET")) this.query += "SET ";
            this.query += key + "=";
            if (value instanceof Integer) {
                this.query += value + " ";
            } else if (value instanceof String) {
                this.query += "'" + value + "' ";
            } else {
                throw new IllegalArgumentException("Type of value object must be an integer or a string");
            }
            return this;
        }

        public QueryBuilder build() {
            return new QueryBuilder(this);
        }
    }

    private QueryBuilder(Builder builder) {
        this.query = builder.query;
    }
}
