package kien.SQLHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import kien.objects.License;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KIEN on 08/05/2015.
 */
public class SqliteDataController extends SQLiteOpenHelper {
    @SuppressLint("SdCardPath")
    public static String DB_PATH = "/data/data/kien.license/databases/";
    private SQLiteDatabase database;
    private static String DB_NAME = "license.db";
    private final Context mContext;
    String SQL_QUERY;
    public List<License> list;
    public static final String DB_TABLE = "province";
    public static final String ID = "id";
    public static final String NUMBER_PROVINCE = "number_province";
    public static final String NAME_PROVINCE = "name_province";
    public static final String POPULATION = "population";
    public static final String AREA = "area";
    public static final String NAME_GOOGLE = "name_google";

    public SqliteDataController(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
    }

    public SqliteDataController(Context context, String DB_NAME) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
    }

    public boolean isCreatedDatabase() throws IOException {
        boolean result = true;
        if (!checkExistDatabase()) {
            this.getReadableDatabase();
            try {
                copyDatabase();
                result = false;
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Copy Database fail");
            }
        }
        return result;
    }

    public boolean isCreatedDatabase(String DB_NAME) throws IOException {
        boolean result = true;
        if (!checkExistDatabase(DB_NAME)) {
            this.getDatabaseName();
            try {
                copyDatabase(DB_NAME);
                result = false;
            } catch (Exception e) {
                e.printStackTrace();
                throw new Error("Error copying Database");
            }
        }
        return result;
    }

    private void copyDatabase() throws IOException {
        InputStream myInput = mContext.getAssets().open(DB_NAME);
        //Log.d("Du", myInput.toString());
        OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private void copyDatabase(String DB_NAME) throws IOException {
        InputStream myInput = mContext.getAssets().open(DB_NAME);
        OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkExistDatabase() {
        try {
            String myPath = DB_PATH + DB_NAME;
            File fileDb = new File(myPath);

            if (fileDb.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    private boolean checkExistDatabase(String DB_NAME) {
        try {
            String myPath = DB_PATH + DB_NAME;
            File fileDb = new File(myPath);

            if (fileDb.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public boolean deleteDatabase() {
        File file = new File(DB_PATH + DB_NAME);
        return file.delete();
    }

    public synchronized void openData() throws SQLException {
        database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void openData(String DB_NAME) throws SQLException {
        database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public List<License> getList() {
        list = new ArrayList<License>();
        String[] columns = new String[]{ID, NUMBER_PROVINCE, NAME_PROVINCE};
        Cursor c = database.query(DB_TABLE, columns, null, null, null, null, null);
        c.moveToFirst();
        int iNumber = c.getColumnIndex(NUMBER_PROVINCE);
        int iName = c.getColumnIndex(NAME_PROVINCE);
        String number_province, name_province;
        int id = 0;
        while (!c.isAfterLast()) {

            number_province = c.getString(iNumber);
            name_province = c.getString(iName);
            list.add(new License(number_province, name_province, id));
        }
        c.close();
        return list;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
