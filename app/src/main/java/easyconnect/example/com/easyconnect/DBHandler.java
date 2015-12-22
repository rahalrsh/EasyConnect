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
    public static final String USER_IMAGE = "img";

    public static final String TABLE_NAME = "contactInfoTable";
    public static final String DATA_BASE_NAME = "devDataBase";
    public static final int DATA_BASE_VERSION = 1;

    public static final String TABLE_CREATE =   "CREATE TABLE contactInfoTable (userID INTEGER PRIMARY KEY AUTOINCREMENT,firstName TEXT, lastName TEXT, email TEXT, mobile TEXT, company TEXT, fbUID TEXT, fbLink TEXT, twitterUID TEXT, twitterLink TEXT, img BLOB);";
    public static final String TABLE_DROP_IF_EXIST = "DROP TABLE IF EXISTS contactInfoTable";

    // AdsInfoTable
    public static final String ADS_TABLE = "adsTable";
    public static final String USER_NAME = "userName";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String IMAGE_URL = "imageURL";
    public static final String PHONE = "phone";

    public static final String ADS_TABLE_CREATE = "CREATE TABLE adsTable (adID INTEGER PRIMARY KEY AUTOINCREMENT,userName TEXT, title TEXT, description TEXT, imageURL TEXT, phone TEXT);";
    public static final String ADS_TABLE_DROP_IF_EXIST = "DROP TABLE IF EXISTS adsTable";

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
                db.execSQL(ADS_TABLE_CREATE);
                // db.execSQL(IMAGES_TABLE_CREATE);
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
            db.execSQL(ADS_TABLE_DROP_IF_EXIST);
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

    public void insertFBDataInRowOne (byte[] img, String firstName, String lastName, String FBLink ,String FBuid){
        ContentValues content = new ContentValues();
        content.put(USER_ID, 1);
        content.put(USER_IMAGE, img);
        content.put(FIRST_NAME, firstName);
        content.put(LAST_NAME, lastName);
        content.put(FB_LINK, FBLink);
        content.put(FB_UID, FBuid);

        db.insertWithOnConflict(TABLE_NAME, null, content, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public Cursor returnImageInRowOne (){
        //                           columns,selection, selection Args, having, group by, order by
        return db.query(TABLE_NAME, new String[]{USER_ID+"=1", USER_IMAGE}, null, null, null, null, null);
    }

    public Cursor returnData (){
        //                           columns,selection, selection Args, having, group by, order by
        return db.query(TABLE_NAME, new String[]{USER_ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILE, COMPANY, FB_UID, FB_LINK, TWITTER_UID, TWITTER_LINK, USER_IMAGE}, null, null, null, null, null);
    }


    public void printDBInfo(){
        // retrieve data from database
        open();
        Cursor c = returnData();
        if (c.moveToFirst()){ // if cursor move to first that means there are some data
             do{
                 Log.i ("printDBInfo", "------------------------");
                 Log.i ("printDBInfo", "UserID: "+ c.getInt(0));
                 Log.i ("printDBInfo","F Name: "+ c.getString(1));
                 Log.i ("printDBInfo","L Name: "+ c.getString(2));
                 Log.i ("printDBInfo","Email: "+ c.getString(3));
                 Log.i ("printDBInfo", "fb UID: "+ c.getInt(6));

             }while (c.moveToNext());
         }
        close();
    }

    public void insertOrUpdateFirstNameInRowOne(String firstName){
        String DBFirstName;
        ContentValues content = new ContentValues();

        open();
        Cursor c =  db.query(TABLE_NAME, new String[]{USER_ID, FIRST_NAME, LAST_NAME, EMAIL, MOBILE, COMPANY, FB_UID, FB_LINK, TWITTER_UID, TWITTER_LINK}, null, null, null, null, null);
        if (c.moveToFirst()){ // if cursor move to first that means there are some data
            DBFirstName = c.getString(1);
            // If nothing on DB then insert the new value
            if (DBFirstName == null){
                     db.execSQL("INSERT INTO contactInfoTable (firstName) VALUES ("+firstName+") WHERE userID=1;");
            }
            printDBInfo();

        }
        close();
    }


    // Methods for adsTable
    public long insertAd (String adTitle, String userName, String adDescription, String imageURL, String phone){
        ContentValues content = new ContentValues();
        content.put(TITLE, adTitle);
        content.put(USER_NAME, userName);
        content.put(DESCRIPTION, adDescription);
        content.put(IMAGE_URL, imageURL);
        content.put(PHONE, phone);
        return db.insertOrThrow(ADS_TABLE, null, content);
    }

    public long selectLastInsearted(){
        String selectQuery = "SELECT adID FROM " + ADS_TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        return cursor.getLong(0);
    }

    public Cursor searchAdbyID (long adId){
        return db.query(ADS_TABLE, new String[]{TITLE, USER_NAME, DESCRIPTION, IMAGE_URL, PHONE}, "adID="+adId, null, null, null, null);
    }



}
