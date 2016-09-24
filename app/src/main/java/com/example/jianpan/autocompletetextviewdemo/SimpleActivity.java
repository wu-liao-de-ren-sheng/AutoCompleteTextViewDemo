package com.example.jianpan.autocompletetextviewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

/**
 * Created by 键盘 on 2016/9/24.
 *
 */
public class SimpleActivity extends AppCompatActivity{

    String[] NAME = {"Android", "Java", "Android", "Java"};

    private AutoCompleteTextView mAutoCompleteTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.search_view);
        mAutoCompleteTextView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, NAME));
        //设置输入1个字时，就弹窗
        mAutoCompleteTextView.setThreshold(1);
    }
}
