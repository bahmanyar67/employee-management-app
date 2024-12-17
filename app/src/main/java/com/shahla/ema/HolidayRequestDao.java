package com.shahla.ema;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidayRequestDao {

    private SQLiteDatabase db;

    private DatabaseHelper dbHelper;

    private Context context;

    public HolidayRequestDao(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        this.context = context;
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }
    }


    public long insert(HolidayRequest request) {
        ContentValues values = new ContentValues();
        values.put("user_id", request.getEmployee().getId());
        values.put("from_date", request.getFromDate().toString());
        values.put("to_date", request.getToDate().toString());
        values.put("note", request.getNote());
        values.put("status", request.getStatus().name());

        return db.insert("holiday_requests", null, values);
    }

    public List<HolidayRequest> getHolidayRequests() {
        List<HolidayRequest> requests = new ArrayList<>();
        Cursor cursor = db.query("holiday_requests", null, null, null, null, null, null );

        if (cursor.moveToFirst()) {
            do {
                HolidayRequest request = new HolidayRequest();
                request.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));

                int employeeId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                Employee employee = new UserDao(context).getEmployeeById(employeeId);

                request.setEmployee(employee);
                request.setFromDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("from_date"))));
                request.setToDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("to_date"))));
                request.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
                request.setStatus(HolidayRequest.Status.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("status"))));

                requests.add(request);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requests;
    }

    public int getWaitingHolidayRequestsCount() {
        List<HolidayRequest> requests = new ArrayList<>();
        Cursor cursor = db.query("holiday_requests", null, "status = ?",
                new String[]{String.valueOf(HolidayRequest.Status.WAITING)}, null, null,
                "from_date DESC");

        return cursor.getCount();
    }

    public List<HolidayRequest> getHolidayRequestsByEmployeeId(int employeeId) {
        List<HolidayRequest> requests = new ArrayList<>();
        Cursor cursor = db.query("holiday_requests", null, "user_id = ?",
                new String[]{String.valueOf(employeeId)}, null, null,
                "from_date DESC");

        UserDao userDao = new UserDao(context);
        Employee employee = userDao.getEmployeeById(employeeId);

        if (cursor.moveToFirst()) {
            do {
                HolidayRequest request = new HolidayRequest();
                request.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                request.setEmployee(employee);
                request.setFromDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("from_date"))));
                request.setToDate(LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("to_date"))));
                request.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
                request.setStatus(HolidayRequest.Status.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("status"))));

                requests.add(request);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return requests;
    }

    public void updateHolidayRequestStatus(int requestId, HolidayRequest.Status status) {
        ContentValues values = new ContentValues();
        values.put("status", status.name());

        db.update("holiday_requests", values, "id = ?", new String[]{String.valueOf(requestId)});
    }

    public void deleteHolidayRequest(int requestId) {
        db.delete("holiday_requests", "id = ?", new String[]{String.valueOf(requestId)});
    }
}