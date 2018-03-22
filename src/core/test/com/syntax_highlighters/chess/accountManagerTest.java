package syntax_highlighters.chess;
import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.AccountManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class accountManagerTest {

    @Test
    public void saveLoadTest(){
        AccountManager am = new AccountManager();


        for(int i =1; i<5; i++){
            am.addAccount(new Account("a"+i));
        }

        am.save("/Users/robinelsalim/desktop/accounts.txt");
        AccountManager am2 = new AccountManager();

        am2.load("/Users/robinelsalim/desktop/accounts.txt");

        assertEquals(4, am2.accountSize());




    }

}
