package kien.SQLHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import kien.objects.License;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KIEN on 6/16/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/kien.license/databases/";
    private static String DB_NAME = "license.db";
    private SQLiteDatabase myDatabase;
    private final Context mContext;
    public static final String DB_TABLE = "province";
    public static final String ID = "id";
    public static final String NUMBER_PROVINCE = "number_province";
    public static final String NAME_PROVINCE = "name_province";

    public DataBaseHelper(Context context) {
        super(context, null, null, 1);
        mContext = context;
    }
    public DataBaseHelper(Context context, String DB_NAME) {
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
//    public void createDatabase() throws IOException {
//        boolean dbExist = checkDatabase();
//        if (dbExist) {
//            Log.i("KIEN", "HELLO");
//
//        } else {
//            this.getWritableDatabase();
//            try {
//                Log.i("COPY", "COPY DataBase");
//                copyDatabase();
//            } catch (IOException e) {
//                throw new Error("Error");
//            }
//            Log.i("COPY", "COPY SUCCESS");
//        }
//    }
//
//    public boolean checkDatabase() {
//        SQLiteDatabase checkDB = null;
//        try {
//            String myPath = DB_PATH + DB_NAME;
//            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
//        } catch (SQLiteException e) {
//            if (checkDB != null) {
//                checkDB.close();
//            }
//        } finally {
//            if (checkDB != null) {
//                checkDB.close();
//            }
//            this.close();
//        }
//        Log.i("COPY", "CHECK DATABASE " + checkDB);
//        return checkDB != null ? true : false;
//    }
//
//    private void copyDatabase() throws IOException {
//        InputStream myInput = myContext.getAssets().open(DB_NAME);
//        String outputFilename = DB_PATH + DB_NAME;
//        Log.i("COPY", "COPY DataBase: " + outputFilename);
//        OutputStream myOuput = new FileOutputStream(outputFilename);
//
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = myInput.read(buffer)) > 0) {
//            myOuput.write(buffer, 0, length);
//        }
//        Log.e("COPY", "COPY SUCCESS");
//        myOuput.flush();
//        myOuput.close();
//        myInput.close();
//    }

    public SQLiteDatabase openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        return myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }
    public List<License> getListDistrict(int id){
        List<License> licenseList = new ArrayList<License>();
        SQLiteDatabase myBD;
        try {
            myBD = this.openDataBase();
            String myQuery = "select id, number_district, name_district from district where id_province = " + id + " ;";
            Cursor c = myBD.rawQuery(myQuery, null);
            if (c != null) {
                c.moveToFirst();
                int iNumber = c.getColumnIndex(NUMBER_PROVINCE);
                int iName = c.getColumnIndex(NAME_PROVINCE);
                String number_province, name_province;
                int idDis;
                while (!c.isAfterLast()) {
                    idDis = c.getInt(0);
                    number_province = c.getString(1);
                    name_province = c.getString(2);
                    Log.e("DISTRICT", number_province);
                    c.moveToNext();
                    licenseList.add(new License(number_province, name_province, idDis));
                }
            }
            c.close();
        } catch (SQLException e) {

        }
        return licenseList;
    }
    public List<License> getListSearch(String search){
        List<License> licenseList = new ArrayList<License>();
        SQLiteDatabase myDB;
        try {
            myDB = this.openDataBase();
            String myQuery = "select id, number_province, name_province from province where number_province like %" + search + " ;";
            Cursor c = myDB.rawQuery(myQuery, null);
            if (c!=null){
                c.moveToFirst();
                String number_province, name_province;
                int id;
                while (!c.isAfterLast()) {
                    id = c.getInt(0);
                    number_province = c.getString(1);
                    name_province = c.getString(2);
                    c.moveToNext();
                    licenseList.add(new License(number_province, name_province, id));
                }
            }
        }
        catch (SQLException e){

        }
        return licenseList;
    }
    public List<License> getListProvince(String sort_by) {
        List<License> licenseList = new ArrayList<License>();
        SQLiteDatabase myBD;
        try {
            myBD = this.openDataBase();
            String myQuery = "select id, number_province, name_province from province order by " + sort_by;
            Cursor c = myBD.rawQuery(myQuery, null);
            Log.e("CURSOR", "BUG" + c);
            if (c != null) {
                c.moveToFirst();
                int iNumber = c.getColumnIndex(NUMBER_PROVINCE);
                int iName = c.getColumnIndex(NAME_PROVINCE);
                String number_province, name_province;
                int id;
                while (!c.isAfterLast()) {
                    id = c.getInt(0);
                    number_province = c.getString(1);
                    name_province = c.getString(2);
                    c.moveToNext();
                    Log.e("TRUNGKIEN", "CUR");
                    licenseList.add(new License(number_province, name_province, id));
                }
            }
            c.close();
        } catch (SQLException e) {

        }
        return licenseList;
    }
    public int getPopulationProvince(int id){
        SQLiteDatabase myDB;
        int population = 0;
        try {
            myDB = this.openDataBase();
            String myQuery = "select population from province where id = " + id + ";";
            Cursor cursor = myDB.rawQuery(myQuery, null);
            cursor.moveToFirst();
            population = cursor.getInt(0);
        }
        catch (SQLException e)
        {

        }
        return population;
    }
    public String getAddressLocal(int id){
        SQLiteDatabase myDB;
        String addressLocal = null;
        try {
            myDB = this.openDataBase();
            String myQuery = "select name_province from province where id = " + id + ";";
            Cursor cursor = myDB.rawQuery(myQuery, null);
            cursor.moveToFirst();
            addressLocal = cursor.getString(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressLocal;
    }
    public float getAreaProvince(int id){
        float arae = 0;
        SQLiteDatabase myDB;
        try {
            myDB = this.openDataBase();
            String myQuery = "select area from province where id = " + id + ";";
            Cursor cursor = myDB.rawQuery(myQuery, null);
            cursor.moveToFirst();
            arae = cursor.getInt(0);
        }
        catch (SQLException e) {

        }
        return arae;
    }
    public int getPopulationDicstrict(int id){
        SQLiteDatabase myDB;
        int population = 0;
        Log.e("POPULATION", population + "");
        try {
            myDB = this.openDataBase();
            String myQuery = "select population from district where id = " + id + ";";
            Cursor cursor = myDB.rawQuery(myQuery, null);
            cursor.moveToFirst();
            population = cursor.getInt(0);
            Log.e("POPULATION", population + "");
        }
        catch (SQLException e)
        {

        }
        return population;
    }
    public LatLng getLatLong(int id){
        LatLng latLong = new LatLng(0,0);
        SQLiteDatabase myDB;
        String lat;
        String lng;
        float latitute;
        float longtitude;
        try {
            myDB = this.openDataBase();
            String myQuery = "select latitude, longtitude from province where id = " + id + ";";
            Cursor cursor = myDB.rawQuery(myQuery, null);
            cursor.moveToFirst();
            lat = cursor.getString(0);
            lng = cursor.getString(1);
            latitute = Float.parseFloat(lat);
            longtitude = Float.parseFloat(lng);
            latLong = new LatLng(latitute, longtitude);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return latLong;
    }
    public float getAreaDistrict(int id){
        float arae = 0;
        SQLiteDatabase myDB;
        try {
            myDB = this.openDataBase();
            String myQuery = "select area from province where id = " + id + ";";
            Cursor cursor = myDB.rawQuery(myQuery, null);
            cursor.moveToFirst();
            arae = cursor.getInt(0);
        }
        catch (SQLException e) {

        }
        return arae;
    }
    @Override
    public synchronized void close() {
        if (myDatabase != null) {
            myDatabase.close();
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
