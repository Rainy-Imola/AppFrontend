package com.example.JTrace.modify_fragment;

import androidx.appcompat.app.ActionBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.app.Activity;

import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.example.JTrace.R;
import com.example.JTrace.model.User;
import com.example.JTrace.user_info_fragment.UserInfoViewModel;
import com.example.JTrace.widget.ItemGroup;
import com.example.JTrace.widget.RoundImageView;
import com.loper7.layout.TitleBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
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
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    private ItemGroup nameIG, idIG, hobbyIG, constellationIG;
    private RoundImageView avatarView;
    private TitleBar mtitleLayout;
    private NavController mnavController;
    private Context mContext;
    private ConstraintLayout constraintLayout_avatar;
    private String default_link;
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
        constraintLayout_avatar = root.findViewById(R.id.constraintLayout2);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        mnavController = Navigation.findNavController(view);
        mContext = this.getContext();
        mViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                nameIG.setContentEdt(user.getUser_name());
                idIG.setContentEdt(String.valueOf(user.getUser_id()));
                constellationIG.setContentEdt(user.getUser_constellation());
                Glide.with(mContext).asBitmap().load(user.getUser_avatar()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                        avatarView.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Glide.with(mContext).asBitmap().load(default_link).into(avatarView);
                        Toast.makeText(mContext, "?????????????????????????????????", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        hobbyIG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_modifyFragment_to_modify_hobbyFragment);
            }
        });
        constellationIG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
                boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("??????");
                SinglePicker picker = new SinglePicker((Activity) mContext,
                        isChinese ? new String[]{
                                "??????", "??????", "??????", "??????", "??????", "??????",
                                "??????", "??????", "??????", "??????", "??????", "??????"
                        } : new String[]{
                                "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
                                "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
                        });
                picker.setLabel(isChinese ? "???" : "");
                picker.setCanLoop(false);//????????????
                LineConfig config = new LineConfig();
                config.setColor(Color.parseColor("#26A1B0"));//?????????
                config.setAlpha(100);//????????????
                config.setThick(ConvertUtils.toPx(mContext, 2));//??????
                picker.setLineConfig(config);
                picker.setTopHeight(50);//?????????????????????
                picker.setTopLineColor(0xFF33B5E5);//??????????????????????????????
                picker.setTopLineHeight(1);//??????????????????????????????
                picker.setTitleText(isChinese ? "?????????" : "Please pick");
                picker.setTitleTextColor(0xFF999999);//??????????????????
                picker.setTitleTextSize(14);//????????????????????????
                picker.setCancelTextColor(0xFF33B5E5);//??????????????????????????????
                picker.setCancelTextSize(14);
                picker.setSubmitTextColor(0xFF33B5E5);//??????????????????????????????
                picker.setSubmitTextSize(14);
                picker.setSelectedTextColor(0xFFEE0000);
                picker.setUnSelectedTextColor(0xFF999999);
                //???????????????????????????
                picker.setSize(800, 800);
                picker.setBackgroundColor(0xFFF5F5F5);
                picker.setSelectedIndex(10);//???????????????
                picker.setOnItemPickListener(new OnItemPickListener() {
                    @Override
                    public void onItemPicked(int index, Object item) {
                        mViewModel.setUserConstellation(item.toString());
                        Toast.makeText(mContext, "????????????:" + item.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

                picker.show();
            }
        });
        constraintLayout_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "????????????", Toast.LENGTH_SHORT).show();
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


    //???????????????????????????????????????????????????????????????
    private void show_popup_windows() {
        RelativeLayout layout_photo_selected = (RelativeLayout) getLayoutInflater().inflate(R.layout.photo_select, null);
        PopupWindow popupWindow = null;
        if (popupWindow == null) {
            popupWindow = new PopupWindow(layout_photo_selected, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        }
        //??????popupwindows
        popupWindow.showAtLocation(layout_photo_selected, Gravity.CENTER, 0, 0);
        //???????????????
        TextView take_photo = (TextView) layout_photo_selected.findViewById(R.id.take_photo);
        TextView from_albums = (TextView) layout_photo_selected.findViewById(R.id.from_albums);
        LinearLayout cancel = (LinearLayout) layout_photo_selected.findViewById(R.id.cancel);
        //??????????????????
        PopupWindow finalPopupWindow = popupWindow;
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create((Activity) view.getContext())
                        .openCamera(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                String imagepath = result.get(0).getRealPath();
                                byte[] byte_image = UserInfoViewModel.image2Bytes(imagepath);
                                Log.d("image size", String.valueOf(byte_image.length));
                                if (byte_image.length > MAX_FILE_SIZE ){
                                    Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT).show();
                                    Glide.with(view.getContext()).load(imagepath).into(avatarView);
                                    mViewModel.setPath_avatar("");
                                }
                                else {
                                    Glide.with(view.getContext()).load(imagepath).into(avatarView);
                                    mViewModel.setPath_avatar(imagepath);
                                }
                            }

                            @Override
                            public void onCancel() {
                                Log.d("select image", String.valueOf("exit"));
                            }
                        });
                finalPopupWindow.dismiss();


            }
        });
        //??????????????????
        PopupWindow finalPopupWindow1 = popupWindow;
        from_albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create((Activity) view.getContext())
                        .openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)
                        .isWeChatStyle(true)
                        .isEnableCrop(true)
                        .isCompress(true)
                        .compressQuality(80)
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                String imagepath = result.get(0).getCompressPath();
                                Log.d("select image", imagepath);
                                byte[] byte_image = UserInfoViewModel.image2Bytes(imagepath);
                                Log.d("image size", String.valueOf(byte_image.length));
                                if (byte_image.length > MAX_FILE_SIZE ){
                                    Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT).show();
                                    Glide.with(view.getContext()).load(imagepath).into(avatarView);
                                    mViewModel.setPath_avatar("");
                                }
                                else {
                                    Glide.with(view.getContext()).load(imagepath).into(avatarView);
                                    mViewModel.setPath_avatar(imagepath);
                                }

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
        //??????????????????
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




