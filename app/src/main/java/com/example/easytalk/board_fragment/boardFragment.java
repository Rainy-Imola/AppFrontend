package com.example.easytalk.board_fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easytalk.PublishActivity;
import com.example.easytalk.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class boardFragment extends Fragment {

    private BoardViewModel mViewModel;
    private FloatingActionButton fabAdd;
    public static boardFragment newInstance() {
        return new boardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.board_fragment, container, false);
        fabAdd = root.findViewById(R.id.fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PublishActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BoardViewModel.class);
        // TODO: Use the ViewModel

    }

}