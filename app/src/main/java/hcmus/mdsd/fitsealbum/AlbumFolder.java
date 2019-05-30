package hcmus.mdsd.fitsealbum;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AlbumFolder implements Parcelable {

    //Tên thư mục
    private String name;
    //Danh sách các tập tin
    private ArrayList<AlbumFile> mAlbumFiles = new ArrayList<>();
    //Lấy toàn bộ tên các file
    ArrayList<String> GetAllFileName() {
        ArrayList<String> allFileName = new ArrayList<>();
        for(int i=0;i<mAlbumFiles.size();i++) {
            allFileName.add(mAlbumFiles.get(i).getPath());
        }
        return allFileName;
    }
    //Lấy ảnh mới nhất trong thư mục (làm ảnh preview cho thư mục)
    AlbumFile GetNewestFile(){
        return mAlbumFiles.get(0);
    }
    //Constructor mặc định
    AlbumFolder() {
    }
    //Lấy tên thư mục
    public String getName() {
        return name;
    }
    //Gán tên cho thư mục
    public void setName(String name) {
        this.name = name;
    }
    //Lấy danh sách file
    ArrayList<AlbumFile> getAlbumFiles() {
        return mAlbumFiles;
    }
    //Thêm file vào thư mục
    void addAlbumFile(AlbumFile albumFile) {
        mAlbumFiles.add(albumFile);
    }
    //Lấy số lượng file của thư mục
    int getAlbumFolderSize(){return mAlbumFiles.size();};

    private AlbumFolder(Parcel in) {
        name = in.readString();
        mAlbumFiles = in.createTypedArrayList(AlbumFile.CREATOR);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(mAlbumFiles);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<AlbumFolder> CREATOR = new Creator<AlbumFolder>() {
        @Override
        public AlbumFolder createFromParcel(Parcel in) {
            return new AlbumFolder(in);
        }
        @Override
        public AlbumFolder[] newArray(int size) {
            return new AlbumFolder[size];
        }
    };
}