package gui;

import logic.ConnectObj;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @Author Pavel Yurov
 * 27.07.2023
 */
public class WorkPage {
    Vector data;
    Vector columnName;
    ConnectObj connect;
    String table;
    String base;
    JTable jTable;
    String searchText;
    JScrollPane scrollPaneSearch;
    JScrollPane scrollPane;



    public WorkPage(JTable jTable, ConnectObj connectObj){
        this.jTable=jTable;
        this.connect=connectObj;
    }
    public void showWorkPage(JTable jTable){
        JFrame workPageFrame = new JFrame();
        workPageFrame.setSize(800, 600);
        workPageFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        workPageFrame.setLocationRelativeTo(null);

        JPanel dataPanel = new JPanel();
        workPageFrame.add(dataPanel,BorderLayout.CENTER);


        scrollPane = new JScrollPane(jTable);
        dataPanel.add(scrollPane);


        JPanel actionPanel = new JPanel();
        workPageFrame.add(actionPanel, BorderLayout.EAST);


        JPanel searchActionPanel = new JPanel(new GridLayout(2,3,10,10));
        actionPanel.add(searchActionPanel);


        JLabel searchLabel = new JLabel("Поиск по: ");
        JComboBox<String> selectColumn = new JComboBox<>(new Vector<>(connect.getColumnName()));
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Найти");
        JButton cancelSearchButton = new JButton("Назад");
        JLabel searchCount = new JLabel();
        cancelSearchButton.setEnabled(false);


        searchActionPanel.add(searchLabel);
        searchActionPanel.add(selectColumn);
        searchActionPanel.add(searchField);
        searchActionPanel.add(searchCount);
        searchActionPanel.add(searchButton);
        searchActionPanel.add(cancelSearchButton);




        workPageFrame.setVisible(true);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connect.searchOnData(connect,searchField.getText(),selectColumn.getItemAt(selectColumn.getSelectedIndex()));
                    scrollPaneSearch = new JScrollPane(connect.getSearchTable());
                    dataPanel.remove(scrollPane);
                    dataPanel.add(scrollPaneSearch);
                    searchCount.setText("Кол-во "+ connect.getCountRow());
                    workPageFrame.setVisible(false);
                    workPageFrame.setVisible(true);
                    searchButton.setEnabled(false);
                    cancelSearchButton.setEnabled(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                setSearchText(searchField.getText());
            }
        });

        cancelSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scrollPane = new JScrollPane(jTable);
                dataPanel.remove(scrollPaneSearch);
                dataPanel.add(scrollPane);
                workPageFrame.setVisible(false);
                workPageFrame.setVisible(true);
                searchField.setText("");
                connect.setCountRow(0);
                searchCount.setText("       ");
                searchButton.setEnabled(true);
                cancelSearchButton.setEnabled(false);
            }
        });





    }
    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
    public Vector getData() {
        return data;
    }

    public void setData(Vector data) {
        this.data = data;
    }

    public Vector getColumnName() {
        return columnName;
    }

    public void setColumnName(Vector columnName) {
        this.columnName = columnName;
    }

    public ConnectObj getConnect() {
        return connect;
    }

    public void setConnect(ConnectObj connect) {
        this.connect = connect;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public JTable getjTable() {
        return jTable;
    }

    public void setjTable(JTable jTable) {
        this.jTable = jTable;
    }
}
