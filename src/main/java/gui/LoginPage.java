package gui;


import logic.Base;
import logic.Connect;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @Author Pavel Yurov
 * 25.07.2023
 */
public class LoginPage {
    JFrame loginFrame;
    JPanel mainPanel,buttonPanel,roofPanel;
    JButton okButton;
    JLabel url,user,pass,logo,msg;
    JTextField urlInput,userInput;
    JPasswordField userPass;

    public void showLoginGui(){
        // Задаем размеры фрейма
        loginFrame = new JFrame("Gui");
        loginFrame.setSize(500,200);
        // Добавляем компоненты
        url = new JLabel("URL");
        user = new JLabel("User");
        pass = new JLabel("Password");
        logo = new JLabel("MySQL Visor");
        msg = new JLabel();
        urlInput = new JTextField();
        userInput = new JTextField();
        userPass = new JPasswordField();
        okButton = new JButton("Connect");
        // Создаем панели
        mainPanel = new JPanel(new GridLayout(4,1));
        buttonPanel = new JPanel();
        roofPanel = new JPanel();
        // Добавляем компоненты на панель
        mainPanel.add(url);
        mainPanel.add(urlInput);
        mainPanel.add(user);
        mainPanel.add(userInput);
        mainPanel.add(pass);
        mainPanel.add(userPass);
        mainPanel.add(msg);
        buttonPanel.add(okButton);
        roofPanel.add(logo);
        // Добавляем панели на фрейм
        loginFrame.add(mainPanel,BorderLayout.CENTER);
        loginFrame.add(buttonPanel,BorderLayout.SOUTH);
        loginFrame.add(roofPanel,BorderLayout.NORTH);
        loginFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);
        loginFrame.setVisible(true);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Connect connectData = new Connect(urlInput.getText(), userInput.getText(), String.valueOf(userPass.getPassword()));
                connectData.tryConnect();
                if(connectData.getMsg().equals("Ошибка подключения к серверу")){
                    msg.setText(connectData.getMsg());
                    msg.setForeground(Color.red);
                }else {
                    loginFrame.setVisible(false);
                    Base baseSelect = new Base(null,connectData);
                    try {
                        baseSelect.showBases(connectData);
                        BaseSelectPage baseSelectPage = new BaseSelectPage(connectData,baseSelect.getListOfBase());
                        baseSelectPage.showBaseSelectGui(baseSelect.getListOfBase());
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
}
