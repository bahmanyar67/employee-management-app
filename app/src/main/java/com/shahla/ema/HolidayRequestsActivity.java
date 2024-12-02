package com.shahla.ema;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidayRequestsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_requests);


        userDao = new UserDao(this);

        // Set up the toolbar
        setupToolbar();
        setToolbarTitle("Holiday Requests");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<HolidayRequest> holidayRequestList = new ArrayList<>();
        List<User> employeeList = userDao.getEmployees();

        String[] notes = {"Sick leave", "Vacation", "Family reasons", "Personal reasons"};

        // generate the list of holiday requests
        for (int i = 0; i < 10; i++) {
            // random status
            HolidayRequest.Status status = HolidayRequest.Status.values()[(int) (Math.random() * HolidayRequest.Status.values().length)];

            LocalDate fromDate = generateRandomDate();
            LocalDate toDate = generateRandomDate(fromDate);

            // random date
            holidayRequestList.add(new HolidayRequest(employeeList.get(i),
                    fromDate, toDate, notes[i % notes.length], status));

        }

        // show waiting requests first
        holidayRequestList.sort((r1, r2) -> r1.getStatus() == HolidayRequest.Status.WAITING ? -1 : 1);


        HolidayRequestAdapter adapter = new HolidayRequestAdapter(holidayRequestList);
        recyclerView.setAdapter(adapter);
    }


    private LocalDate generateRandomDate() {
        return generateRandomDate(null);
    }
    private LocalDate generateRandomDate(LocalDate previousDate) {
        LocalDate lowerBound = LocalDate.of(2024, 11, 1);
        LocalDate upperBound = LocalDate.of(2025, 5, 1);

        if (previousDate != null) {
            lowerBound = previousDate.plusDays(1);
            upperBound = previousDate.plusDays(5);
        }

        long startEpochDay = lowerBound.toEpochDay();
        long endEpochDay = upperBound.toEpochDay();

        long randomDay = startEpochDay + (long) (Math.random() * (endEpochDay - startEpochDay));
        return LocalDate.ofEpochDay(randomDay);
    }

}