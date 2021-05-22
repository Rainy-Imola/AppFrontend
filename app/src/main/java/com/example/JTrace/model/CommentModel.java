package com.example.JTrace.model;

import androidx.annotation.NonNull;

import com.jidcoo.android.widget.commentview.model.AbstractCommentModel;

import java.util.ArrayList;
import java.util.List;

public class CommentModel extends AbstractCommentModel<comment> {
    private List<comment> comments=new ArrayList<>();
    @Override
    public List<comment> getComments() {
        return comments;
    }
    public void setComments(List<comment> comments){
        this.comments=comments;
    }
    public CommentModel(@NonNull List<comment> comments){
        /*
        comment comment=new comment("RidiculousDoge","this is for test","1",
                new Date(System.currentTimeMillis()));
        comments.add(comment);*/
        setComments(comments);
    }
}
