package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.Period;

public class EmployeeActivity extends BaseActivity {

    private User employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        // Set up the toolbar
        setupToolbar();
        setToolbarTitle(employee != null ?  "Employee Profile" : "New Employee");

        // Get the Employee object from the intent
        employee = (User) getIntent().getSerializableExtra("employee");

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
                    Intent intent = new Intent(EmployeeActivity.this, EmployeesActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    private void populateEmployeeDetails() {
        TextView employeeName = findViewById(R.id.employeeName);
        employeeName.setText(employee.getFirstName());

        TextView employeeEmail = findViewById(R.id.employeeEmail);
        employeeEmail.setText(employee.getEmail());

        TextView employeePosition = findViewById(R.id.employeePosition);
        employeePosition.setText(employee.getDepartment());

        TextView employeeSalary = findViewById(R.id.annualSalary);
        employeeSalary.setText(String.valueOf(employee.getSalary()));

        TextView employeeJoinDate = findViewById(R.id.employmentDate);
        employeeJoinDate.setText(employee.getJoiningDate().toString());

//        TextView employeeTakenDays = findViewById(R.id.employeeTakenDays);
//        employeeTakenDays.setText(employee.getTakenDays().toString());

//        TextView employeeRemainingDays = findViewById(R.id.employeeRemainingDays);
//        employeeRemainingDays.setText(employee.getRemainingDays().toString());

        // Calculate the number of years passed since the employment date
        LocalDate currentDate = LocalDate.now();
        LocalDate employmentDate = employee.getJoiningDate();
        Period period = Period.between(employmentDate, currentDate);
        int yearsPassed = period.getYears();

        // Show the notification if at least one year has passed
        TextView salaryIncreaseNotification = findViewById(R.id.salaryIncreaseNotification);
        if (yearsPassed >= 1) {
            int percentageIncrease = yearsPassed * 5;
            salaryIncreaseNotification.setText("Increase Salary by " + percentageIncrease + "%");
            salaryIncreaseNotification.setVisibility(View.VISIBLE);
        }
    }

    private boolean validate() {
        // Get the input values
        String name = ((TextView) findViewById(R.id.employeeName)).getText().toString();
        String email = ((TextView) findViewById(R.id.employeeEmail)).getText().toString();
        String position = ((TextView) findViewById(R.id.employeePosition)).getText().toString();

        TextView salaryTextView = findViewById(R.id.annualSalary);
        String salaryText = salaryTextView.getText().toString();

        double salary;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid salary input", Toast.LENGTH_SHORT).show();
            return false; // or handle the error appropriately
        }


        TextView employmentDateTextView = findViewById(R.id.employmentDate);

        LocalDate employmentDate;
        try {
            employmentDate = LocalDate.parse(employmentDateTextView.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, "Invalid employment date input", Toast.LENGTH_SHORT).show();
            return false; // or handle the error appropriately
        }

        // check the input values
        if (name.isEmpty() || email.isEmpty() || position.isEmpty() || salary == 0 || employmentDate == null) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}