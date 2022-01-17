package com.flytrom.learning.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.model.MainCategorey.DataCategory;
import com.flytrom.learning.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestCategoryAdapter extends RecyclerView.Adapter<TestCategoryAdapter.ViewHolder> {
    List<DataCategory> regionLodelsList;
    Context context;
    OnItemClickListnerTest callBackNew;

    public interface OnItemClickListnerTest {
        public void onitemClickBank(String id, String total_subjects, String image, String name);
    }
    public TestCategoryAdapter(List<DataCategory> regionLodelsList, Context context, OnItemClickListnerTest callBackNew) {
        this.regionLodelsList = regionLodelsList;
        this.context = context;
        this.callBackNew = callBackNew;
    }

    @NonNull
    @NotNull
    @Override
    public TestCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.test_items, parent, false);
        TestCategoryAdapter.ViewHolder viewHolder = new TestCategoryAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TestCategoryAdapter.ViewHolder holder, int position) {
        holder.tv_title.setText(regionLodelsList.get(position).getTitle());
        if (regionLodelsList.get(position).getTotal_subjects().equalsIgnoreCase("1")){
            holder.subjectQBank.setText(regionLodelsList.get(position).getTotal_subjects()+" Subject");
        }else {
            holder.subjectQBank.setText(regionLodelsList.get(position).getTotal_subjects()+" Subjects");
        }
        holder.titleQbank.setText(regionLodelsList.get(position).getTitle());
        holder.taglineQbank.setText(regionLodelsList.get(position).getTagline());
        String color = regionLodelsList.get(position).getColor();
        holder.card.setCardBackgroundColor(Color.parseColor(color));

        String picTDetail = regionLodelsList.get(position).getIcon();
        Glide.with(context).load(Constants.MEDIA_URL + picTDetail).into(holder.image_lecturer);

        holder.rlCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBackNew.onitemClickBank(regionLodelsList.get(position).getId(),regionLodelsList.get(position).getTotal_subjects(),regionLodelsList.get(position).getImage(),regionLodelsList.get(position).getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return regionLodelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,subjectQBank,titleQbank,taglineQbank;
        ImageView image_lecturer;
        RelativeLayout rlCategory;
        CardView card;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            subjectQBank = itemView.findViewById(R.id.subjectQBank);
            taglineQbank = itemView.findViewById(R.id.taglineQbank);
            image_lecturer = itemView.findViewById(R.id.image_lecturer);
            rlCategory = itemView.findViewById(R.id.rlCategory);
            titleQbank = itemView.findViewById(R.id.titleQbank);
            card = itemView.findViewById(R.id.card);
        }
    }
}
