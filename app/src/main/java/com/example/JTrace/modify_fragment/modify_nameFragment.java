package com.example.JTrace.modify_fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.JTrace.R;
import com.example.JTrace.user_info_fragment.UserInfoViewModel;
import com.loper7.layout.TitleBar;

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
    private TitleBar mTitleBar;
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
        mTitleBar = root.findViewById(R.id.commmon_return);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        Context mContext = this.getContext();
        mTitleBar.setOnMenuListener(new TitleBar.OnMenuListener() {
            @Override
            public void onMenuClick() {
                String newname = usernameEdit.getText().toString();
                if (TextUtils.isEmpty(newname)) {
                    Toast.makeText(mContext, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mViewModel.setUserName(newname);
                Toast.makeText(mContext, "new name" + newname, Toast.LENGTH_SHORT).show();
            }
        });
        mTitleBar.setOnBackListener(new TitleBar.OnBackListener() {
            @Override
            public void onBackClick() {
                Navigation.findNavController(view).navigateUp();
            }
        });
    }
}