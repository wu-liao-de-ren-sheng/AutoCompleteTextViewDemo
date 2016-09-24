package com.example.jianpan.autocompletetextviewdemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by 键盘 on 2016/9/20.
 * 搜索提示文本的adapter
 */
public class SearchListAdapter extends BaseAdapter implements Filterable {

    private Context mContext;

    private String mHeaderText;//头部的提示文本
    private boolean mHasFooter;//是否显示底部
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private View.OnClickListener mClearHistoryOnClick;

    private ArrayFilter mFilter;
    private List<String> mOriginalValues;//所有的Item
    private List<String> mObjects;//过滤后的item

    public SearchListAdapter(Context context, List<String> date) {
        mContext = context;
        mOriginalValues = date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ListViewHolder helper = ListViewHolder.get(mContext, convertView, parent, R.layout.list_item_search, position);
        View headerRootView = helper.getView(R.id.search_header_root_view);
        TextView headerTv = helper.getView(R.id.search_header_tv);
        View centerRootView = helper.getView(R.id.search_center_root_view);
        TextView centerTv = helper.getView(R.id.search_tv);
        View footerRootView = helper.getView(R.id.search_footer_root_view);
        TextView footerTv = helper.getView(R.id.search_clear_history_tv);
        if (position == 0) {//头部
            headerTv.setText(mHeaderText);
            headerRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            headerRootView.setVisibility(View.VISIBLE);
            centerRootView.setVisibility(View.GONE);
            footerRootView.setVisibility(View.GONE);
        } else if (position == getCount() - 1 && mHasFooter) {//尾部
            footerRootView.setVisibility(View.VISIBLE);
            centerRootView.setVisibility(View.GONE);
            headerRootView.setVisibility(View.GONE);
            footerTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClearHistoryOnClick != null) {
                        mClearHistoryOnClick.onClick(v);
                    }
                }
            });
        } else {//中间
            centerTv.setText(mObjects.get(position - 1));//减一，减去头部
            centerRootView.setVisibility(View.VISIBLE);
            headerRootView.setVisibility(View.GONE);
            footerRootView.setVisibility(View.GONE);
            centerTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(null, v, helper.getPosition(), v.getId());
                    }
                }
            });
        }
        return helper.getConvertView();
    }
    /** 更新请求到的数据 */
    public void upData(List<String> data) {
        mOriginalValues = data;
        mFilter.filter("");
    }

    public void setHeaderText(String text) {
        mHeaderText = text;
    }

    public void setHasFooter(boolean hasFooter) {
        if (mHasFooter != hasFooter) {
            mHasFooter = hasFooter;
        }
    }

    private int getFooterCount() {
        return mHasFooter ? 1 : 0;
    }

    @Override
    public String getItem(int position) {
        if (mObjects != null && mObjects.size() > position - 1 && position > 0) {
            return mObjects.get(position - 1);
        } else {
            return "";
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        if (mObjects == null || mObjects.isEmpty()) {
            return 0;
        }
        return mObjects.size() + 1 + getFooterCount();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setClearHistoryOnClick(View.OnClickListener onClick) {
        this.mClearHistoryOnClick = onClick;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mOriginalValues == null || mOriginalValues.isEmpty()) {
                results.values = null;
                results.count = 0;
                return results;
            }
            if (prefix == null || prefix.length() == 0) {
                ArrayList<String> list = new ArrayList<String>(mOriginalValues);
                results.values = list;
                results.count = list.size();
                return results;
            } else {
                String prefixString = prefix.toString().toLowerCase();
                final int count = mOriginalValues.size();
                final ArrayList<String> newValues = new ArrayList<String>(count);

                for (int i = 0; i < count; i++) {
                    final String value = mOriginalValues.get(i);
                    final String valueText = value.toLowerCase();

                    newValues.add(value);
//                    if (valueText.startsWith(prefixString)) {  //源码 ,匹配开头
//                         newValues.add(value);
//                    }
//                    else {
//                        final String[] words = valueText.split(" ");//分隔符匹配，效率低
//                        final int wordCount = words.length;
//
//                        for (int k = 0; k < wordCount; k++) {
//                            if (words[k].startsWith(prefixString)) {
//                                newValues.add(value);
//                                break;
//                            }
//                        }
//                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mObjects = mOriginalValues;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
