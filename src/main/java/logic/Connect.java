package logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Author Pavel Yurov
 * 26.07.2023
 */
public class Connect  {
    private String urlConnect;
    private String username;
    private String password;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
    public Connect(String url, String user, String pass){
        this.urlConnect=url;
        this.username=user;
        this.password=pass;
    }
    public void tryConnect() {
        if (urlConnect != null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://"+urlConnect, username, password);
                setMsg(String.valueOf(conn.getWarnings()));
            } catch (SQLException | ClassNotFoundException ex) {
                setMsg("Ошибка подключения к серверу");
            }
        }
    }
}
