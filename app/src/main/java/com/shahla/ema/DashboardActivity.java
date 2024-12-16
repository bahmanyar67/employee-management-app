package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.OptIn;

import android.widget.Button;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.shahla.ema.databinding.ActivityAdminDashboardBinding;
import com.shahla.ema.databinding.ActivityEmployeeDashboardBinding;
import com.shahla.ema.databinding.ActivityRegisterBinding;


public class DashboardActivity extends BaseActivity {


    private ActivityAdminDashboardBinding adminBinding;
    private ActivityEmployeeDashboardBinding employeeBinding;

    private Button myAccountButton;
    private Button myHolidayRequestsButton;

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

        if (user.getUserType().equals("employee")) {
            setupToolbar(user.getFirstName() + "'s Dashboard");

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
                    intent.putExtra("current_user_id", user.getId());
                    startActivity(intent);
                }
            });

        } else if (user.getUserType().equals("admin")) {
            adminBinding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
            setContentView(adminBinding.getRoot());

            setupToolbar("Admin Dashboard");

            adminBinding.employeesCount.setText(String.valueOf(userDao.getEmployeesCount()));


            // Set up the manage employees button
            adminBinding.employeesCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, EmployeesActivity.class);
                    startActivity(intent);
                }
            });

            // TODO: update the count of pending holiday requests
            // Set up the holiday requests button
            adminBinding.holidayRequestsCard.setOnClickListener(new View.OnClickListener() {
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