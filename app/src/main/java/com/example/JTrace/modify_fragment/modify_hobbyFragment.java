package com.example.JTrace.modify_fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.view.inputmethod.InputMethodManager;
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
import java.util.Arrays;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link modify_hobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class modify_hobbyFragment extends Fragment {
    private UserInfoViewModel mViewModel;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
    private InputMethodManager inputMethodManager;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_modify_hobby, container, false);
        mTitleBar = root.findViewById(R.id.commmon_return);
        mLablesView = root.findViewById(R.id.labels_hobby);
        addTag = root.findViewById(R.id.button3);
        recyclerView = root.findViewById(R.id.recycle_hobby);
        editText = root.findViewById(R.id.editTextTextPersonName);
        inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        list_data = new ArrayList<>();


        TagBean item0 = new TagBean("音乐/歌手");
        list_data.add(item0);
        HobbyBean item0_1 = new HobbyBean("古典");
        HobbyBean item0_2 = new HobbyBean("民谣");
        HobbyBean item0_3 = new HobbyBean("摇滚");
        HobbyBean item0_4 = new HobbyBean("爵士乐");
        HobbyBean item0_5 = new HobbyBean("乡村");
        HobbyBean item0_6 = new HobbyBean("陈奕迅");
        HobbyBean item0_7 = new HobbyBean("张国荣");
        HobbyBean item0_8 = new HobbyBean("五月天");
        List list0 = Arrays.asList(item0_1, item0_2, item0_3, item0_4, item0_5, item0_6, item0_7, item0_8);
        list_data.addAll(list0);

        TagBean item1 = new TagBean("性格标签");
        list_data.add(item1);
        HobbyBean item1_1 = new HobbyBean("随性");
        HobbyBean item1_2 = new HobbyBean("活泼开朗");
        HobbyBean item1_3 = new HobbyBean("极品吃货");
        HobbyBean item1_4 = new HobbyBean("文艺青年");
        HobbyBean item1_5 = new HobbyBean("热血少年");
        HobbyBean item1_6 = new HobbyBean("双重人格");
        List list1 = Arrays.asList(item1_1, item1_2, item1_3, item1_4, item1_5, item1_6);
        list_data.addAll(list1);

        TagBean item2 = new TagBean("书籍/作家");
        list_data.add(item1);
        HobbyBean item2_1 = new HobbyBean("麦田的守望者");
        HobbyBean item2_2 = new HobbyBean("百年孤独");
        HobbyBean item2_3 = new HobbyBean("小王子");
        HobbyBean item2_4 = new HobbyBean("月亮与六便士");
        HobbyBean item2_5 = new HobbyBean("东野圭吾");
        HobbyBean item2_6 = new HobbyBean("海子");
        HobbyBean item2_7 = new HobbyBean("韩寒");
        List list2 = Arrays.asList(item2_1, item2_2, item2_3, item2_4, item2_5, item2_6, item2_7);
        list_data.addAll(list2);

        TagBean item3 = new TagBean("运动");
        list_data.add(item1);
        HobbyBean item3_1 = new HobbyBean("游泳");
        HobbyBean item3_2 = new HobbyBean("足球");
        HobbyBean item3_3 = new HobbyBean("篮球");
        HobbyBean item3_4 = new HobbyBean("台球");
        HobbyBean item3_5 = new HobbyBean("攀岩");
        HobbyBean item3_6 = new HobbyBean("乒乓球");
        HobbyBean item3_7 = new HobbyBean("马拉松");
        HobbyBean item3_8 = new HobbyBean("滑雪");
        HobbyBean item3_9 = new HobbyBean("自行车");
        List list3 = Arrays.asList(item3_1, item3_2, item3_3, item3_4, item3_5, item3_6, item3_7, item3_8, item3_9);
        list_data.addAll(list3);

        TagBean item4 = new TagBean("出没地点");
        list_data.add(item1);
        HobbyBean item4_1 = new HobbyBean("自习室");
        HobbyBean item4_2 = new HobbyBean("学校食堂");
        HobbyBean item4_3 = new HobbyBean("健身房");
        HobbyBean item4_4 = new HobbyBean("包图");
        HobbyBean item4_5 = new HobbyBean("新图");
        HobbyBean item4_6 = new HobbyBean("李图");
        HobbyBean item4_7 = new HobbyBean("致远游泳馆");
        HobbyBean item4_8 = new HobbyBean("南体");
        List list4 = Arrays.asList(item4_1, item4_2, item4_3, item4_4, item4_5, item4_6, item4_7, item4_8);
        list_data.addAll(list4);


        List<String> mhobbylist = mViewModel.readUser().getUser_hobby();
        for (String mdata : mhobbylist) {
            hobbylist.add(mdata);
        }
        Log.d("now:", String.valueOf(hobbylist));
        if (hobbylist.size() == 0) {
            hobbylist.add("+");
            Log.d("now:", String.valueOf(hobbylist));
        }
        if (hobbylist.get(hobbylist.size() - 1) != "+") {
            hobbylist.add("+");
        }
        mLablesView.setLabels(hobbylist);
        Context mContext = this.getContext();


        mLablesView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                if (position == (hobbylist.size() - 1)) {
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
                            hobbylist.add(position, edit.getText().toString());
                            mLablesView.setLabels(hobbylist);
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
                        if (hobbylist.size() != 0 && hobbylist.get(hobbylist.size() - 1) == "+") {
                            hobbylist.remove(hobbylist.size() - 1);
                        }
                        mViewModel.setUserHobby(hobbylist);
                        sectionMultipleItemAdapter.setNewInstance(check(list_data,hobbylist));
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
                if (TextUtils.isEmpty(newhobby)) {
                    Toast.makeText(mContext, "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hobbylist.contains(newhobby)) {
                    Toast.makeText(mContext, "存在重复，请检查", Toast.LENGTH_SHORT).show();
                    return;
                }
                hobbylist.add(hobbylist.size() - 1, newhobby);
                mLablesView.setLabels(hobbylist);
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.setText("");

                sectionMultipleItemAdapter.setNewInstance(check(list_data,hobbylist));
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newkeystr = s.toString();

                sectionMultipleItemAdapter.setNewInstance(filter(list_data, newkeystr));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mTitleBar.setOnMenuListener(new TitleBar.OnMenuListener() {
            @Override
            public void onMenuClick() {


                if (hobbylist.size() != 0 && hobbylist.get(hobbylist.size() - 1) == "+") {
                    hobbylist.remove(hobbylist.size() - 1);
                }
                mViewModel.setUserHobby(hobbylist);
                Toast.makeText(mContext, "已经记录", Toast.LENGTH_SHORT).show();
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
        sectionMultipleItemAdapter = new SectionMultipleItemAdapter(check(list_data,hobbylist));

        sectionMultipleItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull BaseQuickAdapter<?, ?> adapter, @NonNull @NotNull View view, int position) {
            }
        });
        sectionMultipleItemAdapter.addChildClickViewIds(R.id.textView9,R.id.imageView6);
        sectionMultipleItemAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull @NotNull BaseQuickAdapter adapter, @NonNull @NotNull View view, int position) {
                HobbyBean mmhobby = (HobbyBean) adapter.getItem(position);
                if ( hobbylist.contains(mmhobby.getStyle_tag())) {
                    Toast.makeText(mContext, "存在重复，请检查", Toast.LENGTH_SHORT).show();
                    return;
                }
                hobbylist.add(hobbylist.size() - 1, mmhobby.getStyle_tag());
                mLablesView.setLabels(hobbylist);
                sectionMultipleItemAdapter.setNewInstance(check(list_data,hobbylist));
            }
        });
        recyclerView.setAdapter(sectionMultipleItemAdapter);
    }
    public List<MultiItemEntity> check(List<MultiItemEntity> models, List<String> mhobbylist ) {
        List<MultiItemEntity> filter_data = new ArrayList<>();
        for (MultiItemEntity oj : models
        ) {
            if (oj.getItemType() == 1) {
                filter_data.add(oj);
                continue;
            }
            HobbyBean hobby = (HobbyBean) oj;
            if (mhobbylist.contains(hobby.getStyle_tag())) {
                ((HobbyBean) oj).setChecked(true);

            } else {
                ((HobbyBean) oj).setChecked(false);
            }
            filter_data.add(oj);
        }
        return filter_data;
    }


    public List<MultiItemEntity> filter(List<MultiItemEntity> models, String query) {
        List<MultiItemEntity> filter_data = new ArrayList<>();
        for (MultiItemEntity oj : models
        ) {
            if (oj.getItemType() == 1) {
                filter_data.add(oj);
                continue;
            }
            HobbyBean hobby = (HobbyBean) oj;
            if (hobby.getStyle_tag().contains(query)) {
                filter_data.add(oj);
            }
        }
        return filter_data;
    }
}