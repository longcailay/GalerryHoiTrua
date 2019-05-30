package hcmus.mdsd.fitsealbum;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Calendar;

public class SubAlbumFolderActivity extends AppCompatActivity{

    //MyPrefs
    MyPrefs myPrefs;
    //Thanh actionbar, dùng để back ra ngoài
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Đặt lại nền tùy thuộc cài đặt sáng hay tối
        myPrefs = new MyPrefs(this);
        setNightmode();

        super.onCreate(savedInstanceState);
        //View mặc định là activity_pictures
        setContentView(R.layout.activity_pictures);
        //Tạo intent để nhận nội dung
        Intent i = getIntent();
        //Tiêu đề thư mục cũng là tên thư mục
        String title = Objects.requireNonNull(i.getExtras()).getString("name");
        //Tạo mảng, lấy toàn bộ danh sách file gửi từ intent
        ArrayList<String> arrayList = i.getExtras().getStringArrayList("allFileName");
        //Gán thanh actionBar
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(title);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Tạo gridview để chứa ảnh
        GridView gallery = findViewById(R.id.galleryGridView);
        //Gọi hàm setAdapter, truyền vào class gửi và danh sách tập tin
        gallery.setAdapter(new ImageAdapter(SubAlbumFolderActivity.this, arrayList));
        //Lựa chọn số cột để hiển thị, mặc định là 4 (trong layout), màn hình ngang thì 6 cột
        int screenWidth =  Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight =  Resources.getSystem().getDisplayMetrics().heightPixels;
        if (screenWidth > screenHeight)
        {
            gallery.setNumColumns(6);
        }
        //Gán sự kiện click cho mỗi ảnh
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //if (null != PicturesActivity.images && !PicturesActivity.images.isEmpty()) {
                    Intent i = new Intent(SubAlbumFolderActivity.this, FullImageActivity.class);
                    i.putExtra("id", position);
                    i.putExtra("path", PicturesActivity.images.get(position));
                    i.putExtra("allPath", PicturesActivity.images);
                    startActivity(i);
                //}
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Nếu click vào nút back
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return false;
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
