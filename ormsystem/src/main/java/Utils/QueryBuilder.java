package Utils;

import Annotation.AutoIncrement;
import Annotation.NotNull;
import Annotation.PrimaryKey;
import Annotation.Unique;

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
            } else if (value instanceof Boolean) {
                this.query += (Boolean) value ? 1 : 0 + " ";
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

        public <T> Builder delete(Class<T> clz) {
            this.query += "DELETE FROM " + clz.getSimpleName().toLowerCase() + " ";
            return this;
        }

        public <T> Builder truncate(Class<T> clz) {
            this.query += "TRUNCATE TABLE " + clz.getSimpleName().toLowerCase() + " ";
            return this;
        }

        public <T> Builder insert(Class<T> clz) {
            this.query += "INSERT INTO " + clz.getSimpleName().toLowerCase() + " ";
            return this;
        }

        private <T> Builder checkIfContainsAnnotations(Field field) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                this.query += "PRIMARY KEY ";
            }
            if (field.isAnnotationPresent(Unique.class)) {
                this.query += "UNIQUE ";
            }
            if (field.isAnnotationPresent(NotNull.class)) {
                this.query += "NOT NULL ";
            }
            if (field.isAnnotationPresent(AutoIncrement.class)) {
                this.query += "AUTO_INCREMENT ";
            }
            this.query = this.query.trim();
            this.query += ", ";

            return this;
        }

        public <T> Builder createTable(Class<T> clz) {
            this.query += "CREATE TABLE IF NOT EXISTS " + clz.getSimpleName().toLowerCase() + "(";
            Field[] declaredFields = clz.getDeclaredFields();

            Arrays.stream(declaredFields).forEach(field -> {
                this.query += "" + field.getName() + " ";
                Type type = field.getAnnotatedType().getType();

                if (String.class.equals(type)) {
                    this.query += "varchar(50) ";

                } else if (int.class.equals(type)) {
                    this.query += "int ";

                } else if (boolean.class.equals(type)) {
                    this.query += "BOOLEAN ";

                } else {
                    System.out.println("Unsupported class");
                }

                checkIfContainsAnnotations(field);
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
                } else if (value instanceof Boolean) {
                    this.query += (Boolean) value ? 1 : 0 + " ";
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

        public Builder set() {
            if (!this.query.contains("SET")) this.query += "SET ";
            return this;
        }

        public <T> Builder setValue(String key, T value){
            this.query += key + "=";
            if (value instanceof Integer) {
                this.query += value + ", ";
            } else if (value instanceof String) {
                this.query += "'" + value + "', ";
            } else if (value instanceof Boolean) {
                this.query += (Boolean) value ? 1 : 0 + ", ";
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
