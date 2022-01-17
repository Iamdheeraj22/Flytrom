package com.flytrom.learning.utils;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flytrom.learning.R;
import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;
import com.flytrom.learning.pdf_viewer.PDFZoomImageView;

public class BindingAdapterUtils {

    @BindingAdapter(value = {"simpleResourse"})
    public static void setStepGroupIcon(final ImageView imageView, int simpleResourse) {

        if (simpleResourse != -1) {
            imageView.setImageResource(simpleResourse);
        }
    }

    @BindingAdapter(value = {"image_url", "placeholder"}, requireAll = false)
    public static void setImageUrl(final ImageView imageView, String imageUrl, Drawable placeholder) {
        Glide.with(imageView)
                .load(imageUrl)
                .centerCrop()
                .placeholder(placeholder)
                .into(imageView);
    }

    @BindingAdapter(value = {"image_path"}, requireAll = false)
    public static void setBitmpaOnImage(final ImageView imageView, String imagePath) {
        /*Picasso.get().load(new File(imagePath))
                .centerCrop()
                .resize(100, 100)
                .into(imageView);*/
    }

    /*@BindingAdapter({"android:src_circle"})
    public static void setCircleImageViewResource(ImageView imageView, int resource) {
        Picasso.get().load(resource).transform(new CircleImageTransform()).into(imageView);
    }

    @BindingAdapter(value = {"circle_image"}, requireAll = false)
    public static void setCircleImage(final ImageView imageView, String imagePath) {
        Picasso.get().load(imagePath)
                .transform(new CircleImageTransform())
                .centerCrop()
                .resize(100, 100)
                .into(imageView);
    }*/

    @BindingAdapter(value = {"dummy_value"}, requireAll = false)
    public static void setCirclpleImage(final ImageView imageView, String file) {

    }

    @BindingAdapter(value = {"round_pic", "placeholder"}, requireAll = false)
    public static void round(final ImageView imageView, String imageUrl, Drawable placeholder) {
        Glide.with(imageView)
                .load(imageUrl)
                .centerCrop()
                .placeholder(placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);

        /*Picasso.get()
                .load(imageUrl)
                .centerCrop()
                .resize(100, 100)
                .transform(new CircleImageTransform())
                .placeholder(placeholder)
                .into(imageView);*/
    }
    @BindingAdapter(value = {"set_pic", "placeholder"}, requireAll = false)
    public static void setPic(final ImageView imageView, String imageUrl, Drawable placeholder) {
        Glide.with(imageView)
                .load(imageUrl)
                .centerCrop()
                .placeholder(placeholder)
                .into(imageView);

        /*Picasso.get()
                .load(imageUrl)
                .centerCrop()
                .resize(100, 100)
                .transform(new CircleImageTransform())
                .placeholder(placeholder)
                .into(imageView);*/
    }


    /*@BindingAdapter({"like"})
    public static void like(CheckBox checkBox, GetBuyerSellerDetailsBean.DataBeanX.DataBean bean) {
        if (bean != null) {
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(bean.getIs_like() == 1);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> bean.setIs_like(isChecked ? 1 : 0));
        } else checkBox.setChecked(false);
    }*/

    @BindingAdapter("pinPoint")
    public static void pinPoint(View view, SolveMCQBean solveMCQBean) {

        if (solveMCQBean.getUser_id() == null) {
            view.setBackgroundResource(R.drawable.bg_circle_gray_filled);
        } else {
            if (solveMCQBean.getWasCorrect() == 1)
                view.setBackgroundResource(R.drawable.bg_circle_correct_filled);
            else
                view.setBackgroundResource(R.drawable.bg_circle_incorrect_filled);
        }
    }

    @BindingAdapter("setBitmap")
    public static void setBitmap(PDFZoomImageView imageView, Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @BindingAdapter("strikeThrough")
    public static void strikeThrough(TextView textView, Boolean strikeThrough) {
        if (strikeThrough) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }


    @BindingAdapter("setTimeDuration")
    public static void setTimeDuration(TextView textView, String seconds) {
        int sec = Integer.parseInt(seconds);

        int hours = sec / 3600;
        int minutes = (sec % 3600) / 60;
        int second = sec % 60;

        String min;
        String _seconds;
        String hrs;
        if (hours==0){
            if (minutes<=9){
               min =  "0"+minutes;
            }
            else{
                min = minutes+"";
            }
            if (sec<=9){
                _seconds = "0"+second;
            }else{
                _seconds = second+"";
            }
            textView.setText(min+" Minutes ");
        }else{

            if (hours<=9){
                hrs = "0"+hours;
            }else{
                hrs = ""+hours;
            }

            if (minutes<=9){
                min =  "0"+minutes;
            }
            else{
                min = minutes+"";
            }
            if (sec<=9){
                _seconds = "0"+second;
            }else{
                _seconds = second+"";
            }

            textView.setText(hrs+" Hours "+min+" Minutes ");

        }

    }
}
