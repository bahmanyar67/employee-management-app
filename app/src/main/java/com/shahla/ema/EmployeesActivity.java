package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

public class EmployeesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);


        // Set up the toolbar
        setupToolbar();
        setToolbarTitle("Employees List");


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        EmployeeAdapter adapter = new EmployeeAdapter(UserData.getInstance().getEmployees());
        recyclerView.setAdapter(adapter);


        MaterialButton addButton = findViewById(R.id.addNewEmployeeButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeesActivity.this, EmployeeActivity.class);
                startActivity(intent);
            }
        });

    }
}