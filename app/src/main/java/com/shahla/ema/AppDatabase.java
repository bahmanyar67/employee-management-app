package com.shahla.ema;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        AppDatabase.class, "ema-database")
                                .addCallback(new RoomDatabase.Callback() {
                                    @Override
                                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                        super.onCreate(db);
                                        Log.d("DatabaseCreation", "Database created successfully");

                                        // Insert the initial data
                                        try {
                                            String encryptedPassword = Utilities.hashPassword("Shahla123!");

                                            db.execSQL("INSERT INTO users (first_name, last_name, email, password, user_type, department, salary) VALUES" +
                                                    " ('Shahla', 'Admin', 'admin@domain.test', '" + encryptedPassword + "', 'admin', 'Administrator', 0)");
                                        } catch (Exception e) {
                                            Log.e("DatabaseCreation", "Error inserting initial data: " + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                        super.onOpen(db);
                                        Log.d("DatabaseCreation", "Database opened successfully");
                                    }
                                })
                                .build();
                        Log.d("DatabaseCreation", "Try scope completed");
                    } catch (Exception e) {
                        Log.e("DatabaseCreation", "Error creating database: " + e.getMessage());
                    }
                }
            }
        }
        return INSTANCE;
    }
}