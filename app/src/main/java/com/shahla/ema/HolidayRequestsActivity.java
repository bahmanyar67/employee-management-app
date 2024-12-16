package com.shahla.ema;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidayRequestsActivity extends BaseActivity {

    protected HolidayRequestDao holidayRequestDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_requests);


        userDao = new UserDao(this);
        holidayRequestDao = new HolidayRequestDao(this);

        // Set up the toolbar
        setupToolbar("Holiday Requests");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get all holiday requests from the database
        List<HolidayRequest> holidayRequestList = holidayRequestDao.getHolidayRequests();


        HolidayRequestAdapter adapter = new HolidayRequestAdapter(holidayRequestList);
        recyclerView.setAdapter(adapter);
    }

}