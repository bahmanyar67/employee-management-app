package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.shahla.ema.databinding.ActivityRegisterBinding;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private Button registerButton;

    private Employee employee;
    private Button loginButtonInRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.validate(
                        RegisterActivity.this,
                        binding.employeeFirstName,
                        binding.employeeLastName,
                        binding.employeeEmail,
                        binding.employeePassword,
                        binding.employeePasswordRepeat,
                        binding.employeeDepartment,
                        binding.annualSalary,
                        null,
                        null,
                        null
                ).thenAccept(valid -> {
                    if (valid) {
                        saveEmployee();
                    }
                });
            }
        });


        loginButtonInRegister = findViewById(R.id.loginButtonInRegister);
        loginButtonInRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void saveEmployee() {
        String firstName = binding.employeeFirstName.getText().toString();
        String lastName = binding.employeeLastName.getText().toString();
        String email = binding.employeeEmail.getText().toString();
        String password = binding.employeePassword.getText().toString();
        String encryptedPassword = Utilities.hashPassword(password);
        String department = binding.employeeDepartment.getText().toString();
        double salary = Double.parseDouble(binding.annualSalary.getText().toString());
        LocalDate joiningDate = LocalDate.now();
        int leaves = 30;  // New employees get 30 leaves by default

        // check if the employee is already in the database (Email should be unique)

        Employee employee = new Employee(firstName, lastName, email, encryptedPassword, department,
                salary, joiningDate, leaves);

        ApiService apiService = new ApiService(this);
        apiService.addEmployee(employee, response -> {
            Log.d("API", "Employee added successfully");
            apiService.getEmployeeIdByEmail(email, id -> {
                if (id > 0) {
                    employee.setId(id);
                }
                UserDao userDao = new UserDao(this);
                userDao.insert(employee);
                userDao.close();
                goBackLogin();
                Toast.makeText(RegisterActivity.this, "Registration successful",
                        Toast.LENGTH_SHORT).show();
            }, error -> {
                Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar
                        .LENGTH_LONG).show();
            });
        }, error -> {
            Toast.makeText(RegisterActivity.this, "There is a problem with registration",
                    Toast.LENGTH_SHORT).show();
            Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
        });
    }

    private void goBackLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}