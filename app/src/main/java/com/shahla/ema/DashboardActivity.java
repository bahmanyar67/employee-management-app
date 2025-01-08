package com.shahla.ema;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.shahla.ema.databinding.ActivityAdminDashboardBinding;
import com.shahla.ema.databinding.ActivityEmployeeDashboardBinding;

public class DashboardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the current user id and user object from the intent
        currentUserId = getIntent().getIntExtra("current_user_id", 0);
        UserDao userDao = new UserDao(this);
        User user = userDao.getUserById(currentUserId);

        // Check the user type and load the appropriate activity
        if (user.getUserType().equals("employee")) {
            setContentView(R.layout.activity_employee_dashboard);
        } else if (user.getUserType().equals("admin")) {
            setContentView(R.layout.activity_admin_dashboard);
        }

        HolidayRequestDao holidayRequestDao = new HolidayRequestDao(this);

        if (user.getUserType().equals("employee")) {
            ActivityEmployeeDashboardBinding employeeBinding = ActivityEmployeeDashboardBinding.
                    inflate(getLayoutInflater());
            setContentView(employeeBinding.getRoot());

            setupToolbar(user.getFirstName() + "'s Dashboard");

            // update holiday requests count
            int myWaitingHolidayRequestsCount = holidayRequestDao.getMyWaitingHolidayRequestsCount(user.getId());
            employeeBinding.myHolidayRequestsCount.setText(String.valueOf(myWaitingHolidayRequestsCount));

            // get the user account
            employeeBinding.myAccountCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, MyAccountActivity.class);
                    intent.putExtra("current_user_id", user.getId());
                    startActivity(intent);
                }
            });

            // handle notification
            int myNotifiedHolidayRequestsCount = holidayRequestDao.getMyNotifiedHolidayRequestsCount(user.getId());
            if (myNotifiedHolidayRequestsCount > 0 && user.isNotificationsEnabled()) {
                sendNewHolidayRequestNotification(
                        "Holiday Request Changed",
                        "Your holiday request has been updated.",
                        MyHolidayRequestsActivity.class);
            }

            employeeBinding.myHolidayRequestsCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, MyHolidayRequestsActivity.class);
                    intent.putExtra("current_user_id", user.getId());
                    startActivity(intent);
                }
            });

        } else if (user.getUserType().equals("admin")) {
            ActivityAdminDashboardBinding adminBinding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
            setContentView(adminBinding.getRoot());

            createNotificationChannel();

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

            int waitingHolidayRequestsCount = holidayRequestDao.getWaitingHolidayRequestsCount();
            adminBinding.holidayRequestsCount.setText(String.valueOf(waitingHolidayRequestsCount));

            if (waitingHolidayRequestsCount > 0 && user.isNotificationsEnabled()) {
                sendNewHolidayRequestNotification(
                        "New Holiday Request",
                        "A new holiday request has been submitted.",
                        HolidayRequestsActivity.class);
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

    private void sendNewHolidayRequestNotification(String title, String message, Class<?> targetActivity) {

        Intent intent = new Intent(this, targetActivity);
        intent.putExtra("current_user_id", currentUserId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (this, "holidayRequestChannel")
                .setSmallIcon(R.drawable.ic_app)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            if (!askForNotificationPermission()) {
                return;
            }
        }
        notificationManager.notify(1, builder.build());
    }
}