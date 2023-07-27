import gui.BaseSelectPage;
import gui.LoginPage;
import logic.Connect;


import java.sql.SQLException;

/**
 * @Author Pavel Yurov
 * 25.07.2023
 */
public class App {
    public static void main(String[] args) {
        LoginPage loginPage = new LoginPage();
        loginPage.showLoginGui();
    }
}
