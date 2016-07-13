package com.example.fbulou.materialdatetimepicker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    /*
        https://github.com/wdullaer/MaterialDateTimePicker
    */

    Button btnSetDate, btnSetTime;
    TextView dateTextView, timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dateTextView = (TextView) findViewById(R.id.dateTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        btnSetDate = (Button) findViewById(R.id.btnSetDate);
        btnSetTime = (Button) findViewById(R.id.btnSetTime);

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDatePickerDialog();
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTimePickerDialog();
            }
        });
    }

    private void createDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        //Same for tpd
        dpd.setAccentColor(Color.parseColor("#009688"));
        // dpd.setTitle("mTitle");
        // dpd.setMaxDate(now);
        // dpd.setMinDate(Calendar calenderObj);

        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    private void createTimePickerDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);

        tpd.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String date = "You picked the following date: " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        dateTextView.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        String time = "You picked the following time: " + hourOfDay + "h" + minute;
        timeTextView.setText(time);
    }
}
