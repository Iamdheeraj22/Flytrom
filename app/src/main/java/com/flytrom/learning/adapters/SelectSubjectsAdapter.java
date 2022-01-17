package com.flytrom.learning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseRecyclerViewAdapter;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.databinding.ItemSelectSubjectMainBinding;
import com.flytrom.learning.databinding.ItemSelectSubjectSubBinding;

import java.util.ArrayList;
import java.util.List;

public class SelectSubjectsAdapter extends BaseRecyclerViewAdapter<SubjectBean, SelectSubjectsAdapter.ViewHolder> {

    private Context context;
    private List<SubjectBean> dataList;
    private BaseCallback baseCallback;
    private SimpleRecyclerViewAdapter.SimpleCallback<ChapterBean> callback;

    public SelectSubjectsAdapter(Context context, BaseCallback baseCallback,
                                 SimpleRecyclerViewAdapter.SimpleCallback<ChapterBean> callback) {
        this.context = context;
        this.baseCallback = baseCallback;
        this.callback = callback;
        dataList = new ArrayList<>();
    }

    public void setDataList(List<SubjectBean> dateList) {
        this.dataList = dateList;
        notifyDataSetChanged();
    }

    public void addToDataList(List<SubjectBean> newDataList) {
        if (newDataList == null) {
            return;
        }
        int positionStart = dataList.size();
        int itemCount = newDataList.size();
        dataList.addAll(newDataList);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public List<SubjectBean> getData() {
        return dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSelectSubjectMainBinding itemSelectSubjectMainBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_select_subject_main, parent, false);
        return new ViewHolder(itemSelectSubjectMainBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemSelectSubjectMainBinding.setVariable(BR.bean, dataList.get(position));
        holder.itemSelectSubjectMainBinding.setVariable(BR.callback, baseCallback);
        holder.itemSelectSubjectMainBinding.setVariable(BR.pos, position);
        if (dataList.get(position).getChapters() != null) {
            if (dataList.get(position).getChapters().size() > 0)
                holder.adapterChapters.setList(dataList.get(position).getChapters());
        }
        if (dataList.get(position).isExpanded()) {
            holder.itemSelectSubjectMainBinding.relativeChapters.setVisibility(View.VISIBLE);
        } else {
            holder.itemSelectSubjectMainBinding.relativeChapters.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemSelectSubjectMainBinding itemSelectSubjectMainBinding;

        SimpleRecyclerViewAdapter<ChapterBean, ItemSelectSubjectSubBinding> adapterChapters
                = new SimpleRecyclerViewAdapter<>(R.layout.item_select_subject_sub, BR.bean, callback);

        ViewHolder(@NonNull ItemSelectSubjectMainBinding binding) {
            super(binding.getRoot());
            this.itemSelectSubjectMainBinding = binding;
            this.itemSelectSubjectMainBinding.recyclerChapters.setLayoutManager(new LinearLayoutManager(context));
            this.itemSelectSubjectMainBinding.recyclerChapters.setAdapter(adapterChapters);
        }
    }
}
