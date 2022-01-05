package com.example.lessonservice.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.lessonservice.App;
import com.example.lessonservice.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.btDownload.setOnClickListener(view -> downloadFile());
        App.getInstance().getPathDownload().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s==null)return;
                showImage(s);
            }
        });
    }

    private void showImage(String path) {
        binding.ivDownloadImage.setImageURI(Uri.fromFile(new File(path)));
        Intent intent = new Intent(this,DownloadService.class);
        stopService(intent);
    }

    private void downloadFile() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){ //kiểm tra permission
            requestPermissions(new String[]{// câu lệnh yêu cầu quyền * Cần khai báo trong quyền trong Manifest
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET,
            },101);
            return;
        }
        Intent intent = new Intent(this, DownloadService.class); // Câu lệnh để chạy Service
        intent.putExtra(DownloadService.KEY_LINK,binding.edtLink.getText().toString());
        startService(intent);
    }

}