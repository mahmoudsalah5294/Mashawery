package com.mnm.mashawery;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DialogPicker {
    public static String time;
    public  static String date;
    Calendar calendar = Calendar.getInstance();
    public void datePickerDialog(Context context, int year, int month, int day){
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        if (year<calendar.get(Calendar.YEAR)){
                            datePickerDialog( context,  year,  month,  day);
                            Toast.makeText(context, "This Date has Already Passed", Toast.LENGTH_SHORT).show();
                        }
                        else if(month<calendar.get(Calendar.MONTH)) {
                            datePickerDialog( context,  year,  month,  day);
                            Toast.makeText(context, "This Date has Already Passed", Toast.LENGTH_SHORT).show();
                        }
                        else if(day<calendar.get(Calendar.DAY_OF_MONTH)) {
                            datePickerDialog( context,  year,  month,  day);
                            Toast.makeText(context, "This Date has Already Passed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            month = month + 1;
                            Toast.makeText(context, day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
                            date = day + "/" + month + "/" + year;
                        }
                    }
                },year,month,day);
       datePickerDialog.show();
    }

    public void timePickerDialog(Context context, int hour, int min, boolean is24Hour){
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(hourOfDay<calendar.get(Calendar.HOUR_OF_DAY)){
                    timePickerDialog(context,hour,min,false);
                    Toast.makeText(context, "This Time has Already Passed", Toast.LENGTH_SHORT).show();
                }
                else if(minute<calendar.get(Calendar.MINUTE)){
                    timePickerDialog(context,hour,min,false);
                    Toast.makeText(context, "This Time has Already Passed", Toast.LENGTH_SHORT).show();
                }
                else{
                Toast.makeText(context, hourOfDay+":"+minute, Toast.LENGTH_SHORT).show();
                time=hourOfDay+":"+minute;
                }
            }
        },hour,min,is24Hour);
        timePickerDialog.show();
    }

}
