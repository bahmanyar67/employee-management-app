package com.shahla.ema;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Set up the toolbar
        setupToolbar();
        setToolbarTitle("Settings");

        Switch notificationSwitch = findViewById(R.id.holidayNotificationSwitch);
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // show a toast message that the switch is enabled
                Toast.makeText(SettingsActivity.this, "Notification is enabled", Toast.LENGTH_SHORT).show();

            } else {
                // show a toast message that the switch is disabled
                Toast.makeText(SettingsActivity.this, "Notification is disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}