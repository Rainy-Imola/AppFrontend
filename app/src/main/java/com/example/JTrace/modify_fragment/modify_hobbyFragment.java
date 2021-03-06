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


        TagBean item0 = new TagBean("??????/??????");
        list_data.add(item0);
        HobbyBean item0_1 = new HobbyBean("??????");
        HobbyBean item0_2 = new HobbyBean("??????");
        HobbyBean item0_3 = new HobbyBean("??????");
        HobbyBean item0_4 = new HobbyBean("?????????");
        HobbyBean item0_5 = new HobbyBean("??????");
        HobbyBean item0_6 = new HobbyBean("?????????");
        HobbyBean item0_7 = new HobbyBean("?????????");
        HobbyBean item0_8 = new HobbyBean("?????????");
        List list0 = Arrays.asList(item0_1, item0_2, item0_3, item0_4, item0_5, item0_6, item0_7, item0_8);
        list_data.addAll(list0);

        TagBean item1 = new TagBean("????????????");
        list_data.add(item1);
        HobbyBean item1_1 = new HobbyBean("??????");
        HobbyBean item1_2 = new HobbyBean("????????????");
        HobbyBean item1_3 = new HobbyBean("????????????");
        HobbyBean item1_4 = new HobbyBean("????????????");
        HobbyBean item1_5 = new HobbyBean("????????????");
        HobbyBean item1_6 = new HobbyBean("????????????");
        List list1 = Arrays.asList(item1_1, item1_2, item1_3, item1_4, item1_5, item1_6);
        list_data.addAll(list1);

        TagBean item2 = new TagBean("??????/??????");
        list_data.add(item2);
        HobbyBean item2_1 = new HobbyBean("??????????????????");
        HobbyBean item2_2 = new HobbyBean("????????????");
        HobbyBean item2_3 = new HobbyBean("?????????");
        HobbyBean item2_4 = new HobbyBean("??????????????????");
        HobbyBean item2_5 = new HobbyBean("????????????");
        HobbyBean item2_6 = new HobbyBean("??????");
        HobbyBean item2_7 = new HobbyBean("??????");
        List list2 = Arrays.asList(item2_1, item2_2, item2_3, item2_4, item2_5, item2_6, item2_7);
        list_data.addAll(list2);

        TagBean item3 = new TagBean("??????");
        list_data.add(item3);
        HobbyBean item3_1 = new HobbyBean("??????");
        HobbyBean item3_2 = new HobbyBean("??????");
        HobbyBean item3_3 = new HobbyBean("??????");
        HobbyBean item3_4 = new HobbyBean("??????");
        HobbyBean item3_5 = new HobbyBean("??????");
        HobbyBean item3_6 = new HobbyBean("?????????");
        HobbyBean item3_7 = new HobbyBean("?????????");
        HobbyBean item3_8 = new HobbyBean("??????");
        HobbyBean item3_9 = new HobbyBean("?????????");
        List list3 = Arrays.asList(item3_1, item3_2, item3_3, item3_4, item3_5, item3_6, item3_7, item3_8, item3_9);
        list_data.addAll(list3);

        TagBean item4 = new TagBean("????????????");
        list_data.add(item4);
        HobbyBean item4_1 = new HobbyBean("?????????");
        HobbyBean item4_2 = new HobbyBean("????????????");
        HobbyBean item4_3 = new HobbyBean("?????????");
        HobbyBean item4_4 = new HobbyBean("??????");
        HobbyBean item4_5 = new HobbyBean("??????");
        HobbyBean item4_6 = new HobbyBean("??????");
        HobbyBean item4_7 = new HobbyBean("???????????????");
        HobbyBean item4_8 = new HobbyBean("??????");
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
                    builder.setTitle("?????????");    //?????????????????????
                    builder.setIcon(android.R.drawable.btn_star);   //?????????????????????????????????
                    final EditText edit = new EditText(mContext);
                    builder.setView(edit);
                    builder.setCancelable(true);
                    builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "???????????????: " + edit.getText().toString(), Toast.LENGTH_SHORT).show();
                            hobbylist.add(position, edit.getText().toString());
                            mLablesView.setLabels(hobbylist);
                        }
                    });
                    builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create();  //???????????????
                    dialog.setCanceledOnTouchOutside(true); //???????????????????????????????????????,???????????????????????????????????????
                    dialog.show();
                }
            }
        });
        mLablesView.setOnLabelLongClickListener(new LabelsView.OnLabelLongClickListener() {
            @Override
            public boolean onLabelLongClick(TextView label, Object data, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("???????????????");    //?????????????????????
                builder.setIcon(android.R.drawable.btn_star);   //?????????????????????????????????
                builder.setCancelable(true);
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "???????????? ", Toast.LENGTH_SHORT).show();
                        hobbylist.remove(position);
                        mLablesView.setLabels(hobbylist);
                        if (hobbylist.size() != 0 && hobbylist.get(hobbylist.size() - 1) == "+") {
                            hobbylist.remove(hobbylist.size() - 1);
                        }
                        mViewModel.setUserHobby(hobbylist);
                        sectionMultipleItemAdapter.setNewInstance(check(list_data,hobbylist));
                    }
                });
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();  //???????????????
                dialog.setCanceledOnTouchOutside(true); //???????????????????????????????????????,???????????????????????????????????????
                dialog.show();
                return false;
            }
        });
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newhobby = editText.getText().toString();
                if (TextUtils.isEmpty(newhobby)) {
                    Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (hobbylist.contains(newhobby)) {
                    Toast.makeText(mContext, "????????????????????????", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "????????????????????????", Toast.LENGTH_SHORT).show();
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