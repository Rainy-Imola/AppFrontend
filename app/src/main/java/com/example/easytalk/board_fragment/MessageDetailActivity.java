package com.example.easytalk.board_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easytalk.Constants;
import com.example.easytalk.R;
import com.example.easytalk.model.comment;
import com.example.easytalk.model.message;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MessageDetailActivity extends AppCompatActivity {
    //TODO: post comment
    //TODO: click author name to author information layout
    private TextView contentView,authorView,dateView;
    private EditText commentPostEditTextView;
    private Button commentPostButton;
    private SimpleDraweeView coverView;
    private message msg;
    private commentAdapter mCommentAdapter;
    private RecyclerView CommentRecyclerView;
    private List<comment> comments=new ArrayList<>();
    private commentViewModel mCommentViewModel;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        Intent intent=getIntent();
        msg= (message) intent.getSerializableExtra("message");
        contentView=findViewById(R.id.contentDetail);
        coverView=findViewById(R.id.cover);
        /*测试长文本
        contentView.setText("这是一个以光速往前发展的城市。这是一个浩瀚的巨大时代。这是一个像是地下迷宫一样错综复杂的城市。这是一个匕首般锋利的冷漠时代。我们躺在自己小小的被窝里，我们微茫得几乎什么都不是。当我在这个又浩瀚又锋利的时代里，被早晨尖锐的闹钟唤醒了50%的灵魂之后，我凭借着自己的顽强的求生本能，把闹钟往远方一推。然后一片满意的宁静。但结果是，昨天晚上浇花后因为懒惰而没有放回厕所的水桶被我遗忘在床边上，在我半小时后尖叫着醒来时，我看见了安静地躺在水桶里的那个闹钟，然后我尖叫了第二声。我拿着闹钟放到阳台上，希望水分蒸发之后它还能坚强地挺住。为了加速水分的蒸发，我拿着闹钟猛甩几下，想要把水分从里面甩出来。但当我停下来的时候，发现闹钟背后的盖子神奇地不翼而飞，然后楼下传来了一个中年女人的尖叫，哦哟，要死啊！而上一次听到这句话是在我把一张重达10公斤的棉被从阳台上掉下去的时候。南湘从公车上下来后慢悠悠地朝学校走去。沿路是很多新鲜而亢奋的面孔。每一年开学的时候都会有无数的新生带着激动与惶恐的心情走进这所在全中国以建筑前卫奢华同时95%都是上海本地学生而闻名的大学。走在自己前面的几个女生刚刚从计程车上下来，说实话，学校的位置并不在市中心，如果她们不是刚巧住在附近的话，那么以那笔一定会超过三位数的出租车费用来判断的话，家境富裕后面绝对不会跟上一个问号。\n" +
                "————————————————");*/
        contentView.setText(msg.getContent());
        Log.d("debug", (String) contentView.getText());
        coverView.setImageURI(msg.getImageUrl());
        authorView=findViewById(R.id.author_textView);
        dateView=findViewById(R.id.date_textView);
        authorView.setText(msg.getAuthor()+" ");
        Date date=msg.getCreatedAt();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String resDate=format.format(date);
        dateView.setText(resDate);
        commentPostEditTextView=findViewById(R.id.postCommentText);
        commentPostButton=findViewById(R.id.postCommentButton);
        commentPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postComment()){
                    Toast.makeText(MessageDetailActivity.this,"评论发布成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mContext=this;
        CommentRecyclerView=findViewById(R.id.commentRecyclerView);

        //TODO:use commentViewModel

        mCommentViewModel=new ViewModelProvider(this).get(commentViewModel.class);

        mCommentViewModel.requestData(msg.getId());
        mCommentViewModel.getStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                Log.d("comment_activity","onChanged called");
                if(status=="comment"){
                    Log.d("comment_activity","onChanged if called");
                    Log.d("comment_activity", String.valueOf(mCommentViewModel.getmComments().size()));
                    for(comment icomment:mCommentViewModel.getmComments()){
                        comments.add(icomment);
                    }
                    mCommentAdapter=new commentAdapter(comments);
                    CommentRecyclerView.setAdapter(mCommentAdapter);
                    CommentRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                }
            }
        });

    }

    public boolean postComment(){
        return true;
    }
}