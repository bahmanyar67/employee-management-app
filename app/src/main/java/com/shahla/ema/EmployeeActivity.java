package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.shahla.ema.databinding.ActivityEmployeeBinding;

import java.time.LocalDate;
import java.time.Period;

public class EmployeeActivity extends BaseActivity {

    private ActivityEmployeeBinding binding;

    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        binding = ActivityEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Set up the toolbar
        setupToolbar();
        setToolbarTitle(employee != null ? "Employee Profile" : "New Employee");

        // Get the Employee object from the intent
        employee = (Employee) getIntent().getSerializableExtra("employee");

        // Display the employee details if editing
        if (employee != null) {
            populateEmployeeDetails();
        }

        // Set up the save button to return to EmployeesActivity
        MaterialButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (employee != null) {
                        updateEmployee();
                    } else {
                        saveEmployee();
                    }
                }
            }
        });
    }

    private void goBackToEmployeesActivity() {
        Intent intent = new Intent(EmployeeActivity.this, EmployeesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setText(TextView view, String text) {
        if (text != null) {
            view.setText(text);
        }
    }

    private void populateEmployeeDetails() {
        if (employee == null) return;

        setText(binding.employeeFirstName, employee.getFirstName());
        setText(binding.employeeLastName, employee.getLastName());
        setText(binding.employeeEmail, employee.getEmail());
        setText(binding.employeeDepartment, employee.getDepartment());
        setText(binding.annualSalary, String.valueOf(employee.getSalary()));
        setText(binding.employmentDate, employee.getJoiningDate().toString());
        setText(binding.employeeAllowedLeaves, employee.getLeaves().toString());

        // Calculate the number of years passed since the employment date
        showSalaryIncreaseNotification();
    }

    private void showSalaryIncreaseNotification() {
        LocalDate currentDate = LocalDate.now();
        LocalDate employmentDate = employee.getJoiningDate();
        Period period = Period.between(employmentDate, currentDate);
        int yearsPassed = period.getYears();

        if (yearsPassed >= 1) {
            int percentageIncrease = yearsPassed * 5;
            binding.salaryIncreaseNotification.setText(
                    String.format("Increase Salary by %d%%", percentageIncrease)
            );
            binding.salaryIncreaseNotification.setVisibility(View.VISIBLE);
        } else {
            binding.salaryIncreaseNotification.setVisibility(View.GONE);
        }
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

    // store the employee details in the database
    private void saveEmployee() {
        String firstName = binding.employeeFirstName.getText().toString();
        String lastName = binding.employeeLastName.getText().toString();
        String email = binding.employeeEmail.getText().toString();
        String password = binding.employeePassword.getText().toString();
        String encryptedPassword = Utilities.hashPassword(password);
        String department = binding.employeeDepartment.getText().toString();
        double salary = Double.parseDouble(binding.annualSalary.getText().toString());
        LocalDate joiningDate = LocalDate.parse(binding.employmentDate.getText().toString());
        int leaves = Integer.parseInt(binding.employeeAllowedLeaves.getText().toString());

        // TODO: check if the employee is already in the database (Email should be unique)

        Employee employee = new Employee(firstName, lastName, email, encryptedPassword, department, salary, joiningDate, leaves);

        ApiService apiService = new ApiService(this);
        apiService.addEmployee(employee, response -> {
            Log.d("API", "Employee added successfully");
            UserDao userDao = new UserDao(this);
            userDao.insert(employee);
            userDao.close();
            goBackToEmployeesActivity();
        }, error -> {
            Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
        });
    }

    private void updateEmployee() {
        String firstName = binding.employeeFirstName.getText().toString();
        String lastName = binding.employeeLastName.getText().toString();
        String email = binding.employeeEmail.getText().toString();
        String department = binding.employeeDepartment.getText().toString();
        double salary = Double.parseDouble(binding.annualSalary.getText().toString());
        LocalDate joiningDate = LocalDate.parse(binding.employmentDate.getText().toString());
        int leaves = Integer.parseInt(binding.employeeAllowedLeaves.getText().toString());

        // update if password is not empty
        if (!binding.employeePassword.getText().toString().isEmpty()) {
            String password = binding.employeePassword.getText().toString();
            employee.setPassword(Utilities.hashPassword(password));
        }

        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setDepartment(department);
        employee.setSalary(salary);
        employee.setJoiningDate(joiningDate);
        employee.setLeaves(leaves);


        ApiService apiService = new ApiService(this);
        apiService.updateEmployee(employee.getId(), employee, response -> {
            Log.d("API", "Employee updated successfully");
            UserDao userDao = new UserDao(this);
            userDao.update(employee);
            userDao.close();
            goBackToEmployeesActivity();
        }, error -> {
            Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
        });
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