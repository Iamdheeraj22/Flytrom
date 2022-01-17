package com.flytrom.learning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.model.CouponModel.CouponData;
import com.flytrom.learning.model.CouponModel.CouponPojo;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.ViewHolder> {
    Context context;
    List<CouponData> couponPojo;
    OnItemClickListnerCoupon callBackNew;
    public interface OnItemClickListnerCoupon {
        public void onitemClickCoupon(String code,String type,String discount);
    }
    public CouponsAdapter(Context context, List<CouponData> couponPojo,OnItemClickListnerCoupon callBackNew) {
        this.context = context;
        this.couponPojo = couponPojo;
        this.callBackNew = callBackNew;

    }

    @NonNull
    @NotNull
    @Override
    public CouponsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.coupon_list, parent, false);
        CouponsAdapter.ViewHolder viewHolder = new CouponsAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CouponsAdapter.ViewHolder holder, int position) {
        holder.cCode.setText(couponPojo.get(position).getCode());

        holder.discount.setText(couponPojo.get(position).getDiscount()+"%");
        holder.coupontext.setText(couponPojo.get(position).getDiscount()+"% OFF on Purchases Over "+couponPojo.get(position).getPrice_condition());
//        holder.planName.setText("Applicable on "+couponPojo.get(position).getPlan_details().getPlan_name());
        if (couponPojo.get(position).getPlan_details().getPlan_name().equalsIgnoreCase(null)){

        }else {
            holder.planName.setText("Applicable on "+couponPojo.get(position).getPlan_details().getPlan_name());
        }
        String inputPattern = "yyyy-mm-dd";
        String outputPattern = "dd-mm-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(couponPojo.get(position).getExpiry_date());
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.expiryDate.setText(str);
        holder.applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callBackNew.onitemClickCoupon(couponPojo.get(position).getCode(),couponPojo.get(position).getType(),couponPojo.get(position).getDiscount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return couponPojo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cCode,expiryDate,discount,coupontext,planName;
        CardView applyCoupon;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            cCode = itemView.findViewById(R.id.cCode);
            expiryDate = itemView.findViewById(R.id.expiryDate);
            discount = itemView.findViewById(R.id.discount);
            applyCoupon = itemView.findViewById(R.id.applyCoupon);
            coupontext = itemView.findViewById(R.id.coupontext);
            planName = itemView.findViewById(R.id.planName);

        }
    }


}
