package com.example.JTrace.commom_user_fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.JTrace.R;
import com.loper7.layout.TitleBar;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFriendFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String username2;
    private EditText userverifyEdit;
    private TitleBar mTitleBar;
    private Button mAdd;
    private CommonUserInfoViewModel mViewModel;
    private LifecycleOwner mLifecycleOwner;
    private String mParam1;
    private String mParam2;

    public AddFriendFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFriendFragment.
     */
    public static AddFriendFragment newInstance(String param1, String param2) {
        AddFriendFragment fragment = new AddFriendFragment();
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
            username2 = getArguments().getString("name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_friend, container, false);
        userverifyEdit = root.findViewById(R.id.editTextTextMultiLine);
        mTitleBar = root.findViewById(R.id.commmon_return);
        mAdd = root.findViewById(R.id.button_add);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(CommonUserInfoViewModel.class);
        mLifecycleOwner = getViewLifecycleOwner();
        Context mContext = this.getContext();
        mTitleBar.setOnBackListener(new TitleBar.OnBackListener() {
            @Override
            public void onBackClick() {
                Navigation.findNavController(view).navigateUp();
            }
        });
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname = userverifyEdit.getText().toString();
                if (TextUtils.isEmpty(newname)) {
                    Toast.makeText(mContext, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                mViewModel.requestAddFriend(username2, newname);
                mViewModel.getStatus().observe(mLifecycleOwner, new Observer<String>() {
                    @Override
                    public void onChanged(String satus) {
                        if (satus == "addsuccess") {
                            Toast.makeText(mContext, "Request successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}