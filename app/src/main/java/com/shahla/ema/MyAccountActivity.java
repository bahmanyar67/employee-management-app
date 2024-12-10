package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.shahla.ema.databinding.ActivityMyAccountBinding;

import java.time.LocalDate;

public class MyAccountActivity  extends BaseActivity{

    private ActivityMyAccountBinding binding;

    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        binding = ActivityMyAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up the toolbar
        setupToolbar();
        setToolbarTitle("My Account");


        // get current user id from the intent and get the employee object from the database
        int currentUserId = getIntent().getIntExtra("current_user_id", 0);
        UserDao userDao = new UserDao(this);
        employee = userDao.getEmployeeById(currentUserId);

        setText(binding.employeeFirstName, employee.getFirstName());
        setText(binding.employeeLastName, employee.getLastName());
        setText(binding.employeeEmail, employee.getEmail());
        setText(binding.employeeDepartment, employee.getDepartment());
        setText(binding.annualSalary, String.valueOf(employee.getSalary()));
        setText(binding.employmentDate, employee.getJoiningDate().toString());
        setText(binding.employeeAllowedLeaves, employee.getLeaves().toString());

        // disable editing Department, salary, employment date and allowed leaves
        binding.employeeDepartment.setEnabled(false);
        binding.annualSalary.setEnabled(false);
        binding.employmentDate.setEnabled(false);
        binding.employeeAllowedLeaves.setEnabled(false);


        // Set up the save button to return to EmployeesActivity
        MaterialButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    updateUser();
                }
            }
        });
    }

    private void setText(TextView view, String text) {
        if (text != null) {
            view.setText(text);
        }
    }

    private void updateUser() {
        String firstName = binding.employeeFirstName.getText().toString();
        String lastName = binding.employeeLastName.getText().toString();
        String email = binding.employeeEmail.getText().toString();


        // update if password is not empty
        if (!binding.employeePassword.getText().toString().isEmpty()) {
            String password = binding.employeePassword.getText().toString();
            employee.setPassword(Utilities.hashPassword(password));
        }

        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);

        ApiService apiService = new ApiService(this);
        apiService.updateEmployee(employee.getId(), employee, response -> {
            Log.d("API", "Employee updated successfully");
            UserDao userDao = new UserDao(this);
            userDao.update(employee);
            userDao.close();
            Intent intent = new Intent(MyAccountActivity.this, DashboardActivity.class);
            intent.putExtra("current_user_id", employee.getId());
            startActivity(intent);
            finish();
        }, error -> {
            Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
        });
    }

    private boolean validate() {

        boolean isValid = true;

        isValid &= validateField(binding.employeeFirstName, "First Name is required");
        isValid &= validateField(binding.employeeLastName, "Last Name is required");
        isValid &= validateField(binding.employeeEmail, "Email is required");
        if (employee == null) {
            isValid &= validateField(binding.employeePassword, "Password is required");
        }
        isValid &= validateField(binding.employeeDepartment, "Department is required");
        isValid &= validateField(binding.annualSalary, "Salary is required");
        isValid &= validateField(binding.employmentDate, "Joining Date is required");
        isValid &= validateField(binding.employeeAllowedLeaves, "Allowed Leaves is required");

        if (!Utilities.isValidEmail(binding.employeeEmail.getText().toString())) {
            binding.employeeEmail.setError("Invalid Email Address");
            isValid = false;
        }

        if (!isEmailUnique(binding.employeeEmail.getText().toString())) {
            binding.employeeEmail.setError("Email already exists");
            isValid = false;
        }

        if (employee == null || !binding.employeePassword.getText().toString().isEmpty()) {
            if (!Utilities.isValidPassword(binding.employeePassword.getText().toString())) {
                binding.employeePassword.setError("Password is not secure");
                isValid = false;
            }
        }

        if (!Utilities.isValidDate(binding.employmentDate.getText().toString())) {
            binding.employmentDate.setError("Invalid Date");
            isValid = false;
        }

        return isValid;
    }

    private boolean isEmailUnique(String email) {
        UserDao userDao = new UserDao(this);
        User user = userDao.findUserByEmail(email);
        userDao.close();
        return user == null || (employee != null && user.getId() == employee.getId());
    }

    private boolean validateField(TextInputEditText field, String errorMessage) {
        String text = field.getText().toString().trim();
        if (text.isEmpty()) {
            field.setError(errorMessage);
            return false;
        }
        return true;
    }
}
