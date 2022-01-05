package com.example.lessonservice.bindservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lessonservice.databinding.ActivityPlayerBinding;

public class PlayerActivity extends AppCompatActivity {
    private ActivityPlayerBinding binding;
    private PlayService playService;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // Đây là nơi sẽ lấy Service ra để điều khiển sau khi kết nối thành công
            playService = ((PlayService.MyBinder) iBinder).getService();
            // This line is for testing github
        }


        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.btPlay.setOnClickListener(view -> {
            playService.play();
            binding.seekbar.setMax(playService.getTotalTime());
            playService.getCurrentTime().observe(this, integer -> binding.seekbar.setProgress(integer));
        });
        binding.btPause.setOnClickListener(view -> playService.pause());
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playService.seekTo(seekBar.getProgress());
            }
        });
        // Kết nối đến Service
        Intent intent = new Intent(this,PlayService.class);
        boolean isConnected = bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
        Toast.makeText(this,"Service is "+(isConnected?"connected":"not connected"),Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        // Ngắt kết nối với Service
        unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
