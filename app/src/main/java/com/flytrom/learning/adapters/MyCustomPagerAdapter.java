package com.flytrom.learning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.flytrom.learning.R;
import com.flytrom.learning.beans.normal_beans.NoteBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyCustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<NoteBean> dataList;

    public MyCustomPagerAdapter(Context context) {
        this.mContext = context;
        dataList = new ArrayList<>();
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<NoteBean> getDataList() {
        return dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_notes_viewpager, container, false);

        ImageView imageView = itemView.findViewById(R.id.image);
        TextView textView = itemView.findViewById(R.id.text_counter);
        imageView.setImageBitmap(dataList.get(position).getBitmap());
        textView.setText(String.valueOf(position + 1));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((LinearLayout) object);
    }

    public void setList(List<NoteBean> notesList) {
        this.dataList = notesList;
    }
}
