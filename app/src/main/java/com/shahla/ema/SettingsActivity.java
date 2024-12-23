package com.shahla.ema;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up the toolbar
        setupToolbar("Settings");

        SwitchCompat notificationSwitch = findViewById(R.id.notificationSwitch);

        // Get the current user id from the intent
        int currentUserId = getIntent().getIntExtra("current_user_id", 0);

        // Get the user object from the database
        UserDao userDao = new UserDao(this);
        User user = userDao.getUserById(currentUserId);

        boolean holidayNotificationsEnabled = user.isNotificationsEnabled();
        notificationSwitch.setChecked(holidayNotificationsEnabled);

        // Set up the switch listeners
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            // Save the setting to the database
            user.setNotificationsEnabled(isChecked);
            userDao.updateNotification(user);

            String message = isChecked ? "Notification is enabled" : "Notification is disabled";
            Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}