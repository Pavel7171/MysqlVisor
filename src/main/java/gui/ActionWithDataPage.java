package gui;

import logic.MysqlActionController;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
/**
 * @Author Pavel Yurov
 * 27.07.2023
 */
public class ActionWithDataPage {
    public void showWorkPage(MysqlActionController mysqlActionController, JTable jTable) {
        JFrame actionWithDataFrame = new JFrame("MySQL Visor: "
                +mysqlActionController.getSelectBaseName()
                +" -> "+mysqlActionController.getSelectTableName());
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
                showWorkPage(mysqlActionController, mysqlActionController.showDataFromTable(mysqlActionController));
                actionWithDataFrame.dispose();
        });

        downloadFileButton.addActionListener(e -> {
            int count = mysqlActionController.getTableColumnName().size()-1;
            JOptionPane.showMessageDialog(actionWithDataFrame,"The number of comma separated values must be equal to "+ count);
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter fileFormat = new FileNameExtensionFilter("CSV (Comma-Separated Values)", "csv");
            jFileChooser.setFileFilter(fileFormat);
            int approveOptionForChooser = jFileChooser.showDialog(null, "Открыть файл");
            if (approveOptionForChooser == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                if(mysqlActionController.downloadDataFromTable(mysqlActionController, file.getAbsolutePath()).equals("File uploaded successfully")){
                    jFileChooser.setVisible(false);
                    showWorkPage(mysqlActionController, mysqlActionController.showDataFromTable(mysqlActionController));
                    actionWithDataFrame.dispose();
                }else {
                    JOptionPane.showMessageDialog(actionWithDataFrame,mysqlActionController.downloadDataFromTable(mysqlActionController, file.getAbsolutePath()));
                }
            }
        });

        backToTablePageButton.addActionListener(e -> {
            SelectTablePage selectTablePage = new SelectTablePage();
            selectTablePage.showTableSelect(mysqlActionController, mysqlActionController.getTableList());
            actionWithDataFrame.dispose();
        });

        inputSearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = inputSearchField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    if(text.equals("+")||text.equals("-")||text.equals(".")){
                        text = "["+text+"]";
                    }
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
                    System.out.println();
                }
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = inputSearchField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    if(text.equals("+")||text.equals("-")||text.equals(".")){
                        text = "["+text+"]";
                    }
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?iu)" + text));
                }
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        });
        jTable.getModel().addTableModelListener(e -> {
            Object valueChanged = jTable.getCellEditor().getCellEditorValue();
            mysqlActionController.changeDataInTable(mysqlActionController, valueChanged, jTable.getSelectedColumn(), jTable.getValueAt(jTable.getSelectedRow(), 0));
        });
    }

}