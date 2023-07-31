import gui.LoginPage;


import java.sql.SQLException;

/**
 * @Author Pavel Yurov
 * 25.07.2023
 */
public class App {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        LoginPage loginPage = new LoginPage();
        loginPage.showLoginGui();
    }
}
