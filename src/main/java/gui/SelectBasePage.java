package gui;

import logic.MysqlActionController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @Author Pavel Yurov
 * 26.07.2023
 */
public class SelectBasePage {
    public void showBaseSelectGui(MysqlActionController mysqlActionController, List <String> list){
        JFrame selectBaseFrame = new JFrame("Database from: "+mysqlActionController.getUrl());
        selectBaseFrame.setSize(350,400);
        selectBaseFrame.setVisible(true);
        selectBaseFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        selectBaseFrame.setLocationRelativeTo(null);
        JLabel textLogo = new JLabel("Select database for show tables ");
        JButton selectButton = new JButton("Select");
        JButton backToLoginPage = new JButton("Back to login form");
        JList <String> baseList = new JList<>(list.toArray(new String[0]));
        JScrollPane scrollerForListOfBase = new JScrollPane();
        scrollerForListOfBase.setViewportView(baseList);
        baseList.setLayoutOrientation(JList.VERTICAL);
        JPanel baseSelectPanel = new JPanel();
        baseSelectPanel.setLayout(new GridLayout(3,1,10,10));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        buttonPanel.add(backToLoginPage);
        JPanel textLogoPanel = new JPanel();
        textLogoPanel.add(textLogo);
        baseSelectPanel.add(textLogoPanel);
        baseSelectPanel.add(scrollerForListOfBase);
        baseSelectPanel.add(buttonPanel);
        selectButton.setEnabled(false);
        selectBaseFrame.add(baseSelectPanel);

        baseList.addListSelectionListener(e -> selectButton.setEnabled(true));

        selectButton.addActionListener(e -> {
            mysqlActionController.setSelectBaseName(baseList.getSelectedValue());
            mysqlActionController.showTablesInSelectBase(mysqlActionController,baseList.getSelectedValue());
            SelectTablePage selectTablePage = new SelectTablePage();
            selectTablePage.showTableSelect(mysqlActionController,mysqlActionController.showTablesInSelectBase(mysqlActionController,mysqlActionController.getSelectBaseName()));
            selectBaseFrame.dispose();
        });
        backToLoginPage.addActionListener(e -> {
            selectBaseFrame.dispose();
            LoginPage loginPage = new LoginPage();
            loginPage.showLoginGui();
        });
    }
}