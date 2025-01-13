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

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        binding = ActivityEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userDao = new UserDao(this);

        // Get the Employee object from the intent
        if (getIntent().hasExtra("employee_id")) {
            employee = userDao.getEmployeeById(getIntent().getIntExtra("employee_id", 0));
        } else {
            employee = null;
        }

        // Set up the toolbar
        setupToolbar(employee != null ? "Employee Profile" : "New Employee");

        // Display the employee details if editing
        if (employee != null) {
            populateEmployeeDetails();
        }


        // Set up the save button to return to EmployeesActivity
        MaterialButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.validate(
                        EmployeeActivity.this,
                        binding.employeeFirstName,
                        binding.employeeLastName,
                        binding.employeeEmail,
                        binding.employeePassword,
                        null,
                        binding.employeeDepartment,
                        binding.annualSalary,
                        binding.employmentDate,
                        binding.employeeAllowedLeaves,
                        employee
                ).thenAccept(isValid -> {
                    Log.d("VALIDATION", "isValid: " + isValid);
                    if (isValid) {
                        if (employee != null) {
                            updateEmployee();
                        } else {
                            saveEmployee();
                        }
                    }
                });
            }
        });
    }

    private void goBackToEmployeesActivity() {
        Intent intent = new Intent(EmployeeActivity.this, EmployeesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("current_user_id", getIntent().getIntExtra("current_user_id", 0));
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
            float newSalary = (float) (employee.getSalary() + (employee.getSalary() * percentageIncrease / 100));
            binding.salaryIncreaseNotification.setText(
                    // show new salary with 2 decimal places
                    String.format("Salary increased by %d%% (%d years)  New Salar: £%.2f", percentageIncrease, yearsPassed, newSalary)
            );
            binding.salaryIncreaseNotification.setVisibility(View.VISIBLE);
        } else {
            binding.salaryIncreaseNotification.setVisibility(View.GONE);
        }
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

        Employee employee = new Employee(firstName, lastName, email, encryptedPassword, department,
                salary, joiningDate, leaves);

        ApiService apiService = new ApiService(this);
        apiService.addEmployee(employee, response -> {
            Log.d("API", "Employee added successfully");
            apiService.getEmployeeIdByEmail(email, id -> {
                if (id > 0) {
                    employee.setId(id);
                }
                userDao.insert(employee);
                userDao.close();
                goBackToEmployeesActivity();
            }, error -> {
                Snackbar.make(binding.getRoot(), "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            });
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

    private boolean validateField(TextInputEditText field, String errorMessage) {
        String text = field.getText().toString().trim();
        if (text.isEmpty()) {
            field.setError(errorMessage);
            return false;
        }
        return true;
    }

}