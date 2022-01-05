package com.example.lessonservice.service;
//Service Activity đều chạy trên MainThread


import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.lessonservice.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadService extends Service implements Mtask.OnMTaskCallBack {
    private final static String TAG = DownloadService.class.getName();
    public final static String KEY_LINK= "KEY_LINK";
    private static final String KEY_DOWNLOAD = "KEY_DOWNLOAD";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ...");
        String link = intent.getStringExtra(KEY_LINK);
        startDownload(link);
        return super.onStartCommand(intent, flags, startId);
//        return START_NOT_STICKY; là onDestroy ngay khi app bị tắt
//        return START_STICKY; là tỉnh dậy lần nữa xem đã xong việc chưa rồi onDestroy
//        Nếu muốn không bị kill ngay khi đóng app thì cần Start service foreground by Notification
    }

    private void startDownload(String link) {
        Log.i(TAG, "startDownload: ..."+link);
        Mtask mtask = new Mtask(KEY_DOWNLOAD,this);
        mtask.start(link);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ...");
    }


    @Override
    public Object executeTask(String key, Object data, Mtask mtask) {
        try {
            String link = (String) data;// Ép kiểu data về String để đọc
            String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath(); //Lấy path của folder External
            path+=new File(link).getName();// Tạo 1 file theo path và đặt tên theo tên của File muốn download

            URLConnection conn = new URL(link).openConnection(); // tạo cổng kết nối
            InputStream in = conn.getInputStream(); // mở đọc file lấy dữ liệu theo cổng kết nối internet
            FileOutputStream out = new FileOutputStream(path); // ghi dữ liệu theo vị trị path đã tạo ở trên

            byte[] buff = new byte[1024]; // tạo số lượng byte mỗi lần ghi
            int len = in.read(buff);// dùng inputStream đọc file lần đầu theo số lượng đã tạo ở trên

            while (len>0){// dùng while đọc đến khi len không còn gì
                out.write(buff,0,len);//ghi file - truyền vào số lượng byte mỗi lần đọc,đọc cho đến khi hết,số lượng còn lại
                len=in.read(buff); //truyền vào dữ liệu vào buff và số lượng byte vào len
            }
            out.close();
            in.close();
            Log.i(TAG, "executeTask: ...done");
            return path;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void completeTask(String key, Object o) {
        Log.e(TAG, "completeTask: ..."+(((String) o)==null?"fail":o ));
        // Tự kill sau khi hoàn thành
        // Báo cho activity để hiển thị giao diện
        // stopSelf();
        App.getInstance().getPathDownload().setValue((String) o);

    }
}
