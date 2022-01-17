package com.flytrom.learning.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.model.MainCategorey.DataCategory;
import com.flytrom.learning.model.progressModel.ProgressData;
import com.flytrom.learning.utils.Constants;
import com.skydoves.progressview.ProgressView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import hiennguyen.me.circleseekbar.CircleSeekBar;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ViewHolder> {
    Context context;
    List<ProgressData> data;

    public ProgressAdapter(Context context, List<ProgressData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @NotNull
    @Override
    public ProgressAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.ptogress_list, parent, false);
        ProgressAdapter.ViewHolder viewHolder = new ProgressAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProgressAdapter.ViewHolder holder, int position) {
        holder.progress_barQBank.getProgressDrawable().mutate();
        if (data.get(position).getSubject_details().getVideos_visibility().equalsIgnoreCase("1")) {
            holder.lecCard.setVisibility(View.GONE);
            holder.lectureRL.setVisibility(View.VISIBLE);
        } else {
            holder.lecCard.setVisibility(View.VISIBLE);
            holder.lectureRL.setVisibility(View.GONE);
        }
        if (data.get(position).getSubject_details().getQuestion_bank_visibility().equalsIgnoreCase("1")) {
            holder.qBankCard.setVisibility(View.GONE);
            holder.qBankRL.setVisibility(View.VISIBLE);
            holder.progressView2.setVisibility(View.VISIBLE);
        } else {
            holder.qBankCard.setVisibility(View.VISIBLE);
            holder.qBankRL.setVisibility(View.GONE);
            holder.progressView2.setVisibility(View.GONE);
            holder.accuracyTxt.setVisibility(View.GONE);
        }
        if (data.get(position).getSubject_details().getTests_visibility().equalsIgnoreCase("1")) {
            holder.testCard.setVisibility(View.GONE);
            holder.tstRL.setVisibility(View.VISIBLE);
            holder.progressView3.setVisibility(View.VISIBLE);
        } else {
            holder.testCard.setVisibility(View.VISIBLE);
            holder.tstRL.setVisibility(View.GONE);
            holder.progressView3.setVisibility(View.GONE);
            holder.accuracyTxt2.setVisibility(View.GONE);
        }
        String valueTest = String.valueOf(data.get(position).getTest_accuracy());
        String valueQbank = String.valueOf(data.get(position).getQb_accuracy());
        if (data.get(position).getQb_completion_percent().equalsIgnoreCase("0") || data.get(position).getQb_completion_percent().isEmpty()) {
            holder.progress_barQBank.setProgress(0);
        }else {
            holder.progress_barQBank.setProgress(Integer.parseInt(data.get(position).getQb_completion_percent()));
        }
        holder.progress_barTest.setProgress(Integer.parseInt(data.get(position).getTest_completion_percent()));
        holder.progress_bar.setProgress(Integer.parseInt(data.get(position).getVideo_completion_percent()));
        holder.textVideo.setText(data.get(position).getVideo_completion_percent() + "%");
        holder.textQBank.setText(data.get(position).getQb_completion_percent() + "%");
        holder.textTest.setText(data.get(position).getTest_completion_percent() + "%");
        holder.progressView2.setProgress(Float.valueOf(valueQbank));
        holder.progressView3.setProgress(Float.parseFloat(valueTest));
        holder.progressView3.setLabelText(valueTest + "%");
        holder.progressView2.setLabelText(valueQbank + "%");
        holder.nameProgress.setText(data.get(position).getSubject());
        holder.videoTotal.setText(data.get(position).getVideo_completed() + " out of " + data.get(position).getVideo_total() + " Lectures");
        holder.QbankTotal.setText(data.get(position).getQb_completed() + " out of " + data.get(position).getQb_total() + " Questions");
        holder.testTotal.setText(data.get(position).getTest_completed() + " out of " + data.get(position).getTest_total() + " Test Questions");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleSeekBar circular, circular2, circular3;
        ProgressBar progress_bar, progress_barQBank, progress_barTest;
        TextView textVideo, textQBank, textTest, nameProgress, videoTotal, QbankTotal, testTotal, accuracyTxt, accuracyTxt2;
        ProgressView progressView, progressView2, progressView3;
        LinearLayout qBankRL, lectureRL, tstRL;
        CardView lecCard, qBankCard, testCard;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            progress_bar = itemView.findViewById(R.id.progress_bar);
            progress_barQBank = itemView.findViewById(R.id.progress_barQBank);
            progress_barTest = itemView.findViewById(R.id.progress_barTest);
            textVideo = itemView.findViewById(R.id.textVideo);
            textQBank = itemView.findViewById(R.id.textQBank);
            textTest = itemView.findViewById(R.id.textTest);
            progressView2 = itemView.findViewById(R.id.progressView2);
            progressView3 = itemView.findViewById(R.id.progressView3);
            nameProgress = itemView.findViewById(R.id.nameProgress);
            videoTotal = itemView.findViewById(R.id.videoTotal);
            QbankTotal = itemView.findViewById(R.id.QbankTotal);
            testTotal = itemView.findViewById(R.id.testTotal);

            qBankRL = itemView.findViewById(R.id.qBankRL);
            lectureRL = itemView.findViewById(R.id.lectureRL);
            tstRL = itemView.findViewById(R.id.tstRL);

            qBankCard = itemView.findViewById(R.id.qBankCard);
            lecCard = itemView.findViewById(R.id.lecCard);
            testCard = itemView.findViewById(R.id.testCard);

            accuracyTxt = itemView.findViewById(R.id.accuracyTxt);
            accuracyTxt2 = itemView.findViewById(R.id.accuracyTxt2);


        }
    }
}
