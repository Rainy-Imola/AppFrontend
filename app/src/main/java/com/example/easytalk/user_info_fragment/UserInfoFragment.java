package com.example.easytalk.user_info_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.labels.LabelsView;
import com.example.easytalk.R;
import com.example.easytalk.model.User;
import com.example.easytalk.model.message;

import java.io.IOException;
import java.util.List;

public class UserInfoFragment extends Fragment {

    private UserInfoViewModel mViewModel;
    private TextView user_name;
    private LabelsView user_hobby;
    private TextView user_constellation;
    private ImageView user_avatar;
    private List<message> mItems;
    private MessageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private User mUser;
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
        user_name = (TextView)root.findViewById(R.id.user_info_name);
        user_constellation = (TextView) root.findViewById(R.id.user_info_constellation);
        user_hobby = (LabelsView) root.findViewById(R.id.labels);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                NavHostFragment.findNavController(this).navigate(R.id.action_navigation_myinfo_to_modifyFragment);
                return true;
            case R.id.user_info_write:
                return true;
            case R.id.user_info_exit:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        try {
            mItems=mViewModel.getMessage();
            mUser = mViewModel.getUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        user_hobby.setLabels(mUser.getUser_hobby());
        user_constellation.setText(mUser.getUser_constellation());
        user_name.setText(mUser.getUser_name());
        mAdapter = new MessageAdapter(this.getContext(), mItems, new MessageAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String str) {
                Log.d("debug","onClickCalled");
            }

            @Override
            public void onItemLongClick(View view, String str) {
                Log.d("debug","onClicklongCalled");
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}