package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.dbHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryTransactionDAO;

public class PersistentDemoExpenseManager extends ExpenseManager {
    Context context;
    public PersistentDemoExpenseManager(Context context){
        this.context = context;
        setup();
    }
    @Override
    public void setup()  {
        AccountDAO persistentMemoryAccountDAO = new PersistentMemoryAccountDAO(context);
        setAccountsDAO(persistentMemoryAccountDAO);

        TransactionDAO persistentMemoryTransactionDAO = new PersistentMemoryTransactionDAO(context);
        setTransactionsDAO(persistentMemoryTransactionDAO);


    }
}
