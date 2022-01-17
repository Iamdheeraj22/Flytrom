package com.flytrom.learning.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseRecyclerViewAdapter;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanDataBean;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanMetadataBean;
import com.flytrom.learning.databinding.ItemSubscribeMainBinding;
import com.flytrom.learning.databinding.ItemSubscribeSubBinding;

import java.util.ArrayList;
import java.util.List;

public class AllPlansAdapter extends BaseRecyclerViewAdapter<PlanDataBean, AllPlansAdapter.ViewHolder> {

    private Context context;
    private List<PlanDataBean> dataList;
    private BaseCallback baseCallback;
    private SimpleRecyclerViewAdapter.SimpleCallback<PlanMetadataBean> callback;

    public AllPlansAdapter(Context context, BaseCallback baseCallback,
                           SimpleRecyclerViewAdapter.SimpleCallback<PlanMetadataBean> callback) {
        this.context = context;
        this.callback = callback;
        this.baseCallback = baseCallback;
        dataList = new ArrayList<>();
    }

    public void setDataList(List<PlanDataBean> dateList) {
        this.dataList = dateList;
        setPercentage();
        notifyDataSetChanged();
    }

    void setPercentage() {
        for (int i = 0; i < this.dataList.size(); i++) {
            if (this.dataList.get(i).getPlanMetadata() != null){
                for (int j = 0; j < this.dataList.get(i).getPlanMetadata().size(); j++) {

                    if (this.dataList.get(i).getPlanMetadata().get(j).getOldAmount() != null) {
                        int n = Integer.parseInt(this.dataList.get(i).getPlanMetadata().get(j).getAmount());
                        int v = Integer.parseInt(this.dataList.get(i).getPlanMetadata().get(j).getOldAmount());
                        int price = v - n;
                        int percent = 0;
                        if (v>0) {
                            percent = price * 100 / v;
                        }
                        this.dataList.get(i).getPlanMetadata().get(j).setPercentage(String.valueOf(percent));
                    }

                    if (this.dataList.get(i).getPlanMetadata().get(j).getOldAmount2() != null) {
                        int n2 = Integer.parseInt(this.dataList.get(i).getPlanMetadata().get(j).getAmount2());
                        int v2 = Integer.parseInt(this.dataList.get(i).getPlanMetadata().get(j).getOldAmount2());
                        int price2 = v2 - n2;
                        int percent2 = 0;
                        if (v2 > 0) {
                            percent2 = price2 * 100 / v2;
                        }
                        this.dataList.get(i).getPlanMetadata().get(j).setPercentage2(String.valueOf(percent2));
                    }

                }

            }
        }
    }

    public void addToDataList(List<PlanDataBean> newDataList) {
        if (newDataList == null) {
            return;
        }
        int positionStart = dataList.size();
        int itemCount = newDataList.size();
        dataList.addAll(newDataList);
        setPercentage();
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public List<PlanDataBean> getData() {
        return dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubscribeMainBinding itemSubscribeMainBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_subscribe_main, parent, false);
        return new ViewHolder(itemSubscribeMainBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemSubscribeMainBinding.setVariable(BR.bean, dataList.get(position));
        holder.itemSubscribeMainBinding.setVariable(BR.callback, baseCallback);
        holder.itemSubscribeMainBinding.setVariable(BR.pos, position);

        if (dataList.get(position).getColor() != null){
            holder.itemSubscribeMainBinding.cardShoe.setCardBackgroundColor(Color.parseColor(dataList.get(position).getColor()));
        }else {
            holder.itemSubscribeMainBinding.cardShoe.setCardBackgroundColor(Color.parseColor("#EAEDED"));
        }
        holder.itemSubscribeMainBinding.cardShoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dataList.get(position).isColorSelect()){
                    dataList.get(position).setColorSelect(true);
                    holder.itemSubscribeMainBinding.cardShoe.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }else {
                    dataList.get(position).setColorSelect(false);
                    if (dataList.get(position).getColor() != null){
                        holder.itemSubscribeMainBinding.cardShoe.setCardBackgroundColor(Color.parseColor(dataList.get(position).getColor()));
                    }else {
                        holder.itemSubscribeMainBinding.cardShoe.setCardBackgroundColor(Color.parseColor("#EAEDED"));
                    }
                }

                holder.itemSubscribeMainBinding.rvSubPlans.setVisibility(holder.itemSubscribeMainBinding.rvSubPlans.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                holder.itemSubscribeMainBinding.show.setVisibility(holder.itemSubscribeMainBinding.show.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

//        if (dataList.get(position).isSelected() == true){
//            holder.itemSubscribeMainBinding.cardShoe.setCardBackgroundColor(Color.parseColor("#ffffff"));
//        }else {
//            holder.itemSubscribeMainBinding.cardShoe.setCardBackgroundColor(Color.parseColor(dataList.get(position).getColor()));
//        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
//        params.setMargins(30, 30, 0, 0);
        holder.itemSubscribeMainBinding.textName.setLayoutParams(params);
        if (dataList.get(position).getHasMeta().equals("1"))
            holder.adapterSubPlans.setList(dataList.get(position).getPlanMetadata());

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemSubscribeMainBinding itemSubscribeMainBinding;

        SimpleRecyclerViewAdapter<PlanMetadataBean, ItemSubscribeSubBinding> adapterSubPlans
                = new SimpleRecyclerViewAdapter<>(R.layout.item_subscribe_sub, BR.bean, callback);

         ImageView shows;
        ViewHolder(@NonNull ItemSubscribeMainBinding binding) {
            super(binding.getRoot());
            this.itemSubscribeMainBinding = binding;
            this.itemSubscribeMainBinding.rvSubPlans.setLayoutManager(new LinearLayoutManager(context));
            this.itemSubscribeMainBinding.rvSubPlans.setAdapter(adapterSubPlans);
            boolean select = false;

 //            shows.findViewById(R.id.show);
        }
    }
}
