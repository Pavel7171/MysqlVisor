package logic;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Pavel Yurov
 * 31.07.2023
 */
public class MysqlActionController {
    private String url;//172.21.114.198
    private final String user;
    private final String password;
    private Statement connectStatement;
    private List<String> baseList;
    private List<String> tableList;
    private JTable fullTable;
    private String checkQueryResultMessage;
    private int countRow;
    private Vector <String> tableColumnName;
    private String selectBaseName, selectTableName;
    public MysqlActionController(String url, String user, String password){
        this.url=url;
        this.user=user;
        this.password = password;
    }
    public void tryServerConnect(){
        if (url != null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://"+url ,user ,password);
                setConnectStatement(connection.createStatement());
                setCheckQueryResultMessage(String.valueOf(connection.getWarnings()));
            } catch (SQLException | ClassNotFoundException ex) {
                setCheckQueryResultMessage("Error");
            }
        }
    }
    public List<String> showBases(MysqlActionController mysqlActionController){
        String sqlQueryShowBases ="SHOW DATABASES";
        List<String> listOfBase,errorListOfBase;
        try{
            ResultSet resultSet = mysqlActionController.getConnectStatement().executeQuery(sqlQueryShowBases);
            listOfBase = new ArrayList<>();
            while (resultSet.next()) {
                String baseName = resultSet.getString(1);
                listOfBase.add(baseName);
            }
            resultSet.close();
            setBaseList(listOfBase);
        }catch (Exception e){
            errorListOfBase = new ArrayList<>();
            errorListOfBase.add(0,e.getMessage());
            setBaseList(errorListOfBase);
        }
        return this.getBaseList();
    }

    public List<String> showTablesInSelectBase(MysqlActionController mysqlActionController, String baseName) {
        String sqlQueryShowTablesFromBase = "SHOW TABLES from "+baseName;
        List<String> listOfTable,errorListOfTable;
        try{
            ResultSet resultSet = mysqlActionController.getConnectStatement().executeQuery(sqlQueryShowTablesFromBase);
            listOfTable = new ArrayList<>();
            while (resultSet.next()) {
                String tableName = resultSet.getString(1);
                listOfTable.add(tableName);
            }
            resultSet.close();
            setTableList(listOfTable);
        }catch (Exception e){
            errorListOfTable = new ArrayList<>();
            errorListOfTable.add(0,e.getMessage());
            setTableList(errorListOfTable);
        }
        return this.getTableList();
    }
    public JTable showDataFromTable(MysqlActionController mysqlActionController){
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<Object> data = new ArrayList<>();
        try{
            mysqlActionController.setUrl("jdbc:mysql://"+ mysqlActionController.getUrl()+"/"+ mysqlActionController.getSelectBaseName());
            String sqlQuerySelectDataFromTable = "SELECT * FROM "+ mysqlActionController.getSelectBaseName()+"."+ mysqlActionController.getSelectTableName();
            ResultSet resultSet = mysqlActionController.getConnectStatement().executeQuery(sqlQuerySelectDataFromTable);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columns = metaData.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            while (resultSet.next()) {
                ArrayList <Object> row = new ArrayList<>(columns);
                for (int i = 1; i <= columns; i++) {
                    row.add(resultSet.getObject(i));
                }
                data.add(row);
            }
            Vector <Vector> dataVector = new Vector<>();
            for (Object datum : data) {
                ArrayList subArray = (ArrayList) datum;
                Vector subVector = new Vector<>(subArray);
                dataVector.add(subVector);
            }
            Vector<String> columnNamesVector = new Vector<>(columnNames);
            setTableColumnName(columnNamesVector);
            setCountRow(mysqlActionController.countRow);
            JTable table = new JTable(dataVector, columnNamesVector) {
                public Class getColumnClass(int column) {
                    for (int row = 0; row < mysqlActionController.getCountRow(); row++) {
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
            resultSet.close();
        }catch (Exception e){
            JTable jTableErrorLog = new JTable();
            setFullTable(jTableErrorLog);
        }
        return this.getFullTable();
    }
    public void changeDataInTable(MysqlActionController mysqlActionController, Object changeData, int columnIndex, Object idIndex) {
        String sqlQueryChangeDataInTable ="UPDATE "+ mysqlActionController.getSelectBaseName()+"."
                + mysqlActionController.getSelectTableName()+" SET "
                + mysqlActionController.getTableColumnName().get(columnIndex)+" = "
                +"'"+changeData+"'"+" WHERE ID = "+idIndex;
        try{
            mysqlActionController.getConnectStatement().executeUpdate(sqlQueryChangeDataInTable);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private String checkDataFormatDownloadFile(String pathToFile){
        String msg = "OK";
        Set<Integer> arrayLengthRepeat = new HashSet<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFile));
            String lineRow = bufferedReader.readLine();
            while (lineRow!=null){
                String[] arrayFileData = lineRow.split(",");
                arrayLengthRepeat.add(arrayFileData.length);
                if(arrayLengthRepeat.size()>1){
                    msg ="Error";
                }else {
                    msg = "OK";
                }
                lineRow=bufferedReader.readLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return msg;
    }
    public String downloadDataFromTable(MysqlActionController mysqlActionController, String pathToFile){
        String answerQuery = null;
        String path = pathToFile.replaceAll(Pattern.quote("\\"), Matcher.quoteReplacement("\\\\"));
        if(checkDataFormatDownloadFile(path).equals("OK")){
            int maxId=0;
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
                String lineRow = bufferedReader.readLine();
                while (lineRow !=null){
                    String sqlQueryMaxId = "SELECT MAX(ID) FROM "+ mysqlActionController.getSelectBaseName()+"."+ getSelectTableName();
                    ResultSet resultSet = mysqlActionController.getConnectStatement().executeQuery(sqlQueryMaxId);
                    if(resultSet.next()){
                        maxId =resultSet.getInt(1)+1;
                        String sqlQuery = "INSERT INTO "+ mysqlActionController.getSelectBaseName()
                                +"."+ getSelectTableName()
                                +" ("+ tableColumnName.get(0)+") "+"values ('"+maxId+"')";
                        mysqlActionController.getConnectStatement().executeUpdate(sqlQuery);
                    }
                    String[] arrayFileData = lineRow.split(",");
                    for(int j =0;j<arrayFileData.length;j++){
                        String elem = arrayFileData[j].replaceAll(Pattern.quote("\\"), Matcher.quoteReplacement("\\\\"));
                        arrayFileData[j]=elem;
                    }
                    for(int i = 2; i<= mysqlActionController.tableColumnName.size(); i++){
                        String sqlAddData = "UPDATE "+ mysqlActionController.getSelectBaseName()+
                                "."+ getSelectTableName()+" SET "+ tableColumnName.get(i-1)
                                +" = "+"'"+arrayFileData[i-2]+"'"+" WHERE "+ tableColumnName.get(0)
                                +" = "+"'"+maxId+"'";
                        mysqlActionController.getConnectStatement().executeUpdate(sqlAddData);
                    }
                    lineRow=bufferedReader.readLine();
                }
                answerQuery = "File uploaded successfully";
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            answerQuery = "The number of columns in the file does not correspond to the number of columns in the table\n";
        }
        return answerQuery;
    }
    public void deleteData(MysqlActionController mysqlActionController, Object id) {
        String sqlQueryDeleteRow = "DELETE FROM "
                + mysqlActionController.getSelectBaseName()+"."
                + getSelectTableName()+" WHERE "+ tableColumnName.get(0)+" = "+id;
        try {
            mysqlActionController.getConnectStatement().executeUpdate(sqlQueryDeleteRow);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public JTable getFullTable() {return fullTable;}
    public void setFullTable(JTable fullTable) {this.fullTable = fullTable;}
    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
    public String getSelectBaseName() {return selectBaseName;}
    public void setSelectBaseName(String selectBaseName) {this.selectBaseName = selectBaseName;}
    public String getSelectTableName() {return selectTableName;}
    public void setSelectTableName(String selectTableName) {this.selectTableName = selectTableName;}
    public Statement getConnectStatement() {return connectStatement;}
    public void setConnectStatement(Statement connectStatement) {this.connectStatement = connectStatement;}
    public String getCheckQueryResultMessage() {return checkQueryResultMessage;}
    public void setCheckQueryResultMessage(String checkQueryResultMessage) {this.checkQueryResultMessage = checkQueryResultMessage;}
    public List<String> getBaseList() {return baseList;}
    public void setBaseList(List<String> baseList) {this.baseList = baseList;}
    public List<String> getTableList() {return tableList;}
    public void setTableList(List<String> tableList) {this.tableList = tableList;}
    public Vector<String> getTableColumnName() {return tableColumnName;}
    public void setTableColumnName(Vector<String> tableColumnName) {this.tableColumnName = tableColumnName;}
    public int getCountRow() {return countRow;}
    public void setCountRow(int countRow){this.countRow=countRow;}
}