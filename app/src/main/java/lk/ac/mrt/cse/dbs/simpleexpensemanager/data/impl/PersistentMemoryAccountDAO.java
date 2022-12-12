package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.dbHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentMemoryAccountDAO implements AccountDAO {
    Context context;
    dbHandler database;

    public PersistentMemoryAccountDAO(Context context){
        this.context = context;
        database = new dbHandler(context);

    }
    @Override
    public List<String> getAccountNumbersList() {
        return database.getAccNumberList();
    }

    @Override
    public List<Account> getAccountsList() {

        return database.getAllAccountData();

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        boolean result = database.accExists(accountNo);
        if (result){
            return database.getAcc(accountNo);
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        database.insertAccData(account);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        boolean result = database.accExists(accountNo);
        if (result){
            database.removeAcc(accountNo);
        }
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        boolean result = database.accExists(accountNo);
        if (result){
            Account account = getAccount(accountNo);
            switch (expenseType) {
                case EXPENSE:
                    account.setBalance(account.getBalance()-amount);
                    database.updateBalanceInfo(account);
                    //System.out.println("balance is"+(account.getBalance()-amount));
                    break;
                case INCOME:
                    account.setBalance(account.getBalance() + amount);
                    database.updateBalanceInfo(account);
                    break;
            }
        }else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);

        }
    }
}
