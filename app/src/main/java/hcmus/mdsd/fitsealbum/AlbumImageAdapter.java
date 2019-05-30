package hcmus.mdsd.fitsealbum;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AlbumImageAdapter extends BaseAdapter {

    /**
     * Class ViewHolder
     */
    private class ViewHolder {
        //TextView hiển thị tên thư mục
        private TextView textView;
        //ImageView hiển thị hình ảnh của thư mục
        public ImageView imageView;
    }
    //Activity
    private Activity activity;
    //Layout inflater
    private LayoutInflater mInflater;
    /**
     * Constructor
     * @param localContext ngữ cảnh
     */
    AlbumImageAdapter(Activity localContext) {
        //Gán vào activity hiện tại
        activity = localContext;
        //Gọi hàm getAllMedia để lấy toàn bộ dữ liệu thư mục, đưa vào biến static folderAlbum của class AlbumActivity
        AlbumActivity.folderAlbum = getAllMedia(activity);
        //Lấy layout từ activity
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     *
     * @return Các hàm mặc định, không dùng trực tiếp
     */
    public int getCount() { return AlbumActivity.folderAlbum.size(); }
    public Object getItem(int position) { return position; }
    public long getItemId(int position) { return position; }

    /**
     * Hàm thực hiện load hình ảnh và tên thư mục lên giao diện
     * @param position Vị trí của ảnh
     * @param convertView convertView
     * @param parent parent
     * @return View
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            //Tìm convertView dựa vào layout chứa 2 item hình ảnh và chữ
            convertView = mInflater.inflate(R.layout.album_folder_image_item, parent, false);
            //Tìm và gán TextView bằng id
            mViewHolder.textView = (TextView) convertView.findViewById(R.id.nameAlbumFolder);
            //Tìm và gán ImageView bằng id
            mViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageAlbumFolder);
            //Căn chỉnh ảnh được load vào imageView theo kiểu Center Crop
            mViewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //Đoạn code để xác định kích thước ảnh của album
            //Nếu màn hình portrait, chỉ hiện 1 cột
            //Nếu màn hình landscape, hiển thị 2 cột
            int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
            int sizeOfImage = screenWidth;
            if (screenWidth > screenHeight) {
                sizeOfImage = screenWidth / 2;
            }
            //Set param cho imageView, ảnh là hình chữ nhật
            mViewHolder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(sizeOfImage,sizeOfImage/2));
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        //dùng Glide để load ảnh thumbnail vào imageView
        //Ảnh tại vị trí position của folderAlbum, load bằng đường dẫn, ảnh theo kiểu Center Crop
        Glide.with(activity).load(AlbumActivity.folderAlbum.get(position).GetNewestFile().getPath())
                .apply(new RequestOptions().placeholder(null).centerCrop())
                .into(mViewHolder.imageView);
        //Lấy tên của thư mục
        String title = AlbumActivity.folderAlbum.get(position).getName();
        //Hiện số lượng item trong thư mục, nếu bằng 0 thì thư mục không được load vào
        if (AlbumActivity.folderAlbum.get(position).getAlbumFolderSize()==1)
            title += "\n(1 item)";
        else
            title += "\n(" + String.valueOf(AlbumActivity.folderAlbum.get(position).getAlbumFolderSize()) + " items)";
        mViewHolder.textView.setText(title);
        return convertView;
    }

    /**
     * Các đối tượng cần lấy trong của 1 ảnh
     * DATA: đường dẫn của ảnh
     * BUCKET_DISPLAY_NAME: thư mục chứa ảnh
     * DATE_MODIFIED: ngày chỉnh sửa ảnh
     */
    private static final String[] IMAGES = {
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
    };

    /**
     * Lấy tất cả dữ liệu ảnh để phân loại theo album
     * @param activity Activity
     * @return Mảng thư mục
     */
    private ArrayList<AlbumFolder> getAllMedia(Activity activity) {
        //Sử dụng hashmap lưu giá trị theo cặp
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        //Dùng con trỏ để đọc dữ liệu từ media (cần cấp quyền), đọc các thông tin quy định trong IMAGES, sắp xếp theo ngày sửa giảm dần
        Cursor cursor = activity.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGES, null, null, "DATE_MODIFIED DESC");
        if (cursor != null) {
            //Duyệt qua từng ảnh
            while (cursor.moveToNext()) {
                //Biến lưu tên của thư mục chứa ảnh đang duyệt
                String bucketName = cursor.getString(1);
                //Tạo file ảnh mới
                AlbumFile imageFile = new AlbumFile();
                //Lưu đường dẫn của file ảnh
                imageFile.setPath(cursor.getString(0));
                //Lưu tên của file ảnh
                imageFile.setBucketName(bucketName);
                //Tạo 1 thư mục mới từ tên thư mục chứa ảnh hiện tại
                AlbumFolder albumFolder = albumFolderMap.get(bucketName);
                //Thư mục không rỗng, chỉ thêm ảnh khác vào
                if (albumFolder != null)
                    albumFolder.addAlbumFile(imageFile);
                //Nếu thư mục mới
                else {
                    //Khởi tạo lại thư mục
                    albumFolder = new AlbumFolder();
                    //Gán tên cho thư mục
                    albumFolder.setName(bucketName);
                    //Đưa file đang duyệt vào thư mục
                    albumFolder.addAlbumFile(imageFile);
                    //Đưa vào hashmap
                    albumFolderMap.put(bucketName, albumFolder);
                }
            }
            cursor.close();
        }
        //Tạo mảng kết quả
        ArrayList<AlbumFolder> albumFolders = new ArrayList<>();
        //Duyệt trong hashmap, lấy những tên của thư mục đưa vào mảng mới
        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getAlbumFiles());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }
}