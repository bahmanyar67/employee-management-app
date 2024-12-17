package com.shahla.ema;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.OptIn;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

            createNotificationChannel();

            setupToolbar("Admin Dashboard");

            HolidayRequestDao holidayRequestDao = new HolidayRequestDao(this);

            adminBinding.employeesCount.setText(String.valueOf(userDao.getEmployeesCount()));

            // Set up the manage employees button
            adminBinding.employeesCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, EmployeesActivity.class);
                    startActivity(intent);
                }
            });


            int waitingHolidayRequestsCount = holidayRequestDao.getWaitingHolidayRequestsCount();
            adminBinding.holidayRequestsCount.setText(String.valueOf(waitingHolidayRequestsCount));

            if (waitingHolidayRequestsCount > 0) {
                sendNewHolidayRequestNotification();
            }

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

    private boolean askForNotificationPermission() {
        // Check and request POST_NOTIFICATIONS permission only for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API level 33
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        1 // Request code
                );
                return true;
            }
        }
        return false;
    }

    private void createNotificationChannel() {
        CharSequence name = "HolidayRequestChannel";
        String description = "Channel for holiday request notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("holidayRequestChannel", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void sendNewHolidayRequestNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "holidayRequestChannel")
                .setSmallIcon(R.drawable.ic_app) // Replace with your app's notification icon
                .setContentTitle("New Holiday Request")
                .setContentText("A new holiday request has been submitted.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (!askForNotificationPermission()) {
                return;
            }
        }
        notificationManager.notify(1, builder.build());
    }
}