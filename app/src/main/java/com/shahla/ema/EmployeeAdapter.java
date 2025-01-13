package com.shahla.ema;

import android.app.AlertDialog;
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

    private User currentUser;

    public EmployeeAdapter(List<Employee> employeeList, User currentUser) {
        this.employeeList = employeeList;
        this.filteredEmployeeList = new ArrayList<>(employeeList);
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_item, parent,
                false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = filteredEmployeeList.get(position);
        holder.employeeName.setText(employee.getFirstName() + " " + employee.getLastName());
        holder.employeePosition.setText(employee.getDepartment());

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), EmployeeActivity.class);
            intent.putExtra("employee_id", employee.getId());
            intent.putExtra("current_user_id", currentUser.getId());
            v.getContext().startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Employee")
                    .setMessage("Are you sure you want to delete this employee?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        ApiService apiService = new ApiService(v.getContext());

                        apiService.deleteEmployee(employee.getId(), response -> {
                            Log.d("API", "Employee deleted successfully");

                            // prevent admin from deleting themselves
                            if (employee.getId() >= 100) {
                                UserDao userDao = new UserDao(v.getContext());
                                userDao.delete(employee);
                                userDao.close();
                            }
                            employeeList.remove(employee);
                            filter(""); // Refresh the filtered list
                        }, error -> {
                            Snackbar.make(v, "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
                        });
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
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