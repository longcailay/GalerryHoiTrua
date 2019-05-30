package hcmus.mdsd.fitsealbum;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import static hcmus.mdsd.fitsealbum.FavoriteActivity.favoriteImages;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolBar;
    private MyPrefs myPrefs;

    FragmentTransaction ft;
    PicturesActivity pictures;
    AlbumActivity album;
 //  CloudStorageActivity cloud;
    FavoriteActivity favorite;

    private int codeOfFragment;
    public static String _name_cloud ="";

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("defaultFragment", codeOfFragment);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = new MyPrefs(this);

        setNightmode();
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        String json = sharedPreferences.getString("savedFavoriteImages","");

        SharedPreferences sharedPreferences2 = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        MainActivity._name_cloud = sharedPreferences2.getString("name_cloud","");

        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        favoriteImages = gson.fromJson(json, type);

        setContentView(R.layout.activity_main);

        toolBar = (Toolbar) findViewById(R.id.nav_actionBar);
        setSupportActionBar(toolBar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState!=null)
        {
            int id = savedInstanceState.getInt("defaultFragment");
            switch(id) {
                case R.id.nav_album:{
                    toolBar.setTitle("Album");
                    ft = getFragmentManager().beginTransaction();
                    album = AlbumActivity.newInstance();
                    ft.replace(R.id.content_frame, album);
                    ft.commit();
                    break;
                }
                case R.id.nav_favorite:{
                    toolBar.setTitle("Favorite");
                    ft = getFragmentManager().beginTransaction();
                    favorite = FavoriteActivity.newInstance();
                    ft.replace(R.id.content_frame, favorite);
                    ft.commit();
                    break;
                }
//                case R.id.nav_cloud:{
//                    toolBar.setTitle("Cloud Storage");
//                    ft = getFragmentManager().beginTransaction();
//                    cloud = CloudStorageActivity.newInstance();
//                    ft.replace(R.id.content_frame, cloud);
//                    ft.commit();
//                    break;
//                }
                default:{
                    toolBar.setTitle("Images");
                    ft = getFragmentManager().beginTransaction();
                    pictures = PicturesActivity.newInstance();
                    ft.replace(R.id.content_frame, pictures);
                    ft.commit();
                    break;
                }
            }
            codeOfFragment = id;
        }
        else {
            ft = getFragmentManager().beginTransaction();
            pictures = PicturesActivity.newInstance();
            ft.replace(R.id.content_frame, pictures);
            ft.commit();
            toolBar.setTitle("Images");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; add items to the action bar
        getMenuInflater().inflate(R.menu.status_bar, menu);
        return true;
    }
 //dung de click vo option de no show ra side bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // user clicked a menu-item from ActionBar
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_search) {
            // perform SEARCH operations...
            return true;
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pictures) {
            toolBar.setTitle("Images");
            ft = getFragmentManager().beginTransaction();
            pictures = PicturesActivity.newInstance();
            ft.replace(R.id.content_frame, pictures);
            ft.commit();
            codeOfFragment = R.id.nav_pictures;
        }
        else if (id == R.id.nav_album) {
            toolBar.setTitle("Album");
            ft = getFragmentManager().beginTransaction();
            album = AlbumActivity.newInstance();
            ft.replace(R.id.content_frame, album);
            ft.commit();
            codeOfFragment = R.id.nav_album;
        }
        else if (id == R.id.nav_favorite) {
            toolBar.setTitle("Favorite");

            ft = getFragmentManager().beginTransaction();
            favorite = FavoriteActivity.newInstance();
            ft.replace(R.id.content_frame, favorite);
            ft.commit();
            codeOfFragment = R.id.nav_favorite;
        }
//        else if (id == R.id.nav_cloud) {
////            toolBar.setTitle("Cloud Storage");
////            ft = getFragmentManager().beginTransaction();
////            cloud = CloudStorageActivity.newInstance();
////            ft.replace(R.id.content_frame, cloud);
////            ft.commit();
////            codeOfFragment = R.id.nav_cloud;
////        }
        else if (id == R.id.nav_settings) {
            startActivity(new Intent(MainActivity.this,SettingsActivity.class));
            finish();
        }
//        else if (id == R.id.nav_feedback) {
//            startActivity(new Intent(MainActivity.this,FeedbackActivity.class));
//        }
        else if (id == R.id.nav_help) {
            startActivity(new Intent(MainActivity.this,HelpActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
 //night mode
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
            setTheme(R.style.DayNoActionBarTheme);
        }
        else if(myPrefs.loadNightModeState() == 1){
            setTheme(R.style.NightNoActionBarTheme);
        }
        else if(myPrefs.loadNightModeState() == 2) {
            hour = c.get(Calendar.HOUR_OF_DAY);
            if(6 <= hour && hour <= 17){
                setTheme(R.style.DayNoActionBarTheme);
            }
            else{
                setTheme(R.style.NightNoActionBarTheme);
            }
        }
        else{
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            boolean nightmode = CheckTime(hour,minute,myPrefs.loadStartHour(),myPrefs.loadStartMinute(),myPrefs.loadEndHour(),myPrefs.loadEndMinute());
            if(true == nightmode){
                setTheme(R.style.NightNoActionBarTheme);
            }
            else{
                setTheme(R.style.DayNoActionBarTheme);
            }
        }
    }
}
