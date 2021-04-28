package com.example.easytalk.board_fragment;

import androidx.lifecycle.ViewModel;

import com.example.easytalk.model.message;

import java.util.ArrayList;
import java.util.List;

public class BoardViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    List<message> MessageViewModel=new ArrayList<>() ;
    message tmp1=new message("author1","123");
    message tmp2=new message("author2","345");
    public BoardViewModel(){
        testRecyclerView();
    }
    public void testRecyclerView(){
        MessageViewModel.clear();
        MessageViewModel.add(tmp1);
        MessageViewModel.add(tmp2);
    }
}