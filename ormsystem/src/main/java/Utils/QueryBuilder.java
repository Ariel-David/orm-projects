package Utils;

public class QueryBuilder {
    private String query;

    public String getQuery() {
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
            } else if(value instanceof String) {
                this.query += "'" + value + "' ";
            } else {
                throw new IllegalArgumentException("Type of element must be an integer or a string");
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

        public Builder limit(int amount){
            this.query += "LIMIT " + amount;
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
