package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.OptIn;

import android.widget.Button;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.button.MaterialButton;


public class DashboardActivity extends BaseActivity {


    private Button manageEmployeesButton;
    private Button myAccountButton;
    private Button myHolidayRequestsButton;

    private MaterialButton holidayRequestsButton;

    private BadgeDrawable holidayRequestsBadge;

    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Get the current user id and user object from the intent
        int currentUserId = getIntent().getIntExtra("current_user_id", 0);
        UserDao userDao = new UserDao(this);
        User user = userDao.getUserById(currentUserId);

        // Check the user type and load the appropriate activity
        if (user.getUserType().equals("employee")) {
            setContentView(R.layout.activity_employee_dashboard);
        } else if (user.getUserType().equals("admin")) {
            setContentView(R.layout.activity_admin_dashboard);
        }

        // Set up the toolbar
        setupToolbar();
        if (user.getUserType().equals("employee")) {


            setToolbarTitle( user.getFirstName() + " Dashboard");

            // get the user account
            myAccountButton = findViewById(R.id.myAccountButton);
            myAccountButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, MyAccountActivity.class);
                    intent.putExtra("current_user_id", user.getId());
                    startActivity(intent);
                }
            });

            myHolidayRequestsButton = findViewById(R.id.myHolidayRequestsButton);
            myHolidayRequestsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, MyHolidayRequestsActivity.class);
                    intent.putExtra("user", user.getId());
                    startActivity(intent);
                }
            });

        } else if (user.getUserType().equals("admin")) {
            setToolbarTitle("Admin Dashboard");

            // Set up the manage employees button
            manageEmployeesButton = findViewById(R.id.manageEmployeesButton);
            manageEmployeesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, EmployeesActivity.class);
                    startActivity(intent);
                }
            });
            // Set up the holiday requests button
            holidayRequestsButton = findViewById(R.id.holidayRequestsButton);
            holidayRequestsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, HolidayRequestsActivity.class);
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    protected void setupBackButton() {
        // Implement this method to set up the back button
    }
}