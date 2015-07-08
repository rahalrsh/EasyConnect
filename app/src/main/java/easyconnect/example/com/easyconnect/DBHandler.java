package easyconnect.example.com.easyconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rahal on 15-07-07.
 * Watch database tutorial - https://www.youtube.com/watch?v=fceqoJ61ANY
 * Official Android SQL Database tutorial - http://developer.android.com/training/basics/data-storage/databases.html
 */
public class DBHandler {
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String TABLE_NAME = "userInfoTable";
    public static final String DATA_BASE_NAME = "devDataBase";
    public static final int DATA_BASE_VERSION = 1;

    public static final String TABLE_CREATE = "create table userInfoTable (name text not null, email text not null);";
    public static final String TABLE_DROP_IF_EXIST = "DROP TABLE IF EXISTS userInfoTable";

    DataBaseHelper dbhelper;
    Context ctx;
    SQLiteDatabase db;
    public DBHandler(Context ctx){
        this.ctx = ctx;
        dbhelper = new DataBaseHelper(ctx);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper{

        public DataBaseHelper(Context ctx){
            super(ctx, DATA_BASE_NAME, null, DATA_BASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(TABLE_CREATE);
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // First Drop Table if it exist already
            db.execSQL(TABLE_DROP_IF_EXIST);
            // Now create Table by calling onCreate(db)
            onCreate(db);
        }
    }

    // Method for opening database
    public DBHandler open(){
        db = dbhelper.getWritableDatabase();
        return this;
    }

    // Method for closing database
    public void close(){
        dbhelper.close();
    }

    public long insertData (String name, String email){
        ContentValues content = new ContentValues();
        content.put(NAME, name);
        content.put(EMAIL, email);
        return db.insertOrThrow(TABLE_NAME, null, content);
    }

    public Cursor returnData (){
        //                           columns,selection, selection Args, having, group by, order by
        return db.query(TABLE_NAME, new String[]{NAME, EMAIL}, null, null, null, null, null);
    }
}
