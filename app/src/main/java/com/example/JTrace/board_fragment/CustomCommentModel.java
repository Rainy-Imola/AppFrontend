package com.example.JTrace.board_fragment;

import com.jidcoo.android.widget.commentview.model.AbstractCommentModel;
import com.jidcoo.android.widget.commentview.model.CommentEnable;
import com.jidcoo.android.widget.commentview.model.ReplyEnable;

import java.util.List;

public class CustomCommentModel  extends AbstractCommentModel<CustomCommentModel.CustomComment> {


    public List<CustomComment> comments;
    @Override
    public List<CustomComment> getComments() {
        return comments;
    }

    public void setComments(List<CustomComment> comments) {
        this.comments = comments;
    }


    public static class CustomComment extends CommentEnable {
        public List<CustomReply> replies;
        public String posterName;
        public String data;

        public CustomComment() {

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
            public String replierName;
            public String data;
            public CustomReply() {

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
