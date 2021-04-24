package com.example.easytalk.board_fragment;

import androidx.lifecycle.ViewModel;

import com.example.easytalk.Message;
import com.example.easytalk.R;

import java.util.ArrayList;
import java.util.List;

public class BoardViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    List<Message> MessageViewModel=new ArrayList<>() ;
    Message tmp=new Message("123");
    public BoardViewModel(){
        MessageViewModel.add(tmp);
    }
}