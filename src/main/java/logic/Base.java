package logic;

import gui.LoginPage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Pavel Yurov
 * 26.07.2023
 */
public class Base {
    private String baseName;
    private Object connectData;



    private List<String> listOfBase;
    public Base(String baseName, Object connect){
        this.baseName=baseName;
        this.connectData=connect;
    }
    public List<String> showBases(Connect connect) throws SQLException {
        String sqlQueryShowBase = "SHOW DATABASES";
        ResultSet resultSet = connect.getStatement().executeQuery(sqlQueryShowBase);
        List<String> listBase = new ArrayList<>();
        while (resultSet.next()) {
            String data = resultSet.getString(1);
            listBase.add(data);
        }
        setListOfBase(listBase);
        return this.getListOfBase();
    }
    public List<String> getListOfBase() {
        return listOfBase;
    }

    public void setListOfBase(List<String> listOfBase) {
        this.listOfBase = listOfBase;
    }
}
