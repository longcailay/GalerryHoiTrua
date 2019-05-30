package hcmus.mdsd.fitsealbum;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class PicturesActivity extends Fragment {
    //View
    View pictures;
    //Mảng tĩnh chứa danh sách file
    public static ArrayList<String> images;
    //Trạng thái ẩn/hiện của toolbar, 0 là hiện, 1 là ẩn
    static int hideToolbar = 0;
    //MyPrefs
    MyPrefs myPrefs;

    //Tạo instance
    public static PicturesActivity newInstance() {
        return new PicturesActivity();
    }

    private float x1, x2, y1, y2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Lấy view theo id của activity_pictures
        pictures = (View) inflater.inflate(R.layout.activity_pictures, container, false);
        //Khởi tạo myprefs
        myPrefs = new MyPrefs(getContext());
        //Tạo gridview để hiển thị ảnh theo id của galleryGridView
        final GridView gallery = (GridView) pictures.findViewById(R.id.galleryGridView);
        //Xác định số dòng, cột của gridview dựa vào màn hình
        //Màn hình portrait thì 4 cột (mặc định trong layout xml), màn hình landscape thì 6 cột
        //columns[0]: số cột khi màn hình đứng, columns[1]: số cột khi màn hình ngang
        final Integer[] columns = myPrefs.getNumberOfColumns();
        final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        if (screenWidth > screenHeight) {
            gallery.setNumColumns(columns[1]);
        } else {
            gallery.setNumColumns(columns[0]);
        }
        //Dùng hàm setAdapter
        gallery.setAdapter(new ImageAdapter(this.getActivity()));

        //Gán sự kiện click cho mỗi ảnh => xem ảnh full màn hình
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (null != images && !images.isEmpty()) {
                    //Tạo intent gửi đến FullImageActivity
                    Intent i = new Intent(PicturesActivity.super.getActivity(), FullImageActivity.class);
                    //Gửi vị trí ảnh hiện tại, tên ảnh và cả mảng file
                    i.putExtra("id", position);
                    i.putExtra("path", images.get(position));
                    i.putExtra("allPath", images);
                    startActivity(i);
                }
            }
        });
        //Gán sự kiện long click cho mỗi ảnh => tùy chọn multi selection
        gallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View arg1,
                                       int position, long arg3) {
                Toast.makeText(getActivity(), images.get(position) , Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        //Ước tính khoảng cách tối thiểu để xem như thao tác vuốt
        final int MIN_DISTANCE = 150;
        //Gán thao tác chạm vào gridview
        gallery.setOnTouchListener(new AdapterView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //Đánh dấu tọa độ lúc chạm vào màn hình
                        x1 = event.getX();
                        y1 = event.getY();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        //Đánh dấu tọa độ lúc ngón tay rời màn hình
                        x2 = event.getX();
                        y2 = event.getY();
                        //Tính khoảng cách di chuyển giữa 2 thao tác theo trục X và Y
                        float deltaX = x2 - x1;
                        float deltaY = y2 - y1;
                        //Khoảng cách theo trục X lớn hơn hằng số và không vuốt theo đường chéo => thao tác vuốt ngang
                        if (Math.abs(deltaX) >= MIN_DISTANCE && Math.abs(deltaY) <= MIN_DISTANCE / 2) {
                            //Xác định cột hàng ngang hay dọc sẽ thay đổi
                            int index = (screenWidth > screenHeight)? 1:0;
                            //Cử chỉ vuốt trái sang phải => giảm số cột
                            if (x2 > x1) {
                                gallery.setNumColumns(gallery.getNumColumns() - 1);
                                columns[index]--;
                            }
                            //Cử chỉ vuốt phải sang trái => tăng số cột
                            else if (x2 < x1) {
                                gallery.setNumColumns(gallery.getNumColumns() + 1);
                                columns[index]++;
                            }
                            //Đặt lại gridview
                            gallery.setAdapter(null);
                            gallery.setAdapter(new ImageAdapter(PicturesActivity.this.getActivity()));
                            //Lưu lại thông tin số cột
                            myPrefs.SetNumberOfColumns(columns);
                            break;
                        }
                    }
                }
                return false;
            }
        });
        return pictures;
    }
}