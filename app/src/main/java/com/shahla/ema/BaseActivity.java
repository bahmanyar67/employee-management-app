package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BaseActivity extends AppCompatActivity {

    protected UserDao userDao;

    protected int currentUserId;

    protected User currentUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUserId = getIntent().getIntExtra("current_user_id", 0);
        if (currentUserId != 0) {
            userDao = new UserDao(this);
            currentUser = userDao.getUserById(currentUserId);
        }
    }

    protected void setupToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Disable default title
        }

        TextView toolbarTitle = findViewById(R.id.toolbarTitle);
        if (getSupportActionBar() != null) {
            toolbarTitle.setText(title);
        }

        ImageView profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileMenu(v);
            }
        });

        setupBackButton();
    }

    protected void setupBackButton() {
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showProfileMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.settings) {
                    Intent intent = new Intent(BaseActivity.this, SettingsActivity.class);
                    intent.putExtra("current_user_id", currentUserId);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.dashboardMenuItem) {
                    Intent intent = new Intent(BaseActivity.this, DashboardActivity.class);
                    intent.putExtra("current_user_id", currentUserId);
                    startActivity(intent);
                    return true;

                } else if (itemId == R.id.logout) {
                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}