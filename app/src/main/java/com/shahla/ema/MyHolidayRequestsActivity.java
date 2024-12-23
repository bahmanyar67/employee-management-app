package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MyHolidayRequestsActivity extends BaseActivity {

    private Employee employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_holiday_requests);

        // Set up the toolbar
        setupToolbar("My Holiday Requests");

        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get current user id from the intent and get the employee object from the database
        int currentUserId = getIntent().getIntExtra("current_user_id", 0);
        UserDao userDao = new UserDao(this);
        employee = userDao.getEmployeeById(currentUserId);


        // get employee's holiday requests from the database
        HolidayRequestDao holidayRequestDao = new HolidayRequestDao(this);
        List<HolidayRequest> holidayRequestList = holidayRequestDao.getHolidayRequestsByEmployeeId(employee.getId());

        MyHolidayRequestAdapter adapter = new MyHolidayRequestAdapter(holidayRequestList);
        recyclerView.setAdapter(adapter); // display the list of holiday requests


        // update holiday requests that has should_notify
        holidayRequestDao.resetNotifiedHolidayRequests(employee.getId());


        // new request button
        Button newRequestButton = findViewById(R.id.newRequestButton);
        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyHolidayRequestsActivity.this, NewHolidayRequestActivity.class);
                intent.putExtra("current_user_id", employee.getId());
                startActivity(intent);
            }
        });


    }

}
