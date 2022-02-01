package com.flytrom.learning.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.model.CommentModel.SubComment;
import com.flytrom.learning.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetSubCommentAdapterMore extends RecyclerView.Adapter<GetSubCommentAdapterMore.ViewHolder> {
    Context context;
    List<SubComment> data;
    public GetSubCommentAdapterMore(Context context, List<SubComment> data) {
        this.context =context;
        this.data = data;
    }

    @NonNull
    @NotNull
    @Override
    public GetSubCommentAdapterMore.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.holder_sub_comments, parent, false);
        GetSubCommentAdapterMore.ViewHolder viewHolder = new GetSubCommentAdapterMore.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GetSubCommentAdapterMore.ViewHolder holder, int position) {

            holder.tv_user_name.setText(data.get(position).getUser_name());
            holder.tv_commnet.setText(data.get(position).getComment());
            Glide.with(context).load(Constants.MEDIA_URL + data.get(position).getUser_picture()).into(holder.iv_profile);
            Log.d("FCGJFC",data.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user_name,tv_commnet,tv_time;
        CircleImageView iv_profile;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_commnet = itemView.findViewById(R.id.tv_commnet);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_time = itemView.findViewById(R.id.titleQbank);
        }
    }
}
