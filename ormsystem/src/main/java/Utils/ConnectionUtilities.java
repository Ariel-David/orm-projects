package Utils;

import Sql.MysqlDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class ConnectionUtilities {
    private static Logger logger = LogManager.getLogger(MysqlDatabase.class.getName());

    public static <T> ResultSet TableConnectionWithSelectQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public static int TableConnectionWithDeleteQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public static int TableConnectionWithCreateTableQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public static int TableConnectionWithUpdateQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public static boolean TableConnectionWithInsertQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.execute(query);
    }

    public static boolean TableConnectionWithBooleanResponse(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.execute(query);
    }

    public static int TableConnectionWithIntegerResponse(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public static <T> ResultSet TableConnectionWithResultSetResponse(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public static Connection getConnectionInstance() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.fatal("Connection"+ExceptionMessage.RUNTIME.getMessage());
            throw new RuntimeException(e);
        }
        return DriverManager.getConnection(SqlConfig.getUrl(), SqlConfig.getUsername(), SqlConfig.getPassword());
    }

}
