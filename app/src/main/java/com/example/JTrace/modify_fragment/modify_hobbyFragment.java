package com.example.JTrace.modify_fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.donkingliang.labels.LabelsView;
import com.example.JTrace.R;
import com.example.JTrace.user_info_fragment.UserInfoViewModel;
import com.loper7.layout.TitleBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link modify_hobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class modify_hobbyFragment extends Fragment {
    private UserInfoViewModel mViewModel;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button msave;
    private TitleBar mTitleBar;
    private LabelsView mLablesView;
    private List<String> hobbylist = new ArrayList<>();
    private Button addTag;
    private Button searchTag;
    private RecyclerView recyclerView;
    private SectionMultipleItemAdapter sectionMultipleItemAdapter;
    private EditText editText;
    private List<MultiItemEntity> list_data;
    public modify_hobbyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment modify_hobbyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static modify_hobbyFragment newInstance(String param1, String param2) {
        modify_hobbyFragment fragment = new modify_hobbyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_modify_hobby, container, false);
        mTitleBar = root.findViewById(R.id.commmon_return);
        mLablesView = root.findViewById(R.id.labels_hobby);
        addTag = root.findViewById(R.id.button3);
        //searchTag = root.findViewById(R.id.button3);
        recyclerView =root.findViewById(R.id.recycle_hobby);
        editText = root.findViewById(R.id.editTextTextPersonName);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        list_data = new ArrayList<>();
        int lvCount =6;
        int lv1Count =5;
        for (int i = 0; i < lvCount; i++) {
            TagBean item0 = new TagBean("一级列表标题" + i);
            list_data.add(item0);
            for (int j = 0; j < lv1Count; j++) {
                HobbyBean item1 = new HobbyBean("二级列表标题" + j);
                list_data.add(item1);
            }
        }
        List<String> mhobbylist = mViewModel.readUser().getUser_hobby();
        for (String mdata:mhobbylist) {
            hobbylist.add(mdata);
        }
        Log.d("now:", String.valueOf(hobbylist));
        if(hobbylist.size()==0){
            hobbylist.add("+");
            Log.d("now:", String.valueOf(hobbylist));
        }
        if(hobbylist.get(hobbylist.size()-1)!="+"){
            hobbylist.add("+");
        }
        mLablesView.setLabels(hobbylist);
        Context mContext = this.getContext();

        mLablesView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                if(position==(hobbylist.size()-1)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("请输入");    //设置对话框标题
                    builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                    final EditText edit = new EditText(mContext);
                    builder.setView(edit);
                    builder.setCancelable(true);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "你输入的是: " + edit.getText().toString(), Toast.LENGTH_SHORT).show();
                            hobbylist.add(position,edit.getText().toString());
                            mLablesView.setLabels(hobbylist);
                            Log.d("now:", String.valueOf(hobbylist));
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "你点了取消", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create();  //创建对话框
                    dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                    dialog.show();
                }
            }
        });
        mLablesView.setOnLabelLongClickListener(new LabelsView.OnLabelLongClickListener() {
            @Override
            public boolean onLabelLongClick(TextView label, Object data, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("确定删除？");    //设置对话框标题
                builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
                builder.setCancelable(true);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "确认删除 ", Toast.LENGTH_SHORT).show();
                        hobbylist.remove(position);
                        mLablesView.setLabels(hobbylist);
                        if(hobbylist.size()!=0&&hobbylist.get(hobbylist.size()-1)=="+"){
                            hobbylist.remove(hobbylist.size()-1);
                        }
                        Log.d("save", String.valueOf(hobbylist));
                        mViewModel.setUserHobby(hobbylist);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "你点了取消", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();  //创建对话框
                dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
                dialog.show();
                return false;
            }
        });
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newhobby = editText.getText().toString();
                if(TextUtils.isEmpty(newhobby)){
                    Toast.makeText(mContext,"不能为空",Toast.LENGTH_SHORT).show();
                    Log.d("hobby","空");
                    return;
                }
                hobbylist.add(hobbylist.size()-1,newhobby);
                mLablesView.setLabels(hobbylist);
                Log.d("now:", String.valueOf(hobbylist));
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newkeystr = s.toString();

                sectionMultipleItemAdapter.setNewInstance(filter(list_data,newkeystr));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTitleBar.setOnMenuListener(new TitleBar.OnMenuListener() {
            @Override
            public void onMenuClick() {

                if(hobbylist.size()!=0&&hobbylist.get(hobbylist.size()-1)=="+"){
                    hobbylist.remove(hobbylist.size()-1);
                }
                Log.d("save", String.valueOf(hobbylist));
                mViewModel.setUserHobby(hobbylist);
                Toast.makeText(mContext,"已经记录",Toast.LENGTH_SHORT).show();
            }
        });
        mTitleBar.setOnBackListener(new TitleBar.OnBackListener() {
            @Override
            public void onBackClick() {
                Navigation.findNavController(view).navigateUp();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        sectionMultipleItemAdapter = new SectionMultipleItemAdapter(list_data);

        sectionMultipleItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
                Log.d("level 1", String.valueOf(list_data.get(position).getItemType()));
            }
        });
        sectionMultipleItemAdapter.addChildClickViewIds(R.id.textView9);
        sectionMultipleItemAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
                HobbyBean mmhobby = (HobbyBean) adapter.getItem(position);
                hobbylist.add(hobbylist.size()-1,mmhobby.getStyle_tag());
                mLablesView.setLabels(hobbylist);
                Log.d("from list now:", String.valueOf(hobbylist)+mmhobby.getStyle_tag());
            }
        });
        recyclerView.setAdapter(sectionMultipleItemAdapter);
    }
    public List<MultiItemEntity> filter(List<MultiItemEntity> models, String query){
        List<MultiItemEntity> filter_data = new ArrayList<>();
        for (MultiItemEntity oj: models
             ) {
            if(oj.getItemType()==1){
                filter_data.add(oj);
                continue;
            }
            HobbyBean hobby = (HobbyBean) oj;
            if(hobby.getStyle_tag().contains(query)){
                filter_data.add(oj);
            }
        }
        return  filter_data;
    }
}