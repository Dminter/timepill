package com.zncm.timepill.modules.ft;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.umeng.analytics.MobclickAgent;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.services.UserService;
import com.zncm.timepill.modules.ui.UserSettingAc;
import com.zncm.timepill.modules.ui.UserUpdateAc;
import com.zncm.timepill.utils.MyPath;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.TimeUtils;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.File;
import java.io.Serializable;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class UserSettingFt extends BaseFt {

    View view;
    private UserData userData;
    private String curImgPath;
    private String upFilePath;
    private RelativeLayout rlHead;
    private ImageView ivIcon;
    private RelativeLayout rlName;
    private TextView tvName;
    private RelativeLayout rlDesc;
    private TextView tvIntro;
    UserSettingAc ctx;

    private void assignViews() {
        rlHead = (RelativeLayout) view.findViewById(R.id.rlHead);
        ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
        rlName = (RelativeLayout) view.findViewById(R.id.rlName);
        tvName = (TextView) view.findViewById(R.id.tvName);
        rlDesc = (RelativeLayout) view.findViewById(R.id.rlDesc);
        tvIntro = (TextView) view.findViewById(R.id.tvIntro);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = (UserSettingAc) getActivity();
        view = inflater.inflate(R.layout.user_setting, null);
        assignViews();
        initViews();
        getUserInfo();
        return view;
    }


    public void initViews() {
        Intent intent = ctx.getIntent();
        if (intent != null && intent.getExtras() != null) {
            Serializable dataParam = intent.getSerializableExtra(TpConstants.KEY_PARAM_DATA);
            userData = (UserData) dataParam;
        }
        initData();


        rlHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPickPhotoAction();
            }
        });
        rlName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, UserUpdateAc.class);
                intent.putExtra(TpConstants.KEY_ID, "修改名字");
                intent.putExtra("user", userData);
                startActivity(intent);
            }
        });


        rlDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, UserUpdateAc.class);
                intent.putExtra(TpConstants.KEY_ID, "修改介绍");
                intent.putExtra("user", userData);
                startActivity(intent);
            }
        });
    }


    private void initData() {
        if (userData != null) {
            if (StrUtil.notEmptyOrNull(userData.getName())) {
                tvName.setVisibility(View.VISIBLE);
                tvName.setText(userData.getName());
            } else {
                tvName.setVisibility(View.GONE);
            }
            String intro = userData.getIntro();
            if (StrUtil.notEmptyOrNull(intro)) {
                tvIntro.setText(intro);
            } else {
                tvIntro.setText("修改介绍");
            }
            if (StrUtil.notEmptyOrNull(userData.getCoverUrl())) {
                ivIcon.setVisibility(View.VISIBLE);
                XUtil.getImageLoader().displayImage(userData.getCoverUrl(), ivIcon, XUtil.getRoundedOptions());
            } else {
                ivIcon.setVisibility(View.GONE);
            }
        }
    }

    private void doPickPhotoAction() {
        new MaterialDialog.Builder(getActivity())
                .items(new String[]{"拍照", "相册"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                doTakePhoto();
                                break;
                            case 1:
                                doPickPhotoFromGallery();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();


    }

    private void doTakePhoto() {
        try {
            curImgPath = TimeUtils.getFileSaveTime() ;
            File mCurrentPhotoFile = new File(MyPath.getPathPicture(), curImgPath);
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, TpConstants.PICTURE_TAKE);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }


    // 请求Gallery程序
    protected void doPickPhotoFromGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, TpConstants.PICTURE_PICKER);
        } catch (ActivityNotFoundException e) {
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case TpConstants.PICTURE_PICKER:
                doCropPhotoSmall(data.getData());
                break;
            case TpConstants.PICTURE_TAKE:
                upFilePath = MyPath.getPathPicture() + File.separator + curImgPath;
                Uri uri = XUtil.getFileUri(upFilePath);
                if (uri != null) {
                    doCropPhotoSmall(uri);
                }
                break;
            case TpConstants.REQUESTCODE_CROP:
                if (data != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (ivIcon != null && bitmap != null) {
                        ivIcon.setImageBitmap(bitmap);
                        upFilePath = XUtil.saveImage(bitmap);
                        uploadHead();
                    }
                } else {
                    XUtil.tShort("选取图片失败");
                }
                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadHead() {
        if (StrUtil.isEmptyOrNull(upFilePath)) {
            return;
        }
        ServiceFactory.getService(UserService.class).setUserCover(new TypedFile("image/jpeg", new File(upFilePath)), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                MobclickAgent.onEvent(ctx, "uploadHead");
                XUtil.tShort("修改头像成功~");
                getUserInfo();
            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
            }
        });
    }


    //获取用户信息
    public void getUserInfo() {
        ServiceFactory.getService(UserService.class).getMyInfo(new Callback<UserData>() {
            @Override
            public void success(UserData userData, Response response) {
                if (userData == null) {
                    return;
                }
                MobclickAgent.onEvent(ctx, "getUserInfo");
                UserSettingFt.this.userData = userData;
                userData.setAccess_token(TpApplication.getInstance().getUserData().getAccess_token());
                TpApplication.getInstance().setUserData(userData);
                TpSp.setUserInfo(userData.toString());
                initData();

            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
            }
        });

    }


    public void doCropPhotoSmall(Uri uri) {
        try {
            Intent intent = getCropImageIntent(uri, 240, 240);
            startActivityForResult(intent, TpConstants.REQUESTCODE_CROP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Intent getCropImageIntent(Uri uri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        return intent;
    }


}