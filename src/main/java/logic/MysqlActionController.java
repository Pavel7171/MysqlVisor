package logic;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Pavel Yurov
 * 31.07.2023
 */
public class MysqlActionController {
    private String url,user,pass; //172.22.235.118
    private Statement statement;
    private List<String> listOfBase;
    private List<String> listOfTable;
    private JTable fullTable;
    private JTable searchTable;
    private String msg;
    private int countRow;
    private Vector<String> columnName;
    private String baseName,tableName;
    public MysqlActionController(String url, String user, String pass){
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

    public List<String> showBases(MysqlActionController mysqlActionController) throws SQLException{
        String sqlQuery ="SHOW DATABASES";
        ResultSet resultSet = mysqlActionController.getStatement().executeQuery(sqlQuery);
        List<String> listBase = new ArrayList<>();
        while (resultSet.next()) {
            String data = resultSet.getString(1);
            listBase.add(data);
        }
        setListOfBase(listBase);
        return this.getListOfBase();
    }

    public List<String> showTables(MysqlActionController mysqlActionController, String baseName) throws SQLException {
        String sqlQuery = "SHOW TABLES from "+baseName;
        ResultSet resultSet = mysqlActionController.getStatement().executeQuery(sqlQuery);
        List<String> listOfTable = new ArrayList<>();
        while (resultSet.next()) {
            String data = resultSet.getString(1);
            listOfTable.add(data);
        }
        setListOfTable(listOfTable);
        return this.getListOfTable();
    }
    public JTable showDataFromTable(MysqlActionController mysqlActionController) throws SQLException {
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<Object> data = new ArrayList<>();
        mysqlActionController.setUrl("jdbc:mysql://"+ mysqlActionController.getUrl()+"/"+ mysqlActionController.getBaseName());
        String sqlQuery = "SELECT * FROM "+ mysqlActionController.getBaseName()+"."+ mysqlActionController.getTableName();
        ResultSet resultSet = mysqlActionController.getStatement().executeQuery(sqlQuery);
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
        Vector columnNamesVector = new Vector<>();
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
        JTable table = new JTable(dataVector, columnNamesVector) {
            public Class getColumnClass(int column) {
                for (int row = 0; row < mysqlActionController.countRow; row++) {
                    Object o = getValueAt(row, column);
                    if (o != null) {
                        return o.getClass();
                    }
                }
                return Object.class;
            }
        };
        table.setPreferredSize(null);
        setFullTable(table);
        return this.getFullTable();
    }
    public void updateData(MysqlActionController mysqlActionController, Object changeData, int columnIndex, Object idIndex) throws SQLException {
        String sqlQuery ="UPDATE "+ mysqlActionController.getBaseName()+"."+ mysqlActionController.getTableName()+" SET "+ mysqlActionController.getColumnName().get(columnIndex)+" = "+"'"+changeData+"'"+" WHERE id = "+idIndex;
        mysqlActionController.getStatement().executeUpdate(sqlQuery);
        System.out.println("OK");
    }

    public void downloadData(MysqlActionController mysqlActionController, String pathToFile){
        String path = pathToFile.replaceAll(Pattern.quote("\\"), Matcher.quoteReplacement("\\\\"));
        System.out.println(getColumnName().size());
        int maxId=0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line = bufferedReader.readLine();
            while (line !=null){
                String sqlQueryMaxId = "SELECT MAX(ID) FROM "+ mysqlActionController.getBaseName()+"."+getTableName();
                ResultSet resultSet = mysqlActionController.getStatement().executeQuery(sqlQueryMaxId);
                if(resultSet.next()){
                    System.out.println(resultSet.getInt(1));
                    maxId =resultSet.getInt(1)+1; //получение ID
                    String sqlAddId = "INSERT INTO "+ mysqlActionController.getBaseName()+"."+getTableName()+" ("+columnName.get(0)+") "+"values ('"+maxId+"')";
                    System.out.println(sqlAddId);
                    mysqlActionController.getStatement().executeUpdate(sqlAddId);
                }
                String[] arrayFileData = line.split(",");
                for(int j =0;j<arrayFileData.length;j++){
                    String elem =arrayFileData[j].toString().replaceAll(Pattern.quote("\\"), Matcher.quoteReplacement("\\\\"));
                    arrayFileData[j]=elem;
                }
                for(int i = 2; i<= mysqlActionController.columnName.size(); i++){

                    String sqlAddData = "UPDATE "+ mysqlActionController.getBaseName()+"."+getTableName()+" SET "+columnName.get(i-1)+" = "+"'"+arrayFileData[i-2]+"'"+" WHERE "+columnName.get(0)+" = "+"'"+maxId+"'";
                    System.out.println(sqlAddData);
                    mysqlActionController.getStatement().executeUpdate(sqlAddData);
                }
                line=bufferedReader.readLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void deleteData(MysqlActionController mysqlActionController, Object id) {
        String sqlQueryDeleteRow = "DELETE FROM "+ mysqlActionController.getBaseName()+"."+getTableName()+" WHERE "+columnName.get(0)+" = "+id;
        System.out.println("Ok");
        try {
            mysqlActionController.getStatement().executeUpdate(sqlQueryDeleteRow);
        }catch (Exception e){

        }
    }

    public JTable getSearchTable() {return searchTable;}
    public void setSearchTable(JTable searchTable) {this.searchTable = searchTable;}
    public JTable getFullTable() {return fullTable;}
    public void setFullTable(JTable fullTable) {this.fullTable = fullTable;}
    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
    public String getUser() {return user;}
    public String getPass() {return pass;}
    public String getBaseName() {return baseName;}
    public void setBaseName(String baseName) {this.baseName = baseName;}
    public String getTableName() {return tableName;}
    public void setTableName(String tableName) {this.tableName = tableName;}
    public Statement getStatement() {return statement;}
    public void setStatement(Statement statement) {this.statement = statement;}
    public String getMsg() {return msg;}
    public void setMsg(String msg) {this.msg = msg;}
    public List<String> getListOfBase() {return listOfBase;}
    public void setListOfBase(List<String> listOfBase) {this.listOfBase = listOfBase;}
    public List<String> getListOfTable() {return listOfTable;}
    public void setListOfTable(List<String> listOfTable) {this.listOfTable = listOfTable;}
    public Vector<String> getColumnName() {return columnName;}
    public void setColumnName(Vector<String> columnName) {this.columnName = columnName;}
    public int getCountRow() {return countRow;}
    public void setCountRow(int countRow) {this.countRow = countRow;}
}