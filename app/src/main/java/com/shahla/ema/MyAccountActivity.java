package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class MyAccountActivity  extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        // Set up the toolbar
        setupToolbar();
        setToolbarTitle("My Account");


        // Get the User object from the intent
        User user = (User) getIntent().getSerializableExtra("user");

        // Display the employee details
        TextView employeeName = findViewById(R.id.userName);
        employeeName.setText(user.getFirstName());

        TextView employeeEmail = findViewById(R.id.userEmail);
        employeeEmail.setText(user.getEmail());

        TextView employeePosition = findViewById(R.id.userPosition);
        employeePosition.setText(user.getDepartment());

        TextView employeeSalary = findViewById(R.id.annualSalary);
        employeeSalary.setText(String.valueOf(user.getSalary()));

        TextView employeeJoinDate = findViewById(R.id.employmentDate);
        employeeJoinDate.setText(user.getJoiningDate().toString());

//        TextView employeeTakenDays = findViewById(R.id.userTakenDays);
//        employeeTakenDays.setText(user.getTakenDays().toString());

//        TextView employeeRemainingDays = findViewById(R.id.userRemainingDays);
//        employeeRemainingDays.setText(user.getRemainingDays().toString());


        // Set up the save button to return to EmployeesActivity
        MaterialButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to EmployeesActivity
                Intent intent = new Intent(MyAccountActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
