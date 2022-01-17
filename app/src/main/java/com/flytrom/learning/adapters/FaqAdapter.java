package com.flytrom.learning.adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.model.DatumFaq;
import com.flytrom.learning.model.MainCategorey.DataCategory;
import com.flytrom.learning.utils.Constants;
import com.google.android.exoplayer2.C;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
    List<DatumFaq> datumFaqs;
    Context context;
    boolean check = false;

    public FaqAdapter(List<DatumFaq> datumFaqs, Context context) {
        this.datumFaqs = datumFaqs;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public FaqAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.faq_list, parent, false);
        FaqAdapter.ViewHolder viewHolder = new FaqAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FaqAdapter.ViewHolder holder, int position) {
       holder.titleFaq.setText(datumFaqs.get(position).getQuestion());
       holder.ans.setText(Html.fromHtml(datumFaqs.get(position).getAnswer()));

       holder.llfaq.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               holder.ans.setVisibility(holder.ans.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

               holder.show.setVisibility(holder.ans.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

           }
       });
       holder.faqAns.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               holder.ans.setVisibility(holder.ans.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

               holder.show.setVisibility(holder.ans.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

           }
       });
    }

    @Override
    public int getItemCount() {
        return datumFaqs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ans,titleFaq;
        CardView faqAns;
        ImageView show,showHit;
        LinearLayout llfaq;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ans = itemView.findViewById(R.id.ans);
            titleFaq = itemView.findViewById(R.id.titleFaq);
            faqAns = itemView.findViewById(R.id.faqAns);
            show = itemView.findViewById(R.id.show);
            showHit = itemView.findViewById(R.id.showHit);
            llfaq = itemView.findViewById(R.id.llfaq);
//            rlCategory = itemView.findViewById(R.id.rlCategory);
//            titleQbank = itemView.findViewById(R.id.titleQbank);
//            card = itemView.findViewById(R.id.card);
        }
    }
}
