import logic.Connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Author Pavel Yurov
 * 25.07.2023
 */
public class App {
    static String urlConnect = "172.22.215.118";
    static String username = "java";
    static String password ="java";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://"+urlConnect, username, password);
        System.out.println(conn.getWarnings());

    }
}
