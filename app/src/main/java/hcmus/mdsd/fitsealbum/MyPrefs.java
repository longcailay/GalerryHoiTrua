package hcmus.mdsd.fitsealbum;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPrefs {
    SharedPreferences myPrefs;

    public MyPrefs(Context context){
        myPrefs = context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }

    public void setNightModeState(int state){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putInt("NightMode",state);
        editor.commit();
    }

    public int loadNightModeState(){
        Integer state = myPrefs.getInt("NightMode", 0);

        return state;
    }

    public void setPassword(String password){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("Password",password);
        editor.commit();
    }

    public String getPassword(){
        String password = myPrefs.getString("Password","");

        return password;
    }

    public void setPassMode(Integer passMode){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putInt("PassMode",passMode);
        editor.commit();
    }

    public Integer getPassMode(){
        //passMode = 0 là chế độ login, passMode = 1 là chế độ sửa đổi password, passMode = 2 là chế độ xoá password
        Integer passMode = myPrefs.getInt("PassMode",0);

        return passMode;
    }

    void SetNumberOfColumns(Integer[] columns) {
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putInt("columnVertical",columns[0]);
        editor.putInt("columnHorizontal",columns[1]);
        editor.apply();
    }

    Integer[] getNumberOfColumns() {
        Integer[] columns = new Integer[2];
        columns[0] = myPrefs.getInt("columnVertical", 4);
        columns[1] = myPrefs.getInt("columnHorizontal", 6);
        return columns;
    }

    public void setStartHour(int hour){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putInt("StartHour",hour);
        editor.commit();
    }
    public int loadStartHour(){
        Integer hour = myPrefs.getInt("StartHour", 0);
        return hour;
    }
    public void setStartMinute(int minute){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putInt("StartMinute",minute);
        editor.commit();
    }
    public int loadStartMinute(){
        Integer minute = myPrefs.getInt("StartMinute", 0);
        return minute;
    }
    public void setEndHour(int hour){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putInt("EndHour",hour);
        editor.commit();
    }
    public int loadEndHour(){
        Integer hour = myPrefs.getInt("EndHour", 0);
        return hour;
    }
    public void setEndMinute(int minute){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putInt("EndMinute",minute);
        editor.commit();
    }
    public int loadEndMinute(){
        Integer minute = myPrefs.getInt("EndMinute", 0);
        return minute;
    }
}