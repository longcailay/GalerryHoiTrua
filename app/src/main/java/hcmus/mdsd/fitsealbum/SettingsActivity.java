package hcmus.mdsd.fitsealbum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    MyPrefs myPrefs;
    Button btnAbout, btnSetPassword, btnDeletePassword, btnNightmode;
    ActionBar actionBar;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = new MyPrefs(this);
        password = myPrefs.getPassword();
        setNightmode();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setTitle("Settings");

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

//        btnSetPassword = findViewById(R.id.btn_setPassword);
//        btnSetPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myPrefs.setPassMode(1);
//                if (password.matches("")) {
//                    startActivity(new Intent(SettingsActivity.this, SetPasswordActivity.class));
//                    finish();
//                } else {
//                    startActivity(new Intent(SettingsActivity.this, PasswordActivity.class));
//                }
//            }
//        });
//
//        btnDeletePassword = findViewById(R.id.btn_deletePassword);
//        btnDeletePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myPrefs.setPassMode(2);
//                if (password.matches("")) {
//                    Toast.makeText(SettingsActivity.this, "Password hasn't been set", Toast.LENGTH_SHORT).show();
//                } else {
//                    startActivity(new Intent(SettingsActivity.this, PasswordActivity.class));
//                    finish();
//                }
//            }
//        });

        btnNightmode = findViewById(R.id.btn_nightMode);
        btnNightmode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, NightmodeActivity.class));
                finish();
            }
        });

        btnAbout = findViewById(R.id.btn_about);
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // user clicked a menu-item from ActionBar
        int id = item.getItemId();

        if (id == android.R.id.home) {
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            finish();
            return true;
        }
        return false;
    }

    public boolean CheckTime(int curHour, int curMinute, int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        boolean nightmode = true;
        if (hourStart < hourEnd) {
            if (hourStart <= curHour && curHour <= hourEnd) {
                if (hourStart == curHour) {
                    if (minuteStart > curMinute) {
                        nightmode = false;
                    }
                }
                if (hourEnd == curHour) {
                    if (curMinute > minuteEnd) {
                        nightmode = false;
                    }
                }
            } else {
                nightmode = false;
            }
        } else if (hourStart == hourEnd) {
            if (hourStart == curHour) {
                if (minuteStart < minuteEnd) {
                    if (minuteStart > curMinute || curMinute > minuteEnd) {
                        nightmode = false;
                    }
                } else if (minuteStart > minuteEnd) {
                    if (minuteEnd <= curMinute && curMinute <= minuteStart) {
                        nightmode = false;
                    }
                }
            } else {
                nightmode = false;
            }
        } else {
            if (hourEnd >= curHour || curHour >= hourStart) {
                if (hourStart == curHour) {
                    if (minuteStart > curMinute) {
                        nightmode = false;
                    }
                }
                if (hourEnd == curHour) {
                    if (curMinute > minuteEnd) {
                        nightmode = false;
                    }
                }
            }
        }
        return nightmode;
    }

    public void setNightmode() {
        Calendar c = Calendar.getInstance();
        int hour, minute;
        if (myPrefs.loadNightModeState() == 0) {
            setTheme(R.style.DayAppTheme);
        } else if (myPrefs.loadNightModeState() == 1) {
            setTheme(R.style.NightAppTheme);
        } else if (myPrefs.loadNightModeState() == 2) {
            hour = c.get(Calendar.HOUR_OF_DAY);
            if (6 <= hour && hour <= 17) {
                setTheme(R.style.DayAppTheme);
            } else {
                setTheme(R.style.NightAppTheme);
            }
        } else {
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            boolean nightmode = CheckTime(hour, minute, myPrefs.loadStartHour(), myPrefs.loadStartMinute(), myPrefs.loadEndHour(), myPrefs.loadEndMinute());
            if (nightmode) {
                setTheme(R.style.NightAppTheme);
            } else {
                setTheme(R.style.DayAppTheme);
            }
        }
    }
}
