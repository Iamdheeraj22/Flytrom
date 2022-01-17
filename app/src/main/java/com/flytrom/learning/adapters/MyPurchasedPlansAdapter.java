package com.flytrom.learning.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseRecyclerViewAdapter;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.response_beans.auth.PurchasedPlansBean;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanDataBean;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanMetadataBean;
import com.flytrom.learning.databinding.ItemMyPurchasePlanBinding;
import com.flytrom.learning.utils.UtilMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyPurchasedPlansAdapter extends BaseRecyclerViewAdapter<PlanDataBean, MyPurchasedPlansAdapter.ViewHolder> {

    String expDate;
    private Context context;
    private List<PurchasedPlansBean> dataList;
    private SimpleRecyclerViewAdapter.SimpleCallback<PurchasedPlansBean> callback;

    public MyPurchasedPlansAdapter(Context context, SimpleRecyclerViewAdapter.SimpleCallback<PurchasedPlansBean> callback) {
        this.context = context;
        this.callback = callback;
        dataList = new ArrayList<>();
    }

    public void setDataList(List<PurchasedPlansBean> dateList) {
        this.dataList = dateList;
        notifyDataSetChanged();
    }

    public void addToDataList(List<PurchasedPlansBean> newDataList) {
        if (newDataList == null) {
            return;
        }
        int positionStart = dataList.size();
        int itemCount = newDataList.size();
        dataList.addAll(newDataList);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public List<PurchasedPlansBean> getData() {
        return dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyPurchasePlanBinding itemMyPurchasePlanBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_my_purchase_plan, parent, false);
        itemMyPurchasePlanBinding.setVariable(BR.callback, callback);
        return new ViewHolder(itemMyPurchasePlanBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Calendar c = Calendar.getInstance();
        String year= String.valueOf(c.get(Calendar.YEAR));
        String date= String.valueOf(c.get(Calendar.DATE));
        String month= String.valueOf(c.get(Calendar.MONTH));

        expDate = date+"-"+month+"-"+year;
        Log.d("FGGGXHFK",year+"-"+date+"-"+month);
        try {



            holder.myPurchasePlanBinding.setVariable(BR.pos, position);
            holder.myPurchasePlanBinding.setVariable(BR.bean, dataList.get(position));
            PurchasedPlansBean mainBean = dataList.get(position);
            String inputPattern = "yyyy-mm-dd";
            String outputPattern = "dd-mm-yyyy";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date datee = null;
            String str = null;

            try {
                datee = inputFormat.parse(UtilMethods.splitString(mainBean.getTxTime()));
                str = outputFormat.format(datee);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (expDate.equalsIgnoreCase(String.valueOf(str))){
                holder.myPurchasePlanBinding.viewExp.setVisibility(View.VISIBLE);
            }
            holder.myPurchasePlanBinding.viewReceipt2.setText(String.format("Expires on \n%s", mainBean.getPlan_expiry()));
            holder.myPurchasePlanBinding.textPurchased.setText(String.format(mainBean.getValidity()+" months"));
            if (mainBean.getPlanDetails().getHasMeta() != null && mainBean.getPlanDetails().getHasMeta().equals("1")) {
                holder.myPurchasePlanBinding.textName.setText(String.format("%s %s", mainBean.getPlanMetadataBean().getSubjects(),
                        mainBean.getPlanMetadataBean().getPlanTypes().getTitle()));

                if (mainBean.getPlanMetadataBean().getPlanTypes().getVideos() == 1)
                    holder.myPurchasePlanBinding.linearLectures.setVisibility(View.VISIBLE);
                else
                    holder.myPurchasePlanBinding.linearLectures.setVisibility(View.GONE);
                if (mainBean.getPlanMetadataBean().getPlanTypes().getQuestionBank() == 1)
                    holder.myPurchasePlanBinding.linearQBank.setVisibility(View.VISIBLE);
                else
                    holder.myPurchasePlanBinding.linearQBank.setVisibility(View.GONE);
                if (mainBean.getPlanMetadataBean().getPlanTypes().getTests() == 1)
                    holder.myPurchasePlanBinding.linearTests.setVisibility(View.VISIBLE);
                else
                    holder.myPurchasePlanBinding.linearTests.setVisibility(View.GONE);

            } else {
                holder.myPurchasePlanBinding.textName.setText(String.format("%s %s", mainBean.getPlanDetails().getPlanName(),
                        mainBean.getPlanDetails().getPlanTypes().getTitle()));
                holder.myPurchasePlanBinding.textName.setText(String.format("%s %s", mainBean.getPlanDetails().getPlanName(),
                        mainBean.getPlanMetadataBean().getPlanTypes().getTitle()));
                if (mainBean.getPlanDetails().getPlanTypes().getVideos() == 1)
                    holder.myPurchasePlanBinding.linearLectures.setVisibility(View.VISIBLE);
                else
                    holder.myPurchasePlanBinding.linearLectures.setVisibility(View.GONE);
                if (mainBean.getPlanDetails().getPlanTypes().getQuestionBank() == 1)
                    holder.myPurchasePlanBinding.linearQBank.setVisibility(View.VISIBLE);
                else
                    holder.myPurchasePlanBinding.linearQBank.setVisibility(View.GONE);
                if (mainBean.getPlanDetails().getPlanTypes().getTests() == 1)
                    holder.myPurchasePlanBinding.linearTests.setVisibility(View.VISIBLE);
                else
                    holder.myPurchasePlanBinding.linearTests.setVisibility(View.GONE);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemMyPurchasePlanBinding myPurchasePlanBinding;

        ViewHolder(@NonNull ItemMyPurchasePlanBinding binding) {
            super(binding.getRoot());
            this.myPurchasePlanBinding = binding;
        }
    }
}
