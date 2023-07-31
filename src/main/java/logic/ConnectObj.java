package logic;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @Author Pavel Yurov
 * 31.07.2023
 */
public class ConnectObj {
    private String url,user,pass; //172.22.235.118
    Connection connect;
    Statement statement;
    private List<String> listOfBase;
    private List<String> listOfTable;
    JTable fullTable;
    JTable searchTable;
    String msg;



    int countRow;


    Vector<String> columnName;

    private String baseName,tableName;
    public ConnectObj(String url, String user, String pass){
        this.url=url;
        this.user=user;
        this.pass=pass;
    }
    public String tryConnect(){
        if (url != null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://"+url, user, pass);
                setStatement(conn.createStatement());
                setMsg(String.valueOf(conn.getWarnings()));
            } catch (SQLException | ClassNotFoundException ex) {
                setMsg("Ошибка подключения к серверу");
            }
        }
        return this.getMsg();
    }

    public List<String> showBases(ConnectObj connectObj) throws SQLException{
        String sqlQuery ="SHOW DATABASES";
        ResultSet resultSet = connectObj.getStatement().executeQuery(sqlQuery);
        List<String> listBase = new ArrayList<>();
        while (resultSet.next()) {
            String data = resultSet.getString(1);
            listBase.add(data);
        }
        setListOfBase(listBase);
        return this.getListOfBase();
    }

    public List<String> showTables(ConnectObj connectObj,String baseName) throws SQLException {
        String sqlQuery = "SHOW TABLES from "+baseName;
        ResultSet resultSet = connectObj.getStatement().executeQuery(sqlQuery);
        List<String> listOfTable = new ArrayList<>();
        while (resultSet.next()) {
            String data = resultSet.getString(1);
            listOfTable.add(data);
        }
        setListOfTable(listOfTable);
        return this.getListOfTable();
    }
    public JTable showDataFromTable(ConnectObj connectObj) throws SQLException {
        ArrayList columnNames = new ArrayList();
        ArrayList data = new ArrayList();
        connectObj.setUrl("jdbc:mysql://"+connectObj.getUrl()+"/"+connectObj.getBaseName());
        Connection connection = DriverManager.getConnection(getUrl(),getUser(),getPass());
        String sqlQuery = "SELECT * FROM "+connectObj.getTableName();
        ResultSet resultSet = connection.createStatement().executeQuery(sqlQuery);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = metaData.getColumnCount();
        //  Get column names
        for (int i = 1; i <= columns; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        //  Get row data
        while (resultSet.next()) {
            ArrayList row = new ArrayList(columns);
            for (int i = 1; i <= columns; i++) {
                row.add(resultSet.getObject(i));
            }
            data.add(row);
        }

        Vector columnNamesVector = new Vector();
        Vector dataVector = new Vector();

        for (int i = 0; i < data.size(); i++) {
            ArrayList subArray = (ArrayList) data.get(i);
            Vector subVector = new Vector();
            for (int j = 0; j < subArray.size(); j++) {
                subVector.add(subArray.get(j));
            }
            dataVector.add(subVector);
        }
        for (int i = 0; i < columnNames.size(); i++)
            columnNamesVector.add(columnNames.get(i));
            setColumnName(columnNamesVector);
        JTable table = new JTable(dataVector, columnNamesVector)
        {
            public Class getColumnClass(int column)

            {
                for (int row = 0; row < getRowCount(); row++)
                {
                    Object o = getValueAt(row, column);

                    if (o != null)
                    {
                        return o.getClass();
                    }
                }

                return Object.class;
            }
        };
        setFullTable(table);
        return this.getFullTable();
    }
    public JTable searchOnData(ConnectObj connectObj,String searchText,String columnName) throws SQLException {
        ArrayList<String> columnNames = new ArrayList();
        ArrayList<Object> data = new ArrayList();
        Connection connection = DriverManager.getConnection(getUrl(),getUser(),getPass());
        String sqlQuery = "SELECT * FROM "+connectObj.getTableName()+" WHERE "+columnName+" LIKE '%"+searchText+"%'";
        ResultSet resultSet = connection.createStatement().executeQuery(sqlQuery);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columns = metaData.getColumnCount();
        //  Get column names
        for (int i = 1; i <= columns; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        //  Get row data
        while (resultSet.next()) {
            ArrayList row = new ArrayList(columns);
            for (int i = 1; i <= columns; i++) {
                row.add(resultSet.getObject(i));
            }
            data.add(row);
            countRow++;
        }
        setCountRow(countRow);

        Vector columnNamesVector = new Vector();
        Vector dataVector = new Vector();

        for (int i = 0; i < data.size(); i++) {
            ArrayList subArray = (ArrayList) data.get(i);
            Vector subVector = new Vector();
            for (int j = 0; j < subArray.size(); j++) {
                subVector.add(subArray.get(j));
            }
            dataVector.add(subVector);
        }
        for (int i = 0; i < columnNames.size(); i++)
            columnNamesVector.add(columnNames.get(i));
            JTable searchTable = new JTable(dataVector, columnNamesVector)
        {
            public Class getColumnClass(int column)
            {
                for (int row = 0; row < getRowCount(); row++)
                {
                    Object o = getValueAt(row, column);

                    if (o != null)
                    {
                        return o.getClass();
                    }
                }
                return Object.class;
            }
        };
        setSearchTable(searchTable);
        return this.getSearchTable();
    }
    public JTable getSearchTable() {
        return searchTable;
    }

    public void setSearchTable(JTable searchTable) {
        this.searchTable = searchTable;
    }

    public JTable getFullTable() {
        return fullTable;
    }

    public void setFullTable(JTable fullTable) {
        this.fullTable = fullTable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public List<String> getListOfBase() {
        return listOfBase;
    }

    public void setListOfBase(List<String> listOfBase) {
        this.listOfBase = listOfBase;
    }

    public List<String> getListOfTable() {
        return listOfTable;
    }

    public void setListOfTable(List<String> listOfTable) {
        this.listOfTable = listOfTable;
    }
    public Vector<String> getColumnName() {
        return columnName;
    }

    public void setColumnName(Vector<String> columnName) {
        this.columnName = columnName;
    }
    public int getCountRow() {
        return countRow;
    }

    public void setCountRow(int countRow) {
        this.countRow = countRow;
    }
}
