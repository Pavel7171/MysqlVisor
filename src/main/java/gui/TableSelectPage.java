package gui;

import logic.Connect;
import logic.Table;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @Author Pavel Yurov
 * 26.07.2023
 */
public class TableSelectPage {

    private JPanel mainPanelTableSelectPage, buttonPanel, textPanel;
    private JLabel textForTablePanel;
    private JButton buttonSelect;
    private JList<String> tableList;
    private JScrollPane scrollerForListOfTable;
    private final String textOnFooter = "Выберите таблицу и нажмите \"Показать данные\" ";
    private final String textOnButtonSelect = "Показать данные";
    private List<String> tableLi;
    Connect connect;
    public TableSelectPage(Connect connect, List<String> tableLi){
        this.tableLi=tableLi;
        this.connect=connect;
    }
     public  void showTableSelect() {
         JFrame tableSelectFrame = new JFrame();
        //Устанавливаем размер,выход по Х, позицию в центре при старте
         tableSelectFrame.setSize(350, 400);
         tableSelectFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
         tableSelectFrame.setLocationRelativeTo(null);
        //Добавляем компоненты
        textForTablePanel = new JLabel(textOnFooter); // - Текст на футере
        buttonSelect = new JButton(textOnButtonSelect);// - Текст в кнопке
        tableList = new JList<>(tableLi.toArray(new String[tableLi.size()]));// - Добавляем значения в List
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
        buttonSelect.setEnabled(false);
        //Добавляем панель на фрейм
         tableSelectFrame.add(mainPanelTableSelectPage);
         tableSelectFrame.setVisible(true);
        //Действие при выборе данных на листе (пока ничего не выбрано кнопка "Выбрать" недоступна)
        tableList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                buttonSelect.setEnabled(true);
            }
        });
        //Действие при нажатии на кнопку "Выбрать" -> Переход на новый фрейм
        buttonSelect.addActionListener(new ActionListener() { //при нажатии на выбрать делаем видимым 2 панель
        @Override
        public void actionPerformed(ActionEvent e) {

            tableSelectFrame.setVisible(false);
        }
    });
}
}
