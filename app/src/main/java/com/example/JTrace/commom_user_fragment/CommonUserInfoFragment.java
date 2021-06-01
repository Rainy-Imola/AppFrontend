package com.example.JTrace.commom_user_fragment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donkingliang.labels.LabelsView;
import com.example.JTrace.R;
import com.example.JTrace.board_fragment.MessageDetailActivity;
import com.example.JTrace.chat.MainChatActivity;
import com.example.JTrace.model.User;
import com.example.JTrace.model.message;
import com.example.JTrace.modify_fragment.GlideEngine;
import com.example.JTrace.user_info_fragment.AnimUtil;
import com.example.JTrace.user_info_fragment.MessageAdapter;
import com.example.JTrace.widget.RoundImageView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.loper7.layout.TitleBar;
import com.yalantis.ucrop.util.ScreenUtils;

import org.jetbrains.annotations.NotNull;

import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonUserInfoFragment extends Fragment {

    private String mArgument;
    public static final String ARGUMENT = "argument";
    private CommonUserInfoViewModel mViewModel;
    private TextView mName;
    private TextView user_name;
    private LabelsView user_hobby;
    private TextView user_constellation;
    private RoundImageView user_avatar;
    private List<message> mItems;
    private MessageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private AnimUtil animUtil;
    private PopupWindow mPopupWindow;
    private TitleBar mTitleBar;
    private float bgAlpha = 1f;
    private boolean bright = false;
    private NavController mnavController;
    private TextView tv_1, tv_2, tv_3;
    private Button addorchar;
    private Context mContext;
    private AppBarLayout app_bar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ConstraintLayout constraintLayout;
    private MutableLiveData<Integer> status_bar = new MutableLiveData<Integer>();
    SharedPreferences sharedPreferences;
    private String default_link;

    public MutableLiveData<Integer> getStatus_bar() {
        return status_bar;
    }

    public void setStatus_bar(int status_bar) {
        this.status_bar.postValue(status_bar);
    }

    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;
    private String title = "user info";

    public static CommonUserInfoFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        CommonUserInfoFragment contentFragment = new CommonUserInfoFragment();
        contentFragment.setArguments(bundle);
        return contentFragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
            mArgument = bundle.getString(ARGUMENT);
        mArgument = getActivity().getIntent().getStringExtra("name");
        mnavController = NavHostFragment.findNavController(this);
        mContext = this.getContext();
        default_link = getResources().getString(R.string.default_avatar);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.common_user_info_fragment, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        user_name = (TextView) root.findViewById(R.id.user_info_name);
        user_constellation = (TextView) root.findViewById(R.id.user_info_constellation);
        user_hobby = (LabelsView) root.findViewById(R.id.labels);
        mTitleBar = root.findViewById(R.id.main_titleBar);
        addorchar = root.findViewById(R.id.button);
        user_avatar = root.findViewById(R.id.common_user_info_imageView);
        app_bar = root.findViewById(R.id.appbar);
        collapsingToolbarLayout = root.findViewById(R.id.collapsing_com);
        toolbar = root.findViewById(R.id.toolbar);
        constraintLayout = root.findViewById(R.id.visiablecom);
        sharedPreferences = getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(CommonUserInfoViewModel.class);
        mViewModel.requestUser(mArgument);
        mViewModel.requestMessage(mArgument);
        mViewModel.isFriend(mArgument);
        mViewModel.getFriendStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onChanged(String s) {
                if (s == "friendyes") {
                    addorchar.setText("发消息");
                    addorchar.setTag(Integer.valueOf(1));
                } else if (s == "friendno") {
                    if (sharedPreferences.getString("username", "defaultAuthor").equals(mArgument)) {
                        addorchar.setText("自己噻");
                        addorchar.setTextColor(R.color.black);
                        addorchar.setBackgroundColor(Color.parseColor("#FFF5F5F5"));
                        addorchar.setClickable(false);
                    } else {
                        addorchar.setText("加好友");

                        addorchar.setTag(Integer.valueOf(2));
                    }
                }
            }
        });
        mViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @SuppressLint("ResourceType")
            @Override
            public void onChanged(User mUser) {
                if (mUser.getUser_hobby() != null && mUser.getUser_hobby().isEmpty()) {
                    user_hobby.setLabels(Arrays.asList("未添加任何tag属性"));
                } else {
                    user_hobby.setLabels(mUser.getUser_hobby());
                }
                if (mUser.getUser_constellation().length() == 0) {
                    user_constellation.setText("未添加星座");
                } else {
                    user_constellation.setText("星座：" + mUser.getUser_constellation());
                }
                user_name.setText("昵称：" + mUser.getUser_name());
                title = mUser.getUser_name();
                if (mUser.getUser_avatar().length() != 0) {
                    Glide.with(mContext).asBitmap().load(mUser.getUser_avatar()).error(R.drawable.defaultavatar).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                            user_avatar.setImageBitmap(resource);
                        }
                        @Override
                        public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

                        }
                    });
                }else {
                    Glide.with(mContext).asBitmap().load(default_link).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                            user_avatar.setImageBitmap(resource);
                        }
                        @Override
                        public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                        }
                    });
                }
            }
        });
        mViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if (status == "message") {
                    mItems = new ArrayList<>();
                    for (message imessage : mViewModel.getMessage()
                    ) {
                        mItems.add(imessage);
                    }
                    mLayoutManager = new LinearLayoutManager(getContext());
                    mAdapter = new MessageAdapter(mContext, mItems);
                    mAdapter.setOnItemClickListener(new MessageAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int str) {
                            Intent intent = new Intent(view.getContext(), MessageDetailActivity.class);
                            intent.putExtra("message", (message) mItems.get(str));
                            view.getContext().startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int str) {

                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setHasFixedSize(true);
                }
            }
        });
        mTitleBar.setOnBackListener(new TitleBar.OnBackListener() {
            @Override
            public void onBackClick() {
                requireActivity().onBackPressed();
            }
        });
        addorchar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer) addorchar.getTag() == 2) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", mArgument);
                    mnavController.navigate(R.id.action_commonUserInfoFragment_to_addFriendFragment, bundle);
                } else if ((Integer) addorchar.getTag() == 1) {
                    Intent intent = new Intent(v.getContext(), MainChatActivity.class);
                    intent.putExtra("id", mViewModel.readUser().getUser_id());
                    intent.putExtra("name", mViewModel.readUser().getUser_name());
                    intent.putExtra("avatar", mViewModel.readUser().getUser_avatar());

                    v.getContext().startActivity(intent);
                }
            }
        });
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {

                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                    setStatus_bar(0);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange() / 2) {

                    toolbar.setBackgroundColor(Color.parseColor("#EE6699FF"));
                    setStatus_bar(1);
                }
            }
        });
        getStatus_bar().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    mTitleBar.setTitleText("");
                    mTitleBar.setMenuImageResource(R.drawable.menu_24_black);
                    mTitleBar.setBackImageResource(R.drawable.nav_return_black);
                } else if (integer == 1) {
                    mTitleBar.setTitleText(title);
                    mTitleBar.setMenuImageResource(R.drawable.menu_white_24);
                    mTitleBar.setBackImageResource(R.drawable.nav_return_white);
                }
            }
        });
    }

}