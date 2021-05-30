package com.example.JTrace.modify_fragment;

import androidx.appcompat.app.ActionBar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.app.Activity;

import android.content.Context;

import android.graphics.Color;
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

import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.NumberPicker;
import cn.addapp.pickers.picker.SinglePicker;
import cn.addapp.pickers.util.ConvertUtils;
import cn.addapp.pickers.widget.WheelView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;


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
                boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
                SinglePicker picker = new SinglePicker((Activity) mContext,
                        isChinese ? new String[]{
                                "水瓶", "双鱼", "白羊", "金牛", "双子", "巨蟹",
                                "狮子", "处女", "天秤", "天蝎", "射手", "摩羯"
                        } : new String[]{
                                "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
                                "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
                        });
                picker.setLabel(isChinese ? "座" : "");
                picker.setCanLoop(false);//禁用循环
                LineConfig config = new LineConfig();
                config.setColor(Color.parseColor("#26A1B0"));//线颜色
                config.setAlpha(100);//线透明度
                config.setThick(ConvertUtils.toPx(mContext, 2));//线粗
                picker.setLineConfig(config);
                picker.setTopHeight(50);//顶部标题栏高度
                picker.setTopLineColor(0xFF33B5E5);//顶部标题栏下划线颜色
                picker.setTopLineHeight(1);//顶部标题栏下划线高度
                picker.setTitleText(isChinese ? "请选择" : "Please pick");
                picker.setTitleTextColor(0xFF999999);//顶部标题颜色
                picker.setTitleTextSize(14);//顶部标题文字大小
                picker.setCancelTextColor(0xFF33B5E5);//顶部取消按钮文字颜色
                picker.setCancelTextSize(14);
                picker.setSubmitTextColor(0xFF33B5E5);//顶部确定按钮文字颜色
                picker.setSubmitTextSize(14);
                picker.setSelectedTextColor(0xFFEE0000);
                picker.setUnSelectedTextColor(0xFF999999);
                //中间滚动项文字颜色
                picker.setSize(800, 800);
                picker.setBackgroundColor(0xFFF5F5F5);
                picker.setSelectedIndex(10);//默认选中项
                picker.setOnItemPickListener(new OnItemPickListener() {
                    @Override
                    public void onItemPicked(int index, Object item) {
                        mViewModel.setUserConstellation(item.toString());
                        Toast.makeText(mContext, "选择星座:" + item.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                picker.show();
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
    private void show_popup_windows() {
        RelativeLayout layout_photo_selected = (RelativeLayout) getLayoutInflater().inflate(R.layout.photo_select, null);
        PopupWindow popupWindow = null;
        if (popupWindow == null) {
            popupWindow = new PopupWindow(layout_photo_selected, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        }
        //显示popupwindows
        popupWindow.showAtLocation(layout_photo_selected, Gravity.CENTER, 0, 0);
        //设置监听器
        TextView take_photo = (TextView) layout_photo_selected.findViewById(R.id.take_photo);
        TextView from_albums = (TextView) layout_photo_selected.findViewById(R.id.from_albums);
        LinearLayout cancel = (LinearLayout) layout_photo_selected.findViewById(R.id.cancel);
        //拍照按钮监听
        PopupWindow finalPopupWindow = popupWindow;
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalPopupWindow != null && finalPopupWindow.isShowing()) {
                    PictureSelector.create((Activity) view.getContext())
                            .openCamera(PictureMimeType.ofImage())
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .forResult(new OnResultCallbackListener<LocalMedia>() {
                                @Override
                                public void onResult(List<LocalMedia> result) {
                                    String imagepath = result.get(0).getRealPath();
                                    Glide.with(view.getContext()).load(imagepath).into(avatarView);
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
                                mViewModel.setPath_avatar(imagepath);
                            }

                            @Override
                            public void onCancel() {
                                Log.d("select image", String.valueOf("exit"));
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




