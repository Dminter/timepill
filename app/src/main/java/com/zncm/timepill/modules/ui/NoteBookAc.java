package com.zncm.timepill.modules.ui;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.malinskiy.materialicons.Iconify;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.note.NoteBookData;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.services.NotebooksService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.utils.MyPath;
import com.zncm.timepill.utils.NetworkUtil;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.TimeUtils;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class NoteBookAc extends BaseAc {
    private NoteBookData data;
    private String upFilePath;
    private String curImgPath;
    private ImageView ivIcon;
    private Button btnDel;
    private EditText etSubject;
    private EditText etExpired;
    private EditText etDescription;
    private CheckBox cbPrivacy;
    private CheckBox cbDefault;
    private boolean bPublic = false;
    private boolean bAdd = false;
    private long end;
    private boolean bDelShow = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Serializable dataParam = null;
        if (getIntent().getExtras() != null) {
            dataParam = getIntent().getSerializableExtra("note_book");
        }
        if (dataParam == null) {
            bAdd = true;
            getSupportActionBar().setTitle("新增日记本");
        } else {
            getSupportActionBar().setTitle("日记本设置");
            data = (NoteBookData) dataParam;
            if (StrUtil.notEmptyOrNull(data.getIsExpired()) && data.getIsExpired().equals("false")) {
                bDelShow = true;
            }
        }
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        btnDel = (Button) findViewById(R.id.btnDel);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etExpired = (EditText) findViewById(R.id.etExpired);
        if (bAdd) {
            etExpired.setVisibility(View.VISIBLE);
            ivIcon.setVisibility(View.GONE);
            btnDel.setVisibility(View.GONE);
            initDate();
        } else {
            etExpired.setVisibility(View.GONE);
            ivIcon.setVisibility(View.VISIBLE);
            if (bDelShow) {
                btnDel.setVisibility(View.VISIBLE);
            } else {
                btnDel.setVisibility(View.GONE);
            }
        }
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null) {
                    delDlg(data);
                }
            }
        });
        etExpired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDlg();
            }
        });
        etDescription = (EditText) findViewById(R.id.etDescription);
        cbPrivacy = (CheckBox) findViewById(R.id.cbPrivacy);
        cbDefault = (CheckBox) findViewById(R.id.cbDefault);
        if (data != null) {
            if (StrUtil.notEmptyOrNull(data.getPrivacy()) && data.getPrivacy().equals("10")) {
                cbPrivacy.setChecked(true);
                bPublic = true;
            } else {
                cbPrivacy.setChecked(false);
                bPublic = false;
            }
        } else {
            cbPrivacy.setChecked(true);
            bPublic = true;
        }
        cbPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    bPublic = true;
                } else {
                    bPublic = false;
                }
            }
        });

        if (data != null && StrUtil.notEmptyOrNull(data.getIsExpired()) && data.getIsExpired().equals("false")) {
            cbDefault.setVisibility(View.VISIBLE);
            if (TpSp.getDefaultNotebookId() == data.getId()) {
                cbDefault.setChecked(true);
            } else {
                cbDefault.setChecked(false);
            }
        } else {
            cbDefault.setVisibility(View.GONE);
        }


        cbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TpSp.setDefaultNotebookId(data.getId());
                }
            }
        });
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPickPhotoAction();
            }
        });
        XUtil.initViewTheme(ctx, findViewById(R.id.llBg));
        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.BLACK.getValue()) {
            etExpired.setTextColor(getResources().getColor(R.color.white));
            etDescription.setTextColor(getResources().getColor(R.color.white));
            etSubject.setTextColor(getResources().getColor(R.color.white));
            cbPrivacy.setTextColor(getResources().getColor(R.color.white));
        }
        initData();
    }


    private void delDlg(final NoteBookData noteBookData) {
        new MaterialDialog.Builder(ctx)
                .content("删除日记本!!!")
                .positiveText("删除")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        delDo(noteBookData.getId());
                    }

                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                    }
                })
                .show();
    }

    public void delDo(int _id) {
        ServiceFactory.getService(NotebooksService.class).deleteNotebook(_id, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                XUtil.tShort("删除成功!");
                backDo();
            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
            }
        });
    }

    private void initData() {
        if (data != null) {
            if (StrUtil.notEmptyOrNull(data.getSubject())) {
                etSubject.setText(data.getSubject());
            }
            if (StrUtil.notEmptyOrNull(data.getDescription())) {
                etDescription.setText(data.getDescription());
            }
            if (StrUtil.notEmptyOrNull(data.getCoverUrl())) {
                ivIcon.setVisibility(View.VISIBLE);
                XUtil.getImageLoader().displayImage(data.getCoverUrl(), ivIcon, XUtil.getOptions());
            } else {
                ivIcon.setVisibility(View.GONE);
            }
        }
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        end = calendar.getTimeInMillis();
        etExpired.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void dateDlg() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        end = cal.getTimeInMillis();
                        etExpired.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                },
                calendar.get(Calendar.YEAR), // 传入年份
                calendar.get(Calendar.MONTH), // 传入月份
                calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
        );
        dialog.show();
    }


    private void doPickPhotoAction() {
        new MaterialDialog.Builder(ctx)
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
            curImgPath = TimeUtils.getFileSaveTime() + ".jpg";
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


    protected void doPickPhotoFromGallery() {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, TpConstants.PICTURE_PICKER);
        } catch (ActivityNotFoundException e) {
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case TpConstants.PICTURE_PICKER:
                upFilePath = XUtil.getPathFromUri(NoteBookAc.this, data.getData());
                upFilePath = XUtil.compressAndSaveImage(upFilePath);
                ivIcon.setImageBitmap(XUtil.getBitmapByPath(upFilePath, 180, 180));
                uploadCover();
                break;
            case TpConstants.PICTURE_TAKE:
                upFilePath = MyPath.getPathPicture() + File.separator + curImgPath;
                upFilePath = XUtil.compressAndSaveImage(upFilePath);
                ivIcon.setImageBitmap(XUtil.getBitmapByPath(upFilePath, 180, 180));
                uploadCover();
                break;
        }
    }

    private void uploadCover() {
        try {
            if (StrUtil.notEmptyOrNull(upFilePath)) {
                File tmp = new File(upFilePath);
                if (tmp.exists()) {
                    ServiceFactory.getService(NotebooksService.class).setNotebookCover(data.getId(), new TypedFile("image/jpeg", tmp), new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            XUtil.getImageLoader().getMemoryCache().clear();
                            XUtil.getImageLoader().getDiskCache().clear();
                            XUtil.tShort("修改头像成功~");
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            XUtil.onFailure(error);
                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("comment");
        item.setIcon(XUtil.initIconWhite(Iconify.IconValue.md_save));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
        if (item.getTitle().equals("comment")) {
            String content = etSubject.getText().toString().trim();
            if (NetworkUtil.detect(NoteBookAc.this)) {
                if (StrUtil.notEmptyOrNull(content)) {
                    if (StrUtil.notEmptyOrNull(content)) {
                        updateNoteBookDo();
                    }
                } else {
                    XUtil.tShort("请输入日记本的主题");
                }
            } else {
                XUtil.tShort("网络连接不可用!");
            }

        }
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.ac_notebook;
    }


    public void updateNoteBookDo() {
        try {
            String subject = etSubject.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String expired = etExpired.getText().toString().trim();
            int privacy = 1;
            if (bPublic) {
                privacy = 10;
            }
            if (bAdd) {
                if (StrUtil.notEmptyOrNull(expired)) {
                    //过期时间 最短一周
                    int day = (int) ((end - System.currentTimeMillis()) / TpConstants.DAY);
                    if (day < 6) {
                        XUtil.tShort("过期时间,最短一周!");
                        return;
                    }
                } else {
                    XUtil.tShort("请设置,过期时间,最短一周!");
                    return;
                }
                ServiceFactory.getService(NotebooksService.class).addNotebook(subject, description, privacy, expired, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        XUtil.tShort("日记本添加成功~");
                        backDo();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        XUtil.onFailure(error);
                    }
                });

            } else {
                ServiceFactory.getService(NotebooksService.class).updateNotebook(data.getId(), subject, description, privacy, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        XUtil.tShort("日记本修改成功~");
                        backDo();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        XUtil.onFailure(error);
                    }
                });
            }
        } catch (Exception e) {
        }
    }

    private void backDo() {
        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NOTE_BOOK_REFRESH.getValue()));
        finish();
    }


}