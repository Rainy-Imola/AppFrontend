package com.example.JTrace.board_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.JTrace.R;

import com.example.JTrace.custom_comment.CustomCommentViewHolder;
import com.example.JTrace.custom_comment.CustomReplyViewHolder;
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
import com.loper7.layout.TitleBar;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MessageDetailActivity extends AppCompatActivity {
    //TODO: post comment
    //TODO: click author name to author information layout

    private TextView contentView,authorView,dateView;
//    private EditText commentPostEditTextView;
//    private Button commentPostButton;
    private SimpleDraweeView coverView;
    private message msg;
    private Gson gson;
//    private List<comment> comments=new ArrayList<>();
    private Context context;
    private SharedPreferences sp;
    private int author_id;
//    private commentAdapter mCommentAdapter;
//    private RecyclerView commentRecyclerView;
    private commentViewModel mCommentViewModel;
    private String token;
    private String author;
//    private Handler mHandler=new Handler();
    private CommentView commentView;
//    private InputMethodManager inputMethodManager;
    private final ActivityHandler activityHandler = new ActivityHandler(this);
    private AppBarLayout app_bar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private TitleBar mTitleBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommentViewModel = new ViewModelProvider(this).get(commentViewModel.class);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_message_detail);
        Intent intent=getIntent();
        gson = new Gson();
        commentView = findViewById(R.id.commentView);
        mTitleBar = findViewById(R.id.title_b);
        msg= (message) intent.getSerializableExtra("message");
        mCommentViewModel.setMessage_id(msg.getId());
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
        authorView.setText(msg.getAuthor());
        Date date=msg.getCreatedAt();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String resDate=format.format(date);
        dateView.setText(resDate);
        context=this;
//        commentPostEditTextView=findViewById(R.id.postCommentText);
//        commentPostButton=findViewById(R.id.postCommentButton);
//
//        commentRecyclerView=findViewById(R.id.commentRecyclerView);
//        mCommentAdapter=new commentAdapter(null);


        sp=getSharedPreferences("user_profile",MODE_PRIVATE);
        author_id=sp.getInt("id",-1);
        author=sp.getString("username","defaultAuthor");
        token=sp.getString("token",null);

        commentView.setViewStyleConfigurator(new com.jidcoo.android.widgettest.custom.CustomViewStyleConfigurator(this));
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
        app_bar = findViewById(R.id.appbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        toolbar = findViewById(R.id.toolbar3);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(Math.abs(verticalOffset) < appBarLayout.getTotalScrollRange()){
                    //collapsingToolbarLayout.setTitleEnabled(false);

                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                    setStatus_bar(0);


                }else if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
                    toolbar.setBackgroundColor(Color.parseColor("#EE6699FF"));
                    setStatus_bar(1);
                }
            }
        });
        getStatus_bar().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==0){
                    mTitleBar.setTitleText("");
                    mTitleBar.setMenuImageResource(R.drawable.menu_24_black);
                }
                else if (integer==1){
                    mTitleBar.setTitleText("deta");
                    mTitleBar.setMenuImageResource(R.drawable.menu_white_24);
                }
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
            Toast.makeText(MessageDetailActivity.this, "你点击的评论：" + comment.getData(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void replyItemOnClick(int c_position, int r_position, CustomCommentModel.CustomComment.CustomReply reply, View view) {
            Toast.makeText(MessageDetailActivity.this, "你点击的回复：" + reply.getData(), Toast.LENGTH_SHORT).show();
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
        inputMethodManager = (InputMethodManager) getSystemService(MessageDetailActivity.this.INPUT_METHOD_SERVICE);


        coverView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(),0);
                commentPostEditTextView.setText("");
                return false;
            }
        });
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(),0);
                commentPostEditTextView.setText("");
                return false;
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
                Date date=new Date(System.currentTimeMillis());
                comment comment=new comment(author,content,msg.getId(),date);

                //post comment
                HttpAPI httpAPI=new HttpAPI();
                JSONObject jsonObject=new JSONObject();
                try{
                    jsonObject.put("author",author_id);
                    jsonObject.put("content",content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    httpAPI.postApi_withToken(jsonObject, "/msgboard/" + msg.getId() + "/comments/release/", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(context,"评论上传失败",Toast.LENGTH_SHORT).show();
                            Log.e("commentPost","postFail");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //Toast.makeText(context,"接收到返回消息",Toast.LENGTH_SHORT).show();
                            String res=response.body().string();
                            Log.d("commentPost","res:"+res);

                            try {
                                JSONObject result = new JSONObject(res);
                                int status = result.getInt("status");
                                if(status!=0){
                                    Runnable postFailRunnable=new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context,"评论发布失败",Toast.LENGTH_SHORT).show();
                                        }
                                    };
                                    mHandler.post(postFailRunnable);
                                } else {
                                    Runnable postRunnable=new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context,"评论成功！",Toast.LENGTH_SHORT).show();
                                            refreshCommentData();
                                        }
                                    };
                                    mHandler.post(postRunnable);
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    },token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mCommentViewModel=new ViewModelProvider(this).get(commentViewModel.class);
        refreshCommentData();
        */
    }
/*
    public void refreshCommentData(){
        mCommentViewModel.requestData(msg.getId());
        mCommentViewModel.getStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                Log.d("comment_activity","onChanged called");
                if(status=="comment"){
                    comments.clear();
                    for(comment icomment:mCommentViewModel.getmComments()){
                        Log.d("commentView",icomment.getContent());
                        comments.add(icomment);
                    }
                    mCommentAdapter.setComments(comments);
                    commentRecyclerView.setAdapter(mCommentAdapter);
                    commentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                }

            }
        });
    }

 */

