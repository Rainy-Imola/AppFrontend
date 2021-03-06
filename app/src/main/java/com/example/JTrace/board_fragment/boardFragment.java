package com.example.JTrace.board_fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.JTrace.PublishActivity;
import com.example.JTrace.R;
import com.example.JTrace.model.message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loper7.layout.TitleBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO: 加入上拉刷新机制 or 在标题上加个按钮刷新
public class boardFragment extends Fragment {

    private FloatingActionButton fabAdd;

    public static boardFragment newInstance() {
        return new boardFragment();
    }

    private List<message> messages = new ArrayList<>();
    private MessageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LottieAnimationView animationView;
    private AnimatorSet animatorSet;
    private ObjectAnimator alphaAnimator_lottie, alphaAnimator_message;
    private BoardViewModel boardViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.board_fragment, container, false);
        mRecyclerView = root.findViewById(R.id.board);
        TitleBar title = root.findViewById(R.id.title_b);
        title.setTitleText("主干道");
        fabAdd = root.findViewById(R.id.fab);
        animationView = root.findViewById(R.id.animationView);
        animationView.playAnimation();
        animatorSet = new AnimatorSet();
        mAdapter = new MessageAdapter(messages);
        swipeRefreshLayout = root.findViewById(R.id.swipe_board);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PublishActivity.class);
                startActivity(intent);
            }
        });
        animationView.setAlpha(1f);
        mRecyclerView.setAlpha(0f);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            loadMessages(1500);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        context = getContext();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMessages();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "刷新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        boardViewModel = new ViewModelProvider(requireActivity()).get(BoardViewModel.class);
    }

    public void refreshMessages() {
        boardViewModel.requestMessage();
        boardViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if (status == "message") {
                    messages.clear();
                    for (message imessage : boardViewModel.getmMessage()) {
                        messages.add(imessage);
                    }
                }
            }
        });
    }

    public void loadMessages(int sleepTime) throws IOException, InterruptedException {
        boardViewModel.requestMessage();
        boardViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if (status == "message") {
                    messages.clear();
                    for (message imessage : boardViewModel.getmMessage()) {
                        messages.add(imessage);
                    }
                }
                mAdapter.setmItems(messages);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        if (!boardViewModel.isGetMsgSucc()) {
            Toast.makeText(getContext(), "用户状态错误，请重新登陆", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("status", 2);
            editor.commit();
            NavHostFragment.findNavController(this).navigate(R.id.action_navigation_myinfo_to_loginActivity);
        }

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                //动画淡出，列表淡入
                alphaAnimator_lottie = ObjectAnimator.ofFloat(animationView, "scaleX",
                        0f);
                alphaAnimator_message = ObjectAnimator.ofFloat(mRecyclerView, "alpha",
                        1f);
                animatorSet.play(alphaAnimator_lottie).with(alphaAnimator_message);
                animatorSet.start();
                mAdapter.setmItems(messages);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }, sleepTime);
    }
}