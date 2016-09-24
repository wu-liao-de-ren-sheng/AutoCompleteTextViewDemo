package com.example.jianpan.autocompletetextviewdemo;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by 键盘 on 2016/9/20.
 *
 */
public class CustomAutoComplete extends AutoCompleteTextView {
    private int threshold;

    public CustomAutoComplete(Context context) {
        super(context);
    }

    public CustomAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            performFiltering(getText(), 0);
            showDropDown();
        }
    }
    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    public void setThreshold(int threshold) {
        if (threshold < 0) {
            threshold = 0;
        }
        this.threshold = threshold;
    }
    @Override
    public int getThreshold() {
        return threshold;
    }

    @Override
    public void onFilterComplete(int count) {
//        super.onFilterComplete(count);
    }
}