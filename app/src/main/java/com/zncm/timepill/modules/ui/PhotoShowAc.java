package com.zncm.timepill.modules.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.zncm.timepill.R;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.view.photoview.PhotoView;
import com.zncm.timepill.modules.view.photoview.PhotoViewAttacher;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.MyPath;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.TimeUtils;
import com.zncm.timepill.utils.XUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by MX on 2014/6/27.
 */
public class PhotoShowAc extends BaseAc {
    private PhotoView mPhotoView;
    private String photoUrl;
    private ProgressBar pbProgress;
    private TextView tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        photoUrl = getIntent().getStringExtra(TpConstants.KEY_STRING);
        mPhotoView = (PhotoView) findViewById(R.id.mPhotoView);
        pbProgress = (ProgressBar) findViewById(R.id.pbProgress);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        if (StrUtil.notEmptyOrNull(photoUrl) && !photoUrl.startsWith("http://")) {
            photoUrl = "file://" + photoUrl;
        }
        XUtil.getImageLoader().displayImage(photoUrl, mPhotoView, XUtil.getOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                pbProgress.setVisibility(View.GONE);
                tvProgress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                int prcent = (int) (current * 100 / total);
                pbProgress.setVisibility(View.VISIBLE);
                tvProgress.setVisibility(View.VISIBLE);
                tvProgress.setText(prcent + "%");

            }
        });

        mPhotoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });
        mPhotoView.setOnLongClickListener(new View.OnLongClickListener()

                                          {
                                              @Override
                                              public boolean onLongClick(View v) {

                                                  new MaterialDialog.Builder(ctx)
                                                          .items(new String[]{"保存"})
                                                          .itemsCallback(new MaterialDialog.ListCallback() {
                                                              @Override
                                                              public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                                                  switch (which) {
                                                                      case 0:
                                                                          SaveImgTask task = new SaveImgTask(ctx);
                                                                          task.execute("");
                                                                          break;
                                                                      default:
                                                                          break;
                                                                  }
                                                              }
                                                          })
                                                          .show();
                                                  return true;
                                              }
                                          }
        );

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ac_photoshow;
    }

    public void saveImg() {
        String fileName = TimeUtils.getFileSaveTime();
        File file = XUtil.getImageLoader().getDiscCache().get(photoUrl);
        String oldPath = file.getPath();
//        String newPath = MyPath.getPathPicture() + File.separator + fileName+".jpg";
//        boolean flag = false;
//        if (file.exists()) {
//            try {
//                flag = XUtil.copyFileTo(new File(oldPath), new File(newPath));
//            } catch (IOException e) {
//            }
//        }
//        if (flag) {
//
//        } else {
//            XUtil.tShort("图片保存失败 !");
//        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    oldPath, fileName, null);
        } catch (FileNotFoundException e) {

        }
        // 最后通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + oldPath)));
    }


    class SaveImgTask extends AsyncTask<String, Integer, String> {
        SuperActivityToast progressToast = new SuperActivityToast(PhotoShowAc.this, SuperToast.Type.PROGRESS);
        Context mContext;

        public SaveImgTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressToast.setText("图片保存中...");
            progressToast.setIndeterminate(true);
            progressToast.setProgressIndeterminate(true);
            progressToast.show();
        }

        @Override
        protected String doInBackground(String... params) {
            saveImg();
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            progressToast.dismiss();
            XUtil.tShort("图片已保存到相册!");
        }

    }


}
