package com.example.easytalk.user_info_fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.labels.LabelsView;
import com.example.easytalk.LoginActivity;
import com.example.easytalk.MainActivity;
import com.example.easytalk.R;
import com.example.easytalk.board_fragment.MessageDetailActivity;
import com.example.easytalk.model.User;
import com.example.easytalk.model.message;
import com.loper7.layout.TitleBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel mViewModel;
    private TextView user_name;
    private LabelsView user_hobby;
    private TextView user_constellation;
    private ImageView user_avatar;
    private List<message> mItems= new ArrayList<>();
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
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        //TextView title = (TextView) root.findViewById(R.id.title_i);
        //title.setText("MyInfo");
        //title.setGravity(Gravity.CENTER);
        user_name = (TextView)root.findViewById(R.id.user_info_name);
        user_constellation = (TextView) root.findViewById(R.id.user_info_constellation);
        user_hobby = (LabelsView) root.findViewById(R.id.labels);
        mTitleBar = root.findViewById(R.id.main_titleBar);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        mnavController = NavHostFragment.findNavController(this);
        /*
        List<String> mmlabel = new ArrayList<>(Arrays.asList("打羽毛球", "football", "movie"));

        mUser = new User("tognzhixin");
        mUser.setUser_constellation("金牛座");
        Log.d("userlabel", String.valueOf(mmlabel));
        mUser.setUser_hobby(mmlabel);

        message tmpmsg=new message("test2","This code fails because it is looking for an array of objects, rather than an array of strings:",new Date(),"https://img-blog.csdn.net/20160622151333766");
        mItems.add(tmpmsg);
        tmpmsg=new message("test2","This code fails because it is looking for an array of objects, rather than an array of strings:",new Date(),"https://img-blog.csdn.net/20160622151333766");
        mItems.add(tmpmsg);
        tmpmsg=new message("test2","This code fails because it is looking for an array of objects, rather than an array of strings:",new Date(),"https://img-blog.csdn.net/20160622151333766");
        mItems.add(tmpmsg);
        tmpmsg=new message("test2","This code fails because it is looking for an array of objects, rather than an array of strings:",new Date(),"https://img-blog.csdn.net/20160622151333766");
        mItems.add(tmpmsg);
        tmpmsg=new message("test2","This code fails because it is looking for an array of objects, rather than an array of strings:",new Date(),"https://img-blog.csdn.net/20160622151333766");
        mItems.add(tmpmsg);
        tmpmsg=new message("test2","This code fails because it is looking for an array of objects, rather than an array of strings:",new Date(),"https://img-blog.csdn.net/20160622151333766");
        mItems.add(tmpmsg);

        try {
            mItems=mViewModel.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
        User mUser = mViewModel.getUser();

        if(mUser.getUser_hobby()!=null && mUser.getUser_hobby().isEmpty()){
            user_hobby.setLabels(Arrays.asList("未添加任何tag属性"));
        }else {
            user_hobby.setLabels(mUser.getUser_hobby());
        }
        if(mUser.getUser_constellation().length() == 0){
            user_constellation.setText("未添加星座");
        }else {
            user_constellation.setText(mUser.getUser_constellation());
        }
        user_name.setText(mUser.getUser_name());
        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter = new MessageAdapter(this.getContext(), mItems, new MessageAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("debug", String.valueOf(mItems.get(position)));
                Intent intent=new Intent(view.getContext(), MessageDetailActivity.class);
                intent.putExtra("message", (message)mItems.get(position));
                view.getContext().startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {
                //TODO: delete message
                Log.d("debug","onClicklongCalled");
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mTitleBar.setOnMenuListener(new TitleBar.OnMenuListener() {
            @Override
            public void onMenuClick() {
                mPopupWindow = new PopupWindow();
                animUtil = new AnimUtil();
                showPop();
                toggleBright();
            }
        });
        mTitleBar.setOnBackListener(new TitleBar.OnBackListener() {
            @Override
            public void onBackClick() {
                mnavController.navigateUp();
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
        // 设置布局文件
        mPopupWindow.setContentView(LayoutInflater.from(this.getContext()).inflate(R.layout.pop_add, null));
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        //mPopupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量
        mPopupWindow.showAsDropDown(mTitleBar.getMenuView(), -100, 0);
        // 设置pop关闭监听，用于改变背景透明度
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
                mnavController.navigate(R.id.action_navigation_myinfo_to_loginActivity);

            }
        });
        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // 在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }

    /**
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getActivity().getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        this.getActivity().getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        this.getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


}