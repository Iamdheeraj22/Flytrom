package com.flytrom.learning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseRecyclerViewAdapter;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.TermsConditionBean;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanDataBean;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanMetadataBean;
import com.flytrom.learning.databinding.ItemSubscribeMainBinding;
import com.flytrom.learning.databinding.ItemSubscribeSubBinding;
import com.flytrom.learning.databinding.ItemTermsConditionBinding;

import java.util.ArrayList;
import java.util.List;

public class TermsConditionsAdapter extends BaseRecyclerViewAdapter<PlanDataBean, TermsConditionsAdapter.ViewHolder> {

    private Context context;
    private List<TermsConditionBean> dataList;
    private SimpleRecyclerViewAdapter.SimpleCallback<TermsConditionBean> callback;

    public TermsConditionsAdapter(Context context, SimpleRecyclerViewAdapter.SimpleCallback<TermsConditionBean> callback) {
        this.context = context;
        this.callback = callback;
        dataList = new ArrayList<>();
    }

    public void setDataList(List<TermsConditionBean> dateList) {
        this.dataList = dateList;
        notifyDataSetChanged();
    }

    public List<TermsConditionBean> getData() {
        return dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTermsConditionBinding itemTermsConditionBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_terms_condition, parent, false);
        return new ViewHolder(itemTermsConditionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTermsConditionBinding.setVariable(BR.bean, dataList.get(position));
        holder.itemTermsConditionBinding.setVariable(BR.callback, callback);
        holder.itemTermsConditionBinding.setVariable(BR.pos, position);
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemTermsConditionBinding itemTermsConditionBinding;

        ViewHolder(@NonNull ItemTermsConditionBinding binding) {
            super(binding.getRoot());
            this.itemTermsConditionBinding = binding;
        }
    }
}
