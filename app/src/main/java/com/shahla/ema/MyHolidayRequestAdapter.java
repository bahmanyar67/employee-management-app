package com.shahla.ema;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyHolidayRequestAdapter extends RecyclerView.Adapter<MyHolidayRequestAdapter.HolidayRequestViewHolder> {

    private List<HolidayRequest> holidayRequestList;

    public MyHolidayRequestAdapter(List<HolidayRequest> holidayRequestList) {
        this.holidayRequestList = holidayRequestList;
    }

    @NonNull
    @Override
    public HolidayRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_holiday_request_item, parent, false);
        return new HolidayRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayRequestViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HolidayRequest request = holidayRequestList.get(position);

        // date format should be like "Jan 1, 2025"
        holder.fromDate.setText(request.getFromDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
        holder.toDate.setText(request.getToDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")));

        holder.note.setText(request.getNote());

        // Set button enabled state based on status
        holder.status.setText(request.getStatus().toString());
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
                cardStrokeColor = holder.itemView.getContext().getResources().getColor(R.color.blueColor);
                break;
        }

        cardView.setStrokeColor(cardStrokeColor);
        holder.itemView.setBackgroundColor(cardColor);
        holder.status.setTextColor(cardStrokeColor);
    }

    @Override
    public int getItemCount() {
        return holidayRequestList.size();
    }

    static class HolidayRequestViewHolder extends RecyclerView.ViewHolder {
        TextView fromDate;
        TextView toDate;
        TextView note;
        TextView status;
        Button approveButton;
        Button declineButton;

        HolidayRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            fromDate = itemView.findViewById(R.id.fromDate);
            toDate = itemView.findViewById(R.id.toDate);
            note = itemView.findViewById(R.id.note);
            status = itemView.findViewById(R.id.status);
            approveButton = itemView.findViewById(R.id.approveButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }
}