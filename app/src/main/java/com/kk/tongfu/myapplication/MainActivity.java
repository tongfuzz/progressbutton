package com.kk.tongfu.myapplication;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kk.tongfu.myapplication.widget.ProgressButton;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    ProgressButton mProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressButton=findViewById(R.id.progressButton);
        mProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyHandler(MainActivity.this).sendEmptyMessage(1);
                mProgressButton.setClickable(false);
            }
        });
    }

    static class MyHandler extends Handler{

        WeakReference<MainActivity> reference;
        int progress=0;

        public MyHandler(MainActivity activity){
            reference=new WeakReference<>(activity);
        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(reference.get()!=null&&reference.get() instanceof MainActivity){
                if(progress<=100) {
                    reference.get().mProgressButton.setProgress(progress++);
                    reference.get().mProgressButton.setText("下载中 ...", true);
                    sendEmptyMessageDelayed(1, 50);
                }else{
                    reference.get().mProgressButton.setText("下载完成",false);
                }
            }
        }
    }
}
