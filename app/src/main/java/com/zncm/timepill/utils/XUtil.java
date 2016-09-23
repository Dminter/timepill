package com.zncm.timepill.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.text.method.ArrowKeyMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.Style;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zncm.timepill.BuildConfig;
import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.FaceData;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.note.DraftData;
import com.zncm.timepill.data.base.note.NoteBookData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.services.NotebooksService;
import com.zncm.timepill.modules.services.ServiceFactory;
import com.zncm.timepill.modules.ui.ShowInfoActivity;
import com.zncm.timepill.modules.ui.UserHomeAc;
import com.zncm.timepill.utils.sp.TpSp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class XUtil {


    public static void copyDlg(final Activity ctx, final String str) {
        if (!notEmptyOrNull(str)) {
            return;
        }
        new MaterialDialog.Builder(ctx)
                .items(new String[]{"复制"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                StrUtil.copyText(ctx, str);
                                break;
                        }
                    }
                })
                .show();
    }

    public static void textViewCopy(TextView textView) {
        int __sdkLevel = Build.VERSION.SDK_INT;
        if (__sdkLevel >= 11) {
            textView.setTextIsSelectable(true);
        } else {
            textView.setFocusableInTouchMode(true);
            textView.setFocusable(true);
            textView.setClickable(true);
            textView.setLongClickable(true);
            textView.setMovementMethod(ArrowKeyMovementMethod.getInstance());
            textView.setText(textView.getText(), TextView.BufferType.SPANNABLE);
        }
    }

    public static List<FaceData> getFaces() {
        List<FaceData> faceDatas = new ArrayList<FaceData>();
        ArrayList<DraftData> datas = DbUtils.initDraftData(EnumData.DataTypeEnum.CUSTOM_FACE.getValue());
        if (StrUtil.listNotNull(datas)) {
            for (DraftData data : datas) {
                FaceData faceData = new FaceData(data.getContent());
                faceDatas.add(faceData);
            }
        }
        faceDatas.addAll(TpConstants.getFaces());
        return faceDatas;
    }

    public static ArrayList<String> getFaceStr() {
        ArrayList<String> ret = new ArrayList<String>();
        List<FaceData> datas = getFaces();
        if (StrUtil.listNotNull(datas)) {
            for (FaceData data : datas) {
                ret.add(data.getContent());
            }
        }
        return ret;
    }

    public static void viewUser(Activity ctx, UserData data) {
        if (data != null) {
            Intent newIntent = new Intent(ctx, UserHomeAc.class);
            newIntent.putExtra("isSelf", false);
            newIntent.putExtra("user_info", new MiniUserData(data.getId(), data.getName(), data.getIconUrl()));
            ctx.startActivity(newIntent);
        }

    }


    public static Uri getFileUri(String picPath) {
        if (StrUtil.notEmptyOrNull(picPath)) {
            File file = new File(picPath);
            if (file != null) {
                if (file.exists()) {
                    return Uri.fromFile(file);
                }
            }
        }
        return null;
    }


    public static void onFailure(RetrofitError error) {
        try {
            XUtil.tShort(error.getMessage());
        } catch (Exception e) {

        }
    }

    public static void initViewTheme(Context ctx, View view) {
        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            view.setBackgroundColor(ctx.getResources().getColor(R.color.day_bg));
        } else {
            view.setBackgroundColor(ctx.getResources().getColor(R.color.night_bg));
        }
    }

    public static void initIndicatorTheme(Context ctx, View view) {
        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            view.setBackgroundColor(TpSp.getThemeColor());
        } else {
            view.setBackgroundColor(ctx.getResources().getColor(R.color.night_bg));
        }
    }

    public static void initTextViewTheme(Context ctx, TextView view) {
        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            view.setTextColor(ctx.getResources().getColor(R.color.black));
        } else {
            view.setTextColor(ctx.getResources().getColor(R.color.white));
        }
    }


    public static File createFile(String path) throws IOException {
        if (notEmptyOrNull(path)) {
            File file = new File(path);
            if (!file.exists()) {
                int lastIndex = path.lastIndexOf(File.separator);
                String dir = path.substring(0, lastIndex);
                if (createFolder(dir) != null) {
                    file.createNewFile();
                    return file;
                }
            } else {
                file.createNewFile();
                return file;
            }
        }
        return null;
    }

    public static boolean copyFileTo(File srcFile, File destFile)
            throws IOException {
        if (srcFile == null || destFile == null) {
            return false;
        }
        if (srcFile.isDirectory() || destFile.isDirectory())
            return false;
        if (!srcFile.exists()) {
            return false;
        }
        if (!destFile.exists()) {
            createFile(destFile.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = fis.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        fis.close();
        return true;
    }

    public static boolean isEmptyOrNull(String string) {
        if (string == null || string.trim().length() == 0 || string.equalsIgnoreCase("null")) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean notEmptyOrNull(String string) {
        if (string != null && !string.equalsIgnoreCase("null") && string.trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static File createFolder(String path) {
        if (notEmptyOrNull(path)) {
            File dir = new File(path);
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    return dir;
                }
            }
            dir.mkdirs();
            return dir;
        } else {
            return null;
        }
    }

    public static boolean bNetwork(Context ctx) {
        ConnectivityManager manager =
                (ConnectivityManager) ctx.getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }

    public static void debug(Object string) {
        if (BuildConfig.DEBUG) {
            try {
                Log.i("[TP]", String.valueOf(string));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void tShort(String string) {
        SuperToast.create(TpApplication.getInstance().ctx, string, SuperToast.Duration.VERY_SHORT,
                Style.getStyle(Style.GRAY, SuperToast.Animations.POPUP)).show();
//        Toast.makeText(TpApplication.getInstance().ctx, string, Toast.LENGTH_SHORT).show();

    }

    public static void tShortxx(String string) {
        SuperToast.create(TpApplication.getInstance().ctx, string, SuperToast.Duration.VERY_SHORT,
                Style.getStyle(Style.GRAY, SuperToast.Animations.POPUP)).show();
    }

    public static void tLong(String string) {
//        Toast.makeText(TpApplication.getInstance().ctx, string, Toast.LENGTH_LONG).show();
        SuperToast.Animations animations = getAnimations();
        SuperToast.create(TpApplication.getInstance().ctx, string, SuperToast.Duration.LONG,
                Style.getStyle(Style.GRAY, SuperToast.Animations.POPUP)).show();
    }

    public static SuperToast.Animations getAnimations() {
        SuperToast.Animations animations = null;
        int type = 0;
        type = new Random().nextInt(4);
        switch (type) {
            case 0:
                animations = SuperToast.Animations.FADE;
                break;
            case 1:
                animations = SuperToast.Animations.FLYIN;
                break;
            case 2:
                animations = SuperToast.Animations.SCALE;
                break;
            case 3:
                animations = SuperToast.Animations.POPUP;
                break;
            default:
                animations = SuperToast.Animations.FADE;
                break;
        }
        return animations;
    }


    public static Drawable initIconWhite(Iconify.IconValue iconValue) {
//        return new IconDrawable(TpApplication.getInstance().ctx, iconValue).color(TpSp.getThemeColor()).sizeDp(24);
        return new IconDrawable(TpApplication.getInstance().ctx, iconValue).colorRes(R.color.white).sizeDp(24);
    }

    public static Drawable initIcon(Iconify.IconValue iconValue, int sizeDp) {
        return new IconDrawable(TpApplication.getInstance().ctx, iconValue).color(TpSp.getThemeColor()).sizeDp(sizeDp);
    }

    public static Drawable initIconFab(Iconify.IconValue iconValue) {
        return new IconDrawable(TpApplication.getInstance().ctx, iconValue).color(TpSp.getThemeColor()).sizeDp(20);
    }

    public static Drawable initIconTheme(Iconify.IconValue iconValue) {
        return new IconDrawable(TpApplication.getInstance().ctx, iconValue).color(TpSp.getThemeColor()).sizeDp(30);
    }

    public static Drawable initIconTheme(Iconify.IconValue iconValue, int sizeDp) {
        return new IconDrawable(TpApplication.getInstance().ctx, iconValue).color(TpSp.getThemeColor()).sizeDp(sizeDp);
    }

    public static Drawable initIconTheme(Iconify.IconValue iconValue, int colorRes, int sizeDp) {
        return new IconDrawable(TpApplication.getInstance().ctx, iconValue).colorRes(colorRes).sizeDp(sizeDp);
    }

    public static Drawable initIconThemeUnSel(Iconify.IconValue iconValue) {
        return new IconDrawable(TpApplication.getInstance().ctx, iconValue).colorRes(R.color.mi_text).sizeDp(30);
    }

    public static Drawable initIconThemeSel(Iconify.IconValue iconValue) {
        return new IconDrawable(TpApplication.getInstance().ctx, iconValue).colorRes(R.color.mi_text_sel).sizeDp(30);
    }


    public static int getColorNormal() {
        return TpSp.getThemeColor();
    }

    public static int getColorPressed() {
        return (int) (TpSp.getThemeColor() * 1.1f);
    }

    public static Drawable initIconRed(Iconify.IconValue iconValue) {
        return new IconDrawable(TpApplication.getInstance().ctx, iconValue).colorRes(R.color.red).sizeDp(22);
    }


    public static ImageLoader getImageLoader() {
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.loading_icon)
                .build();
        return options;
    }

    public static DisplayImageOptions getRoundedOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.loading_icon)
                .displayer(new RoundedBitmapDisplayer(TpSp.getRoundHead() ? 180 : 0))
                .build();
        return options;
    }

    public static DisplayImageOptions getNoteBookRoundedOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.loading_icon)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();
        return options;
    }


    public static void dismissShowDialog(DialogInterface dialog, boolean flag) {
//        try {
//            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//            field.setAccessible(true);
//            field.set(dialog, flag);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (flag) {
            dialog.dismiss();
        }

    }


    public static int getFontHeight(Context context, float fontSize) {
        // Convert Dp To Px
        float px = context.getResources().getDisplayMetrics().density * fontSize + 0.5f;
        // Use Paint to get font height
        Paint p = new Paint();
        p.setTextSize(px);
        Paint.FontMetrics fm = p.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    public static Bitmap getTopicPic(Context context, String text, Bitmap pic) {
        int fontHeight = getFontHeight(context, 26.0f);
        int width = fontHeight * 17;
        int height;
        int picWidth = width - 20, picHeight = 0;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(26.0f * DeviceUtil.getDeviceDensity());
        ArrayList<String> lines = new ArrayList<String>();
        String tmp = text;
        while (tmp.length() > 0) {
            String line = "";
            while (tmp.length() > 0) {
                String str = tmp.substring(0, 1);
                line += str;
                tmp = tmp.substring(1, tmp.length());
                if (line.contains("\n") || paint.measureText(line) >= width - fontHeight * 2) {
                    break;
                }
            }
            lines.add(line);
        }
        lines.add("");
        height = fontHeight * (lines.size() + 2);
        if (pic != null) {
            picHeight = (int) (((float) picWidth / (float) pic.getWidth()) * pic.getHeight());
            height += picHeight + 20;
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        paint.setColor(context.getResources().getColor(android.R.color.background_light));
        canvas.drawRect(0, 0, width, height, paint);
        int defColor = TpSp.getThemeColor();
        paint.setColor(defColor);
        float y = fontHeight * 1.5f;
        float x = fontHeight;
        for (String line : lines) {
            int lastPos = 0;
            float xOffset = 0;
            canvas.drawText(line.substring(lastPos, line.length()), x + xOffset, y, paint);
            y += fontHeight;
        }
        if (pic != null) {
            canvas.drawBitmap(pic, new Rect(0, 0, pic.getWidth(), pic.getHeight()),
                    new Rect(10, (int) (y + 10), picWidth + 10, (int) (picHeight + y + 10)), paint);
        }
        return bmp;
    }

    public static Bitmap add2Bitmap(Bitmap first, Bitmap second) {
        Bitmap result = null;
        try {
            int width = Math.max(first.getWidth(), second.getWidth());
            int height = first.getHeight() + second.getHeight();
            debug("add2Bitmap:" + width + " " + height);
            result = Bitmap.createBitmap(720, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(first, 0, 0, null);
            canvas.drawBitmap(second, 0, first.getHeight(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static void autoKeyBoardShow(final EditText editText) {
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }


    public static void hideKeyBoard(final EditText editText) {
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void listViewRandomAnimation(ListView listView, BaseAdapter mAdapter) {
        if (!TpSp.getListAnimation()) {
            listView.setAdapter(mAdapter);
            return;
        }
        int type = 0;
        type = new Random().nextInt(5);
        AnimationAdapter animationAdapter = null;
        switch (type) {
            case 0:
                animationAdapter = new AlphaInAnimationAdapter(mAdapter);
                break;
            case 1:
                animationAdapter = new SwingLeftInAnimationAdapter(mAdapter);
                break;
            case 2:
                animationAdapter = new SwingRightInAnimationAdapter(mAdapter);
                break;
            case 3:
                animationAdapter = new ScaleInAnimationAdapter(mAdapter);
                break;
            case 4:
                animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
                break;
            default:
                break;
        }
        animationAdapter.setAbsListView(listView);
        if (animationAdapter.getViewAnimator() != null) {
            animationAdapter.getViewAnimator().setInitialDelayMillis(300);
        }
        listView.setAdapter(animationAdapter);
    }

    public static void initIndicatorTab(PagerSlidingTabStrip indicator) {
        Context ctx = TpApplication.getInstance().ctx;
        indicator.setTextSize(dip2px(18));
        indicator.setTextColor(ctx.getResources().getColor(R.color.white));
        indicator.setUnderlineColor(ctx.getResources().getColor(R.color.white_sel));
    }

    public static final WindowManager wm = (WindowManager) TpApplication.getInstance().ctx.getSystemService(Context.WINDOW_SERVICE);

    public static DisplayMetrics getDeviceMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static int getDeviceWidth() {
        return getDeviceMetrics().widthPixels;
    }

    public static float getTextLength(final float textSize, String text) {
        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // Define the string.
        mTextPaint.setTextSize(textSize);
        float textWidth = mTextPaint.measureText(text);
        return textWidth;
    }

    public static int getDeviceHeight() {
        return getDeviceMetrics().heightPixels;
    }


    //双击大段文本预览
    public static void doubleTextPre(final Context ctx, View tvText, final String texts) {
        if (tvText == null || isEmptyOrNull(texts)) {
            return;
        }
        DoubleClickImp.registerDoubleClickListener(tvText,
                new DoubleClickImp.OnDoubleClickListener() {
                    @Override
                    public void OnSingleClick(View v) {
                    }

                    @Override
                    public void OnDoubleClick(View v) {
                        if (notEmptyOrNull(texts)) {
                            Intent newIntent = new Intent(ctx, ShowInfoActivity.class);
                            newIntent.putExtra("show", texts);
                            ctx.startActivity(newIntent);
                        }
                    }
                });
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = TpApplication.getInstance().ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = TpApplication.getInstance().ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static void initTheme(Activity ctx, ActionBar actionBar) {
        if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.WHITE.getValue()) {
            ctx.setTheme(R.style.Theme_light);
            actionBar.setBackgroundDrawable(new ColorDrawable(TpSp.getThemeColor()));
        } else if (TpSp.getThemeType().intValue() == EnumData.ThemeTypeEnum.BLACK.getValue()) {
            ctx.setTheme(R.style.Theme_dark);
//            actionBar.setBackgroundDrawable(new ColorDrawable(TpConstants.DEFAULT_COLOR));
        }
    }


    public static String saveImage(Bitmap bitmap) {
        String filePath = null;
        File file = null;
        try {
            file = new File((MyPath.getPathPicture() + File.separator + TimeUtils.getFileSaveTime()));
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            filePath = file.getAbsolutePath();
        }
        return filePath;
    }


    public static int getPictureDegree(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        }
        return degree;
    }


    public static String compressLightAndSaveImage(String fileImage) {
        String filePath = null;
        File file = null;
        try {
            Bitmap bitmap = decodeSampledBitmapFromFile(fileImage, 640,
                    320);
            bitmap = rotaingImageView(getPictureDegree(fileImage), bitmap);
            bitmap = compressImage(bitmap, 50);
            file = new File((MyPath.getPathPicture() + File.separator + TimeUtils.getFileSaveTime()));
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.exists()) {
            filePath = file.getAbsolutePath();
        }

        return filePath;
    }

    public static String compressAndSaveImage(String fileImage) {
        boolean bSrc = false;
        if (EnumData.PicUploadEnum.ATUO.getValue() == TpSp.getPicUpload()) {
            if (NetworkUtil.isWifiOr4g(TpApplication.getInstance().ctx)) {
                bSrc = true;
            }
        } else if (EnumData.PicUploadEnum.SOURCE.getValue() == TpSp.getPicUpload()) {
            bSrc = true;
        }

        if (bSrc) {
            return fileImage;
        }

        String filePath = null;
        File file = null;
        try {
            Bitmap bitmap = decodeSampledBitmapFromFile(fileImage, 720,
                    1280);
            bitmap = rotaingImageView(getPictureDegree(fileImage), bitmap);
            file = new File((MyPath.getPathPicture() + File.separator + TimeUtils.getFileSaveTime()));
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.exists()) {
            filePath = file.getAbsolutePath();
        }

        return filePath;
    }

    public static String compressAndSaveImageFromBitmap(Bitmap bitmap) {
        String filePath = null;
        File file = null;
        try {
            file = new File((MyPath.getPathPicture() + File.separator + TimeUtils.getFileSaveTime()));
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.exists()) {
            filePath = file.getAbsolutePath();
        }
        return filePath;
    }

    public static void deleteFile(File oldPath) {
        if (oldPath.isDirectory()) {
            File[] files = oldPath.listFiles();
            for (File file : files) {
                deleteFile(file);
            }
        }
        oldPath.delete();
    }

    public static String compressAndSaveHeadImage(String fileImage) {
        String filePath = null;
        File file = null;
        try {
            Bitmap bitmap = decodeSampledBitmapFromFile(fileImage, 240,
                    320);
            bitmap = rotaingImageView(getPictureDegree(fileImage), bitmap);
            file = new File((MyPath.getPathPicture() + File.separator + TimeUtils.getFileSaveTime()));
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.exists()) {
            filePath = file.getAbsolutePath();
        }

        return filePath;
    }


    public static InputStream compressImage(String fileImage) {
        InputStream is = null;
        try {
            Bitmap bitmap = decodeSampledBitmapFromFile(fileImage, 720,
                    1280);
            bitmap = rotaingImageView(getPictureDegree(fileImage), bitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
            is = new ByteArrayInputStream(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    public static Bitmap compressImage(Bitmap image, int kb) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > kb) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    public static Bitmap rotaingImageView(int angle, Bitmap subsampledBitmap) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        Matrix m = new Matrix();
        if (angle != 0) {
            m.postRotate((float) angle);
        }
        Bitmap finalBitmap = Bitmap.createBitmap(subsampledBitmap, 0, 0, subsampledBitmap.getWidth(), subsampledBitmap.getHeight(), m, true);
        if (finalBitmap != subsampledBitmap) {
            subsampledBitmap.recycle();
        }
        return finalBitmap;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        BitmapFactory.decodeFile(filename, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        try {
            if (reqWidth == -1) {
                return BitmapFactory.decodeFile(filename, null);
            } else {
                return BitmapFactory.decodeFile(filename, options);
            }

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    public static Bitmap getBitmapByPath(String filename, int width, int height) {
        Bitmap bitmap = null;
        File file = new File(filename);
        FileInputStream fs = null;

        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (!file.exists() || !file.isFile()) {
            return bitmap;
        }

        try {

            Bitmap bmp = null;
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;

            bmp = BitmapFactory.decodeFile(file.getPath(), opts);

            // 计算图片缩放比例
            final int minSideLength = Math.min(width, height);
            opts.inSampleSize = computeSampleSize(opts, minSideLength, width
                    * height);
            opts.inJustDecodeBounds = false;
            opts.inInputShareable = true;
            opts.inPurgeable = true;

            opts.inDither = false;
            opts.inPurgeable = true;
            opts.inTempStorage = new byte[16 * 1024];

            // bitmap = BitmapFactory.decodeFile(file.getPath(), opts);

            if (fs != null) {
                bmp = BitmapFactory
                        .decodeFileDescriptor(fs.getFD(), null, opts);
                bitmap = Bitmap.createBitmap(bmp);
            }
            // ---将图片占有的内存资源释放
            bmp = null;
            //bmp.recycle();//这里的回收，在程序里有点问题
            System.gc();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bitmap = null;
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }

    public static Bitmap getBitmapByPath(String filename) {
        Bitmap bitmap = null;
        File file = new File(filename);
        FileInputStream fs = null;

        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (!file.exists() || !file.isFile()) {
            return bitmap;
        }

        try {
            Bitmap bmp = null;
            if (fs != null) {
                bmp = BitmapFactory
                        .decodeFileDescriptor(fs.getFD(), null, null);
                bitmap = Bitmap.createBitmap(bmp);
            }
            // ---将图片占有的内存资源释放
            bmp = null;
            //bmp.recycle();//这里的回收，在程序里有点问题
            System.gc();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bitmap = null;
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    public static String getPathFromUri(Context context, Uri uri) {
        String path = "";
        if (uri == null || "content://media/external/file/-1".equals(uri.toString())) {
            XUtil.tShort("文件选取失败~");
            return null;
        }
        String[] projection = {"_data"};
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        path = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                    }
                    cursor.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        return path;
    }


    //日记本列表
    public static void getNoteBook() {
        ServiceFactory.getService(NotebooksService.class).getNotebooks(new Callback<List<NoteBookData>>() {
            @Override
            public void success(List<NoteBookData> noteBookDatas, Response response) {
                if (!StrUtil.listNotNull(noteBookDatas)) {
                    return;
                }
                List<NoteBookData> notExpired = new ArrayList<NoteBookData>();
                for (NoteBookData noteBookData : noteBookDatas) {
                    if (noteBookData.getIsExpired().equals("false")) {
                        //逆序 后建的日记本在前面
                        notExpired.add(noteBookData);
                        TpApplication.getInstance().setNoteBookDatas(notExpired);
                        TpSp.setNoteBookData(notExpired.toString());
                    }
                }
                if (!StrUtil.listNotNull(notExpired)) {
                    XUtil.tShort("日记本已全部过期!");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                XUtil.onFailure(error);
            }
        });

    }


}
