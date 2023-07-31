package gui;

import logic.ConnectObj;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @Author Pavel Yurov
 * 26.07.2023
 */
public class BaseSelectPage {
    private JPanel mainPanelBaseSelectPage,buttonPanel,textPanel;
    private JLabel textForBasePanel;
    private JButton buttonSelect;
    private JList<String> baseList;
    private JScrollPane scrollerForListOfBase;
    private final String textOnFooter = "Выберите базу для отображения таблиц ";
    private final String textOnButtonSelect = "Выбрать";
    ConnectObj connectData;
    private String baseName;
    private List<String> baseLi;
    public BaseSelectPage(ConnectObj connectObj, List<String> baseLi){
        this.connectData=connectObj;
        this.baseLi = baseLi;
    }
    public void showBaseSelectGui(List<String> list){
        JFrame baseSelectFrame = new JFrame();
        //Устанавливаем размер,выход по Х, позицию в центре при старте
        baseSelectFrame.setSize(350,400);
        baseSelectFrame.setVisible(true);
        baseSelectFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        baseSelectFrame.setLocationRelativeTo(null);
        //Добавляем компоненты
        textForBasePanel = new JLabel(textOnFooter); // - Текст на футере
        buttonSelect = new JButton(textOnButtonSelect);// - Текст в кнопке
        baseList = new JList<>(list.toArray(new String[list.size()]));// - Добавляем значения в List
        scrollerForListOfBase = new JScrollPane();// - Панель прокрутки
        scrollerForListOfBase.setViewportView(baseList);
        baseList.setLayoutOrientation(JList.VERTICAL);
        //Создаем панель
        mainPanelBaseSelectPage = new JPanel(); // - Общая панель для фрейма
        mainPanelBaseSelectPage.setLayout(new GridLayout(3,1,10,10));
        buttonPanel = new JPanel();// - панель для кнопки
        buttonPanel.add(buttonSelect);
        textPanel = new JPanel();// - панель для текста
        textPanel.add(textForBasePanel);
        //Добавляем компоненты на панель
        mainPanelBaseSelectPage.add(textPanel);
        mainPanelBaseSelectPage.add(scrollerForListOfBase);
        mainPanelBaseSelectPage.add(buttonPanel);
        buttonSelect.setEnabled(false);
        //Добавляем панель на фрейм
        baseSelectFrame.add(mainPanelBaseSelectPage);
        //Действие при выборе данных на листе (пока ничего не выбрано кнопка "Выбрать" недоступна)
        baseList.addListSelectionListener(new ListSelectionListener() { //кнопка выбрать только после переключения значения
            @Override
            public void valueChanged(ListSelectionEvent e) {
                buttonSelect.setEnabled(true);
            }
        });
        //Действие при нажатии на кнопку "Выбрать" -> Переход на новый фрейм
        buttonSelect.addActionListener(new ActionListener() { //при нажатии на выбрать делаем видимым 2 панель
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connectData.showTables(connectData,baseList.getSelectedValue());
                    connectData.setBaseName(baseList.getSelectedValue());
                    TableSelectPage tableSelectPage = new TableSelectPage(connectData,connectData.getListOfTable(),baseList.getSelectedValue());
                    tableSelectPage.showTableSelect();
                    baseSelectFrame.setVisible(false);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
    }
}
