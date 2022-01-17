package com.flytrom.learning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseRecyclerViewAdapter;
import com.flytrom.learning.beans.response_beans.others.OptionsBean;
import com.flytrom.learning.databinding.ItemRandomMcqOptionBinding;

import java.util.ArrayList;
import java.util.List;

public class RandomQuestionAdapter extends BaseRecyclerViewAdapter<OptionsBean, RandomQuestionAdapter.ViewHolder> {

    private Context context;
    private List<OptionsBean> dataList;
    private BaseCallback baseCallback;
    private String mode;

    public RandomQuestionAdapter(Context context, BaseCallback baseCallback) {
        this.context = context;
        this.baseCallback = baseCallback;
        dataList = new ArrayList<>();
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setDataList(List<OptionsBean> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public List<OptionsBean> getData() {
        return dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRandomMcqOptionBinding itemMcqBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_random_mcq_option, parent, false);
        return new ViewHolder(itemMcqBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemMcqBinding.textMcqOption.setText(dataList.get(position).getName());

        if (mode != null && mode.equals("not_answered_yet")) {
            if (dataList.get(position).getSelectedIndex() == position) {
                setColor(holder.itemMcqBinding.textMcqOption, R.color.color_correct_answer);
            } else {
                setColor(holder.itemMcqBinding.textMcqOption, R.color.transparent);
            }
        } else {
            if (dataList.get(position).getIsAnswer().equals("Yes")) {
                setColor(holder.itemMcqBinding.textMcqOption, R.color.color_correct_answer);
            } else {
                if (dataList.get(position).getSelectedIndex() == position) {
                    if (dataList.get(position).getIsAnswer().equals("Yes")) {
                        setColor(holder.itemMcqBinding.textMcqOption, R.color.color_correct_answer);
                    } else {
                        setColor(holder.itemMcqBinding.textMcqOption, R.color.color_wrong_answer);
                    }
                } else {
                    if (dataList.get(position).getHasAnswered().equals("1")) {
                        if (dataList.get(position).getIsAnswer().equals("Yes")) {
                            setColor(holder.itemMcqBinding.textMcqOption, R.color.color_correct_answer);
                        } else {
                            setColor(holder.itemMcqBinding.textMcqOption, R.color.color_wrong_answer);
                        }
                    }
                }
            }
        }

        holder.itemMcqBinding.textMcqOption.setOnClickListener(v -> {
            if (mode != null && mode.equals("not_answered_yet"))
                baseCallback.onViewClick(v, position);
        });
    }

    private void setColor(TextView textMcqOption, int colorId) {
        textMcqOption.setBackgroundColor(context.getResources().getColor(colorId));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemRandomMcqOptionBinding itemMcqBinding;

        ViewHolder(@NonNull ItemRandomMcqOptionBinding binding) {
            super(binding.getRoot());
            this.itemMcqBinding = binding;
        }
    }
}
