package com.shahla.ema;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HolidayRequestAdapter extends RecyclerView.Adapter<HolidayRequestAdapter.HolidayRequestViewHolder>
{
    HolidayRequestDao holidayRequestDao;
    private List<HolidayRequest> holidayRequestList;

    public HolidayRequestAdapter(List<HolidayRequest> holidayRequestList) {
        this.holidayRequestList = holidayRequestList;
    }

    @NonNull
    @Override
    public HolidayRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_request_item, parent,
                false);
        return new HolidayRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayRequestViewHolder holder, @SuppressLint("RecyclerView")
    int position) {
        HolidayRequest request = holidayRequestList.get(position);
        String employeeName = request.getEmployee().getFirstName() + " " + request.getEmployee().getLastName();
        holder.employeeName.setText(employeeName);

        // date format should be like "Jan 1, 2025"
        holder.fromDate.setText(request.getFromDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
        holder.toDate.setText(request.getToDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));

        holder.note.setText(request.getNote());

        // Set button enabled state based on status
        boolean isWaiting = request.getStatus() == HolidayRequest.Status.WAITING;
        holder.approveButton.setEnabled(isWaiting);
        holder.declineButton.setEnabled(isWaiting);

        // Set button background color based on enabled state
        int disabledColor = holder.itemView.getContext().getResources().getColor(R.color.disabledButtonColor);

        holder.approveButton.setBackgroundColor(isWaiting ? holder.itemView.getContext().getResources()
                .getColor(R.color.approveButtonColor) : disabledColor);
        holder.declineButton.setBackgroundColor(isWaiting ? holder.itemView.getContext().getResources()
                .getColor(R.color.declineButtonColor) : disabledColor);

        MaterialCardView cardView = (MaterialCardView) holder.itemView;


        // Set card background color based on status
        int cardColor;
        int cardStrokeColor;
        switch (request.getStatus()) {
            case APPROVED:
                cardColor = holder.itemView.getContext().getResources().getColor(R.color.white);
                cardStrokeColor = holder.itemView.getContext().getResources().getColor(R.color.approveButtonColor);
                break;
            case DECLINED:
                cardColor = holder.itemView.getContext().getResources().getColor(R.color.white);
                cardStrokeColor = holder.itemView.getContext().getResources().getColor(R.color.declineButtonColor);
                break;
            case WAITING:
            default:
                cardColor = holder.itemView.getContext().getResources().getColor(R.color.white);
                cardStrokeColor = holder.itemView.getContext().getResources().getColor(R.color.disabledButtonColor);
                break;
        }

        cardView.setStrokeColor(cardStrokeColor);
        holder.itemView.setBackgroundColor(cardColor);

        holidayRequestDao = new HolidayRequestDao(holder.itemView.getContext());

        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holidayRequestDao.updateHolidayRequestStatus(request.getId(), HolidayRequest.Status.APPROVED);
                request.setStatus(HolidayRequest.Status.APPROVED);

                // update employee's leaves count
                UserDao userDao = new UserDao(holder.itemView.getContext());
                Employee employee = request.getEmployee();

                // different of from_date and to_date
                int new_leaves = request.getToDate().getDayOfYear() - request.getFromDate().getDayOfYear();
                employee.setLeaves(employee.getLeaves() - new_leaves);
                // update the api
                ApiService apiService = new ApiService(holder.itemView.getContext());
                apiService.updateEmployee(employee.getId(), employee, response -> {
                    Log.d("API", "Employee updated successfully");
                    userDao.update(employee);
                    userDao.close();
                }, error -> {
                    Snackbar.make(holder.itemView, "Failed to update employee", Snackbar.LENGTH_SHORT).show();
                });


                notifyItemChanged(position);
            }
        });

        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holidayRequestDao.updateHolidayRequestStatus(request.getId(), HolidayRequest.Status.DECLINED);
                request.setStatus(HolidayRequest.Status.DECLINED);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return holidayRequestList.size();
    }

    static class HolidayRequestViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName;
        TextView fromDate;
        TextView toDate;
        TextView note;
        Button approveButton;
        Button declineButton;

        HolidayRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.employeeName);
            fromDate = itemView.findViewById(R.id.fromDate);
            toDate = itemView.findViewById(R.id.toDate);
            note = itemView.findViewById(R.id.note);
            approveButton = itemView.findViewById(R.id.approveButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }
}