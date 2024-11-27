package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView registerButton;

    private AppDatabase db;
    private UserDao userDao;

    // ExecutorService for running background tasks
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        // Initialize the Room database
        db = AppDatabase.getDatabase(this);
        userDao = db.userDao();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (validateEmail(email) && validatePassword(password)) {
                    authenticateUser(email, password);
                } else {
                    // Show error message if email or password format is incorrect
                    Toast.makeText(LoginActivity.this, "Invalid email format or insecure password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // register
        registerButton = findViewById(R.id.registerText);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void authenticateUser(String email, String password) {
        executor.execute(() -> {
            User user = userDao.findUserByEmail(email);
            boolean passwordMatch;
            if (user != null) {
                passwordMatch = Utilities.checkPassword(password, user.getPassword());
            } else {
                passwordMatch = false;
            }

            // Pass the result back to the main thread
            mainThreadHandler.post(() -> {
                if (user != null && passwordMatch) {
                    // If login is successful, start the DashboardActivity
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    // Show error message if email or password is incorrect
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    private boolean validateEmail(String email) {
        // Regular expression for validating email format
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailPattern, email);
    }

    private boolean validatePassword(String password) {
        // Password should be at least 8 characters long and contain at least one digit, one uppercase letter, and one special character
        String passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

}