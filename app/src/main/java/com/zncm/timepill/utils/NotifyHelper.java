package com.zncm.timepill.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.zncm.timepill.R;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.data.base.base.PushData;
import com.zncm.timepill.data.base.chat.MsgData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.modules.ui.HomeTabActivity;
import com.zncm.timepill.modules.ui.NoteDetailsAc;
import com.zncm.timepill.modules.ui.PwdActivity;
import com.zncm.timepill.modules.ui.TalkAc;
import com.zncm.timepill.modules.ui.UserHomeAc;
import com.zncm.timepill.utils.sp.TpSp;

@SuppressWarnings("deprecation")
public class NotifyHelper {

    private static NotificationManager mNotificationManager;

    public static void showTalkMessageNotification(Context context, MsgData msgData) {
        if (!TpSp.getTip()) {
            return;
        }
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Intent select = new Intent();
        select.setClass(context, TalkAc.class);
        select.putExtra("user", new MiniUserData(msgData.getFriend_id(), msgData.getName(), msgData.getIconUrl()));

        String pwd = TpSp.getPwdInfo();
        if (StrUtil.notEmptyOrNull(pwd)) {
            select.setClass(context, PwdActivity.class);
            select.putExtra(TpConstants.KEY_PARAM_TYPE, EnumData.PwdEnum.CHECK.getValue());
        }
        select.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        noti(msgData.getName(), msgData.getContent(), msgData.getContent(), select, true, msgData.getFriend_id(),TpSp.getTipRing());
    }


    public static void showMessageNotification(Context context, String msgContent, PushData pushData) {

        //helper.showMessageNotification(context, message, pushData.getMsg_content(), pushData.getLink_id(), pushData.getType(), pushData.getLink_id());
        int type = pushData.getType();
        int _id = pushData.getLink_id();
        int notificationId = pushData.getLink_id();
        try {
            notificationId = Integer.parseInt(pushData.getLink_user_id());
        } catch (Exception e) {

        }

//        boolean isSelf = false;
//        try {
//            UserData userData = TpApplication.getInstance().getUserData();
//            if (userData == null) {
//                userData = JSON.parseObject(TpSp.getUserInfo(), UserData.class);
//                TpApplication.getInstance().setUserData(userData);
//            }
//            if (userData == null) {
//                return;
//            }
//            if (userData.getId() == pushData.getUser_id()) {
//                isSelf = true;
//            }
//        } catch (Exception e) {
//
//        }


        String title = "胶囊提醒";
//        String msgContent, int notificationId, int type, int _id

        if (!TpSp.getTip()) {
            return;
        }
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Intent select = new Intent();
        if (type == 1) {
            //回复
            select.setClass(context, NoteDetailsAc.class);
            select.putExtra(TpConstants.KEY_ID, _id);
//            select.putExtra("isSelf", isSelf);
        } else if (type == 2) {
            //关注
            select.setClass(context, UserHomeAc.class);
            select.putExtra("isSelf", false);
            select.putExtra("user_info", new MiniUserData(_id, null, null));
        } else if (type == 3) {
            select.setClass(context, HomeTabActivity.class);
        }

        String pwd = TpSp.getPwdInfo();
        if (StrUtil.notEmptyOrNull(pwd)) {
            select.setClass(context, PwdActivity.class);
            select.putExtra(TpConstants.KEY_PARAM_TYPE, EnumData.PwdEnum.CHECK.getValue());
            select.putExtra(TpConstants.KEY_ID, _id);
        }
        select.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        noti(title, msgContent, msgContent, select, true, _id, TpSp.getTipRing());
    }


    public static void noti(String title, String content, String ticker, Intent intent, boolean autoCancel, int notifyId, boolean bRing) {
        Context ctx = TpApplication.getInstance().ctx;
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
        mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(content)
                .setContentIntent(getDefalutIntent(ctx, intent, PendingIntent.FLAG_UPDATE_CURRENT)) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
                .setTicker(ticker) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(autoCancel)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)

//
                .setSmallIcon(R.drawable.n_icon);//设置通知小ICON

//        Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
        if (bRing) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL);
        }
        mNotificationManager.notify(notifyId, mBuilder.build());
    }


    public static PendingIntent getDefalutIntent(Context ctx, Intent intent, int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 1, intent, flags);
        return pendingIntent;
    }

    public static void showMessage(Context context, NotificationManager nm, String title,
                                   String msgContent, int notificationId, int _id) {
        if (!TpSp.getTip()) {
            return;
        }
        Intent select = new Intent();
        select.setClass(context, HomeTabActivity.class);
        String pwd = TpSp.getPwdInfo();
        if (StrUtil.notEmptyOrNull(pwd)) {
            select.setClass(context, PwdActivity.class);
            select.putExtra(TpConstants.KEY_PARAM_TYPE, EnumData.PwdEnum.CHECK.getValue());
            select.putExtra(TpConstants.KEY_ID, _id);
        }
        select.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        noti(title, msgContent, msgContent, select, true, _id, TpSp.getTipRing());
    }


    public static void clearNotificationById(int id) {
        if (null != mNotificationManager) {
            mNotificationManager.cancel(id);
        }
    }

}
