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
 * Use the {@link modify_constellationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class modify_constellationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText constellationEdit;
    private TitleBar mTitleBar;
    private UserInfoViewModel mViewModel;
    public modify_constellationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment modify_constellationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static modify_constellationFragment newInstance(String param1, String param2) {
        modify_constellationFragment fragment = new modify_constellationFragment();
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
        View root =  inflater.inflate(R.layout.fragment_modify_constellation, container, false);
        constellationEdit = root.findViewById(R.id.new_constellation);
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
                String newname = constellationEdit.getText().toString();
                if(TextUtils.isEmpty(newname)){
                    Toast.makeText(mContext,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
                    Log.d("Login_info","用户名和密码不能为空");
                    return;
                }
                mViewModel.setUserConstellation(newname);
                Toast.makeText(mContext,"new name"+newname,Toast.LENGTH_SHORT).show();
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