package Utils;

import java.sql.*;

public class ConnectionUtilities {

    public static <T> ResultSet TableConnectionWithSelectQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public static int TableConnectionWithDeleteQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
//        PreparedStatement preparedStmt = connection.prepareStatement(query);
//        return preparedStmt.executeUpdate();
    }

    public static int TableConnectionWithCreateTableQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public static int TableConnectionWithUpdateQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
//        PreparedStatement preparedStmt = connection.prepareStatement(query);
//        return preparedStmt.executeUpdate();
    }

    public static boolean TableConnectionWithInsertQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.execute(query);
    }

    public static Connection getConnectionInstance() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword());
    }

}
