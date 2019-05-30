package hcmus.mdsd.fitsealbum;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AlbumFile implements Parcelable, Comparable<AlbumFile> {

    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaType {
    }
    //Đường dẫn của file
    private String mPath;
    //Tên thư mục chứa file
    private String mBucketName;
    //Ngày chỉnh sửa
    private long mDateModified;

    @Override
    public int compareTo(AlbumFile o) {
        long time = o.getDateModified() - getDateModified();
        if (time > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        else if (time < -Integer.MAX_VALUE)
            return -Integer.MAX_VALUE;
        return (int) time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AlbumFile) {
            AlbumFile o = (AlbumFile) obj;
            String inPath = o.getPath();
            if (mPath != null && inPath != null) {
                return mPath.equals(inPath);
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return mPath != null ? mPath.hashCode() : super.hashCode();
    }
    //Lấy đường dẫn của file
    public String getPath() {
        return mPath;
    }
    //Gán đường dẫn cho file
    public void setPath(String path) {
        mPath = path;
    }
    //Lấy tên thư mục chứa file
    void setBucketName(String bucketName) {
        mBucketName = bucketName;
    }
    //Gán tên thư mục chứa cho file
    private long getDateModified() {
        return mDateModified;
    }

    AlbumFile() {

    }
    AlbumFile(Parcel in) {
        mPath = in.readString();
        mBucketName = in.readString();
        mDateModified = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPath);
        dest.writeString(mBucketName);
        dest.writeLong(mDateModified);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlbumFile> CREATOR = new Creator<AlbumFile>() {
        @Override
        public AlbumFile createFromParcel(Parcel in) {
            return new AlbumFile(in);
        }

        @Override
        public AlbumFile[] newArray(int size) {
            return new AlbumFile[size];
        }
    };

}