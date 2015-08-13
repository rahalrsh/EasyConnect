package easyconnect.example.com.easyconnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by rahal on 15-07-07.
 * Watch database tutorial - https://www.youtube.com/watch?v=fceqoJ61ANY
 * Official Android SQL Database tutorial - http://developer.android.com/training/basics/data-storage/databases.html
 */
public class DBHandler {
    public static final String USER_ID = "userID";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String MOBILE = "mobile";
    public static final String COMPANY = "company";
    public static final String FB_UID = "fbUID";
    public static final String FB_LINK = "fbLink";
    public static final String TWITTER_UID = "twitterUID";
    public static final String TWITTER_LINK = "twitterLink";

    public static final String TABLE_NAME = "contactInfoTable";
    public static final String DATA_BASE_NAME = "devDataBase";
    public static final int DATA_BASE_VERSION = 1;

    public static final String TABLE_CREATE =   "CREATE TABLE contactInfoTable (userID INTEGER PRIMARY KEY AUTOINCREMENT,firstName TEXT, lastName TEXT, email TEXT, mobile TEXT, company TEXT, fbUID INTEGER, fbLink TEXT, twitterUID INTEGER, twitterLink TEXT);";
    public static final String TABLE_DROP_IF_EXIST = "DROP TABLE IF EXISTS contactInfoTable";

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
                Log.i("DBHandler.java","Table Created");
            }
            catch (SQLException e){
                e.printStackTrace();
                Log.i("DBHandler.java", "Table Couldn't Create");
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

    public long insertData (String firstName, String lastName, String email, String mobile, String company,
                            int fbUID, String fbLink, int twitterUID, String twitterLink){
        ContentValues content = new ContentValues();
        //content.put(USER_ID, 1235);
        content.put(FIRST_NAME, firstName);
        content.put(LAST_NAME, lastName);
        content.put(EMAIL, email);
        content.put(MOBILE, mobile);
        content.put(COMPANY, company);
        content.put(FB_UID, fbUID);
        content.put(FB_LINK, fbLink);
        content.put(TWITTER_UID, twitterUID);
        content.put(TWITTER_LINK, twitterLink);
        return db.insertOrThrow(TABLE_NAME, null, content);
    }

    public Cursor returnData (){
        //                           columns,selection, selection Args, having, group by, order by
        return db.query(TABLE_NAME, new String[]{USER_ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILE, COMPANY, FB_UID, FB_LINK, TWITTER_UID, TWITTER_LINK}, null, null, null, null, null);
    }
}
