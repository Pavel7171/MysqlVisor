package gui;

import logic.MysqlActionController;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
/**
 * @Author Pavel Yurov
 * 27.07.2023
 */
public class ActionWithDataPage {
    public void showWorkPage(MysqlActionController mysqlActionController, JTable jTable) {
        JFrame actionWithDataFrame = new JFrame("MySQL Visor: "
                +mysqlActionController.getBaseName()
                +" -> "+mysqlActionController.getTableName());
        actionWithDataFrame.setSize(800, 600);
        actionWithDataFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        actionWithDataFrame.setLocationRelativeTo(null);
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getPreferredSize();
        JPanel dataPanel = new JPanel();
        dataPanel.add(scrollPane);
        actionWithDataFrame.add(dataPanel, BorderLayout.CENTER);
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(jTable.getModel());
        jTable.setRowSorter(rowSorter);
        JPanel roofPanel = new JPanel();
        actionWithDataFrame.add(roofPanel, BorderLayout.NORTH);
        JPanel searchActionPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        roofPanel.add(searchActionPanel);
        JPanel buttonPanel = new JPanel();
        actionWithDataFrame.add(buttonPanel, BorderLayout.SOUTH);
        JPanel buttonActionPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(buttonActionPanel);
        JLabel searchTextLabel = new JLabel("Search data : ");
        JTextField inputSearchField = new JTextField();
        JButton downloadFileButton = new JButton("Download from .csv");
        buttonActionPanel.add(downloadFileButton);
        JButton updateFrameButton = new JButton("Reload data");
        buttonActionPanel.add(updateFrameButton);
        JButton deleteRowButton = new JButton("Delete row");
        buttonActionPanel.add(deleteRowButton);
        JButton backToTablePageButton = new JButton("Back to select table");
        buttonActionPanel.add(backToTablePageButton);
        searchActionPanel.add(searchTextLabel);
        searchActionPanel.add(inputSearchField);
        actionWithDataFrame.setVisible(true);

        deleteRowButton.addActionListener(e -> {
            try{
                mysqlActionController.deleteData(mysqlActionController, jTable.getValueAt(jTable.getSelectedRow(), 0));
                JOptionPane.showMessageDialog(actionWithDataFrame,"Rows with ID = "+jTable.getValueAt(jTable.getSelectedRow(),0)+" delete");
                showWorkPage(mysqlActionController, mysqlActionController.showDataFromTable(mysqlActionController));
                actionWithDataFrame.dispose();
            }catch (Exception ex){
                JOptionPane.showMessageDialog(actionWithDataFrame,ex.getMessage());
            }
        });

        updateFrameButton.addActionListener(e -> {
            try {
                showWorkPage(mysqlActionController, mysqlActionController.showDataFromTable(mysqlActionController));
                actionWithDataFrame.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(actionWithDataFrame,ex.getMessage());
            }
        });

        downloadFileButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (Comma-Separated Values)", "csv");
            jFileChooser.setFileFilter(filter);
            int ret = jFileChooser.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                mysqlActionController.downloadData(mysqlActionController, file.getAbsolutePath());
                jFileChooser.setVisible(false);
                try {
                    showWorkPage(mysqlActionController, mysqlActionController.showDataFromTable(mysqlActionController));
                    actionWithDataFrame.dispose();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        backToTablePageButton.addActionListener(e -> {
            SelectTablePage selectTablePage = new SelectTablePage();
            selectTablePage.showTableSelect(mysqlActionController, mysqlActionController.getListOfTable(), null);
            actionWithDataFrame.dispose();
        });

        inputSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = inputSearchField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = inputSearchField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
        jTable.getModel().addTableModelListener(e -> {
            Object a = jTable.getCellEditor().getCellEditorValue(); //получение значения на что изменили
            System.out.println(a.toString());
            System.out.println(jTable.getSelectedColumn()); // получение индекса изменяемой колонки
            System.out.println(jTable.getSelectedRow()); // получение индекса изменяемой строки
            System.out.println(jTable.getValueAt(jTable.getSelectedRow(), 0)); //получение ID для запроса
            try {
                mysqlActionController.updateData(mysqlActionController, a, jTable.getSelectedColumn(), jTable.getValueAt(jTable.getSelectedRow(), 0));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}