package Utils;

import java.sql.*;

public class ConnectionUtilities {

    public static <T> ResultSet TableConnectionWithSelectQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public static Boolean TableConnectionWithDeleteQuery(Connection connection, String query) throws SQLException {
        PreparedStatement preparedStmt = connection.prepareStatement(query);
        return preparedStmt.execute();
    }}
