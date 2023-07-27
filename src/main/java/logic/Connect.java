package logic;

import java.sql.*;

/**
 * @Author Pavel Yurov
 * 26.07.2023
 */
public class Connect  {
    private String urlConnect; //172.22.235.118
    private String username;
    private String password;
    private Statement statement;
    private String msg;
    public Connect(String url, String user, String pass){
        this.urlConnect=url;
        this.username=user;
        this.password=pass;
    }
    public String tryConnect() {
        if (urlConnect != null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://"+urlConnect, username, password);
                setStatement(conn.createStatement());
                setMsg(String.valueOf(conn.getWarnings()));
            } catch (SQLException | ClassNotFoundException ex) {
                setMsg("Ошибка подключения к серверу");
            }
        }
        return this.getMsg();
    }
    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Statement getStatement() {
        return statement;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
