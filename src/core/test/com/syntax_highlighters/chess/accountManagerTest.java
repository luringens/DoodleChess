package syntax_highlighters.chess;

import com.syntax_highlighters.chess.Account;
import com.syntax_highlighters.chess.AccountManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class accountManagerTest {

    @Test
    public void saveLoadTest(){
        List<Account> myAccounts = new ArrayList<Account>();
        AccountManager am = new AccountManager();

        Account a1 = new Account("a1");
        Account a2 = new Account("a2");
        Account a3 = new Account("a3");
        Account a4 = new Account("a4");

        am.addAccount(a1);
        am.addAccount(a2);
        am.addAccount(a3);
        am.addAccount(a4);

        am.save("/Users/robinelsalim/desktop/accounts.txt");
        AccountManager am2 = new AccountManager();

        am2.load("/Users/robinelsalim/desktop/accounts.txt");

        assertEquals(4, am2.accountSize());




    }

}
