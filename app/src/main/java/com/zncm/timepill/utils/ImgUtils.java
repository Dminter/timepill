package com.zncm.timepill.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by jmx on 1/27 0027.
 */
public class ImgUtils {
    private Bitmap addBitmap(Bitmap first, Bitmap second) {
        int width = Math.max(first.getWidth(), second.getWidth());
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, first.getHeight(), 0, null);
        return result;
    }
}
