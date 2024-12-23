package com.shahla.ema;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ema-database.db";
    private static final int DATABASE_VERSION = 5;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create users table
        createTable(db, "users", "id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT, last_name TEXT, email TEXT, password TEXT, department TEXT, salary REAL, user_type TEXT, joining_date TEXT, leaves INTEGER, notifications_enabled INTEGER DEFAULT 1");

        // Create Holiday Requests table
        createTable(db, "holiday_requests", "id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, from_date TEXT, to_date TEXT, note TEXT, status TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, should_notify INTEGER DEFAULT 1, FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE");

        // Insert default admin user
        String encryptedPassword = Utilities.hashPassword("Shahla123!");
        db.execSQL("INSERT INTO users (first_name, last_name, email, password, user_type, department, salary, notifications_enabled) VALUES" +
                " ('Shahla', 'Admin', 'admin@domain.test', '" + encryptedPassword + "', 'admin', 'Administrator', 0, 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE users ADD COLUMN notifications_enabled INTEGER DEFAULT 0");
        }

        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE holiday_requests ADD COLUMN created_at TIMESTAMP");
            db.execSQL("UPDATE holiday_requests SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL");
        }

        if (oldVersion < 4) {
            // Drop the existing holiday_requests table and recreate it with cascade constraints
            db.execSQL("DROP TABLE IF EXISTS holiday_requests");
            createTable(db, "holiday_requests", "id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, from_date TEXT, to_date TEXT, note TEXT, status TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE");
        }

        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE holiday_requests ADD COLUMN should_notify INTEGER DEFAULT 0");
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }


    private void createTable(SQLiteDatabase db, String tableName, String columns) {
        String CREATE_TABLE = "CREATE TABLE " + tableName + " (" + columns + ")";
        db.execSQL(CREATE_TABLE);
    }

}