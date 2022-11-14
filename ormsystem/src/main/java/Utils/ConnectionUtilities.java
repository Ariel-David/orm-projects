package Utils;

import java.sql.*;

public class ConnectionUtilities {

    public static <T> ResultSet TableConnectionWithSelectQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public static int TableConnectionWithDeleteQuery(Connection connection, String query) throws SQLException {
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        return preparedStmt.executeUpdate();
    }

    public static int TableConnectionWithCreateTableQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public static Connection getConnectionInstance() throws SQLException {
        return DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword());
    }


}
        return preparedStmt.execute();
    }

    public static int TableConnectionWithUpdateQuery(Connection connection, String query) throws SQLException {
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        return preparedStmt.executeUpdate();
    }
}
