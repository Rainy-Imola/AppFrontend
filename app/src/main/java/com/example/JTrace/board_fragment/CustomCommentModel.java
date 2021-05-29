package com.example.JTrace.board_fragment;

import com.jidcoo.android.widget.commentview.model.AbstractCommentModel;
import com.jidcoo.android.widget.commentview.model.CommentEnable;
import com.jidcoo.android.widget.commentview.model.ReplyEnable;

import java.util.List;

public class CustomCommentModel extends AbstractCommentModel<CustomCommentModel.CustomComment> {

    public List<CustomComment> comments;

    @Override
    public List<CustomComment> getComments() {
        return comments;
    }

    public void setComments(List<CustomComment> comments) {
        this.comments = comments;
    }


    public static class CustomComment extends CommentEnable {
        public Integer id;
        public List<CustomReply> replies;
        public String posterName;
        public String data;


        public String date;

        public CustomComment() {

        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setReplies(List<CustomReply> replies) {
            this.replies = replies;
        }

        public String getPosterName() {
            return posterName;
        }

        public void setPosterName(String posterName) {
            this.posterName = posterName;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }


        @Override
        public List<CustomReply> getReplies() {
            return replies;
        }

        public static class CustomReply extends ReplyEnable {
            public String date;
            public Integer id;
            public String repliedName;
            public String replierName;
            public String data;

            public Integer getComment_id() {
                return comment_id;
            }

            public void setComment_id(Integer comment_id) {
                this.comment_id = comment_id;
            }

            public Integer comment_id;


            public Integer level;

            public CustomReply() {

            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getRepliedName() {
                return repliedName;
            }

            public void setRepliedName(String repliedName) {
                this.repliedName = repliedName;
            }

            public Integer getLevel() {
                return level;
            }

            public void setLevel(Integer level) {
                this.level = level;
            }

            public String getReplierName() {
                return replierName;
            }

            public void setReplierName(String replierName) {
                this.replierName = replierName;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

        }
    }
}
