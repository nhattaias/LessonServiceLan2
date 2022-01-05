package com.example.lessonservice.bindservice;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.lessonservice.R;
import com.example.lessonservice.service.Mtask;

public class PlayService extends Service implements Mtask.OnMTaskCallBack {
    private final static String TAG = PlayService.class.getName();
    private static final String KEY_UPDATE_MEDIA = "KEY_UPDATE_MEDIA";
    private MediaPlayer mediaPlayer;
    private int totalTime;
    private final MutableLiveData<Integer> currentTime = new MutableLiveData<>();
    private Mtask mtask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ...");
        return new MyBinder();
    }

    public int getTotalTime() {
        return totalTime;
    }

    public MutableLiveData<Integer> getCurrentTime() {
        return currentTime;
    }

    public void play() {
        if (mediaPlayer==null){
            mediaPlayer = MediaPlayer.create(this, R.raw.easy_on_me);
            mtask = new Mtask(KEY_UPDATE_MEDIA,this);
            mtask.start(null);
        }else if (mediaPlayer!=null){
            mediaPlayer.seekTo(currentTime.getValue());
        }
//        mediaPlayer = MediaPlayer.create(this, R.raw.easy_on_me);
        mediaPlayer.start();
        totalTime = mediaPlayer.getDuration();
    }

    public void pause() {
        if (mediaPlayer==null){
            return;
        }else if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    @Override
    public void completeTask(String key, Object o) {

    }

    @Override
    public void updateUI(String key, Object value) {
        currentTime.postValue((Integer) value);
    }

    @Override
    public Object executeTask(String key, Object o, Mtask mtask) {
        while (!mtask.isCancelled()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mtask.requestUpdateUI(mediaPlayer.getCurrentPosition());
            Log.i(TAG, "executeTask: ..."+mediaPlayer.getCurrentPosition());
        }
        return null;
    }

    public void seekTo(int progress) {
        if (mediaPlayer==null)return;
        mediaPlayer.seekTo(progress);
    }


    public class MyBinder extends Binder {
        // IMPORTANT
        // class này giúp đóng gói PlayService để trả về cho PlayerActivity trong phương thức onBind
        public PlayService getService(){
            return PlayService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: ...");
        super.onCreate();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ...");
        return super.onUnbind(intent);
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.i(TAG, "unbindService: ...");
        super.unbindService(conn);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ...");
        mtask.stop();
        super.onDestroy();
    }
}
