package com.flytrom.learning.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
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

public class QBankCustomCreateAdapter extends RecyclerView.Adapter<QBankCustomCreateAdapter.ViewHolder> {
    List<DataCategory> regionLodelsList;
    Context context;
    OnItemClickListnerBank callBackNew;
    private int lastSelectedPosition = -1;

    public interface OnItemClickListnerBank {
        public void onitemClickBankNew(String id);
    }
    public QBankCustomCreateAdapter(List<DataCategory> regionLodelsList, Context context, OnItemClickListnerBank callBackNew) {
        this.regionLodelsList = regionLodelsList;
        this.context = context;
        this.callBackNew = callBackNew;
    }

    @NonNull
    @NotNull
    @Override
    public QBankCustomCreateAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_qbank, parent, false);
        QBankCustomCreateAdapter.ViewHolder viewHolder = new QBankCustomCreateAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull QBankCustomCreateAdapter.ViewHolder holder, int position) {
        holder.categorySelect.setText(regionLodelsList.get(position).getTitle());

        holder.categorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBackNew.onitemClickBankNew(regionLodelsList.get(position).getId());
                lastSelectedPosition = position;
                notifyDataSetChanged();
            }
        });
        holder.categorySelect.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return regionLodelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton categorySelect;
        RelativeLayout rlRadio;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            categorySelect = itemView.findViewById(R.id.categorySelect);
            rlRadio = itemView.findViewById(R.id.rlRadio);
        }
    }
}
