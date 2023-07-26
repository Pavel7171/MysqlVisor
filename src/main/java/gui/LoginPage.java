package gui;


import logic.Connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author Pavel Yurov
 * 25.07.2023
 */
public class LoginPage extends JFrame {
    JPanel mainPanel,buttonPanel,roofPanel;
    JButton okButton;
    JLabel url,user,pass,logo,msg;
    JTextField urlInput,userInput;
    JPasswordField userPass;

    LoginPage(){
        //add component
        url = new JLabel("URL");
        user = new JLabel("User");
        pass = new JLabel("Password");
        logo = new JLabel("MySQL Visor");
        msg = new JLabel();
        urlInput = new JTextField();
        userInput = new JTextField();
        userPass = new JPasswordField();
        okButton = new JButton("Connect");

        //add panel on frame
        mainPanel = new JPanel(new GridLayout(4,1));
        buttonPanel = new JPanel();
        roofPanel = new JPanel();
        //add component on panels
        mainPanel.add(url);
        mainPanel.add(urlInput);
        mainPanel.add(user);
        mainPanel.add(userInput);
        mainPanel.add(pass);
        mainPanel.add(userPass);
        mainPanel.add(msg);
        buttonPanel.add(okButton);
        roofPanel.add(logo);

        //add panels on frame
        add(mainPanel,BorderLayout.CENTER);
        add(buttonPanel,BorderLayout.SOUTH);
        add(roofPanel,BorderLayout.NORTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urlValue = urlInput.getText();
                String userValue = userInput.getText();
                String passwordValue = String.valueOf(userPass.getPassword());
                Connect connect = new Connect(urlValue, userValue, passwordValue);
                connect.tryConnect();
                if(connect.getMsg().equals("Ошибка подключения к серверу")){
                    msg.setText(connect.getMsg());
                    msg.setForeground(Color.red);
                }else {
                    setVisible(false);
                    BaseSelectPage baseSelectPage = new BaseSelectPage();
                    baseSelectPage.setVisible(true);
                }
            }
        });
    }

    public static void main(String[] args) {
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
        loginPage.setSize(500,200);

    }

}
