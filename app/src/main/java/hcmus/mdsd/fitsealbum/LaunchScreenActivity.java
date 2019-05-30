package hcmus.mdsd.fitsealbum;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

public class LaunchScreenActivity extends AppCompatActivity {
    MyPrefs myPrefs;
    String password;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = new MyPrefs(this);
        password = myPrefs.getPassword();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_screen);
        //Nếu chưa được cấp quyền
        if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            //Đã được cấp quyền
            new BackgroundTask().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                //Trường hợp không cấp quyền, grantResults rỗng, ngược lại sẽ > 0
                //Đã nhận được quyền, thực hiện background task
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new BackgroundTask().execute();
                } else {
                    //Không nhận được quyền, thực hiện finish
                    //Vì chưa vào màn hình chính nên màn hình splash là activity duy nhất bị kết thúc, chương trình đóng
                    this.finish();
                }
            }
        }
    }

    public void onStart() {
        super.onStart();
    }

    private class BackgroundTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            /*  Use this method to load background
             * data that your app needs. */
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
//            Pass your loaded data here using Intent
//            intent.putExtra("data_key", "");

            startActivity(new Intent(LaunchScreenActivity.this, MainActivity.class));


//            if (password.matches("")) {
//                startActivity(new Intent(LaunchScreenActivity.this, MainActivity.class));
//            } else {
//                myPrefs.setPassMode(0);
//                startActivity(new Intent(LaunchScreenActivity.this, PasswordActivity.class));
//            }
            finish();
        }
    }
}