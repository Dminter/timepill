package com.zncm.timepill.modules.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.malinskiy.materialicons.Iconify;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.note.NoteBookData;
import com.zncm.timepill.data.base.note.NoteData;
import com.zncm.timepill.data.base.note.NoteTopicData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.services.NoteService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.MyPath;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.TimeUtils;
import com.zncm.timepill.utils.XUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class NoteNewAc extends BaseAc implements View.OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    private EmojiconEditText etContent;
    private ImageView ivImg;
    private List<NoteBookData> noteBookDatas;
    private String[] noteBookName;
    private NoteBookData curNoteBook;
    private boolean haveToic = false;
    private boolean bTopic = false;
    private NoteTopicData topic;
    private String curImgPath;
    private String upFilePath;
    private boolean bTopicImg = false;
    private NoteData noteData;
    private boolean bModify = false;

    ImageView ivCamera, ivPhoto, ivNoteBook, ivFace, ivAutoSave, ivTopic;
    boolean bFace = false;
    FrameLayout faceLayout;

    boolean bClickFirst = true;

    private void assignViews() {
        etContent = (EmojiconEditText) findViewById(R.id.etContent);
        ivImg = (ImageView) findViewById(R.id.ivImg);
        ivCamera = (ImageView) findViewById(R.id.ivCamera);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        ivNoteBook = (ImageView) findViewById(R.id.ivNoteBook);
        ivAutoSave = (ImageView) findViewById(R.id.ivAutoSave);
        ivTopic = (ImageView) findViewById(R.id.ivTopic);
        ivFace = (ImageView) findViewById(R.id.ivFace);


//        ivCamera.setTextColor(TpSp.getThemeColor());
//        ivPhoto.setTextColor(TpSp.getThemeColor());
//        ivPhoto.setTextColor(TpSp.getThemeColor());
//        ivNoteBook.setTextColor(TpSp.getThemeColor());
//        ivAutoSave.setTextColor(TpSp.getThemeColor());
//        ivTopic.setTextColor(TpSp.getThemeColor());
//        ivFace.setTextColor(TpSp.getThemeColor());

        ivCamera.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        ivNoteBook.setOnClickListener(this);
        ivAutoSave.setOnClickListener(this);
        ivFace.setOnClickListener(this);
        ivTopic.setOnClickListener(this);


        faceLayout = (FrameLayout) findViewById(R.id.emojicons);

        XUtil.autoKeyBoardShow(etContent);
    }

    private void initPreData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SEND)) {
                Bundle extras = intent.getExtras();
                String content = String.valueOf(extras.getString(Intent.EXTRA_TEXT));
                if (etContent != null) {
                    etContent.setText(content);
                }
            }
        }
    }

    private void initNoteBooks() {
        noteBookDatas = new ArrayList<NoteBookData>();
        noteBookDatas = TpApplication.getInstance().getNoteBookDatas();
        if (noteBookDatas == null) {
            String noteBookInfo = TpSp.getNoteBookData();
            if (StrUtil.notEmptyOrNull(noteBookInfo)) {
                noteBookDatas = JSON.parseArray(noteBookInfo, NoteBookData.class);
            } else {
                XUtil.getNoteBook();
            }
        }

        if (StrUtil.listNotNull(noteBookDatas)) {
            curNoteBook = noteBookDatas.get(0);
            noteBookName = new String[noteBookDatas.size()];
            for (int i = 0; i < noteBookDatas.size(); i++) {
                if (TpSp.getDefaultNotebookId() != 0 && TpSp.getDefaultNotebookId() == noteBookDatas.get(i).getId()) {
                    curNoteBook = noteBookDatas.get(i);
                }
                noteBookName[i] = noteBookDatas.get(i).getSubject();
            }
            if (bModify && noteData != null) {
                for (NoteBookData tmp : noteBookDatas) {
                    if (tmp.getId() == noteData.getNotebook_id()) {
                        curNoteBook = tmp;
                        break;
                    }
                }
            }
        }
    }


    private void modifyNote() {
        String content = etContent.getText().toString().trim();
        if (StrUtil.isEmptyOrNull(content)) {
            XUtil.tShort("没有内容");
            finish();
            return;
        }
        if (curNoteBook == null) {
            XUtil.tShort("日记本不存在");
            return;
        }
        finish();
        ServiceFactory.getService(NoteService.class).updateNote(noteData.getId(), content, curNoteBook.getId(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                XUtil.tShort("日记修改成功~");
                EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NOTE_REFRESH.getValue()));
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
                XUtil.tShort("日记修改失败~");
                autoSave();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackOn(false);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(XUtil.initIconWhite(Iconify.IconValue.md_done));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bModify) {
                    modifyNote();
                } else {
                    saveNote();
                }
            }
        });

        assignViews();

        initPreData();

        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            findViewById(R.id.llBg).setBackgroundColor(getResources().getColor(R.color.white));
//            findViewById(R.id.llTool).setBackgroundColor(getResources().getColor(R.color.white));
            etContent.setTextColor(getResources().getColor(R.color.black));
            etContent.setHintTextColor(getResources().getColor(R.color.black));
        } else {
            findViewById(R.id.llBg).setBackgroundColor(getResources().getColor(R.color.night_bg));
//            findViewById(R.id.llTool).setBackgroundColor(getResources().getColor(R.color.night_bg));
            etContent.setTextColor(getResources().getColor(R.color.white));
            etContent.setHintTextColor(getResources().getColor(R.color.white));
        }


        etContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (bClickFirst) {
                    bFace = false;
                    faceLayout.setVisibility(View.GONE);
                    bClickFirst = false;
                }

            }
        });
        etContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bFace = false;
                faceLayout.setVisibility(View.GONE);
            }
        });


        String note_content = null;
        Serializable dataParam = null;
        if (getIntent().getExtras() != null) {
            note_content = getIntent().getExtras().getString("note_content");
            dataParam = getIntent().getSerializableExtra("note");
        }


        if (StrUtil.notEmptyOrNull(note_content)) {
            etContent.setText(note_content);
        }

        if (dataParam != null && dataParam instanceof NoteData) {
            noteData = (NoteData) dataParam;
            if (noteData != null && StrUtil.notEmptyOrNull(noteData.getContent())) {
                bModify = true;
                etContent.setText(noteData.getContent());
                etContent.setSelection(noteData.getContent().length());
            }
        }

        initNoteBooks();


        topic = TpApplication.getInstance().getTopic();
        if (topic != null) {
            haveToic = true;
        } else {
            haveToic = false;
        }


        if (haveToic) {
            ivTopic.setVisibility(View.VISIBLE);
        } else {
            ivTopic.setVisibility(View.GONE);
        }

        if (curNoteBook == null) {
            XUtil.tLong("哇哦！！没有日记本了怎么办~~");
            finish();
        } else {
            initNoteBook();

        }
        if (bModify) {
            ivCamera.setVisibility(View.GONE);
            ivPhoto.setVisibility(View.GONE);
            ivTopic.setVisibility(View.GONE);
        }

    }

    private void initNoteBook() {
        if (StrUtil.notEmptyOrNull(curNoteBook.getSubject())) {
            toolbar.setTitle(curNoteBook.getSubject());
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_note_new;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
//        switch (item.getItemId()) {
//            case 1:
//                img();
//                break;
//            case 2:
//                topic();
//                break;
//            case 3:
////                faceView.showPopView(toolbar);
//                setEmojiconFragment(true);
//                break;
//            case 4:
//                chooseNoteBook();
//                break;
//            case 5:
////                autoSave();
////                finish();
//                PictureProcess mPictureProcess = new PictureProcess(NoteNewAc.this);
//// Gallery Select!
//                mPictureProcess.setPictureFrom(PictureFrom.GALLERY);
//                mPictureProcess.setClip(false);
//                mPictureProcess.setMaxPictureCount(5);
//                mPictureProcess.execute(new OnPicturePickListener() {
//                    @Override
//                    public void onSuccess(List<String> pictures) {
//
//                        try {
//                            Bitmap first = XUtil.getBitmapByPath(pictures.get(0), 480, 800);
//                            Bitmap second = XUtil.getBitmapByPath(pictures.get(1), 480, 800);
//                            Bitmap bitmap = XUtil.add2Bitmap(first, second);
//                            if (bitmap != null) {
//                                upFilePath = XUtil.saveImage(bitmap);
//                                bTopicImg = true;
//                                imgPre();
//                            }
//                        } catch (Exception e) {
//                        }
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                });
//
//
//                break;
//            case 6:
//                finish();
//                break;
//
//        }
        return super.onOptionsItemSelected(item);
    }

    private void topic() {
        new MaterialDialog.Builder(ctx)
                .items(!bTopic ? new String[]{"加入话题", "插入话题图"} : new String[]{"取消话题"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                if (bTopic) {
                                    bTopic = false;
                                    toolbar.setSubtitle("");
                                    if (bTopicImg) {
                                        upFilePath = null;
                                        ivImg.setVisibility(View.GONE);
                                    }
                                } else {
                                    bTopic = true;
                                    topicShow();
                                }
                                break;
                            case 1:
                                bTopic = true;
                                topicShow();
                                if (topic != null && StrUtil.notEmptyOrNull(topic.getImageUrl()) && StrUtil.notEmptyOrNull(topic.getTitle())) {
                                    XUtil.getImageLoader().loadImage(topic.getImageUrl(), XUtil.getOptions(), new SimpleImageLoadingListener() {
                                                @Override
                                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                                    String text = topic.getTitle() + "\n\n" + topic.getIntro() + "\n\n" + StrUtil.timeDate(topic.getCreated());
                                                    try {
                                                        Bitmap bitmap = XUtil.getTopicPic(ctx, text, loadedImage);
                                                        if (bitmap != null) {
                                                            upFilePath = XUtil.saveImage(bitmap);
                                                            bTopicImg = true;
                                                            imgPre();
                                                        }
                                                    } catch (Exception e) {
                                                    }

                                                }
                                            }
                                    );
                                }
                                break;
                        }
                    }
                })
                .show();
    }

    private void img() {
        new MaterialDialog.Builder(ctx)
                .items(StrUtil.isEmptyOrNull(upFilePath) ? new String[]{"拍照", "相册"} : new String[]{"拍照", "相册", "删除图片"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                doTakePhoto();
                                break;
                            case 1:
                                doPickPhotoFromGallery();
                                break;
                            case 2:
                                upFilePath = null;
                                ivImg.setVisibility(View.GONE);
                                break;
                        }
                    }
                })
                .show();
    }

    private void chooseNoteBook() {
        if (noteBookName == null) {
            initNoteBooks();
        }
        new MaterialDialog.Builder(ctx)
                .items(noteBookName)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        curNoteBook = noteBookDatas.get(which);
                        initNoteBook();
                    }
                })
                .show();
    }

    private void autoSave() {
        if (bModify) {
            return;
        }
        String content = etContent.getText().toString().trim();
        if (StrUtil.notEmptyOrNull(content)) {
            DbUtils.saveDraftData(content, EnumData.DataTypeEnum.DRAFT.getValue());
        }
    }

    private void topicShow() {
        if (topic != null && StrUtil.notEmptyOrNull(topic.getTitle())) {
            toolbar.setSubtitle("加入话题：" + topic.getTitle());
        }
    }


    private void doTakePhoto() {
        try {
            curImgPath = TimeUtils.getFileSaveTime();
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
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, TpConstants.PICTURE_PICKER);
        } catch (ActivityNotFoundException e) {
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case TpConstants.PICTURE_PICKER:
                final String path = XUtil.compressAndSaveImage(XUtil.getPathFromUri(ctx, data.getData()));
//                if (StrUtil.notEmptyOrNull(upFilePath)) {
//                    new MaterialDialog.Builder(ctx)
//                            .items(new String[]{"替换", "拼接【Beta】"})
//                            .itemsCallback(new MaterialDialog.ListCallback() {
//                                @Override
//                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                                    switch (which) {
//                                        case 0:
//                                            upFilePath = path;
//                                            break;
//                                        case 1:
//                                            Bitmap bitmap = XUtil.add2Bitmap(XUtil.decodeSampledBitmapFromFile(upFilePath, -1,
//                                                    1280), XUtil.decodeSampledBitmapFromFile(path, -1,
//                                                    1280));
//                                            upFilePath = XUtil.compressAndSaveImageFromBitmap(bitmap);
//                                            break;
//                                    }
//                                    imgPre();
//                                }
//                            })
//                            .show();
//                } else {
//                    upFilePath = path;
//                    imgPre();
//                }
                upFilePath = path;
                imgPre();
                break;
            case TpConstants.PICTURE_TAKE:
                String pathTake = MyPath.getPathPicture() + File.separator + curImgPath;
                upFilePath = XUtil.compressAndSaveImage(pathTake);
                imgPre();
                break;
        }
    }

    private void imgPre() {
        if (StrUtil.isEmptyOrNull(upFilePath)) {
            return;
        }
        ivImg.setVisibility(View.VISIBLE);
        XUtil.getImageLoader().displayImage("file://" + upFilePath,
                ivImg, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        ivImg.setImageResource(R.drawable.img_loading);
                        super.onLoadingStarted(imageUri, view);
                    }
                });
        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(ctx)
                        .items(new String[]{"预览", "删除"})
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(ctx, PhotoShowAc.class);
                                        intent.putExtra(TpConstants.KEY_STRING, upFilePath);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        upFilePath = null;
                                        ivImg.setVisibility(View.GONE);
                                        break;

                                }
                            }
                        })
                        .show();


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        SubMenu sub = menu.addSubMenu("");
//        sub.setIcon(XUtil.initIconWhite(Iconify.IconValue.md_more_vert));
//        if (!bModify) {
//            sub.add(0, 1, 0, "图片");
//            if (haveToic) {
//                sub.add(0, 2, 0, "话题");
//            }
//        }
//        sub.add(0, 3, 0, "表情");
//        sub.add(0, 4, 0, "笔记本");
//        if (!bModify) {
//            sub.add(0, 5, 0, "存草稿");
//        }
//        sub.add(0, 6, 0, "退出");
//        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    private void saveNote() {
        try {
            String content = etContent.getText().toString().trim();
            if (!StrUtil.listNotNull(noteBookDatas)) {
                XUtil.tShort("日记本用完了~");
                return;
            }
            if (StrUtil.notEmptyOrNull(upFilePath) && StrUtil.isEmptyOrNull(content)) {
                content = "图片";
            }
            if (StrUtil.isEmptyOrNull(content)) {
                XUtil.tShort("没有内容");
                finish();
                return;
            }
            if (StrUtil.notEmptyOrNull(upFilePath)) {
                File tmp = new File(upFilePath);
                if (tmp.exists() && tmp.length() > 0) {
                    ServiceFactory.getService(NoteService.class).addNoteWithFile(curNoteBook.getId(), content, new TypedFile("image/jpeg", new File(upFilePath)), bTopic ? "1" : null, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            XUtil.tShort("日记发布成功~");
                            EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NOTE_REFRESH.getValue()));
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            XUtil.onFailure(error);
                        }
                    });
                } else {
                    XUtil.tShort("图片文件不存在~");
                }
            } else {
                ServiceFactory.getService(NoteService.class).addNote(curNoteBook.getId(), content, bTopic ? "1" : null, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        XUtil.tShort("日记发布成功~");
                        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NOTE_REFRESH.getValue()));

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        XUtil.tShort("很抱歉，日记发送失败~~");
                        autoSave();
                        XUtil.onFailure(error);
                    }
                });
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        autoSave();
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(etContent, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(etContent);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.ivFace) {
            if (bFace) {
                bFace = false;
                faceLayout.setVisibility(View.GONE);
                XUtil.autoKeyBoardShow(etContent);
            } else {
                bFace = true;
                faceLayout.setVisibility(View.VISIBLE);
                XUtil.hideKeyBoard(etContent);
            }
            setEmojiconFragment(bFace);
        }
        if (v.getId() == R.id.ivPhoto) {
            doPickPhotoFromGallery();
        }
        if (v.getId() == R.id.ivCamera) {
            doTakePhoto();
        }
        if (v.getId() == R.id.ivTopic) {
            topic();
        }
        if (v.getId() == R.id.ivAutoSave) {
            autoSave();
            finish();
        }
        if (v.getId() == R.id.ivNoteBook) {
            chooseNoteBook();
        }

    }
}
