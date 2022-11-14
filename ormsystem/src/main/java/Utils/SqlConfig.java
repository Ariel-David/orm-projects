package Utils;

public class SqlConfig {

    private static final String url = "jdbc:mysql://localhost:3308/world";
    private static final String username = "root";
    private static final String password = "1234";

    public static String getUrl() {
        return url;
    }
    public static String getUsername() {
        return username;
    }
    public static String getPassword() {
        return password;
    }
}
