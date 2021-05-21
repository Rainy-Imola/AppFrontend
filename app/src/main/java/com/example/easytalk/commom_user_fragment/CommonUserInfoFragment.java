package com.example.easytalk.commom_user_fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
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
import com.donkingliang.labels.LabelsView;
import com.example.easytalk.R;
import com.example.easytalk.board_fragment.MessageDetailActivity;
import com.example.easytalk.chat.MainChatActivity;
import com.example.easytalk.model.User;
import com.example.easytalk.model.message;
import com.example.easytalk.user_info_fragment.AnimUtil;
import com.example.easytalk.user_info_fragment.MessageAdapter;
import com.example.easytalk.widget.RoundImageView;
import com.loper7.layout.TitleBar;

import org.jetbrains.annotations.NotNull;

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
    private Button addorchar;
    private Context mContext;
    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;
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
        Log.d("Receive name:",mArgument);
        mnavController = NavHostFragment.findNavController(this);
        mContext = this.getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.common_user_info_fragment, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        user_name = (TextView)root.findViewById(R.id.user_info_name);
        user_constellation = (TextView) root.findViewById(R.id.user_info_constellation);
        user_hobby = (LabelsView) root.findViewById(R.id.labels);
        mTitleBar = root.findViewById(R.id.main_titleBar);
        addorchar = root.findViewById(R.id.button);
        user_avatar = root.findViewById(R.id.common_user_info_imageView);
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
        mViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status == "user"){
                    User mUser = mViewModel.readUser();
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
                    if (mUser.getUser_avatar().length() != 0){
                        Glide.with(mContext).load(mUser.getUser_avatar()).into(user_avatar);
                        Log.d("avatar_uri：",mUser.getUser_avatar());
                    }
                    Log.d("setName：",mUser.getUser_name());
                    user_name.setText(mUser.getUser_name());
                    if(mViewModel.isFriend()){
                        addorchar.setText("发消息");
                        addorchar.setTag(new Integer(1));
                    }else if(!mViewModel.isFriend()){
                        addorchar.setText("加好友");
                        addorchar.setTag(new Integer(2));
                    }
                }
                else if(status == "message"){
                    Log.d("setName：","message");
                    User mUser = mViewModel.readUser();
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
                    Log.d("setName：",mUser.getUser_name());
                    user_name.setText(mUser.getUser_name());
                    if(mViewModel.isFriend()){
                        addorchar.setText("发消息");
                        addorchar.setTag(new Integer(1));
                    }else if(!mViewModel.isFriend()){
                        addorchar.setText("加好友");
                        addorchar.setTag(new Integer(2));
                    }
                    for (message imessage:mViewModel.getMessage()
                    ) {
                        mItems.add(imessage);
                    }
                    mLayoutManager=new LinearLayoutManager(getContext());
                    mAdapter = new MessageAdapter(mContext, mItems, new MessageAdapter.OnRecyclerViewItemClickListener() {
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
                if((Integer)addorchar.getTag()==2){
                    mnavController.navigate(R.id.action_commonUserInfoFragment_to_addFriendFragment);
                }
                else if((Integer)addorchar.getTag()==1){
                    Intent intent=new Intent(v.getContext(), MainChatActivity.class);
                    intent.putExtra("id",mViewModel.readUser().getUser_id());
                    intent.putExtra("name",mViewModel.readUser().getUser_name());
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

}