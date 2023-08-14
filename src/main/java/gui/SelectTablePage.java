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
public class SelectTablePage {
     public void showTableSelect(MysqlActionController mysqlActionController, List<String> tableLi) {
         String baseName = mysqlActionController.getSelectBaseName();
         JFrame selectTableFrame = new JFrame("Select Table in "+ baseName);
         selectTableFrame.setSize(350, 400);
         selectTableFrame.setVisible(true);
         selectTableFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
         selectTableFrame.setLocationRelativeTo(null);
         JLabel textLogo = new JLabel("Select table and click \"Show data\" ");
         JButton selectButton = new JButton("Show data");
         JButton backToBaseSelectPage = new JButton("Back to select base");
         JList<String> tableList = new JList<>(tableLi.toArray(new String[tableLi.size()]));
         JScrollPane scrollerForListOfTable = new JScrollPane();
         scrollerForListOfTable.setViewportView(tableList);
         tableList.setLayoutOrientation(JList.VERTICAL);
         JPanel mainPanelTableSelectPage = new JPanel();
         mainPanelTableSelectPage.setLayout(new GridLayout(3, 1, 10, 10));
         JPanel buttonPanel = new JPanel();
         buttonPanel.add(selectButton);
         buttonPanel.add(backToBaseSelectPage);
         JPanel textLogoPanel = new JPanel();
         textLogoPanel.add(textLogo);
         mainPanelTableSelectPage.add(textLogoPanel);
         mainPanelTableSelectPage.add(scrollerForListOfTable);
         mainPanelTableSelectPage.add(buttonPanel);
         selectButton.setEnabled(false);
         selectTableFrame.add(mainPanelTableSelectPage);

         tableList.addListSelectionListener(e -> selectButton.setEnabled(true));

         selectButton.addActionListener(e -> {
            selectTableFrame.dispose();
            mysqlActionController.setSelectTableName(tableList.getSelectedValue());
             mysqlActionController.showDataFromTable(mysqlActionController);
             ActionWithDataPage actionWithDataPage = new ActionWithDataPage();
             actionWithDataPage.showWorkPage(mysqlActionController,mysqlActionController.showDataFromTable(mysqlActionController));
         });

         backToBaseSelectPage.addActionListener(e -> {
             selectTableFrame.dispose();
             SelectBasePage selectBasePage = new SelectBasePage();
             selectBasePage.showBaseSelectGui(mysqlActionController,mysqlActionController.getBaseList());
         });
     }

}
