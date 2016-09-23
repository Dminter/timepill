package com.zncm.timepill.global;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zncm.timepill.data.base.base.UserData;
import com.zncm.timepill.data.base.note.NoteBookData;
import com.zncm.timepill.data.base.note.NoteTopicData;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.MyPath;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TpApplication extends Application {
    public Context ctx;
    private String storagePath = null;
    public static TpApplication instance;
    private NoteTopicData topic;
    private UserData userData;
    private List<NoteBookData> noteBookDatas;
    private static Map<String, String> historyEtMap = new HashMap<String, String>();
    private ArrayList<Integer> maskUser;
    public boolean isBackground = true;
    public boolean isTalkBackground = true;
    public static DisplayImageOptions displayOption;
    public static boolean msgNew = false;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = getApplicationContext();
        instance = this;
        initImageLoader();
        DbUtils.initMaskUser();
    }

    public void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ctx)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(20)
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                .memoryCacheSize(20 * 1024 * 1024)
                .memoryCacheSizePercentage(50) // default
                .diskCache(new UnlimitedDiskCache(new File(MyPath.getPathCache()))) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(1000)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(ctx)) // default
                .defaultDisplayImageOptions(getDisplayImageOptions()) // default
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions getDisplayImageOptions() {
        if (displayOption == null) {
            displayOption = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.NONE)
                    .build();
        }
        return displayOption;
    }

    public static TpApplication getInstance() {
        return instance;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public SharedPreferences getPreferences(String fileString) {
        return this.getPreferences(fileString, Context.MODE_PRIVATE);
    }

    public SharedPreferences getPreferences(String fileString, int mod) {
        return getSharedPreferences(fileString, mod);
    }

    public List<NoteBookData> getNoteBookDatas() {
        return noteBookDatas;
    }

    public void setNoteBookDatas(List<NoteBookData> noteBookDatas) {
        this.noteBookDatas = noteBookDatas;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }


    public static Map<String, String> getHistoryEtMap() {
        if (!historyEtMap.isEmpty())
            return historyEtMap;
        return null;
    }

    public static void setHistoryEtMap(String key, String value) {
        historyEtMap.put(key, value);
    }

    public NoteTopicData getTopic() {
        return topic;
    }

    public void setTopic(NoteTopicData topic) {
        this.topic = topic;
    }

    public ArrayList<Integer> getMaskUser() {
        return maskUser;
    }

    public void setMaskUser(ArrayList<Integer> maskUser) {
        this.maskUser = maskUser;
    }


    public boolean isBackground() {
        return isBackground;
    }

    public void setBackground(boolean isBackground) {
        this.isBackground = isBackground;
    }


    public boolean isTalkBackground() {
        return isTalkBackground;
    }

    public void setTalkBackground(boolean isTalkBackground) {
        this.isTalkBackground = isTalkBackground;
    }
}
