package com.flytrom.learning.model.CommentModel;

import java.util.List;

public class GetCommentDatum {
    public String id;
    public String video_id;
    public String user_id;
    public String comment;
    public String link_id;
    public String created_at;
    public String admin_comment_name;
    public String admin_image_name;
    public String user_name;
    public String user_picture;
    public String user_type;
    public String total_comments;
    public String is_approve;
    public String file_for_comment;
    public List<SubComment> sub_comments;

    public String getAdmin_comment_name() {
        return admin_comment_name;
    }

    public void setAdmin_comment_name(String admin_comment_name) {
        this.admin_comment_name = admin_comment_name;
    }

    public String getAdmin_image_name() {
        return admin_image_name;
    }

    public void setAdmin_image_name(String admin_image_name) {
        this.admin_image_name = admin_image_name;
    }

    public String getFile_for_comment() {
        return file_for_comment;
    }

    public void setFile_for_comment(String file_for_comment) {
        this.file_for_comment = file_for_comment;
    }

    public String getIs_approve() {
        return is_approve;
    }

    public void setIs_approve(String is_approve) {
        this.is_approve = is_approve;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public List<SubComment> getSub_comments() {
        return sub_comments;
    }

    public void setSub_comments(List<SubComment> sub_comments) {
        this.sub_comments = sub_comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }

    public String getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(String total_comments) {
        this.total_comments = total_comments;
    }
}
