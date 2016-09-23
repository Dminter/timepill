package com.zncm.timepill.utils;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by xm0ff on 9/1 0001.
 */
public class DialogUtils {
    static MaterialDialog exitDialog;

    public static void exitDlg(final Activity ctx) {
        if (exitDialog != null) {
            if (exitDialog.isShowing()) {
                return;
            } else {
                exitDialog.show();
            }
        }
        if (!NetworkUtil.detect(ctx)) {
            exitDialog = new MaterialDialog.Builder(ctx)
                    .title("提示")
                    .content("你的手机没有可用网络，请检查设置")
                    .negativeText("退出")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            super.onNegative(dialog);
                            ctx.finish();
                        }
                    })
                    .show();
        }
    }

}
