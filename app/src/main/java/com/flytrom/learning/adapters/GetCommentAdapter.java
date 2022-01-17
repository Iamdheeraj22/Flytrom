package com.flytrom.learning.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.video_menu.PlayVideoActivity;
import com.flytrom.learning.model.CommentModel.GetCommentDatum;
import com.flytrom.learning.model.CommentModel.SubComment;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetCommentAdapter extends RecyclerView.Adapter<GetCommentAdapter.ViewHolder> {
    PlayVideoActivity context;
    List<GetCommentDatum> data;
    OnClickComment callBackNew;
    OnClickEdit callOnClickEdit;

    public interface OnClickComment {
        public void onitemClickVideo(String id);
    }

    public interface OnClickEdit {
        public void onClickEdit(String id, CharSequence title, String comment);
    }

    public GetCommentAdapter(PlayVideoActivity context, List<GetCommentDatum> data, OnClickComment callBackNew, OnClickEdit callOnClickEdit) {
        this.context = context;
        this.data = data;
        this.callBackNew = callBackNew;
        this.callOnClickEdit = callOnClickEdit;
    }

    public void setList(List<GetCommentDatum> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public GetCommentAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.comment_layout, parent, false);
        GetCommentAdapter.ViewHolder viewHolder = new GetCommentAdapter.ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GetCommentAdapter.ViewHolder holder, int position) {

        try {
            holder.tv_user_name.setText(data.get(position).getUser_name());
            holder.tv_commnet.setText(data.get(position).getComment());

            if (data.get(position).getUser_type().equalsIgnoreCase("Admin")) {
                holder.tv_user_name.setTextColor(Color.parseColor("#039B3B"));
            } else {
                holder.tv_user_name.setTextColor(Color.parseColor("#000000"));
            }
            if (data.get(position).getUser_id().equalsIgnoreCase(MySharedPreferences.getInstance().getString(context, ConstantsNew.USER_ID))) {
                holder.tv_edit.setVisibility(View.VISIBLE);
            } else {
                holder.tv_edit.setVisibility(View.GONE);
            }

            if (data.get(position).getUser_picture() == null || data.get(position).getUser_picture().equalsIgnoreCase("")) {
                holder.iv_profile.setBackgroundResource(R.drawable.ic_user_placeholder);
            } else {
                Glide.with(context).load(Constants.MEDIA_URL + data.get(position).getUser_picture()).into(holder.iv_profile);
            }
            List<SubComment> sublist = data.get(position).getSub_comments();
            Collections.reverse(sublist);
            if (sublist.size() > 0) {
                holder.rv_sub_comments.setVisibility(View.VISIBLE);
                GetSubCommentAdapter getSubCommentAdapter;
                holder.rv_sub_comments.setLayoutManager(new LinearLayoutManager(context));
                getSubCommentAdapter = new GetSubCommentAdapter(context, sublist);
                holder.rv_sub_comments.setItemAnimator(new DefaultItemAnimator());
                holder.rv_sub_comments.setAdapter(getSubCommentAdapter);

            } else {
                holder.rv_sub_comments.setVisibility(View.GONE);
            }


        } catch (Exception e) {

        }


        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.tv_edit);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().toString().equalsIgnoreCase("Edit")) {
                            callOnClickEdit.onClickEdit(data.get(position).getId(), item.getTitle(), data.get(position).getComment());
//                            Toast.makeText(context,"You Clicked Edit : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        } else {
                            callOnClickEdit.onClickEdit(data.get(position).getId(), item.getTitle(), data.get(position).getComment());
//                            Toast.makeText(context,"You Clicked Delete : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }

        });

        holder.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBackNew.onitemClickVideo(data.get(position).getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user_name, tv_commnet, tv_time, tvReply, tv_edit;
        CircleImageView iv_profile;
        RecyclerView rv_sub_comments;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_commnet = itemView.findViewById(R.id.tv_commnet);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_time = itemView.findViewById(R.id.titleQbank);
            rv_sub_comments = itemView.findViewById(R.id.rv_sub_comments);
            tvReply = itemView.findViewById(R.id.tvReply);
            tv_edit = itemView.findViewById(R.id.tv_edit);

        }
    }
}
