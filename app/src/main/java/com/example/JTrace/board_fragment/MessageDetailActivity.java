package com.example.JTrace.board_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.JTrace.R;

import com.example.JTrace.custom_comment.CustomCommentViewHolder;
import com.example.JTrace.custom_comment.CustomReplyViewHolder;
import com.example.JTrace.friends_fragment.FriendDetailActivity;
import com.example.JTrace.model.message;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.jidcoo.android.widget.commentview.CommentView;
import com.jidcoo.android.widget.commentview.callback.CustomCommentItemCallback;
import com.jidcoo.android.widget.commentview.callback.CustomReplyItemCallback;
import com.jidcoo.android.widget.commentview.callback.OnCommentLoadMoreCallback;
import com.jidcoo.android.widget.commentview.callback.OnItemClickCallback;
import com.jidcoo.android.widget.commentview.callback.OnPullRefreshCallback;
import com.jidcoo.android.widget.commentview.callback.OnReplyLoadMoreCallback;
import com.jidcoo.android.widget.commentview.defaults.DefaultCommentModel;
import com.loper7.layout.TitleBar;
import com.yalantis.ucrop.util.ScreenUtils;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MessageDetailActivity extends AppCompatActivity {


    private TextView contentView,authorView,dateView;
    private EditText commentPostEditTextView;
    private Button commentPostButton;
    private SimpleDraweeView coverView;
    private message msg;
    private Gson gson;

    private Context context;
    private SharedPreferences sp;
    private int author_id;
    private commentViewModel mCommentViewModel;
    private String token;
    private String author;

    private CommentView commentView;
    private InputMethodManager inputMethodManager;
    private final ActivityHandler activityHandler = new ActivityHandler(this);


    private TitleBar mTitleBar;
    private ConstraintLayout constraintLayout;
    private ConstraintLayout constraintLayout_header;
    private boolean isReply = false;
    private boolean isChildReply = false;
    private long reply_id;
    private long pid;
    private int cp, rp;

    private MutableLiveData<Integer> status_bar = new MutableLiveData<Integer>();
    public MutableLiveData<Integer> getStatus_bar() {
        return status_bar;
    }

    public void setStatus_bar(int status_bar) {
        this.status_bar.postValue(status_bar);
    }

    static class ActivityHandler extends Handler {
        private final WeakReference<MessageDetailActivity> mActivity;

        public ActivityHandler(MessageDetailActivity activity) {
            mActivity = new WeakReference<MessageDetailActivity>(activity);
        }


        @Override
        public void handleMessage(Message msg) {
            MessageDetailActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        //commentView.loadFailed(true);//实际网络请求中如果加载失败调用此方法
                        activity.commentView.loadComplete((CustomCommentModel) msg.obj);
                        break;
                    case 2:
                        //commentView.refreshFailed();//实际网络请求中如果加载失败调用此方法
                        activity.commentView.refreshComplete((CustomCommentModel) msg.obj);

                        break;
                    case 3:
                        //commentView.loadFailed();//实际网络请求中如果加载失败调用此方法
                        activity.commentView.loadMoreComplete((CustomCommentModel) msg.obj);
                        break;
                    case 4:
                        //commentView.loadMoreReplyFailed();//实际网络请求中如果加载失败调用此方法
                        activity.commentView.loadMoreReplyComplete((CustomCommentModel) msg.obj);
                        break;
                }
            }
        }

    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommentViewModel = new ViewModelProvider(this).get(commentViewModel.class);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_message_detail);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_item_header,null);

        constraintLayout = view.findViewById(R.id.constraintLayout_header);
        constraintLayout_header = view.findViewById(R.id.comment_header);

        Intent intent=getIntent();
        gson = new Gson();
        commentView = findViewById(R.id.myCommentView);
        mTitleBar = findViewById(R.id.title_b);
        msg= (message) intent.getSerializableExtra("message");
        mCommentViewModel.setMessage_id(msg.getId());
        contentView=view.findViewById(R.id.contentDetail);
        coverView=view.findViewById(R.id.cover);
        commentPostButton = findViewById(R.id.button);
        commentPostEditTextView = findViewById(R.id.editor);

        authorView=view.findViewById(R.id.author_textView);
        dateView=view.findViewById(R.id.date_textView);
        ConstraintLayout.LayoutParams content_params = new ConstraintLayout.LayoutParams(contentView.getLayoutParams());

        context=this;



        sp=getSharedPreferences("user_profile",MODE_PRIVATE);
        author_id=sp.getInt("id",-1);
        author=sp.getString("username","defaultAuthor");
        token=sp.getString("token",null);
        mCommentViewModel.getMessageMutableLiveData().observe(this, new Observer<message>() {
            @Override
            public void onChanged(message message) {
                contentView.setText(message.getContent());
                authorView.setText(msg.getAuthor());
                if(message.getImageUrl().equals("")){
                    constraintLayout.removeView(coverView);
                    content_params.width = ScreenUtils.dip2px(context,0);
                    content_params.horizontalBias = 0;
                    content_params.bottomToTop= R.id.view2;
                    content_params.startToStart = R.id.author_textView;
                    content_params.endToEnd = R.id.constraintLayout_header;
                    content_params.topToBottom= R.id.author_textView;

                    content_params.topMargin = ScreenUtils.dip2px(context,24);
                    content_params.rightMargin = ScreenUtils.dip2px(context,24);
                    content_params.bottomMargin = ScreenUtils.dip2px(context,16);
                    contentView.setLayoutParams(content_params);

Log.d("hidde",message.getImageUrl());
                }else {
                    coverView.setImageURI(message.getImageUrl());
                }
                dateView.setText(message.getCreatedAt());
            }
        });


        commentView.setViewStyleConfigurator(new com.jidcoo.android.widgettest.custom.CustomViewStyleConfigurator(this));
        commentView.addHeaderView(constraintLayout_header,true);
        commentView.callbackBuilder()
                //自定义评论布局(必须使用ViewHolder机制)--CustomCommentItemCallback<C> 泛型C为自定义评论数据类
                .customCommentItem(new CustomCommentItemCallback<CustomCommentModel.CustomComment>() {
                    @Override
                    public View buildCommentItem(int groupPosition, CustomCommentModel.CustomComment comment, LayoutInflater inflater, View convertView, ViewGroup parent) {
                        //使用方法就像adapter里面的getView()方法一样
                        final CustomCommentViewHolder holder;
                        if (convertView == null) {
                            //使用自定义布局
                            convertView = inflater.inflate(R.layout.custom_item_comment, parent, false);
                            holder = new CustomCommentViewHolder(convertView);
                            //必须使用ViewHolder机制
                            convertView.setTag(holder);
                        } else {
                            holder = (CustomCommentViewHolder) convertView.getTag();
                        }
                        holder.prizes.setText("100");
                        holder.userName.setText(comment.getPosterName());
                        holder.comment.setText(comment.getData());
                        return convertView;
                    }
                })
                //自定义评论布局(必须使用ViewHolder机制）
                // 并且自定义ViewHolder类必须继承自com.jidcoo.android.widget.commentview.view.ViewHolder
                // --CustomReplyItemCallback<R> 泛型R为自定义回复数据类
                .customReplyItem(new CustomReplyItemCallback<CustomCommentModel.CustomComment.CustomReply>() {
                    @Override
                    public View buildReplyItem(int groupPosition, int childPosition, boolean isLastReply, CustomCommentModel.CustomComment.CustomReply reply, LayoutInflater inflater, View convertView, ViewGroup parent) {
                        //使用方法就像adapter里面的getView()方法一样
                        //此类必须继承自com.jidcoo.android.widget.commentview.view.ViewHolder，否则报错
                        CustomReplyViewHolder holder = null;
                        //此类必须继承自com.jidcoo.android.widget.commentview.view.ViewHolder，否则报错
                        if (convertView == null) {
                            //使用自定义布局
                            convertView = inflater.inflate(R.layout.custom_item_reply, parent, false);
                            holder = new CustomReplyViewHolder(convertView);
                            //必须使用ViewHolder机制
                            convertView.setTag(holder);
                        } else {
                            holder = (CustomReplyViewHolder) convertView.getTag();
                        }
                        holder.userName.setText(reply.getReplierName());
                        holder.reply.setText(reply.getData());
                        holder.prizes.setText("100");
                        return convertView;
                    }
                })
                //下拉刷新回调
                .setOnPullRefreshCallback(new MyOnPullRefreshCallback())
                //评论、回复Item的点击回调（点击事件回调）
                .setOnItemClickCallback(new MyOnItemClickCallback())
                //回复数据加载更多回调（加载更多回复）
                .setOnReplyLoadMoreCallback(new MyOnReplyLoadMoreCallback())
                //上拉加载更多回调（加载更多评论数据）
                .setOnCommentLoadMoreCallback(new MyOnCommentLoadMoreCallback())
                //设置完成后必须调用CallbackBuilder的buildCallback()方法，否则设置的回调无效
                .buildCallback();
        load(1, 1);


        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(),0);
                commentPostEditTextView.setText("");
                return false;
            }
        });

        authorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageDetailActivity.this, "你点击名字：", Toast.LENGTH_SHORT).show();
            }
        });
        coverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageDetailActivity.this, "你点击图片：", Toast.LENGTH_SHORT).show();
            }
        });
        commentPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content=commentPostEditTextView.getText().toString();
                if(content.isEmpty()){
                    Toast.makeText(context,"评论不可以为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isReply && isChildReply) {
                    //现在需要构建一个回复数据实体类
                    CustomCommentModel.CustomComment.CustomReply reply = new CustomCommentModel.CustomComment.CustomReply();
//                    reply.setKid(fid);
                    reply.setReplierName(msg.getAuthor());
                    reply.setData(content);
//                    reply.setDate(System.currentTimeMillis());
//                    reply.setPid(pid);
                    commentView.addReply(reply, cp);
                    inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(),0);
                    commentPostEditTextView.setText("");
                } else if (isReply && !isChildReply) {
                    //现在需要构建一个回复数据实体类
                    CustomCommentModel.CustomComment.CustomReply reply = new CustomCommentModel.CustomComment.CustomReply();
//                    reply.setKid(fid);
                    reply.setReplierName(msg.getAuthor());
                    reply.setData(content);
//                    reply.setDate(System.currentTimeMillis());
//                    reply.setPid(0);
                    commentView.addReply(reply, cp);
                    inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(),0);
                    commentPostEditTextView.setText("");
                } else {
                    CustomCommentModel.CustomComment comment = new CustomCommentModel.CustomComment();
//                    comment.setFid(System.currentTimeMillis());
//                    comment.setId(comment.getFid() + 1);
//                    comment.setDate(comment.getFid());
//                    comment.setPid(0);
                    comment.setPosterName(msg.getAuthor());
                    comment.setData(content);
                    commentView.addComment(comment);
                    inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(),0);
                    commentPostEditTextView.setText("");
                    mCommentViewModel.postMessageComment(comment);
                }
                Date date=new Date(System.currentTimeMillis());

            }
        });
        mTitleBar.setOnBackListener(new TitleBar.OnBackListener() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        authorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), FriendDetailActivity.class);
                intent.putExtra("name",msg.getAuthor());
                v.getContext().startActivity(intent);
            }
        });
    }
    private void load(int code, int handlerId) {
        mCommentViewModel.getCommentModel(code, activityHandler, handlerId);
    }

    /**
     * 下拉刷新回调类
     */
    class MyOnPullRefreshCallback implements OnPullRefreshCallback {

        @Override
        public void refreshing() {
            load(1, 2);


        }

        @Override
        public void complete() {
            //加载完成后的操作
        }

        @Override
        public void failure(String msg) {

        }
    }


    /**
     * 上拉加载更多回调类
     */
    class MyOnCommentLoadMoreCallback implements OnCommentLoadMoreCallback {

        @Override
        public void loading(int currentPage, int willLoadPage, boolean isLoadedAllPages) {
            //因为测试数据写死了，所以这里的逻辑也是写死的
            /*
            if (!isLoadedAllPages) {
                if (willLoadPage == 2) {
                    load(2, 3);
                } else if (willLoadPage == 3) {
                    load(3, 3);
                }
            }

             */
        }

        @Override
        public void complete() {
            //加载完成后的操作
        }

        @Override
        public void failure(String msg) {
        }
    }

    /**
     * 回复加载更多回调类
     */
    class MyOnReplyLoadMoreCallback implements OnReplyLoadMoreCallback<CustomCommentModel.CustomComment.CustomReply> {


        @Override
        public void loading(CustomCommentModel.CustomComment.CustomReply reply, int willLoadPage) {
/*
            if (willLoadPage == 2) {
                load(5, 4);
            } else if (willLoadPage == 3) {
                load(6, 4);
            }

 */
        }

        @Override
        public void complete() {

        }

        @Override
        public void failure(String msg) {

        }
    }

    /**
     * 点击事件回调
     */
    class MyOnItemClickCallback implements OnItemClickCallback<CustomCommentModel.CustomComment, CustomCommentModel.CustomComment.CustomReply> {


        @Override
        public void commentItemOnClick(int position, CustomCommentModel.CustomComment comment, View view) {
            cp = position;
            isChildReply = false;
            Toast.makeText(MessageDetailActivity.this, "你点击的评论：" + comment.getData(), Toast.LENGTH_SHORT).show();
            inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(),0);
            commentPostEditTextView.setText("");
        }

        @Override
        public void replyItemOnClick(int c_position, int r_position, CustomCommentModel.CustomComment.CustomReply reply, View view) {
            cp = c_position;
            rp = r_position;
            isChildReply = true;
            Toast.makeText(MessageDetailActivity.this, "你点击的回复：" + reply.getData(), Toast.LENGTH_SHORT).show();
            inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(),0);
            commentPostEditTextView.setText("");
        }
    }


/*
        authorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求，拿到作者id
                new Thread(){
                    @Override
                    public void run(){
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request request = new Request.Builder().url(Constants.baseUrl + "/users/"+
                                msg.getAuthor()+"/info/")
                                .addHeader("Authorization", token)
                                .build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("requestUsr","请求用户信息失败！");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String res=response.body().string();
                                Log.d("requestUsr",res);
                                try {
                                    JSONObject result=new JSONObject(res);
                                    JSONArray data=result.getJSONArray("data");
                                    JSONObject mid=data.getJSONObject(0);
                                    int id=mid.getInt("id");
                                    Intent intent=new Intent(v.getContext(), FriendDetailActivity.class);
                                    intent.putExtra("id",id);
                                    intent.putExtra("name",msg.getAuthor());
                                    v.getContext().startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                }.start();
            }
        });


        mCommentViewModel=new ViewModelProvider(this).get(commentViewModel.class);
        refreshCommentData();
        */
    }
