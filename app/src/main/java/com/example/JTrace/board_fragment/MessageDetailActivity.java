package com.example.JTrace.board_fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.JTrace.R;

import com.example.JTrace.custom_comment.CustomCommentViewHolder;
import com.example.JTrace.custom_comment.CustomReplyViewHolder;
import com.example.JTrace.custom_comment.CustomViewStyleConfigurator;
import com.example.JTrace.friends_fragment.FriendDetailActivity;
import com.example.JTrace.model.User;
import com.example.JTrace.model.message;
import com.example.JTrace.widget.RoundImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jidcoo.android.widget.commentview.CommentView;
import com.jidcoo.android.widget.commentview.callback.CustomCommentItemCallback;
import com.jidcoo.android.widget.commentview.callback.CustomReplyItemCallback;
import com.jidcoo.android.widget.commentview.callback.OnCommentLoadMoreCallback;
import com.jidcoo.android.widget.commentview.callback.OnItemClickCallback;
import com.jidcoo.android.widget.commentview.callback.OnPullRefreshCallback;
import com.jidcoo.android.widget.commentview.callback.OnReplyLoadMoreCallback;
import com.loper7.layout.TitleBar;
import com.yalantis.ucrop.util.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class MessageDetailActivity extends AppCompatActivity {


    private TextView contentView,authorView,dateView;
    private RoundImageView author_avatar;
    private ImageView prize_image;
    private TextView prizes_message, comments_count;
    private Integer count = 0;
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
    private String default_link;
    private Map<RoundImageView,String> map_ico = new ConcurrentHashMap<>();

    private MutableLiveData<Integer> status_icon = new MutableLiveData<>();

    public MutableLiveData<Integer> getStatus_bar() {
        return status_bar;
    }

    public void setStatus_bar(int status_bar) {
        this.status_bar.postValue(status_bar);
    }

    public void logd(int i){
        Log.d("init_avatar", String.valueOf(i));
    }
    class ActivityHandler extends Handler {
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
                        activity.commentView.loadComplete((CustomCommentModel) msg.obj);
                        break;
                    case 2:
                        activity.commentView.refreshComplete((CustomCommentModel) msg.obj);
                        break;
                    case 3:
                        activity.commentView.loadMoreComplete((CustomCommentModel) msg.obj);
                        break;
                    case 4:
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
        View view = inflater.inflate(R.layout.custom_item_header, null);

        constraintLayout = view.findViewById(R.id.constraintLayout_header);
        constraintLayout_header = view.findViewById(R.id.comment_header);

        Intent intent = getIntent();
        gson = new Gson();
        commentView = findViewById(R.id.myCommentView);
        mTitleBar = findViewById(R.id.title_b);
        msg = (message) intent.getSerializableExtra("message");
        mCommentViewModel.setMessage_id(msg.getId());
        contentView = view.findViewById(R.id.contentDetail);
        coverView = view.findViewById(R.id.cover);
        commentPostButton = findViewById(R.id.button);
        commentPostEditTextView = findViewById(R.id.editor);

        authorView = view.findViewById(R.id.author_textView);
        dateView = view.findViewById(R.id.date_textView);
        dateView=view.findViewById(R.id.date_textView);

        author_avatar = view.findViewById(R.id.message_user_icon);
        prizes_message = view.findViewById(R.id.prizes_msg);
        prize_image = view.findViewById(R.id.comment_item_like);
        comments_count = view.findViewById(R.id.textView11);
        ConstraintLayout.LayoutParams content_params = new ConstraintLayout.LayoutParams(contentView.getLayoutParams());

        context = this;

        sp = getSharedPreferences("user_profile", MODE_PRIVATE);
        author_id = sp.getInt("id", -1);
        author = sp.getString("username", "defaultAuthor");
        token = sp.getString("token", null);
        default_link = getResources().getString(R.string.default_avatar);
        mCommentViewModel.getMessageMutableLiveData().observe(this, new Observer<message>() {
            @Override
            public void onChanged(message message) {
                contentView.setText(message.getContent());
                authorView.setText(msg.getAuthor());
                if (message.getImageUrl().equals("")) {
                    constraintLayout.removeView(coverView);
                    content_params.width = ScreenUtils.dip2px(context, 0);
                    content_params.horizontalBias = 0;
                    content_params.bottomToTop = R.id.constraintLayout4;
                    content_params.startToStart = R.id.constraintLayout_header;
                    content_params.endToEnd = R.id.constraintLayout_header;
                    content_params.topToBottom = R.id.constraintLayout5;

                    content_params.topMargin = ScreenUtils.dip2px(context, 16);

                    content_params.bottomMargin = ScreenUtils.dip2px(context, 16);
                    content_params.setMarginStart(ScreenUtils.dip2px(context, 24));
                    content_params.setMarginEnd(ScreenUtils.dip2px(context, 24));
                    contentView.setLayoutParams(content_params);
                } else {
                    coverView.setImageURI(message.getImageUrl());
                }
                dateView.setText(message.getCreatedAt());
                prizes_message.setText(String.valueOf(message.getLike()));
                Drawable bmpDrawable = ContextCompat.getDrawable(context,R.drawable.pxjh);
                Drawable.ConstantState state = bmpDrawable.getConstantState();
                Drawable warp = DrawableCompat.wrap(state == null ? bmpDrawable : state.newDrawable()).mutate();
                Log.d("accept_message",String.valueOf(message.getAccept()));
                if (Integer.compare(message.getAccept(),1)==0) {
                    prize_image.setTag(Integer.valueOf(1));
                    DrawableCompat.setTint(warp, Color.parseColor("#EE6699FF"));
                }else {
                    DrawableCompat.setTint(warp, Color.parseColor("#2C2C2C"));
                    prize_image.setTag(Integer.valueOf(0));
                }
                prize_image.setImageDrawable(warp);
            }
        });
        mCommentViewModel.requestAvatar(msg.getAuthor());
        mCommentViewModel.getUserMutableLiveData().observe(this, new Observer<User>() {
            @SuppressLint("ResourceType")
            @Override
            public void onChanged(User user) {
                if (user.getUser_avatar().length() != 0) {
                    Glide.with(context).asBitmap().load(user.getUser_avatar()).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                            author_avatar.setImageBitmap(resource);
                        }
                        @Override
                        public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

                        }

                        @Override
                        public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);

                            Glide.with(context).asBitmap().load(default_link).into(author_avatar);
                        }
                    });
                }else {
                    Glide.with(context).asBitmap().load(default_link).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                            author_avatar.setImageBitmap(resource);
                        }
                        @Override
                        public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                        }

                    });
                }
            }
        });

        commentView.setViewStyleConfigurator(new CustomViewStyleConfigurator(this));
        commentView.addHeaderView(constraintLayout_header, true);
        commentView.callbackBuilder()
                //?????????????????????(????????????ViewHolder??????)--CustomCommentItemCallback<C> ??????C???????????????????????????
                .customCommentItem(new CustomCommentItemCallback<CustomCommentModel.CustomComment>() {
                    @Override
                    public View buildCommentItem(int groupPosition, CustomCommentModel.CustomComment comment, LayoutInflater inflater, View convertView, ViewGroup parent) {
                        //??????????????????adapter?????????getView()????????????
                        final CustomCommentViewHolder holder;
                        if (convertView == null) {
                            //?????????????????????
                            convertView = inflater.inflate(R.layout.custom_item_comment, parent, false);
                            holder = new CustomCommentViewHolder(convertView);
                            //????????????ViewHolder??????
                            convertView.setTag(holder);
                        } else {
                            holder = (CustomCommentViewHolder) convertView.getTag();
                        }
                        holder.prizes.setText("1");
                        holder.userName.setText(comment.getPosterName());
                        holder.comment.setText(comment.getData());
                        holder.date_comment.setText(comment.getDate());
                        holder.ico.setTag(String.valueOf(groupPosition));
                        holder.ico.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), FriendDetailActivity.class);
                                intent.putExtra("name", comment.getPosterName());
                                v.getContext().startActivity(intent);
                            }
                        });
                        map_ico.put(holder.ico,comment.getPosterName());
                        count++;
                        status_icon.postValue(count);
                        Log.d("map_ico", String.valueOf(map_ico.values()));
                        return convertView;
                    }
                })
                //?????????????????????(????????????ViewHolder?????????
                // ???????????????ViewHolder??????????????????com.jidcoo.android.widget.commentview.view.ViewHolder
                // --CustomReplyItemCallback<R> ??????R???????????????????????????
                .customReplyItem(new CustomReplyItemCallback<CustomCommentModel.CustomComment.CustomReply>() {
                    @Override
                    public View buildReplyItem(int groupPosition, int childPosition, boolean isLastReply, CustomCommentModel.CustomComment.CustomReply reply, LayoutInflater inflater, View convertView, ViewGroup parent) {
                        //??????????????????adapter?????????getView()????????????
                        //?????????????????????com.jidcoo.android.widget.commentview.view.ViewHolder???????????????
                        CustomReplyViewHolder holder = null;
                        //?????????????????????com.jidcoo.android.widget.commentview.view.ViewHolder???????????????
                        if (convertView == null) {
                            //?????????????????????
                            convertView = inflater.inflate(R.layout.item_reply_more, parent, false);
                            holder = new CustomReplyViewHolder(convertView);
                            //????????????ViewHolder??????
                            convertView.setTag(holder);
                        } else {
                            holder = (CustomReplyViewHolder) convertView.getTag();
                        }
                        holder.userName.setText(reply.getReplierName());
                        holder.reply.setText(reply.getData());
                        holder.prizes.setText("100");
                        holder.date_reply.setText(reply.getDate());
                        holder.ico.setTag(String.valueOf(groupPosition));
                        holder.ico.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), FriendDetailActivity.class);
                                intent.putExtra("name", reply.getReplierName());
                                v.getContext().startActivity(intent);
                            }
                        });
                        map_ico.put(holder.ico,reply.getReplierName());
                        count++;
                        status_icon.postValue(count);
                        Log.d("map_ico", String.valueOf(map_ico.values()));
                        return convertView;
                    }
                })
                //??????????????????
                .setOnPullRefreshCallback(new MyOnPullRefreshCallback())
                //???????????????Item???????????????????????????????????????
                .setOnItemClickCallback(new MyOnItemClickCallback())
                //??????????????????????????????????????????????????????
                .setOnReplyLoadMoreCallback(new MyOnReplyLoadMoreCallback())
                //??????????????????????????????????????????????????????
                .setOnCommentLoadMoreCallback(new MyOnCommentLoadMoreCallback())
                //???????????????????????????CallbackBuilder???buildCallback()????????????????????????????????????
                .buildCallback();
        load(1, 1);


        mCommentViewModel.getResult_avatar_map().observe(this, new Observer<Map<String, String>>() {
            @SuppressLint("ResourceType")
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                for (String key : stringStringMap.keySet()) {
                    if (!stringStringMap.get(key).equals("no")) {
                        for (RoundImageView sub_key : map_ico.keySet()) {
                            if(map_ico.get(sub_key).equals(key)) {
                                if (!sub_key.getTag().equals("")) {
                                    if(stringStringMap.get(key).equals("")){
                                        Log.d("null picture",default_link);
                                        // ????????????????????????????????????
                                        Glide.with(context).asBitmap().load(default_link).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                                                sub_key.setImageBitmap(resource);
                                                sub_key.setTag("");
                                            }
                                            @Override
                                            public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                                            }
                                            @Override
                                            public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                                                super.onLoadFailed(errorDrawable);
                                                sub_key.setImageResource(R.drawable.defaultavatar);
                                            }
                                        });
                                    } else {
                                        Glide.with(context).asBitmap().load(stringStringMap.get(key)).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                                                sub_key.setImageBitmap(resource);
                                                sub_key.setTag("");
                                            }
                                            @Override
                                            public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                                            }
                                            @Override
                                            public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                                                super.onLoadFailed(errorDrawable);
                                                Glide.with(context).asBitmap().load(default_link).into(sub_key);
                                            }
                                        });
                                    }
                                }
                            }
                        }

                    }
                }
            }
        });


        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(), 0);
                commentPostEditTextView.setText("");
                init_comment();
                return false;
            }
        });


        coverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MessageDetailActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
            }
        });
        commentPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = commentPostEditTextView.getText().toString();
                if (content.isEmpty()) {
                    Toast.makeText(context, "????????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isReply && isChildReply) {
                    //?????????????????????????????????????????????
                    CustomCommentModel.CustomComment.CustomReply reply = new CustomCommentModel.CustomComment.CustomReply();
                    reply.setReplierName(author);
                    reply.setData(content);
                    reply.setLevel(rp + 1);
                    CustomCommentModel.CustomComment cur_comment = (CustomCommentModel.CustomComment) commentView.getCommentList().get(cp);
                    reply.setComment_id(cur_comment.getId());
                    reply.setRepliedName(cur_comment.getReplies().get(rp).getReplierName());

                    mCommentViewModel.postReply(activityHandler, reply);

                    inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(), 0);
                    commentPostEditTextView.setText("");
                    init_comment();
                } else if (isReply && !isChildReply) {
                    //?????????????????????????????????????????????
                    CustomCommentModel.CustomComment.CustomReply reply = new CustomCommentModel.CustomComment.CustomReply();
                    reply.setReplierName(author);
                    reply.setData(content);
                    reply.setLevel(0);
                    CustomCommentModel.CustomComment cur_comment = (CustomCommentModel.CustomComment) commentView.getCommentList().get(cp);
                    reply.setComment_id(cur_comment.getId());

                    inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(), 0);
                    commentPostEditTextView.setText("");
                    mCommentViewModel.postReply(activityHandler, reply);
                    init_comment();
                } else {
                    CustomCommentModel.CustomComment comment = new CustomCommentModel.CustomComment();
                    comment.setPosterName(author);
                    comment.setData(content);
                    inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(), 0);
                    commentPostEditTextView.setText("");
                    mCommentViewModel.postMessageComment(activityHandler, comment);
                    init_comment();
                }

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
                Intent intent = new Intent(v.getContext(), FriendDetailActivity.class);
                intent.putExtra("name", msg.getAuthor());
                v.getContext().startActivity(intent);
            }
        });
        author_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FriendDetailActivity.class);
                intent.putExtra("name", msg.getAuthor());
                v.getContext().startActivity(intent);
            }
        });
        prize_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer) prize_image.getTag() == 0){
                    mCommentViewModel.postPrize(1);
                } else if((Integer) prize_image.getTag() == 1) {
                    mCommentViewModel.postPrize(0);
                }
            }
        });

        status_icon.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                init_avatar();
            }
        });
    }
     public  void init_avatar() {
         Log.d("start_init", String.valueOf(map_ico));
         Map<String,String> name_map = mCommentViewModel.getAvatar_map();
         for (RoundImageView key : map_ico.keySet()) {
             name_map.put(map_ico.get(key),"no");

         }
         mCommentViewModel.setAvatar_map(name_map);
         for (RoundImageView key : map_ico.keySet()) {
//             SampleCircleImageView sampleCircleImageView = key;
             if (!mCommentViewModel.getAvatar_map().get(map_ico.get(key)).equals("no")) {
                 continue;
             }
             mCommentViewModel.requestAllAvatar(map_ico.get(key));
         }
    }

    private void load(int code, int handlerId) {
        mCommentViewModel.getCommentModel(code, activityHandler, handlerId);
    }

    /**
     * ?????????????????????
     */
    class MyOnPullRefreshCallback implements OnPullRefreshCallback {

        @Override
        public void refreshing() {
            load(1, 2);
        }

        @Override
        public void complete() {
            //????????????????????????
        }

        @Override
        public void failure(String msg) {
        }
    }


    /**
     * ???????????????????????????
     */
    class MyOnCommentLoadMoreCallback implements OnCommentLoadMoreCallback {

        @Override
        public void loading(int currentPage, int willLoadPage, boolean isLoadedAllPages) {
        }

        @Override
        public void complete() {
            //????????????????????????
        }

        @Override
        public void failure(String msg) {
        }
    }

    /**
     * ???????????????????????????
     */
    class MyOnReplyLoadMoreCallback implements OnReplyLoadMoreCallback<CustomCommentModel.CustomComment.CustomReply> {


        @Override
        public void loading(CustomCommentModel.CustomComment.CustomReply reply, int willLoadPage) {
        }

        @Override
        public void complete() {
        }

        @Override
        public void failure(String msg) {
        }
    }

    /**
     * ??????????????????
     */
    class MyOnItemClickCallback implements OnItemClickCallback<CustomCommentModel.CustomComment, CustomCommentModel.CustomComment.CustomReply> {

        @Override
        public void commentItemOnClick(int position, CustomCommentModel.CustomComment comment, View view) {
            cp = position;
            isReply = true;
            isChildReply = false;
            commentPostEditTextView.setHint("??????@" + comment.getPosterName() + ":");
            Toast.makeText(MessageDetailActivity.this, "??????" + comment.getPosterName() , Toast.LENGTH_SHORT).show();
            inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(), 0);
            commentPostEditTextView.setText("");
        }

        @Override
        public void replyItemOnClick(int c_position, int r_position, CustomCommentModel.CustomComment.CustomReply reply, View view) {
            cp = c_position;
            rp = r_position;
            isReply = true;
            isChildReply = true;
            commentPostEditTextView.setHint("??????@" + reply.getReplierName() + ":");
            Toast.makeText(MessageDetailActivity.this, "?????????" + reply.getReplierName() , Toast.LENGTH_SHORT).show();
            inputMethodManager.hideSoftInputFromWindow(commentPostEditTextView.getWindowToken(), 0);
            commentPostEditTextView.setText("");
        }
    }

    public void init_comment() {
        List<CustomCommentModel.CustomComment> mCustomComment = (List<CustomCommentModel.CustomComment>) commentView.getCommentList();
        cp = mCustomComment.size() - 1;
        isReply = false;
        isChildReply = false;
        commentPostEditTextView.setHint("?????????????????????");
    }
}
