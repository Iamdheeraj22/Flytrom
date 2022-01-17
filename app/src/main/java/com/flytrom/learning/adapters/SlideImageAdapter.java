package com.flytrom.learning.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.beans.response_beans.others.SlidesBean;
import com.flytrom.learning.model.MainCategorey.DataCategory;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ZoomImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class SlideImageAdapter extends PagerAdapter {
    List<SlidesBean> slides;
    Context context;
    LayoutInflater mLayoutInflater;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    String file;
    ZoomImageView imageView;
    public SlideImageAdapter(List<SlidesBean> slides, Context context, String file) {
        this.slides = slides;
        this.context = context;
        this.file = file;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // return the number of images
        return slides.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.slide_image_item, container, false);

        // referencing the image Mview from the item.xml file
        imageView = itemView.findViewById(R.id.imageViewMain);
        String picTDetail = slides.get(position).getFile();
        Glide.with(context).load(Constants.MEDIA_URL + picTDetail).into(imageView);

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }


}
