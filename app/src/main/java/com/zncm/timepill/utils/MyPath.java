package com.zncm.timepill.utils;

import android.os.Environment;

import com.zncm.timepill.global.TpConstants;

import java.io.File;

public class MyPath {
    public static final String PATH_CACHE = "cache";
    public static final String PATH_PICTURE = "picture";

    public static String getFolder(String folderName) {
        if (folderName == null) {
            return null;
        }
        File dir = XUtil.createFolder(folderName);
        if (dir != null) {
            return dir.getAbsolutePath();
        } else {
            return null;
        }
    }


    private static String getPathFolder(String path) {
        File rootPath = Environment.getExternalStoragePublicDirectory(TpConstants.PATH_ROOT);
        return getFolder(rootPath + File.separator
                + path + File.separator);
    }

    public static String getPathCache() {
        return getPathFolder(PATH_CACHE);
    }

    public static String getPathPicture() {
        return getPathFolder(PATH_PICTURE);
    }

}
