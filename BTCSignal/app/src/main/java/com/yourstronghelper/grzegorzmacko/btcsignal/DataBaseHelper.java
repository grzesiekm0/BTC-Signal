package com.yourstronghelper.grzegorzmacko.btcsignal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.yourstronghelper.grzegorzmacko.btcsignal.AlertContract.DB_NAME;
import static com.yourstronghelper.grzegorzmacko.btcsignal.AlertContract.DB_VERSION;

public class DataBaseHelper extends SQLiteOpenHelper {
    //private final Context context;
    private static volatile DataBaseHelper instance;
    private final SQLiteDatabase db;

    /*public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }*/
    private DataBaseHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
        this.db = getWritableDatabase();
    }
    /**
     * We use a Singleton to prevent leaking the SQLiteDatabase or Context.
     * @return {@link DataBaseHelper}
     */
    public static DataBaseHelper getInstance(Context c) {
        if (instance == null) {
            synchronized (DataBaseHelper.class) {
                if (instance == null) {
                    instance = new DataBaseHelper(c);
                }
            }
        }
        return instance;
    }
    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            /*_db.execSQL(LoginDatabaseAdapter.DATABASE_CREATE);
            _db.execSQL(AlertDatabaseAdapter.DATABASE_CREATE);*/
            createAlertsTable(db);
        }catch(Exception er){
            Log.e("Error","exception ");
        }
    }
    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion)
    {
       /* // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");
        // Upgrade the existing database to conform to the new version. Multiple
        // previous versions can be handled by comparing _oldVersion and _newVersion
        // values.
        // The simplest case is to drop the old table and create a new one.
        _db.execSQL("DROP TABLE IF EXISTS " + "User");
        _db.execSQL("DROP TABLE IF EXISTS " + "Alert");

        // Create a new one.
        onCreate(_db);*/
        // Update any SQLite tables here
        db.execSQL("DROP TABLE IF EXISTS [" + AlertContract.Alert.NAME + "];");
        onCreate(db);
    }


    /**
     * Provide access to our database.
     */
    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     * Creates our 'articles' SQLite database table.
     * @param db {@link SQLiteDatabase}
     */
    private void createAlertsTable(SQLiteDatabase db) {
        db.execSQL("create table [" + AlertContract.Alert.NAME + "]" +
                "( [" + AlertContract.Alert.COL_ID + "] integer primary key autoincrement," +
                "[" + AlertContract.Alert.COL_EXCHANGE + "]  text,[" + AlertContract.Alert.COL_CURRENCY + "]  text," +
                " [" + AlertContract.Alert.COL_COURSE + "] text, [" + AlertContract.Alert.COL_ENABLE_ALARM + "] integer); ");
    }
}
