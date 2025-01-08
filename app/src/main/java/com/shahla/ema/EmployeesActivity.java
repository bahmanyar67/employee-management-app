package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class EmployeesActivity extends BaseActivity {

    private UserDao userDao;
    private EmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        userDao = new UserDao(this);

        // Set up the toolbar
        setupToolbar("Employees List");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ApiService apiService = new ApiService(this);

        int[] employeesFromDB = userDao.getEmployees().stream().mapToInt(Employee::getId).toArray();

        apiService.getMyEmployees(employeesFromDB, employees -> {
            adapter = new EmployeeAdapter(employees);
            recyclerView.setAdapter(adapter);
        }, error -> {
            Snackbar.make(findViewById(android.R.id.content), "Error: " + error.getMessage(),
                    Snackbar.LENGTH_LONG).show();
        });

//        adapter = new EmployeeAdapter(userDao.getEmployees());
//        recyclerView.setAdapter(adapter);

        MaterialButton addButton = findViewById(R.id.addNewEmployeeButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeesActivity.this, EmployeeActivity.class);
            startActivity(intent);
        });

        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });

    }

}