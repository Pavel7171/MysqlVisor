package gui;

import logic.MysqlActionController;
import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @Author Pavel Yurov
 * 25.07.2023
 */
public class LoginPage {
    public void showLoginGui(){
        JFrame loginPageFrame = new JFrame("MySQL Visor");
        loginPageFrame.setSize(500,200);
        JLabel textUrl = new JLabel("URL");
        JLabel textUser = new JLabel("User");
        JLabel textPassword = new JLabel("Password");
        JLabel textLogo = new JLabel("Authorization on MySQL server");
        JLabel errorMessage = new JLabel();
        JTextField urlInputField = new JTextField();
        JTextField userInputField = new JTextField();
        JPasswordField passwordInputField = new JPasswordField();
        JButton connectButton = new JButton("Connect");
        JPanel userInputPanel = new JPanel(new GridLayout(4, 1));
        JPanel buttonPanel = new JPanel();
        JPanel textLogoPanel = new JPanel();
        userInputPanel.add(textUrl);
        userInputPanel.add(urlInputField);
        userInputPanel.add(textUser);
        userInputPanel.add(userInputField);
        userInputPanel.add(textPassword);
        userInputPanel.add(passwordInputField);
        userInputPanel.add(errorMessage);
        buttonPanel.add(connectButton);
        textLogoPanel.add(textLogo);
        loginPageFrame.add(userInputPanel,BorderLayout.CENTER);
        loginPageFrame.add(buttonPanel,BorderLayout.SOUTH);
        loginPageFrame.add(textLogoPanel,BorderLayout.NORTH);
        loginPageFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginPageFrame.setLocationRelativeTo(null);
        loginPageFrame.setResizable(false);
        loginPageFrame.setVisible(true);

        connectButton.addActionListener(e -> {
            MysqlActionController mysqlActionController = new MysqlActionController(urlInputField.getText(), userInputField.getText(), String.valueOf(passwordInputField.getPassword()));
            mysqlActionController.tryServerConnect();
            if(mysqlActionController.getCheckQueryResultMessage().equals("Error")){
                JOptionPane.showMessageDialog(loginPageFrame,"Invalid connect from "+mysqlActionController.getUrl()+" , try again ");
                errorMessage.setText("Error to connect... "+mysqlActionController.getUrl());
                errorMessage.setForeground(Color.red);
            }else {
                loginPageFrame.dispose();
                SelectBasePage selectBasePage = new SelectBasePage();
                selectBasePage.showBaseSelectGui(mysqlActionController,mysqlActionController.showBases(mysqlActionController));
                mysqlActionController.showBases(mysqlActionController);
            }
        });
    }
}