package com.syntax_highlighters.chess;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Class containing a list of accounts, and the ability to perform operations on
 * them, such as fetching a given account or return a sorted list of accounts.
 */
public class AccountManager {
    List<Account> myAccounts = new ArrayList<Account>();
    public int accountSize(){
        return myAccounts.size();
    }

    /**
     * Get the account with the given name.
     *
     * @param name The account name
     *
     * @return The account whose name equals the parameter, or null if no such
     * account exists
     */

    public Account getAccount(String name){
        for(Account a: myAccounts)
            if(a.getName().equals(name))
                return a;
        return null;
    }

    /**
     * Helper method: return a list of accounts sorted by their win counts.
     *
     * SUGGESTION: Make use of Comparator object which from the beginning
     * sorts list in reversed order.
     *
     * @param accounts The list of accounts to sort
     *
     * @return a properly sorted list of accounts
     */
    private List<Account> sort(List<Account> accounts){
        accounts.sort(Comparator.comparing(Account::getWinCount));
        List<Account> reverseAccounts = new ArrayList<>();
        for(int i=accounts.size()-1; i>=0; i--)
            reverseAccounts.add(accounts.get(i));
        return reverseAccounts;
    }

    /**
     * Add a new account if it does not already exist.
     *
     * @param acc The account to add
     * @return true if the account was added, false if it already existed in the
     * account list
     */
    public boolean addAccount(Account acc){
        boolean canAdd = true;
        if(myAccounts.isEmpty())
            canAdd = true;
        else{
            for(Account a: myAccounts)
                if(a.getName().equals(acc.getName()))
                    canAdd = false;
        }
        if(canAdd)
            myAccounts.add(acc);
        return canAdd;
    }

    /**
     * Return the top n accounts ordered by win count.
     *
     * @param n Number of accounts to return
     * @return A correctly ordered list of n accounts
     */
    public List<Account> getTop(int n){
        myAccounts = sort(myAccounts);
        if(myAccounts.size() <= n)
            return(myAccounts);
        List<Account> returnAccounts = new ArrayList<Account>();
        for(int i=0; i<n; i++)
            returnAccounts.add(myAccounts.get(i));
        return returnAccounts;
    }

    /**
     * Return all the accounts ordered by win count.
     *
     * @return A correctly ordered list containing all the accounts
     */
    public List<Account> getAll(){
        myAccounts = sort(myAccounts);
        return myAccounts;
    }

    /**
     * Save the account list to a file with the given filename.
     *
     * Overwrites the file in question, or creates it if it doesn't already
     * exist.
     *
     * SUGGESTION: Return boolean value indicating whether or not saving the
     * file succeeded.
     *
     * @param filename The name of the file to save to
     */
    public void save(String filename){
        String filetext = "";
        for(Account a: myAccounts){
            filetext += a.getName() + "," + String.valueOf(a.getWinCount()) + "," + String.valueOf(a.getLossCount()) + "\n";
        }
        try {
            if(Files.exists(Paths.get(filename)))
                Files.write(Paths.get(filename), filetext.getBytes(), StandardOpenOption.WRITE);
            else {
                Files.createFile(Paths.get(filename));
                Files.write(Paths.get(filename), filetext.getBytes(), StandardOpenOption.WRITE);
            }

        }catch (IOException e) {
            System.out.println("You fool, ");
            System.out.println(e);
        }
    }

    /**
     * Load the accounts from a file with the given filename into this
     * AccountManager.
     *
     * SUGGESTION: Return boolean value indicating whether or not loading the
     * file succeeded.
     *
     * @param filename The name of the file to load from
     */
    public void load(String filename){
        try{
            for(String s: Files.readAllLines(Paths.get(filename))){
                String[] stats = s.split(",");
                String name = stats[0];
                int wins = Integer.parseInt(stats[1]);
                int losses = Integer.parseInt(stats[2]);
                myAccounts.add(new Account(name, wins, losses));
            }
        }catch(IOException e){
            // Skip if file doesn't exist
        }
        

    }
}
