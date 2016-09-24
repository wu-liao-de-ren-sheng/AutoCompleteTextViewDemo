package com.example.jianpan.autocompletetextviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by 键盘 on 2016/9/24.
 *
 */
public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void simple(View view){
        startActivity(new Intent(this, SimpleActivity.class));
    }

    public void custom(View view){
        startActivity(new Intent(this, CustomActivity.class));
    }
}
