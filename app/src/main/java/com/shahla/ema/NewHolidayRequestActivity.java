package com.shahla.ema;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.time.temporal.ChronoUnit;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.datepicker.DateValidatorPointForward;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;
import androidx.core.util.Pair;

public class NewHolidayRequestActivity extends BaseActivity {

    private EditText fromDateEditText;
    private EditText toDateEditText;
    private EditText noteEditText;
    private Button submitButton;
    private Button dateRangeButton;

    private Employee employee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_holiday_request);

        setupToolbar("New Holiday Request");

        // get current user id from the intent and get the employee object from the database
        int currentUserId = getIntent().getIntExtra("current_user_id", 0);
        UserDao userDao = new UserDao(this);
        employee = userDao.getEmployeeById(currentUserId);


        fromDateEditText = findViewById(R.id.fromDateEditText);
        toDateEditText = findViewById(R.id.toDateEditText);
        noteEditText = findViewById(R.id.noteEditText);
        submitButton = findViewById(R.id.submitButton);
        dateRangeButton = findViewById(R.id.dateRangeButton);

        dateRangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateRangePicker();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromDateStr = fromDateEditText.getText().toString();
                String toDateStr = toDateEditText.getText().toString();
                String note = noteEditText.getText().toString();

                // Check if any of the fields are empty
                if (fromDateStr.isEmpty() || toDateStr.isEmpty() || note.isEmpty()) {
                    Toast.makeText(NewHolidayRequestActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                LocalDate fromDate = LocalDate.parse(fromDateStr);
                LocalDate toDate = LocalDate.parse(toDateStr);

                //here we are calculating the number of days between the two dates
                //and checking if the employee has enough leaves
                long days = ChronoUnit.DAYS.between(fromDate, toDate) + 1; // +1 to include both start and end dates
                Log.d("NewHolidayRequest", "Days: " + days);
                if (days > employee.getLeaves()) {
                    Toast.makeText(NewHolidayRequestActivity.this, "You don't have enough leaves", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new HolidayRequest object
                HolidayRequest newRequest = new HolidayRequest(employee, fromDate, toDate, note, HolidayRequest.Status.WAITING);

                // Save the new holiday request to the database
                HolidayRequestDao holidayRequestDao = new HolidayRequestDao(NewHolidayRequestActivity.this);
                holidayRequestDao.insert(newRequest);
                holidayRequestDao.close();

                // show a success message and go back to the MyHolidayRequestsActivity
                Toast.makeText(NewHolidayRequestActivity.this, "Request submitted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewHolidayRequestActivity.this, MyHolidayRequestsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDateRangePicker() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select Date Range");
        builder.setCalendarConstraints(constraintsBuilder.build());

        final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();
        picker.show(getSupportFragmentManager(), picker.toString());

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                if (selection != null) {
                    LocalDate fromDate = Instant.ofEpochMilli(selection.first).atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate toDate = Instant.ofEpochMilli(selection.second).atZone(ZoneId.systemDefault()).toLocalDate();

                    fromDateEditText.setText(fromDate.toString());
                    toDateEditText.setText(toDate.toString());
                }
            }
        });
    }
}