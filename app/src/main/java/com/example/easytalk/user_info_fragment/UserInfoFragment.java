package com.example.easytalk.user_info_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
        mViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        /*
        List<String> mmlabel = new ArrayList<>(Arrays.asList("打羽毛球", "football", "movie"));

        mUser = new User("tognzhixin");
        mUser.setUser_constellation("金牛座");
        Log.d("userlabel", String.valueOf(mmlabel));
        mUser.setUser_hobby(mmlabel);
        */
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
        /*
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onStart() {
        super.onStart();
    }
}