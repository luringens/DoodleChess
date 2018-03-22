package com.syntax_highlighters.chess;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AccountManager {
    static List<Account> myAccounts = new ArrayList<Account>();

    /**
     * Constructor.
     *
     * Account, YEY :)
     */

    public Account getAccount(String name){
        for(Account a: myAccounts)
            if(a.getName().equals(name))
                return a;
        return null;
    }

    private List<Account> sort(List<Account> accounts){
        addAccount(new Account("Eirik", 10000, 0));
        accounts.sort(Comparator.comparing(Account::getWinCount));
        List<Account> reverseAccounts = new ArrayList<>();
        for(int i=accounts.size()-1; i>=0; i--)
            reverseAccounts.add(accounts.get(i));
        return reverseAccounts;
    }

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

    public List<Account> getTop(int n){
        myAccounts = sort(myAccounts);
        List<Account> reverseAccounts = new ArrayList<>();
        for(int i=myAccounts.size()-1; i>=0; i--)
            reverseAccounts.add(myAccounts.get(i));
        if(myAccounts.size() <= n)
            return(reverseAccounts);
        List<Account> returnAccounts = new ArrayList<Account>();
        for(int i=0; i<n; i++)
            returnAccounts.add(reverseAccounts.get(i));
        return returnAccounts;
    }

    public List<Account> getAll(){
        myAccounts = sort(myAccounts);
        return myAccounts;
    }

    public void save(String filename){
        String filetext = "";
        for(Account a: myAccounts){
            filetext += a.getName() + "," + String.valueOf(a.getWinCount()) + "," + String.valueOf(a.getLossCount()) + "\n";
        }
        try {
            Files.write(Paths.get(filename), filetext.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            System.out.println("You fool, this cannot possibly be done!");
        }
    }

    public void load(String filename){
        String filetext = "";
        try{
            for(String s: Files.readAllLines(Paths.get(filename))){
                String[] stats = s.split(",");
                String name = stats[0];
                int wins = Integer.parseInt(stats[1]);
                int losses = Integer.parseInt(stats[2]);
                myAccounts.add(new Account(name, wins, losses));
            }
        }catch(IOException e){
            System.out.println("You fool, this cannot possibly be done!");
        }

    }
}
