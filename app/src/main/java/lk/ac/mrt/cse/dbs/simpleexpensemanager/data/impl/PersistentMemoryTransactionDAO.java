package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.dbHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentMemoryTransactionDAO implements TransactionDAO {
    Context context;
    dbHandler database;

    public PersistentMemoryTransactionDAO(Context context){
        this.context = context;
        database = new dbHandler(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
            Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
            database.insertTransactionData(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        return database.getTransaction();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        List<Transaction> transactionList = database.getTransaction();
        int size = transactionList.size();
        if (size <= limit) {
            return transactionList;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactionList.subList(size - limit, size);


    }
}
