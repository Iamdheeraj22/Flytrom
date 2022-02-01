package com.flytrom.learning.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetSubCommentAdapter extends RecyclerView.Adapter<GetSubCommentAdapter.ViewHolder> {
    PlayVideoActivity context;
    List<SubComment> data;
    public GetSubCommentAdapter(PlayVideoActivity context, List<SubComment> data) {
        this.context =context;
        this.data = data;
    }

    @NonNull
    @NotNull
    @Override
    public GetSubCommentAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.holder_sub_comments, parent, false);
        GetSubCommentAdapter.ViewHolder viewHolder = new GetSubCommentAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GetSubCommentAdapter.ViewHolder holder, int position) {


        holder.tv_commnet.setText(data.get(position).getComment());

        if(data.get(position).getUser_type().equalsIgnoreCase("Admin")){
            holder.tv_user_name.setText(data.get(position).getAdmin_comment_name());
            holder.tv_commnet.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            if (data.get(position).getAdmin_image_name() == null || data.get(position).getAdmin_image_name().equalsIgnoreCase("")) {
                holder.iv_profile.setBackgroundResource(R.drawable.ic_user_placeholder);
            } else {
                Glide.with(context).load(Constants.MEDIA_URL + data.get(position).getAdmin_image_name()).into(holder.iv_profile);
            }
        }else {
            holder.tv_user_name.setText(data.get(position).getUser_name());
            if (data.get(position).getUser_picture() == null || data.get(position).getUser_picture().equalsIgnoreCase("")) {
                holder.iv_profile.setBackgroundResource(R.drawable.ic_user_placeholder);
            } else {
                Glide.with(context).load(Constants.MEDIA_URL + data.get(position).getUser_picture()).into(holder.iv_profile);
            }
        }



        if (data.get(position).getUser_id().equalsIgnoreCase(MySharedPreferences.getInstance().getString(context, ConstantsNew.USER_ID))){
            holder.tv_edit_sub.setVisibility(View.VISIBLE);
        }else {
            holder.tv_edit_sub.setVisibility(View.GONE);
        }
        holder.tv_edit_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, holder.tv_edit_sub);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().toString().equalsIgnoreCase("Edit")){
                            context.showEdt(data.get(position).getId(),data.get(position).getComment());
                        }else {
                            context.showDlt(data.get(position).getId());
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }

        });
       // if (data.size() <= 2){
            if (data.get(position).getUser_type()!=null) {
                if (data.get(position).getUser_type().equalsIgnoreCase("Admin")) {
                    holder.tv_user_name.setTextColor(Color.parseColor("#039B3B"));
                } else {
                    holder.tv_user_name.setTextColor(Color.parseColor("#000000"));
                }
            }



        Log.d("FCGJFC",data.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user_name,tv_commnet,tv_time,tv_edit_sub;
        CircleImageView iv_profile;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_commnet = itemView.findViewById(R.id.tv_commnet);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_time = itemView.findViewById(R.id.titleQbank);
            tv_edit_sub = itemView.findViewById(R.id.tv_edit_sub);


        }
    }
}
