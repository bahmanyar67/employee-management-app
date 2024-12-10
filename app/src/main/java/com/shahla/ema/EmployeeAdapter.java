package com.shahla.ema;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList;
    private List<Employee> filteredEmployeeList;

    public EmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
        this.filteredEmployeeList = new ArrayList<>(employeeList);
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = filteredEmployeeList.get(position);
        holder.employeeName.setText(employee.getFirstName() + " " + employee.getLastName());
        holder.employeePosition.setText(employee.getDepartment());

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EmployeeActivity.class);
            intent.putExtra("employee", employee);
            v.getContext().startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {

            ApiService apiService = new ApiService(v.getContext());

            apiService.deleteEmployee(employee.getId(), response -> {
                Log.d("API", "Employee deleted successfully");
                employeeList.remove(employee);

                // prevent admin from deleting themselves
                if (employee.getUserType() != null && !employee.getUserType().equals("admin")) {
                    UserDao userDao = new UserDao(v.getContext());
                    userDao.delete(employee);
                    userDao.close();
                }
                filter(""); // Refresh the filtered list
            }, error -> {
                Snackbar.make(v, "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            });
        });
    }

    @Override
    public int getItemCount() {
        return filteredEmployeeList.size();
    }

    public void filter(String query) {
        filteredEmployeeList.clear();
        if (query.isEmpty()) {
            filteredEmployeeList.addAll(employeeList);
        } else {
            for (Employee employee : employeeList) {
                if (employee.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                        employee.getLastName().toLowerCase().contains(query.toLowerCase())) {
                    filteredEmployeeList.add(employee);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName;
        TextView employeePosition;
        ImageView editButton;
        ImageView deleteButton;

        EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.employeeName);
            employeePosition = itemView.findViewById(R.id.employeePosition);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}