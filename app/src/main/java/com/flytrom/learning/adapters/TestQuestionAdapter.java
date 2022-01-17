package com.flytrom.learning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseRecyclerViewAdapter;
import com.flytrom.learning.beans.response_beans.others.OptionsBean;
import com.flytrom.learning.databinding.ItemMcqOptionOfTestBinding;
import com.flytrom.learning.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionAdapter extends BaseRecyclerViewAdapter<OptionsBean, TestQuestionAdapter.ViewHolder> {

    private Context context;
    private List<OptionsBean> dataList;
    private BaseCallback baseCallback;
    private String mMode;

    public TestQuestionAdapter(Context context, BaseCallback baseCallback) {
        this.context = context;
        this.baseCallback = baseCallback;
        dataList = new ArrayList<>();
    }

    public void setmMode(String mMode) {
        this.mMode = mMode;
    }

    public void setDataList(List<OptionsBean> datalist) {
        this.dataList = datalist;
        notifyDataSetChanged();
    }

    public List<OptionsBean> getData() {
        return dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMcqOptionOfTestBinding itemMcqBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_mcq_option_of_test, parent, false);
        //itemMcqBinding.setVariable(BR.callback, baseCallback);
        return new ViewHolder(itemMcqBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        setColor(holder.itemMcqOptionOfTestBinding.textMcqOption, R.color.transparent);
        //holder.itemMcqOptionOfTestBinding.textMcqOption.setSelected(true);
        holder.itemMcqOptionOfTestBinding.textMcqOption.setText(Constants.abcValues[position] + dataList.get(position).getName());

        if (mMode != null) {
            if (mMode.equals("1")) {
                if (dataList.get(position).getSelectedIndex() != -1) {
                    if (dataList.get(position).getIsAnswer().equals("Yes")) {
                        setColor(holder.itemMcqOptionOfTestBinding.textMcqOption, R.color.color_correct_answer);
                    } else {

                        if (dataList.get(position).getSelectedIndex() == position) {

                            if (dataList.get(position).getIsAnswer().equals("Yes")) {
                                setColor(holder.itemMcqOptionOfTestBinding.textMcqOption, R.color.color_correct_answer);
                            } else {
                                setColor(holder.itemMcqOptionOfTestBinding.textMcqOption, R.color.color_wrong_answer);
                            }
                        }
                    }
                }
            } else {
                if (dataList.get(position).isSelected()) {
                    setColor(holder.itemMcqOptionOfTestBinding.textMcqOption, R.color.color_correct_answer);
                }
            }
        }

        holder.itemMcqOptionOfTestBinding.textMcqOption.setOnClickListener(v -> baseCallback.onViewClick(v, position));
    }

    private void setColor(TextView textMcqOption, int colorId) {
        textMcqOption.setBackgroundColor(context.getResources().getColor(colorId));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMcqOptionOfTestBinding itemMcqOptionOfTestBinding;

        ViewHolder(@NonNull ItemMcqOptionOfTestBinding binding) {
            super(binding.getRoot());
            this.itemMcqOptionOfTestBinding = binding;
        }
    }
}
