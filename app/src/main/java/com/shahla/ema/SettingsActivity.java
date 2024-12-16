package com.shahla.ema;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up the toolbar
        setupToolbar("Settings");

        SwitchCompat holidayNotificationSwitch = findViewById(R.id.holidayNotificationSwitch);
        SwitchCompat employeeNotificationSwitch = findViewById(R.id.employeeNotificationSwitch);


        boolean holidayNotificationsEnabled = false;
        boolean employeeNotificationsEnabled = false;
        holidayNotificationSwitch.setChecked(holidayNotificationsEnabled);
        employeeNotificationSwitch.setChecked(employeeNotificationsEnabled);

        // Set up the switch listeners
        holidayNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String message = isChecked ? "Notification is enabled" : "Notification is disabled";
            Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
        });

        employeeNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String message = isChecked ? "Notification is enabled" : "Notification is disabled";
            Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }
}