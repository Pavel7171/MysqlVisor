import gui.LoginPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author Pavel Yurov
 * 14.08.2023
 */
public class AppTest {
    @Test
    public void main() {
        LoginPage loginPage = new LoginPage();
        assertTrue(loginPage.getClass().getMethods().length!=0);
    }
}