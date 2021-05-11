package com.example.easytalk.commom_user_fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easytalk.R;

public class CommonUserInfoFragment extends Fragment {
    private String mArgument;
    public static final String ARGUMENT = "argument";
    private CommonUserInfoViewModel mViewModel;
    private TextView mName;
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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.common_user_info_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CommonUserInfoViewModel.class);
        // TODO: Use the ViewModel
    }

}