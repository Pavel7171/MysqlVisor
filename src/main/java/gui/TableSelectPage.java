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
public class TableSelectPage extends JFrame {

    private JPanel mainPanelTableSelectPage, buttonPanel, textPanel;
    private JLabel textForTablePanel;
    private JButton buttonSelect;
    private JList<String> tableList;
    private JScrollPane scrollerForListOfTable;
    private final String textOnFooter = "Выберите таблицу и нажмите \"Показать данные\" ";
    private final String textOnButtonSelect = "Показать данные";


    TableSelectPage() {
        //Устанавливаем размер,выход по Х, позицию в центре при старте
        setSize(350, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //Добавляем компоненты
        textForTablePanel = new JLabel(textOnFooter); // - Текст на футере
        buttonSelect = new JButton(textOnButtonSelect);// - Текст в кнопке
        tableList = new JList<>(getTableName());// - Добавляем значения в List
        scrollerForListOfTable = new JScrollPane();// - Панель прокрутки
        scrollerForListOfTable.setViewportView(tableList);
        tableList.setLayoutOrientation(JList.VERTICAL);
        //Создаем панель
        mainPanelTableSelectPage = new JPanel(); // - Общая панель для фрейма
        mainPanelTableSelectPage.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel = new JPanel();// - панель для кнопки
        buttonPanel.add(buttonSelect);
        textPanel = new JPanel();// - панель для текста
        textPanel.add(textForTablePanel);
        //Добавляем компоненты на панель
        mainPanelTableSelectPage.add(textPanel);
        mainPanelTableSelectPage.add(scrollerForListOfTable);
        mainPanelTableSelectPage.add(buttonPanel);
        buttonSelect.setVisible(false);
        //Добавляем панель на фрейм
        add(mainPanelTableSelectPage);
        //Действие при выборе данных на листе (пока ничего не выбрано кнопка "Выбрать" недоступна)
        tableList.addListSelectionListener(new ListSelectionListener() {
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

    public String[] getTableName() {
        String[] table = new String[]{"sfasfasdfsfs", "dfsdgsdgsdgs", "dfsdgsqwdddddddddd", "1d1d3d"
                , "dfsdgsdgsdgs", "dfsdgsqwdddddddddd", "1d1d3d"
                , "dfsdgsdgsdgs", "dfsdgsqwdddddddddd", "1d1d3d"
                , "dfsdgsdgsdgs", "dfsdgsqwdddddddddd", "1d1d3d"
                , "dfsdgsdgsdgs", "dfsdgsqwdddddddddd", "1d1d3d"
                , "dfsdgsdgsdgs", "dfsdgsqwdddddddddd", "1d1d3d"};
        return table;
    }
}
