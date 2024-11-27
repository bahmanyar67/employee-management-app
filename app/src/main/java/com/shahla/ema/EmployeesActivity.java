package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.button.MaterialButton;

public class EmployeesActivity extends BaseActivity {

    private AppDatabase db;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);


        // Initialize the Room database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "ema-database").allowMainThreadQueries().build();
        userDao = db.userDao();


        // Set up the toolbar
        setupToolbar();
        setToolbarTitle("Employees List");


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        EmployeeAdapter adapter = new EmployeeAdapter(userDao.getEmployees());
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