package logic;

import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author Pavel Yurov
 * 14.08.2023
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MysqlActionControllerTest {
    static MysqlActionController mysqlActionController;

    @BeforeAll
    public void prepare(){
        mysqlActionController = new MysqlActionController("","","");
    }

    @Test
    public void tryServerConnectInputData() {
        mysqlActionController.tryServerConnect();
        assertNotNull(mysqlActionController.getConnectStatement());
        assertEquals("null",mysqlActionController.getCheckQueryResultMessage());
        System.out.println("Test 1 tryConnectToServer passed "+ mysqlActionController);
    }
    @Test
    public void showBasesFromDB(){
        mysqlActionController.showBases(mysqlActionController);
        assertNotNull(mysqlActionController.getBaseList());
        System.out.println("test 2 getBaseList passed "+mysqlActionController);
    }

    @AfterAll
    public void closeConnection() throws SQLException {
        mysqlActionController.getConnectStatement().close();
        assertTrue(mysqlActionController.getConnectStatement().isClosed());
    }

}