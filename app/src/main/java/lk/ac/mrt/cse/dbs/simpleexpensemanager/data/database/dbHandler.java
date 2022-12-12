package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class dbHandler extends SQLiteOpenHelper {
    private static final int VERSION = 3;
    private static final String DB_NAME = "200348C";

    //table names
    private static final String TABLE_ACCOUNT = "Accounts";
    private static final String TABLE_TRANSACTION = "Transation";

    //table - Account column names
    private static final String Acc_No = "Account_No";
    private static final String Bank = "Bank";
    private static final String Acc_Holder = "Account_Holder";
    private static final String init_bal = "Initial_Balance";

    //table - Transaction column names
    private static final String Id = "Id";
    private static final String Date = "Date";
    private static final String Type = "Type";
    private static final String Amount = "Amount";

    public dbHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    //This method is used to create new tables
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String TABLE_ACCOUNT_CREATE_QUERY = "CREATE TABLE "+TABLE_ACCOUNT+" "+
                "("
                    +Acc_No+" TEXT PRIMARY KEY,"
                    +Bank+" TEXT,"
                    +Acc_Holder+" TEXT,"
                    +init_bal+" REAL"+
                ");" ;


        String TABLE_TRANSACTION_CREATE_QUERY = "CREATE TABLE "+TABLE_TRANSACTION+" "+
                "("
                    +Id+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +Date+" TEXT,"
                    +Acc_No+" TEXT,"
                    +Type+" TEXT,"
                    +Amount+" REAL"+
                ");";
        sqLiteDatabase.execSQL(TABLE_ACCOUNT_CREATE_QUERY);
        sqLiteDatabase.execSQL(TABLE_TRANSACTION_CREATE_QUERY);

    }

    @Override
    //This method is used incase we have to delete the existing table and make a new table
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String DROP_ACCOUNT_TABLE_QUERY = "DROP TABLE IF EXISTS "+TABLE_ACCOUNT;
        //Drop older table if existed
        sqLiteDatabase.execSQL(DROP_ACCOUNT_TABLE_QUERY);

        String DROP_TRANSACTION_TABLE_QUERY = "DROP TABLE IF EXISTS "+TABLE_TRANSACTION;
        sqLiteDatabase.execSQL(DROP_TRANSACTION_TABLE_QUERY);

        onCreate(sqLiteDatabase);
    }

    public void insertAccData(Account account){
        try{
            SQLiteDatabase sqLiteDatabase = getWritableDatabase() ;

            ContentValues contentValues = new ContentValues();

            contentValues.put(Acc_No,account.getAccountNo());
            contentValues.put(Bank,account.getBankName());
            contentValues.put(Acc_Holder,account.getAccountHolderName());
            contentValues.put(init_bal,account.getBalance());

            if (accExists(Acc_No)){
                System.out.println("This sssss account number already exists");

            }else{
                //save the table
                sqLiteDatabase.insert(TABLE_ACCOUNT,null,contentValues);

                //close the table
                //sqLiteDatabase.close();
            }

        }catch (Exception e){
            System.out.println(e);

        }

    }

    public void insertTransactionData(Transaction transaction){

        try{
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            //format to date object
            String pattern = "dd/mm/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            Date transactionDate  = transaction.getDate();
            String dateToString = df.format(transactionDate);

            contentValues.put(Date,dateToString);

            contentValues.put(Acc_No,transaction.getAccountNo());

            contentValues.put(Type,transaction.getExpenseType().name());

            contentValues.put(Amount,transaction.getAmount());


            sqLiteDatabase.insert(TABLE_TRANSACTION,null,contentValues);
            //sqLiteDatabase.close();

        }catch (Exception e){
            System.out.println(e);
        }
    }

    public List<Account> getAllAccountData(){
        List<Account> accList = new ArrayList<>();
        try{
            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM "+TABLE_ACCOUNT;

            Cursor cursor = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do{
                    Account account = new Account();
                    account.setAccountNo(cursor.getString(0));
                    account.setBankName(cursor.getString(1));
                    account.setAccountHolderName(cursor.getString(2));
                    account.setBalance(cursor.getFloat(3));

                    accList.add(account);
                }while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            System.out.println(e);

        }

        return accList;

    }

    public List<Transaction> getTransaction(){

        List<Transaction> transactionList = new ArrayList<>();
        try{
            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM "+TABLE_TRANSACTION;

            Cursor cursor  = db.rawQuery(query,null);
            if (cursor.moveToFirst()){
                do{
                    Transaction transaction = new Transaction();
                    //changing string to data type
                    SimpleDateFormat sf = new SimpleDateFormat("dd/mm/yyyy");
                    try{
                        Date date = sf.parse(cursor.getString(1));
                        transaction.setDate(date);
                    }catch (ParseException e){
                        e.printStackTrace();

                    }

                    transaction.setAccountNo(cursor.getString(2));
                    //change string to expense data type
                    String expenseType = cursor.getString(3);

                    ExpenseType expenseTypeEnum = ExpenseType.valueOf(expenseType);
                    transaction.setExpenseType(expenseTypeEnum);

                    transaction.setAmount(cursor.getDouble(4));

                    transactionList.add(transaction);
                }while(cursor.moveToNext());

            }
            cursor.close();
        }catch (Exception e){
            System.out.println(e);

        }
        return transactionList;
    }

    public List<String> getAccNumberList(){
        List<String> accNumberList = new ArrayList<>();
        try{
            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT "+Acc_No+" FROM "+TABLE_ACCOUNT;
            Cursor cursor = db.rawQuery(query,null);

            if (cursor.moveToFirst()){
                do{
                    accNumberList.add(cursor.getString(0));
                }while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            System.out.println(e);
        }


        return  accNumberList;

    }

    public Account getAcc(String accNo){
        Account account = new Account();
        try{

            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM "+TABLE_ACCOUNT+" WHERE "+ Acc_No +"="+"'"+accNo+"'";
            Cursor cursor = db.rawQuery(query,null);
            account.setAccountNo(cursor.getString(0));
            account.setBankName(cursor.getString(1));
            account.setAccountHolderName(cursor.getString(2));
            account.setBalance(cursor.getFloat(3));
            cursor.close();
        } catch (Exception e){
            System.out.println(e);
        }
        return account;
    }

    public void removeAcc(String accNo){
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_ACCOUNT,Acc_No+"=?",new String[]{accNo});
            //db.close();
        }catch (Exception e){
            System.out.println(e);

        }

    }

    public void updateBalanceInfo(Account account){

        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(init_bal,account.getBalance());
            int status = db.update(TABLE_ACCOUNT,contentValues,Acc_No+"=?",
                    new String[]{String.valueOf(account.getAccountNo())});
            //db.close();

        } catch (Exception e){
            System.out.println(e);
        }


    }

    public boolean accExists(String accNo) {
        try {
            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_ACCOUNT + " WHERE " + Acc_No + "=" + "'" + accNo + "'";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
