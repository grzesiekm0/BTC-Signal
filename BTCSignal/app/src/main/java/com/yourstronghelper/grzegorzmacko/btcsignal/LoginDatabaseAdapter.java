package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class LoginDatabaseAdapter {
    static final String DATABASE_NAME = "database.db";
    String ok="OK";
    static final int DATABASE_VERSION = 1;
    public  static String getPassword="";
    public static final int NAME_COLUMN = 1;
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    static final String DATABASE_CREATE = "create table User( UserId integer primary key autoincrement,Name  text,PhoneNumber  text); ";
    // Variable to hold the database instance
    public static SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private static DataBaseHelper dbHelper;
    public  LoginDatabaseAdapter(Context _context)
    {
        context = _context;
        //dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Method to openthe Database
    public  LoginDatabaseAdapter open() throws SQLException
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
    public String insertEntry(String name,String phoneNumber)
    {
        try {
            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put("Name", name);
            newValues.put("PhoneNumber", phoneNumber);
            // Insert the row into your table
            db = dbHelper.getWritableDatabase();
            long result=db.insert("User", null, newValues);
            System.out.print(result);
            Toast.makeText(context, "Sukces", Toast.LENGTH_LONG).show();
        }catch(Exception ex) {
            System.out.println("Exceptions " +ex);
            Log.e("Note", "One row entered");
        }
        return ok;
    }
    // method to delete a Record of UserName
    public int deleteEntry(String UserName)
    {
        String where="Name=?";
        int numberOFEntriesDeleted= db.delete("User", where, new String[]{UserName}) ;
        Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
        return numberOFEntriesDeleted;
    }
    // method to get the password  of userName
    public String getSinlgeEntry(String userName)
    {
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.query("User", null, "Name=?", new String[]{userName}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
            return "NOT EXIST";
        cursor.moveToFirst();
        getPassword= cursor.getString(cursor.getColumnIndex("PhoneNumber"));
        return getPassword;
    }
    // Method to Update an Existing
    public void  updateEntry(String userName,String password)
    {
        //  create object of ContentValues
        ContentValues updatedValues = new ContentValues();
        // Assign values for each Column.
        updatedValues.put("Name", userName);
        updatedValues.put("PhoneNumber", password);
        String where="Name = ?";
        db.update("LOGIN",updatedValues, where, new String[]{userName});
    }

}