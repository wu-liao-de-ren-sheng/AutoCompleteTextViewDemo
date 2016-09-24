package com.example.jianpan.autocompletetextviewdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomActivity extends AppCompatActivity {

    public static final String NAME_LIST = "name_list";

    private CustomAutoComplete mCustomAutoComplete;
    private View mLine;
    private SearchListAdapter mAdapter;
    private String mSearchStr;
    private boolean isOnItemClick;//当点击搜索后请求到的商品名称不需要再弹popupWindow

    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        mSp = getSharedPreferences(getPackageName() + "_sp", Context.MODE_PRIVATE);
        mEditor = mSp.edit();
        mCustomAutoComplete = (CustomAutoComplete) findViewById(R.id.search_view);
        mLine               = findViewById(R.id.title_search_line_view);
        mAdapter = new SearchListAdapter(this, getHistorySearchStr());
        //设置popupWindow的宽度
        mCustomAutoComplete.setDropDownWidth(getScreenWidth());
        mCustomAutoComplete.setAdapter(mAdapter);
        mCustomAutoComplete.setThreshold(0);
        mLine.post(new Runnable() {
            @Override
            public void run() {
                //计算popupWindow显示的位置
                float offset = mLine.getBottom() - mCustomAutoComplete.getBottom();
                mCustomAutoComplete.setDropDownVerticalOffset((int) offset);
            }
        });

        mCustomAutoComplete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mCustomAutoComplete.getText().toString().length() == 0){
                    mAdapter.setHeaderText(getResources().getString(R.string.search_goods_history));
                    mAdapter.setHasFooter(true);
                    mAdapter.upData(getHistorySearchStr());
                }
                return false;
            }
        });

        mCustomAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchStr = s.toString();
                if (mSearchStr.length() == 0){
                    mAdapter.setHeaderText(getResources().getString(R.string.search_goods_history));
                    mAdapter.setHasFooter(true);
                    mAdapter.upData(getHistorySearchStr());
                    if (!mCustomAutoComplete.isPopupShowing()){
                        mCustomAutoComplete.showDropDown();
                    }
                }else {
                    //访问接口
                    if (!isOnItemClick) {
                        refresh();
                    }
                    isOnItemClick = false;
                }
            }
        });
        //当点击键盘上的搜索时
        mCustomAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //搜索
                    String searchStr = mCustomAutoComplete.getText().toString().trim();
                    if (searchStr.length() > 0){
                        mCustomAutoComplete.dismissDropDown();
                        saveSearchStr(searchStr);
                        mSearchStr = searchStr;
                        //搜索数据
                        refresh();
                    }
                }
                return false;
            }
        });


        mAdapter.setHeaderText(getResources().getString(R.string.search_goods_history));
        mAdapter.setHasFooter(true);

        mAdapter.setClearHistoryOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearHistorySearchStr();
                mAdapter.upData(null);
            }
        });
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isOnItemClick = true;
                mSearchStr = mAdapter.getItem(position);
                mCustomAutoComplete.setText(mSearchStr);
                saveSearchStr(mSearchStr);
                //设置光标
                Editable spannable = mCustomAutoComplete.getText();
                Selection.setSelection(spannable, spannable.length());
                mCustomAutoComplete.dismissDropDown();
                //搜索数据
                refresh();
            }
        });
    }

    private void saveSearchStr(String text){
        String historyText = mSp.getString(NAME_LIST, "");
        if (!historyText.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(historyText);
            sb.insert(0, text + ",");
            mEditor.putString(NAME_LIST, sb.toString());
            mEditor.commit();
        }
    }

    private List<String> getHistorySearchStr(){
        String text = mSp.getString(NAME_LIST, null);
        if (text == null || text.length() == 0){
            return null;
        }else {
            return Arrays.asList(text.split(","));
        }
    }

    private void clearHistorySearchStr(){
        mEditor.remove(NAME_LIST);
        mEditor.commit();
    }

    private void refresh(){
        //模拟网络搜索
        String [] date = {"aa", "ab", "ac", "ad", "ba", "bb", "bc", "bd"};
        List<String> responseDate = new ArrayList<>();
        for (String text : date){
            if (text.contains(mSearchStr)){
                responseDate.add(text);
            }
        }
        response(responseDate);
    }

    private void response(List<String> response){
        mAdapter.setHeaderText(getResources().getString(R.string.search_goods));
        mAdapter.setHasFooter(false);
        mAdapter.upData(response);
    }

    public void cancel(View view){
        finish();
    }

    /** 获得屏幕宽度 */
    public int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }
}
