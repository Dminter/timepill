package com.zncm.timepill.modules.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.zncm.timepill.data.EnumData;
import com.zncm.timepill.data.base.base.CommentData;
import com.zncm.timepill.data.base.base.MiniUserData;
import com.zncm.timepill.data.base.base.PushData;
import com.zncm.timepill.data.base.base.PushTalkData;
import com.zncm.timepill.data.base.chat.MsgData;
import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.global.TpConstants;
import com.zncm.timepill.utils.DbUtils;
import com.zncm.timepill.utils.L;
import com.zncm.timepill.utils.NotifyHelper;
import com.zncm.timepill.utils.RefreshEvent;
import com.zncm.timepill.utils.StrUtil;
import com.zncm.timepill.utils.sp.TpSp;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;


public class TpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            processCustomMessage(context, bundle);
        }
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String content_type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
        int pushType = 0;
        if (StrUtil.notEmptyOrNull(content_type)) {
            try {
                pushType = Integer.parseInt(content_type);
            } catch (Exception e) {
            }
        }
        NotifyHelper helper = new NotifyHelper();
        //value:10 talk
        //CONTENT_TYPE, value:1 tips
        ArrayList<Integer> maskUser = TpApplication.getInstance().getMaskUser();

        if (pushType == EnumData.PushType.MESSAGE.getValue() && TpSp.getIsTalk()) {
            PushTalkData talkData = JSON.parseObject(extras, PushTalkData.class);
            if (talkData != null) {
                MsgData msgData = JSON.parseObject(talkData.getContent(), MsgData.class);

                if (maskUser != null) {
                    if (maskUser.contains(msgData.getFriend_id())) {
                        return;
                    }
                }
                if (msgData != null) {
                    msgData.setIs_send(EnumData.MsgOwner.RECEIVER.getValue());
                    msgData.setCreated(String.valueOf(System.currentTimeMillis()));
                    DbUtils.initTalkList(new MiniUserData(msgData.getFriend_id(), msgData.getName(), msgData.getIconUrl()));
                    if (TpApplication.getInstance().isTalkBackground()) {
                        msgData.setRead(EnumData.MsgRead.UN_READ.getValue());
                        helper.showTalkMessageNotification(context, msgData);
                    } else {
                        msgData.setRead(EnumData.MsgRead.READ.getValue());
                        // 聊天消息发送到聊天界面
                        Intent msgIntent = new Intent(TpConstants.MESSAGE_RECEIVED_ACTION);
                        msgIntent.putExtra(TpConstants.KEY_MESSAGE, msgData.toString());
                        context.sendBroadcast(msgIntent);
                    }
                    DbUtils.saveTalkData(msgData);
                    TpApplication.msgNew = true;
                    EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NEW_MSG.getValue()));
                    EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NEW_MSG_TALK.getValue()));
                }
            }
        } else if (pushType == EnumData.PushType.TIPS.getValue()) {
            TpSp.setHaveNotify(true);
            PushData pushData = JSON.parseObject(extras, PushData.class);
            if (pushData != null) {
                try {
                    L.i("push-msg:" + pushData);
                    String content = pushData.getContent();
                    if (StrUtil.notEmptyOrNull(content)) {
                        CommentData commentData = JSON.parseObject(content, CommentData.class);
                        if (commentData != null) {
                            MiniUserData miniUserData = JSON.parseObject(commentData.getAuthor(), MiniUserData.class);
                            if (miniUserData != null) {
                                if (maskUser != null) {
                                    if (maskUser.contains(miniUserData.getId())) {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    helper.showMessageNotification(context, message, pushData);
                    TpApplication.msgNew = true;
                    EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.NEW_MSG.getValue()));
                } catch (Exception e) {
                }
            }
        }
    }
}
