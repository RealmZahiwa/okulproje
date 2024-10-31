package com.example.proje;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "dbtest11.db"; // Veritabanı dosya adı
    private static String DB_PATH = "";
    private SQLiteDatabase myDatabase;
    private final Context myContext;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Veritabanını kopyalayacağımız için burada bir şey yapmaya gerek yok
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Veritabanını kopyalayacağımız için burada bir şey yapmaya gerek yok
    }

    // Veritabanını assets klasöründen kopyalamak için
    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    // Veritabanının var olup olmadığını kontrol eder
    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLException e) {
            // Veritabanı henüz mevcut değil
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    // Veritabanını assets klasöründen kopyalar
    private void copyDatabase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        OutputStream myOutput = new FileOutputStream(DB_PATH);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    // Veritabanını açar
    public void openDatabase() throws SQLException {
        myDatabase = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (myDatabase != null) {
            myDatabase.close();
        }
        super.close();
    }

    // Veritabanı nesnesini döndürür
    public SQLiteDatabase getDatabase() {
        return myDatabase;
    }
}