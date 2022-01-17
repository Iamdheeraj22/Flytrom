package com.flytrom.learning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.model.DataRegion;
import com.flytrom.learning.model.MainCategorey.DataCategory;
import com.flytrom.learning.model.regionLodel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {
    List<DataRegion> regionLodelsList;
    Context context;
    OnItemClickListner callBackNew;

    public interface OnItemClickListner {
        public void onitemClickNew(String click, int position, String id);
    }

    public RegionAdapter(List<DataRegion> regionLodelsList, Context context, OnItemClickListner callBackNew) {
        this.regionLodelsList = regionLodelsList;
        this.context = context;
        this.callBackNew = callBackNew;
    }



    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.resion_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RegionAdapter.ViewHolder holder, int position) {
        holder.regionName.setText(regionLodelsList.get(position).getName());
        holder.select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBackNew.onitemClickNew(regionLodelsList.get(position).getName(),position,regionLodelsList.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return regionLodelsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView regionName;
        CardView select;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            regionName = itemView.findViewById(R.id.regionName);
            select = itemView.findViewById(R.id.select);



        }
    }
}
