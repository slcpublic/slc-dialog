package com.example.slc_dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.SlcBottomSheetAlertDialog;
import com.google.android.material.bottomsheet.SlcBottomSheetAlertDialog2;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.mb_show_dialog_bottom_old).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SlcBottomSheetAlertDialog.Builder(MainActivity.this).setTitle("测试")
                        .setMessage("测试内容")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        });
        findViewById(R.id.mb_show_dialog_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SlcBottomSheetAlertDialog2.Builder(MainActivity.this)
                        .setTitle("测试")
                        .setMessage("测试内容")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        });
        findViewById(R.id.mb_show_dialog_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setTitle("測試").setMessage("測試内容").setTitle("测试")
                        .setMessage("测试内容")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        });

    }
}
