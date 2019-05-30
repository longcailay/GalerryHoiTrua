package hcmus.mdsd.fitsealbum;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import hcmus.mdsd.fitsealbum.MyPrefs;

public class NightmodeActivity extends AppCompatActivity {
    ActionBar actionBar;
    RadioGroup radNightmode;
    RadioButton radOn,radOff,radAuto,radOption;
    MyPrefs myPrefs;
    TextView txtTimeStart, txtTimeEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        myPrefs = new MyPrefs(this);
        setNightmode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nightmode);
        txtTimeStart = (TextView)findViewById(R.id.txt_timeStart);
        txtTimeStart.setPaintFlags(txtTimeStart.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        ViewStartTime();
        txtTimeEnd = (TextView)findViewById(R.id.txt_timeEnd);
        txtTimeEnd.setPaintFlags(txtTimeEnd.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        ViewEndTime();
        actionBar = getSupportActionBar();
        actionBar.setTitle("Nightmode");
        actionBar.setDisplayHomeAsUpEnabled(true);
        radNightmode = (RadioGroup)findViewById(R.id.rad_Nightmode);
        if(myPrefs.loadNightModeState() == 0){
            radNightmode.check(R.id.radNM_Off);
        }
        else if(myPrefs.loadNightModeState() == 1){
            radNightmode.check(R.id.radNM_On);
        }
        else if(myPrefs.loadNightModeState() == 2){
            radNightmode.check(R.id.radNM_Auto);
        }
        else{
            radNightmode.check(R.id.radNM_Option);
        }
        radOff = (RadioButton)findViewById(R.id.radNM_Off);
        radOn = (RadioButton)findViewById(R.id.radNM_On);
        radAuto = (RadioButton)findViewById(R.id.radNM_Auto);
        radOption = (RadioButton)findViewById(R.id.radNM_Option);
        radOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPrefs.setNightModeState(0);
                restartApp();
            }
        });
        radOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPrefs.setNightModeState(1);
                restartApp();
            }
        });
        radAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPrefs.setNightModeState(2);
                restartApp();
            }
        });
        radOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPrefs.setNightModeState(3);
                restartApp();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; add items to the action bar
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // user clicked a menu-item from ActionBar
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
            return true;
        }
        return false;
    }
    public void restartApp(){
        startActivity(new Intent(getApplicationContext(),NightmodeActivity.class));
        finish();
    }
    public void SetTimeStart(View v){
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                calendar.set(0,0,0,hourOfDay,minute);
                myPrefs.setStartHour(hourOfDay);
                myPrefs.setStartMinute(minute);
                String chosenTime;
                if(minute < 10){
                    String strMinute = "0"+ minute;
                    chosenTime = hourOfDay + ":" + strMinute;
                }
                else{
                    chosenTime = hourOfDay + ":" + minute;
                }
                txtTimeStart.setText(chosenTime);
                restartApp();
            }
        },0,0, true);
        timePickerDialog.show();
    }
    public void SetTimeEnd(View v){
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                calendar.set(0,0,0,hourOfDay,minute);
                myPrefs.setEndHour(hourOfDay);
                myPrefs.setEndMinute(minute);
                String chosenTime;
                if(minute < 10){
                    String strMinute = "0"+ minute;
                    chosenTime = hourOfDay + ":" + strMinute;
                }
                else{
                    chosenTime = hourOfDay + ":" + minute;
                }
                txtTimeEnd.setText(chosenTime);
                restartApp();
            }
        },0,0, true);
        timePickerDialog.show();
    }
    public void ViewStartTime() {
        String timeStart;
        int hour = myPrefs.loadStartHour();
        int minute = myPrefs.loadStartMinute();
        if(minute < 10){
            timeStart = hour + ":0" + minute;
        }
        else{
            timeStart = hour + ":" + minute;
        }
        txtTimeStart.setText(timeStart);
    }
    public void ViewEndTime() {
        String timeEnd;
        int hour = myPrefs.loadEndHour();
        int minute = myPrefs.loadEndMinute();
        if(minute < 10){
            timeEnd = hour + ":0" + minute;
        }
        else{
            timeEnd = hour + ":" + minute;
        }
        txtTimeEnd.setText(timeEnd);
    }
    public boolean CheckTime(int curHour,int curMinute, int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        boolean nightmode = true;
        if(hourStart < hourEnd){
            if(hourStart <= curHour && curHour <= hourEnd){
                if(hourStart == curHour){
                    if(minuteStart > curMinute){
                        nightmode = false;
                    }
                }
                if(hourEnd == curHour){
                    if(curMinute > minuteEnd){
                        nightmode = false;
                    }
                }
            }
            else{
                nightmode = false;
            }
        }
        else if(hourStart == hourEnd){
            if(hourStart == curHour){
                if(minuteStart<minuteEnd){
                    if(minuteStart > curMinute || curMinute > minuteEnd){
                        nightmode = false;
                    }
                }
                else if(minuteStart>minuteEnd){
                    if(minuteEnd <= curMinute && curMinute <= minuteStart){
                        nightmode = false;
                    }
                }
            }
            else{
                nightmode = false;
            }
        }
        else{
            if(hourEnd >= curHour || curHour >= hourStart){
                if(hourStart == curHour){
                    if(minuteStart > curMinute){
                        nightmode = false;
                    }
                }
                if(hourEnd == curHour){
                    if(curMinute > minuteEnd){
                        nightmode = false;
                    }
                }
            }
        }
        return nightmode;
    }
    public void setNightmode(){
        Calendar c = Calendar.getInstance();
        int hour, minute;
        if(myPrefs.loadNightModeState() == 0){
            setTheme(R.style.DayAppTheme);
        }
        else if(myPrefs.loadNightModeState() == 1){
            setTheme(R.style.NightAppTheme);
        }
        else if(myPrefs.loadNightModeState() == 2) {
            hour = c.get(Calendar.HOUR_OF_DAY);
            if(6 <= hour && hour <= 17){
                setTheme(R.style.DayAppTheme);
            }
            else{
                setTheme(R.style.NightAppTheme);
            }
        }
        else{
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            boolean nightmode = CheckTime(hour,minute,myPrefs.loadStartHour(),myPrefs.loadStartMinute(),myPrefs.loadEndHour(),myPrefs.loadEndMinute());
            if(true == nightmode){
                setTheme(R.style.NightAppTheme);
            }
            else{
                setTheme(R.style.DayAppTheme);
            }
        }
    }
}