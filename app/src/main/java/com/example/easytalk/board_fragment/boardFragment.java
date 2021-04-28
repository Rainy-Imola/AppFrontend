package com.example.easytalk.board_fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easytalk.PublishActivity;
import com.example.easytalk.R;
import com.example.easytalk.model.message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class boardFragment extends Fragment {

    private BoardViewModel mViewModel;
    private FloatingActionButton fabAdd;
    public static boardFragment newInstance() {
        return new boardFragment();
    }
    private List<message> messages;
    private MessageAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.board_fragment, container, false);
        mRecyclerView=root.findViewById(R.id.board);
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
    @Override
    public void onStart() {
        super.onStart();
        messages=mViewModel.MessageViewModel;
        mAdapter=new MessageAdapter(messages);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}