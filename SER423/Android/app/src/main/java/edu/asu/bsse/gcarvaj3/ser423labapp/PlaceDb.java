package edu.asu.bsse.gcarvaj3.ser423labapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ser423Lab.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Copyright 2019 Gianni Carvajal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Gianni Carvajal mailto:gcarvaj3@asu.edu
 * @version November 29, 2019
 */

public class PlaceDb extends SQLiteOpenHelper {
    private static final boolean debugon = true;
    private static final int DATABASE_VERSION = 3;
    private static String dbName = "placedb";
    private String dbPath;
    private SQLiteDatabase crsDB;
    private final Context context;

    public PlaceDb(Context context){
        super(context, dbName, null, DATABASE_VERSION);
        this.context = context;
        // place the database in the files directory. Could also place it in the databases directory
        // with dbPath = context.getDatabasePath("dbName"+".db").getPath();
        dbPath = context.getFilesDir().getPath()+"/";
        android.util.Log.d(this.getClass().getSimpleName(),"db path is: "+
                context.getDatabasePath("placedb"));
        android.util.Log.d(this.getClass().getSimpleName(),"dbpath: "+dbPath);
    }

    public void createDB() throws IOException {
        this.getReadableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            android.util.Log.w(this.getClass().getSimpleName(),
                    "createDB Error copying database " + e.getMessage());
        }
    }

    /**
     * Does the database exist and has it been initialized? This method determines whether
     * the database needs to be copied to the data/data/pkgName/files directory by
     * checking whether the file exists. If it does it checks to see whether the db is
     * uninitialized or whether it has the course table.
     * @return false if the database file needs to be copied from the assets directory, true
     * otherwise.
     */
    private boolean checkDB(){    //does the database exist and is it initialized?
        SQLiteDatabase checkDB = null;
        boolean crsTabExists = false;
        try{
            String path = dbPath + dbName + ".db";
            debug("PlaceDB --> checkDB: path to db is", path);
            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {
                    debug("PlaceDb --> checkDB","opened db at: " + checkDB.getPath());
                    @SuppressLint("Recycle") Cursor tabChk = checkDB.rawQuery(
                            "SELECT name FROM sqlite_master where type='table' " +
                                    "and name='places';", null);
                    if(tabChk == null){
                        debug("PlaceDb --> checkDB","check for places table result set is null");
                    } else{
                        tabChk.moveToNext();
                        debug("PlaceDb --> checkDB","check for places table result set is: " +
                                ((tabChk.isAfterLast() ? "empty" : (String) tabChk.getString(0))));
                        crsTabExists = !tabChk.isAfterLast();
                    }
                    if(crsTabExists){
                        @SuppressLint("Recycle") Cursor c = checkDB.rawQuery(
                                "SELECT * FROM places", null);
                        c.moveToFirst();
                        while(!c.isAfterLast()) {
                            int placeName = c.getInt(c.getColumnIndex("name"));
                            debug("PlaceDb --> checkDB","Place table has name: "+
                                    placeName);
                            c.moveToNext();
                        }
                        crsTabExists = true;
                    }
                }
            }
        }catch(SQLiteException e){
            android.util.Log.w("PlaceDb->checkDB",e.getMessage());
        }
        if(checkDB != null){
            checkDB.close();
        }
        return crsTabExists;
    }

    public void copyDB() throws IOException{
        try {
            if(!checkDB()){
                // only copy the database if it doesn't already exist in my database directory
                debug("PlaceDb --> copyDB", "checkDB returned false, starting copy");
                InputStream ip =  context.getResources().openRawResource(R.raw.placedb);
                // make sure the database path exists. if not, create it.
                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op =  dbPath  +  dbName +".db";
                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            android.util.Log.w("PlaceDb --> copyDB", "IOException: "+e.getMessage());
        }
    }

    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPath + dbName + ".db";
        if(checkDB()) {
            crsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            debug("PlaceDb --> openDB", "opened db at path: " + crsDB.getPath());
        }else{
            try {
                this.copyDB();
                crsDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }catch(Exception ex) {
                android.util.Log.w(this.getClass().getSimpleName(),
                        "unable to copy and open db: "+ex.getMessage());
            }
        }
        return crsDB;
    }

    @Override
    public synchronized void close() {
        if(crsDB != null)
            crsDB.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void debug(String hdr, String msg){
        if(debugon){
            android.util.Log.d(hdr,msg);
        }
    }

}
