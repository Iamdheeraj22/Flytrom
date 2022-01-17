package com.flytrom.learning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseRecyclerViewAdapter;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.NoteBean;
import com.flytrom.learning.databinding.ItemPdfSimpleImageBinding;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends BaseRecyclerViewAdapter<NoteBean, NotesAdapter.ViewHolder> {

    private Context context;
    private List<NoteBean> dataList;
    private SimpleRecyclerViewAdapter.SimpleCallback simpleCallback;

    public NotesAdapter(Context context, SimpleRecyclerViewAdapter.SimpleCallback simpleCallback) {
        this.context = context;
        this.simpleCallback = simpleCallback;
        dataList = new ArrayList<>();
    }

    public void setDataList(List<NoteBean> dateList) {
        this.dataList = dateList;
        notifyDataSetChanged();
    }

    public List<NoteBean> getData() {
        return dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPdfSimpleImageBinding itemPdfSimpleImageBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_pdf_simple_image, parent, false);
        itemPdfSimpleImageBinding.setVariable(BR.callback, simpleCallback);
        return new ViewHolder(itemPdfSimpleImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemPdfSimpleImageBinding.setVariable(BR.pos, position);
        holder.itemPdfSimpleImageBinding.setVariable(BR.bean, dataList.get(position));

        holder.itemPdfSimpleImageBinding.ivSimple.setImageBitmap(dataList.get(position).getBitmap());

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemPdfSimpleImageBinding itemPdfSimpleImageBinding;

        ViewHolder(@NonNull ItemPdfSimpleImageBinding binding) {
            super(binding.getRoot());
            this.itemPdfSimpleImageBinding = binding;
        }
    }
}
