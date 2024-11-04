package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MyHolidayRequestsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_holiday_requests);

        // Set up the toolbar
        setupToolbar();
        setToolbarTitle("My Holiday Requests");

        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the User object from the intent
        User user = (User) getIntent().getSerializableExtra("user");

        // generate the list of holiday requests
        List<HolidayRequest> holidayRequestList = new ArrayList<>();

        LocalDate fromDate = LocalDate.of(2024, 11, 1);
        LocalDate toDate = LocalDate.of(2024, 11, 6);

        holidayRequestList.add(new HolidayRequest(
                user,
                LocalDate.of(2024, 5, 1),
                LocalDate.of(2024, 5, 6),
                "Vocation", HolidayRequest.Status.DECLINED)
        );

        holidayRequestList.add(new HolidayRequest(
                user,
                LocalDate.of(2024, 9, 16),
                LocalDate.of(2024, 9, 19),
                "Sick leave", HolidayRequest.Status.APPROVED)
        );

        holidayRequestList.add(new HolidayRequest(
                user,
                LocalDate.of(2024, 11, 1),
                LocalDate.of(2024, 11, 6),
                "Family Reason", HolidayRequest.Status.WAITING)
        );


        // sort the list by date
       holidayRequestList.sort((r1, r2) -> r1.getFromDate().compareTo(r2.getFromDate()));


        MyHolidayRequestAdapter adapter = new MyHolidayRequestAdapter(holidayRequestList);
        recyclerView.setAdapter(adapter);// display the list of holiday requests



        // new request button
        Button newRequestButton = findViewById(R.id.newRequestButton);
        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyHolidayRequestsActivity.this, NewHolidayRequestActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });


    }

}
