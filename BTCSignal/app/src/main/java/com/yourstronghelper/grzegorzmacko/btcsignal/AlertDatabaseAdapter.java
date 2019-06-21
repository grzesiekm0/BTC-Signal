package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AlertDatabaseAdapter {
    static final String DATABASE_NAME = "database.db";
    String ok="OK";
    static final int DATABASE_VERSION = 1;
    public  static String getPassword="";
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table Alert( AlertId integer primary key autoincrement,Exchange  text,Currency  text, Course text, EnableAlarm integer); ";
    // Variable to hold the database instance
    public static SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private static DataBaseHelper dbHelper;
    public  AlertDatabaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Method to openthe Database
    public  AlertDatabaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();        return this;
    }
    // Method to close the Database
    public void close()
    {
        db.close();
    }
    // method returns an Instance of the Database
    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }
    // method to insert a record in Table
    public String insertEntry(String exchange,String currency, String course, int enableAlarm)
    {
        try {
            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("Exchange", exchange);
            newValues.put("Currency", currency);
            newValues.put("Course", course);
            newValues.put("EnableAlarm", enableAlarm);
            // Insert the row into your table
            db = dbHelper.getWritableDatabase();
            long result=db.insert("Alert", null, newValues);
            System.out.print(result);
            Toast.makeText(context, "Dodano alert", Toast.LENGTH_LONG).show();
        }catch(Exception ex) {
            System.out.println("Exceptions " +ex);
            Log.e("Note", "One row entered");
        }
        return ok;
    }
    // method to delete a Record of UserName
    public int deleteEntry(String UserName)
    {
        String where="AlertId=?";
        int numberOFEntriesDeleted= db.delete("Alert", where, new String[]{UserName}) ;
        Toast.makeText(context, "Usunięto z powodzeniem : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
    // method to get something  of alert
    public String getSinlgeEntry(String userName)
    {
        db=dbHelper.getReadableDatabase();
        String[] columns = {"AlertId", "Exchange", "Currency", "Course", "EnableAlarm"};
        Cursor cursor=db.query("Alert", null, "AlertId=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
            return "NOT EXIST";
        cursor.moveToFirst();
        getPassword= cursor.getString(cursor.getColumnIndex("PhoneNumber"));
        return getPassword;
    }

    public ArrayList<Alert> getSinlgeEntry()
    {
        ArrayList<Alert> alerts = new ArrayList<>();
        db=dbHelper.getReadableDatabase();
        String[] columns = {"AlertId", "Exchange", "Currency", "Course", "EnableAlarm"};
        Cursor cursor = db.query("Alert", columns, null, null, null, null, null);
        /*if(cursor.getCount()<1) // UserName Not Exist
            return "NOT EXIST";*/
        while(cursor.moveToNext()){
            Alert alert = new Alert();
            alert.setExchange(cursor.getString(1));
            alert.setCurrency(cursor.getString(2));
            alert.setCourse(cursor.getString(3));
            alert.setEnableAlarm(cursor.getInt(4));
            alerts.add(alert);
        }
        return alerts;
    }
    // Method to Update an Existing
    public void  updateEntry(String exchange,String currency, String course, int enableAlarm)
    {
        //  create object of ContentValues
        ContentValues updatedValues = new ContentValues();
        // Assign values for each Column.
        updatedValues.put("Exchange", exchange);
        updatedValues.put("Currency", currency);
        updatedValues.put("Course", course);
        updatedValues.put("EnableAlarm", enableAlarm);
        String where="AlertId = ?";
        db.update("Alert",updatedValues, where, new String[]{exchange});
    }

    // Method to Update an Existing
    public void  updateEntrySwitch(String alertId, int enableAlarm)
    {
        //  create object of ContentValues
        ContentValues updatedValues = new ContentValues();
        // Assign values for each Column.
        updatedValues.put("EnableAlarm", enableAlarm);
        String where="AlertId = ?";
        db.update("Alert",updatedValues, where, new String[]{alertId});
    }

}
