package com.shahla.ema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ema-database.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create users table
        createTable(db, "users", "id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT, last_name TEXT, email TEXT, password TEXT, department TEXT, salary REAL, user_type TEXT, joining_date TEXT, leaves INTEGER");


        // Create Holiday Requests table
        createTable(db, "holiday_requests", "id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, from_date TEXT, to_date TEXT, note TEXT, status TEXT, FOREIGN KEY(user_id) REFERENCES users(id)");


        // Insert default admin user
        String encryptedPassword = Utilities.hashPassword("Shahla123!");
        db.execSQL("INSERT INTO users (first_name, last_name, email, password, user_type, department, salary) VALUES" +
                " ('Shahla', 'Admin', 'admin@domain.test', '" + encryptedPassword + "', 'admin', 'Administrator', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }


    private void createTable(SQLiteDatabase db, String tableName, String columns) {
        String CREATE_TABLE = "CREATE TABLE " + tableName + " (" + columns + ")";
        db.execSQL(CREATE_TABLE);
    }

}