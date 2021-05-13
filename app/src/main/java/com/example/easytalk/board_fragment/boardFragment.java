package com.example.easytalk.board_fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.easytalk.Constants;
import com.example.easytalk.HttpAPI;
import com.example.easytalk.PublishActivity;
import com.example.easytalk.R;
import com.example.easytalk.model.message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//TODO: 加入上拉刷新机制 or 在标题上加个按钮刷新
public class boardFragment extends Fragment {

    //private BoardViewModel mViewModel;
    private FloatingActionButton fabAdd;
    public static boardFragment newInstance() {
        return new boardFragment();
    }
    private List<message> messages;
    private MessageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LottieAnimationView animationView;
    private AnimatorSet animatorSet;
    private ObjectAnimator alphaAnimator_lottie,alphaAnimator_message;
    private BoardViewModel boardViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.board_fragment, container, false);
        mRecyclerView=root.findViewById(R.id.board);
        TextView title = (TextView) root.findViewById(R.id.title_b);
        title.setText("主干道");
        title.setGravity(Gravity.CENTER);
        fabAdd = root.findViewById(R.id.fab);
        animationView=root.findViewById(R.id.animationView);
        animationView.playAnimation();
        animatorSet=new AnimatorSet();
        mAdapter=new MessageAdapter(messages);

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
            refreshMessages(2000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        boardViewModel=new ViewModelProvider(requireActivity()).get(BoardViewModel.class);
        boardViewModel.requestMessage();
        this.messages=boardViewModel.getmMessage();
    }
    /*
    public List<message> getMessages() throws IOException {
        List<message> msgs=new ArrayList<>();
        OkHttpClient okHttpClient=new OkHttpClient();
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token","");
        Log.d("MessageInfo_token",token);
        Request request=new Request.Builder().url(Constants.baseUrl+"/msgboard/")
                .addHeader("Authorization",token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("MessageInfo","request_handle_failed");
                Toast.makeText(getContext(),"请求留言数据失败！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                Log.d("MessageInfo",res);
                try {
                    JSONObject mid=new JSONObject(res);
                    JSONArray result= (JSONArray) mid.get("data");
                    Log.d("MessageInfo", "resultLength:"+String.valueOf(result.length()));
                    for(int i=result.length()-1;i>=0;i--){
                        //og.d("MessageInfo","enter loop");
                        JSONObject cur_msg=result.getJSONObject(i);
                        //Log.d("MessageInfo","try");
                        String id=(cur_msg.has("id"))? cur_msg.getString("id"):"1";
                        String author=cur_msg.getString("author");
                        String content=cur_msg.getString("content");
                        String date=cur_msg.getString("date");
                        //Log.d("MessageInfo","date:"+date);
                        String picture=(cur_msg.has("picture")? cur_msg.getString("picture"):null);
                        //Log.d("MessageInfo","cur_msg_info:"+"id:"+id+" "+" author:"+author+" content:"+content);
                        //handle date
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'Z'");
                        //Log.d("MessageInfo","test");
                        Date FormattedDate=format.parse(date);
                        message msg=new message(id,author,content,FormattedDate,picture);

                        msgs.add(msg);
                        //Log.d("MessageInfo","finished one circle");
                    }
                    Log.d("MessageInfo","msgs_Size: "+ String.valueOf(msgs.size()));
                } catch (JSONException | ParseException e) {
                    Log.d("MessageInfo","Parse failed");
                    e.printStackTrace();
                }
            }
        });
        return msgs;
    }*/

    public void refreshMessages(int sleepTime) throws IOException, InterruptedException {
        boardViewModel.requestMessage();
        boardViewModel.getStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status=="message"){
                    messages.clear();
                    for(message imessage:boardViewModel.getmMessage()){
                        messages.add(imessage);
                    }
                }
            }
        });
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("MessageInfo","animation run Called");
                //动画淡出，列表淡入
                alphaAnimator_lottie=ObjectAnimator.ofFloat(animationView,"scaleX",
                        0f);
                alphaAnimator_message=ObjectAnimator.ofFloat(mRecyclerView,"alpha",
                        1f);
                animatorSet.play(alphaAnimator_lottie).with(alphaAnimator_message);
                animatorSet.start();
                Log.d("MessageInfo",messages.toString());
                mAdapter.setmItems(messages);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                Log.d("MessageInfo","onActivityCreatedFinished");
            }
        },sleepTime);
    }
}