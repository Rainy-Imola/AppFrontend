package com.example.easytalk.modify_fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.easytalk.LoginActivity;
import com.example.easytalk.R;
import com.example.easytalk.user_info_fragment.UserInfoViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link modify_nameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class modify_nameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText usernameEdit;
    private Button msave;
    private UserInfoViewModel mViewModel;
    public modify_nameFragment() {
        // Required empty public constructor
    }
    public static ModifyFragment newInstance() {
        return new ModifyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_modify_name, container, false);
        usernameEdit = root.findViewById(R.id.new_name);
        msave = root.findViewById(R.id.save_name_button);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        Context mContext = this.getContext();
        msave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname = usernameEdit.getText().toString();
                if(TextUtils.isEmpty(newname)){
                    Toast.makeText(v.getContext(),"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
                    Log.d("Login_info","用户名和密码不能为空");
                    return;
                }
                mViewModel.setUserName(newname);
                Toast.makeText(v.getContext(),"new name"+newname,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}