package gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author Pavel Yurov
 * 26.07.2023
 */
public class BaseSelectPage extends JFrame {
    private JPanel mainPanelBaseSelectPage,buttonPanel,textPanel;
    private JLabel textForBasePanel;
    private JButton buttonSelect;
    private JList<String> baseList;
    private JScrollPane scrollerForListOfBase;
    private final String textOnFooter = "Выберите базу для отображения таблиц ";
    private final String textOnButtonSelect = "Выбрать";


    BaseSelectPage(){
        //Устанавливаем размер,выход по Х, позицию в центре при старте
        setSize(350,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //Добавляем компоненты
        textForBasePanel = new JLabel(textOnFooter); // - Текст на футере
        buttonSelect = new JButton(textOnButtonSelect);// - Текст в кнопке
        baseList = new JList<>(getBaseName());// - Добавляем значения в List
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
        buttonSelect.setVisible(false);
        //Добавляем панель на фрейм
        add(mainPanelBaseSelectPage);
        //Действие при выборе данных на листе (пока ничего не выбрано кнопка "Выбрать" недоступна)
        baseList.addListSelectionListener(new ListSelectionListener() { //кнопка выбрать только после переключения значения
            @Override
            public void valueChanged(ListSelectionEvent e) {
                buttonSelect.setVisible(true);
            }
        });
        //Действие при нажатии на кнопку "Выбрать" -> Переход на новый фрейм
        buttonSelect.addActionListener(new ActionListener() { //при нажатии на выбрать делаем видимым 2 панель
            @Override
            public void actionPerformed(ActionEvent e) {
                TableSelectPage tableSelectPage = new TableSelectPage();
                tableSelectPage.setVisible(true);
                setVisible(false);
            }
        });
    }




    public String[] getBaseName(){
        String [] base = new String[]{"sfasfasdfsfs","dfsdgsdgsdgs","dfsdgsqwdddddddddd","1d1d3d"
                ,"dfsdgsdgsdgs","dfsdgsqwdddddddddd","1d1d3d"
                ,"dfsdgsdgsdgs","dfsdgsqwdddddddddd","1d1d3d"
                ,"dfsdgsdgsdgs","dfsdgsqwdddddddddd","1d1d3d"
                ,"dfsdgsdgsdgs","dfsdgsqwdddddddddd","1d1d3d"
                ,"dfsdgsdgsdgs","dfsdgsqwdddddddddd","1d1d3d"
                ,"dfsdgsdgsdgs","dfsdgsqwdddddddddd","1d1d3d"
                ,"dfsdgsdgsdgs","dfsdgsqwdddddddddd","1d1d3d"};
        return base;
    }



}
