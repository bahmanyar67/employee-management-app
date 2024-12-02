package com.shahla.ema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public void insert(User user) {
        ContentValues values = new ContentValues();
        values.put("first_name", user.getFirstName());
        values.put("last_name", user.getLastName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("department", user.getDepartment());
        values.put("salary", user.getSalary());
        values.put("user_type", user.getUserType());
        values.put("joining_date", user.getJoiningDate().toString());
        values.put("leaves", user.getLeaves());
        db.insert("users", null, values);
    }

    public void update(User user) {
        ContentValues values = new ContentValues();
        values.put("first_name", user.getFirstName());
        values.put("last_name", user.getLastName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("department", user.getDepartment());
        values.put("salary", user.getSalary());
        values.put("user_type", user.getUserType());
        values.put("joining_date", user.getJoiningDate().toString());
        values.put("leaves", user.getLeaves());
        db.update("users", values, "id = ?", new String[]{String.valueOf(user.getId())});
    }

    public void delete(User user) {
        db.delete("users", "id = ?", new String[]{String.valueOf(user.getId())});
    }

    public List<User> getEmployees() {
        List<User> employees = new ArrayList<>();
        Cursor cursor = db.query(
                "users",
                null,
                "user_type = ?", new String[]{"employee"},
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
                user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
                user.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow("department")));
                user.setSalary(cursor.getDouble(cursor.getColumnIndexOrThrow("salary")));
                user.setUserType(cursor.getString(cursor.getColumnIndexOrThrow("user_type")));
                String joiningDateStr = cursor.getString(cursor.getColumnIndexOrThrow("joining_date"));
                if (joiningDateStr != null) {
                    user.setJoiningDate(LocalDate.parse(joiningDateStr));
                } else {
                    user.setJoiningDate(null);
                }
                user.setLeaves(cursor.getInt(cursor.getColumnIndexOrThrow("leaves")));
                employees.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return employees;
    }

    public User getEmployeeById(int id) {
        Cursor cursor = db.query("users", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
            user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow("department")));
            user.setSalary(cursor.getDouble(cursor.getColumnIndexOrThrow("salary")));
            user.setUserType(cursor.getString(cursor.getColumnIndexOrThrow("user_type")));
            user.setJoiningDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("joining_date"))));
            user.setLeaves(cursor.getInt(cursor.getColumnIndexOrThrow("leaves")));
            cursor.close();
            return user;
        } else {
            return null;
        }
    }

    public User findUserByEmail(String email) {
        if (db == null || !db.isOpen()) {
            throw new IllegalStateException("Database is not open");
        }

        Cursor cursor = db.query(
                "users",
                null,
                "email = ?", new String[]{email},
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
            user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow("department")));
            user.setSalary(cursor.getDouble(cursor.getColumnIndexOrThrow("salary")));
            user.setUserType(cursor.getString(cursor.getColumnIndexOrThrow("user_type")));
            String joiningDateStr = cursor.getString(cursor.getColumnIndexOrThrow("joining_date"));
            if (joiningDateStr != null) {
                user.setJoiningDate(LocalDate.parse(joiningDateStr));
            } else {
                user.setJoiningDate(null);
            }
            user.setLeaves(cursor.getInt(cursor.getColumnIndexOrThrow("leaves")));
            cursor.close();
            return user;
        } else {
            return null;
        }
    }
}