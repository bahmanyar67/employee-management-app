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
        String CREATE_USERS_TABLE = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT," +
                "last_name TEXT," +
                "email TEXT," +
                "password TEXT," +
                "department TEXT," +
                "salary REAL," +
                "user_type TEXT," +
                "joining_date TEXT," +
                "leaves INTEGER)";
        db.execSQL(CREATE_USERS_TABLE);

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
}