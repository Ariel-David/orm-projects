package Client;

import java.sql.*;

public class Client {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/sql_testing";
        String username = "root";
        String password = "1234";

        System.out.println("Connecting database...");

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/sql_testing", "root", "1234");
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from test_table");
////            while (rs.next())
////                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
//            System.out.println("Connection is solved");
//            con.close();
//        } catch (Exception e) {
//            System.out.println(e);
//        }

    }
}
