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
        if (user.getId() != 0) {
            values.put("id", user.getId());
        }
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

    public void update(Employee user) {
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

    public void updateNotification(User user) {
        ContentValues values = new ContentValues();
        values.put("notifications_enabled", user.isNotificationsEnabled() ? 1 : 0);
        db.update("users", values, "id = ?", new String[]{String.valueOf(user.getId())});
    }

    public void delete(Employee employee) {
        db.delete("users", "id = ?", new String[]{String.valueOf(employee.getId())});
    }

    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
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
                Employee user = new Employee();
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
                }
//                else {
//                    user.setJoiningDate(null);
//                }
                user.setLeaves(cursor.getInt(cursor.getColumnIndexOrThrow("leaves")));
                employees.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return employees;
    }

    public Employee getEmployeeById(int id) {
        Cursor cursor = db.query("users", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Employee user = new Employee();
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

    public User getUserById(int id) {
        Cursor cursor = db.query("users", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
            user.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setDepartment(cursor.getString(cursor.getColumnIndexOrThrow("department")));
            // user.setSalary(cursor.getDouble(cursor.getColumnIndexOrThrow("salary")));
            user.setUserType(cursor.getString(cursor.getColumnIndexOrThrow("user_type")));
            // user.setJoiningDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("joining_date"))));
            // user.setLeaves(cursor.getInt(cursor.getColumnIndexOrThrow("leaves")));
            user.setNotificationsEnabled(cursor.getInt(cursor.getColumnIndexOrThrow("notifications_enabled")) == 1);
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
        if (cursor != null && cursor.moveToFirst()) {
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
            }
//            else {
//                user.setJoiningDate(null);
//            }
            user.setLeaves(cursor.getInt(cursor.getColumnIndexOrThrow("leaves")));
            cursor.close();
            return user;
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return null;
        }
    }

    public boolean isEmailUnique(String email, Employee employee) {
        User user = this.findUserByEmail(email);
        this.close();
        return user == null || (employee != null && user.getId() == employee.getId());
    }

    public int getEmployeesCount() {
        String countQuery = "SELECT * FROM users WHERE user_type = 'employee'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    // get all employees ids
    public List<Integer> getEmployeesIds() {
        List<Integer> ids = new ArrayList<>();
        Cursor cursor = db.query(
                "users",
                new String[]{"id"},
                "user_type = ?", new String[]{"employee"},
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ids;
    }
}