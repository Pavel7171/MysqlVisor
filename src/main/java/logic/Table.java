package logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Pavel Yurov
 * 26.07.2023
 */
public class Table {
    private String base;
    private Object connect;
    private List<String> listOfTable;
    public Table(Connect connect,String base){
        this.base=base;
        this.connect=connect;
    }
    public List<String> getTableList(Connect connect,String baseName ) throws SQLException {
        String sqlQuery = "SHOW TABLES from "+baseName;
        ResultSet resultSet = connect.getStatement().executeQuery(sqlQuery);
        List<String> listOfTable = new ArrayList<>();
        while (resultSet.next()) {
            String data = resultSet.getString(1);
            listOfTable.add(data);
        }
        setListOfTable(listOfTable);
        return this.getListOfTable();
    }
    public List<String> getListOfTable() {
        return listOfTable;
    }

    public void setListOfTable(List<String> listOfTable) {
        this.listOfTable = listOfTable;
    }
}
