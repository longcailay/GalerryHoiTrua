package hcmus.mdsd.fitsealbum;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class AlbumActivity extends Fragment {
    //View
    View album;
    //Mảng tĩnh static, lưu các thư mục chứa ảnh
    public static ArrayList<AlbumFolder> folderAlbum;
    //Constructor mặc định
    public AlbumActivity(){ }
    //Hàm tạo instance
    public static AlbumActivity newInstance() {
        return new AlbumActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Tạo view từ layout activity_album
        album = (View) inflater.inflate(R.layout.activity_album, container, false);
        //Tạo gridview
        final GridView gallery = (GridView) album.findViewById(R.id.albumFolderGridView);
        //Gán giá trị cho gridview
        gallery.setAdapter(new AlbumImageAdapter(AlbumActivity.super.getActivity()));
        //Xác định số cột của gridview, nếu màn hình portrait thì số cột là 1, màn hình landscape là 2
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        if (screenWidth > screenHeight)
            gallery.setNumColumns(2);
        else
            gallery.setNumColumns(1);
        //Gán sự kiện click, click vào ảnh nào sẽ vào giao diện hiển thị toàn bộ các ảnh của thư mục đó
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (null != AlbumActivity.folderAlbum && !AlbumActivity.folderAlbum.isEmpty()) {
                    //Tạo intent gửi đến SubAlbumFolderActivity
                    //Gửi tên thư mục và các file ảnh có trong thư mục đó
                    Intent i = new Intent(AlbumActivity.super.getActivity(), SubAlbumFolderActivity.class);
                    i.putExtra("name",AlbumActivity.folderAlbum.get(position).getName());
                    i.putExtra("allFileName", AlbumActivity.folderAlbum.get(position).GetAllFileName());
                    startActivity(i);
                }
            }
        });
        return album;
    }
}
