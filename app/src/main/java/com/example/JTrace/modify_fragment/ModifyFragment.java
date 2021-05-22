package com.example.JTrace.modify_fragment;

import androidx.appcompat.app.ActionBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.app.Activity;

import android.content.Context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.JTrace.R;
import com.example.JTrace.model.User;
import com.example.JTrace.user_info_fragment.UserInfoViewModel;
import com.example.JTrace.widget.ItemGroup;
import com.example.JTrace.widget.RoundImageView;
import com.loper7.layout.TitleBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;


import org.jetbrains.annotations.NotNull;

import java.util.List;



public class ModifyFragment extends Fragment {

    private UserInfoViewModel mViewModel;
    private ItemGroup nameIG, idIG, hobbyIG, constellationIG;
    private RoundImageView avatarView;
    private TitleBar mtitleLayout;
    private NavController mnavController;

    public static ModifyFragment newInstance() {
        return new ModifyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.modify_fragment, container, false);
        idIG = root.findViewById(R.id.ig_id);
        nameIG = root.findViewById(R.id.ig_name);
        hobbyIG = root.findViewById(R.id.ig_hobby);
        constellationIG = root.findViewById(R.id.ig_constellation);
        nameIG = root.findViewById(R.id.ig_name);
        avatarView = root.findViewById(R.id.ri_portrait);
        mtitleLayout = root.findViewById(R.id.tl_title);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        mnavController = Navigation.findNavController(view);
        Context mContext = this.getContext();



        mViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                nameIG.setContentEdt(user.getUser_name());
                idIG.setContentEdt(String.valueOf(user.getUser_id()));
                constellationIG.setContentEdt(user.getUser_constellation());
                Glide.with(mContext).load(user.getUser_avatar()).into(avatarView);
            }
        });
        hobbyIG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了更改爱好", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_modifyFragment_to_modify_hobbyFragment);
            }
        });
        constellationIG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了更改星座", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_modifyFragment_to_modify_constellationFragment);
            }
        });
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了更改头像", Toast.LENGTH_SHORT).show();
                show_popup_windows();
            }
        });
        mtitleLayout.setOnMenuListener(new TitleBar.OnMenuListener() {
            @Override
            public void onMenuClick() {
                mViewModel.requestSave();

                mViewModel.requestAvatarPost();

            }
        });
        mtitleLayout.setOnBackListener(new TitleBar.OnBackListener() {
            @Override
            public void onBackClick() {
                mnavController.navigateUp();
            }
        });
    }


    //展示修改头像的选择框，并设置选择框的监听器
    private void show_popup_windows(){
        RelativeLayout layout_photo_selected = (RelativeLayout) getLayoutInflater().inflate(R.layout.photo_select,null);
        PopupWindow popupWindow = null;
        if(popupWindow==null){
            popupWindow = new PopupWindow(layout_photo_selected, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        }
        //显示popupwindows
        popupWindow.showAtLocation(layout_photo_selected, Gravity.CENTER, 0, 0);
        //设置监听器
        TextView take_photo =  (TextView) layout_photo_selected.findViewById(R.id.take_photo);
        TextView from_albums = (TextView)  layout_photo_selected.findViewById(R.id.from_albums);
        LinearLayout cancel = (LinearLayout) layout_photo_selected.findViewById(R.id.cancel);
        //拍照按钮监听
        PopupWindow finalPopupWindow = popupWindow;
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalPopupWindow != null && finalPopupWindow.isShowing()) {
                    PictureSelector.create((Activity) view.getContext())
                            .openCamera(PictureMimeType.ofImage())
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .forResult(new OnResultCallbackListener<LocalMedia>() {
                        @Override
                        public void onResult(List<LocalMedia> result) {
                            String imagepath = result.get(0).getRealPath();
                            Glide.with(view.getContext()).load(imagepath).into(avatarView);
                            Log.d("select image of uri", String.valueOf(imagepath));
                            mViewModel.setPath_avatar(imagepath);
                        }

                        @Override
                        public void onCancel() {
                            Log.d("select image", String.valueOf("exit"));
                        }
                    });
                    finalPopupWindow.dismiss();
                }
            }
        });
        //相册按钮监听
        PopupWindow finalPopupWindow1 = popupWindow;
        from_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create((Activity) view.getContext())
                        .openGallery(PictureMimeType.ofImage())
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                String imagepath = result.get(0).getRealPath();
                                Glide.with(view.getContext()).load(imagepath).into(avatarView);
                                Log.d("select image", String.valueOf(imagepath));
                                mViewModel.setPath_avatar(imagepath);
                                // onResult Callback
                            }

                            @Override
                            public void onCancel() {
                                Log.d("select image", String.valueOf("exit"));
                                // onCancel Callback
                            }
                        });
                finalPopupWindow1.dismiss();
                Log.d("select image from bulkm", String.valueOf("hello"));
            }
        });
        //取消按钮监听
        PopupWindow finalPopupWindow2 = popupWindow;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalPopupWindow2 != null && finalPopupWindow2.isShowing()) {
                    finalPopupWindow2.dismiss();
                }
            }
        });
    }




}




