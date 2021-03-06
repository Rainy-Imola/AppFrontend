package com.example.JTrace.user_info_fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donkingliang.labels.LabelsView;
import com.example.JTrace.R;
import com.example.JTrace.baseActivity;
import com.example.JTrace.board_fragment.MessageDetailActivity;
import com.example.JTrace.model.User;
import com.example.JTrace.model.message;
import com.example.JTrace.widget.RoundImageView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.loper7.layout.TitleBar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel mViewModel;
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
    private Context mContext;
    private AppBarLayout app_bar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private String title = "user info";
    private String default_link;


    private MutableLiveData<Integer> status_bar = new MutableLiveData<Integer>();
    public MutableLiveData<Integer> getStatus_bar() {
        return status_bar;
    }

    public void setStatus_bar(int status_bar) {
        this.status_bar.postValue(status_bar);
    }
    private ConstraintLayout constraintLayout;
    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;
    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root=  inflater.inflate(R.layout.userinfo_fragment, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerView);
        user_name = root.findViewById(R.id.user_info_name);
        user_constellation = root.findViewById(R.id.user_info_constellation);
        user_hobby = root.findViewById(R.id.labels);
        mTitleBar = root.findViewById(R.id.main_titleBar);
        user_avatar = root.findViewById(R.id.user_info_imageView);
        app_bar = root.findViewById(R.id.appbar);
        collapsingToolbarLayout = root.findViewById(R.id.collapsing);
        toolbar = root.findViewById(R.id.toolbar2);
        constraintLayout = root.findViewById(R.id.visiablecon);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset == 0){
                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                    setStatus_bar(0);


                }else if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()/2){
                    toolbar.setBackgroundColor(Color.parseColor("#EE6699FF"));
                    setStatus_bar(1);
                }
            }
        });
        default_link = getResources().getString(R.string.default_avatar);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = this.getContext();
        mViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        mnavController = NavHostFragment.findNavController(this);
        mViewModel.questUser();
        try {
            mViewModel.requestMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getStatus_bar().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==0){
                    mTitleBar.setTitleText("");
                    mTitleBar.setMenuImageResource(R.drawable.menu_24_black);
                }
                else if (integer==1){
                    mTitleBar.setTitleText(title);
                    mTitleBar.setMenuImageResource(R.drawable.menu_white_24);
                }
            }
        });
        mViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @SuppressLint("ResourceType")
            @Override
            public void onChanged(User mUser) {
                if(mUser.getUser_hobby()!=null && mUser.getUser_hobby().isEmpty()){
                    user_hobby.setLabels(Arrays.asList("???????????????tag??????"));
                }else {
                    user_hobby.setLabels(mUser.getUser_hobby());
                }
                if(mUser.getUser_constellation().length() == 0){
                    user_constellation.setText("???????????????");
                }else {
                    user_constellation.setText("?????????" + mUser.getUser_constellation());
                }
                user_name.setText("?????????"+ mUser.getUser_name());
                title = mUser.getUser_name();
                if (mUser.getUser_avatar().length() != 0) {
                    Glide.with(mContext).asBitmap().load(mUser.getUser_avatar()).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                            user_avatar.setImageBitmap(resource);
                        }
                        @Override
                        public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                        }

                        @Override
                        public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            Glide.with(mContext).asBitmap().load(default_link).into(user_avatar);
                            Toast.makeText(mContext, "??????????????????????????????????????????", Toast.LENGTH_LONG).show();

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

                        @Override
                        public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            user_avatar.setImageResource(R.drawable.defaultavatar);
                        }
                    });
                }

            }
        });
        mViewModel.getStatus_save().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if ( s.equals("fail_avatar")) {
                    Toast.makeText(mContext, "??????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status == "message") {
                    mItems = new ArrayList<>();
                    try {
                        for (message imessage:mViewModel.getMessage()) {
                            mItems.add(imessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mLayoutManager=new LinearLayoutManager(getActivity());
                    mAdapter = new MessageAdapter(mRecyclerView.getContext(), mItems);

                    mAdapter.setOnItemClickListener(new MessageAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int str) {
                            Intent intent=new Intent(view.getContext(), MessageDetailActivity.class);
                            intent.putExtra("message", (message)mItems.get(str));
                            view.getContext().startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int str) {
                            Log.d("debug","onClicklongCalled");
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("???????????????");    //?????????????????????
                            builder.setIcon(android.R.drawable.btn_star);   //?????????????????????????????????
                            builder.setCancelable(true);
                            builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mViewModel.deleteMessage(mItems.get(str).getId(),str);
                                    Toast.makeText(mContext, "????????? ", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(mContext, "?????????", Toast.LENGTH_SHORT).show();
                                }
                            });
                            AlertDialog dialog = builder.create();  //???????????????
                            dialog.setCanceledOnTouchOutside(true); //???????????????????????????????????????,???????????????????????????????????????
                            dialog.show();
                            //

                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setHasFixedSize(true);

                }
            }
        });
        mTitleBar.setOnMenuListener(new TitleBar.OnMenuListener() {
            @Override
            public void onMenuClick() {
                mPopupWindow = new PopupWindow();
                animUtil = new AnimUtil();
                showPop();
                toggleBright();
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.userinfo_nav_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.user_info_setting:
                return true;
            case R.id.user_info_write:
                NavHostFragment.findNavController(this).navigate(R.id.action_navigation_myinfo_to_modifyFragment);
                return true;
            case R.id.user_info_exit:
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("status",2);
                editor.commit();

                NavHostFragment.findNavController(this).navigate(R.id.action_navigation_myinfo_to_loginActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showPop() {
        // ??????????????????
        mPopupWindow.setContentView(LayoutInflater.from(this.getContext()).inflate(R.layout.pop_add, null));
        // ????????????????????????????????????????????????????????????????????????
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // ??????pop????????????
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
        // ??????pop????????????
        //mPopupWindow.setAnimationStyle(R.style.pop_add);
        // ??????pop????????????????????????false?????????????????????????????????Activity?????????pop??????Editor?????????focusable????????????true
        mPopupWindow.setFocusable(true);
        // ??????pop???????????????false??????????????????????????????true
        mPopupWindow.setTouchable(true);
        // ????????????pop????????????????????????false??????focusable???true???????????????????????????
        mPopupWindow.setOutsideTouchable(true);
        // ????????? + ??????????????????????????????????????????
        mPopupWindow.showAsDropDown(mTitleBar.getMenuView(), -100, 0);
        // ??????pop??????????????????????????????????????????
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });

        tv_1 = mPopupWindow.getContentView().findViewById(R.id.tv_1);
        tv_2 = mPopupWindow.getContentView().findViewById(R.id.tv_2);
        tv_3 = mPopupWindow.getContentView().findViewById(R.id.tv_3);

        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                mnavController.navigate(R.id.action_navigation_myinfo_to_modifyFragment);
            }
        });
        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("status",2);
                editor.commit();
//                final Intent intent = new Intent();
//                intent.setAction("com.xch.servicecallback.content");
//                Intent eintent = new Intent(getExplicitIntent(mContext,intent));
//                mContext.stopService(eintent);
                sendFinishActivityBroadcast(mContext);
                mnavController.navigate(R.id.action_navigation_myinfo_to_loginActivity);

            }
        });
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                mnavController.navigate(R.id.action_navigation_myinfo_to_settingFragment);
            }
        });

    }

    private void toggleBright() {
        // ????????????????????????????????? ????????? ??????????????????????????????????????????????????????0.5f--1f???
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // ?????????????????????????????????????????????
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getActivity().getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        this.getActivity().getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // ???????????????????????????????????????????????????????????????
        this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public static void sendFinishActivityBroadcast(Context context) {
        Intent intent = new Intent(baseActivity.RECEIVER_ACTION_FINISH);
        context.sendBroadcast(intent);
    }

    @Override
    public void onPause() {
        if(mPopupWindow != null){
            mPopupWindow.dismiss();
        }

        super.onPause();
    }
}