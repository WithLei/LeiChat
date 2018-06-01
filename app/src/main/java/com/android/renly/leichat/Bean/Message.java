package com.android.renly.leichat.Bean;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class Message {
    //发送类型
    private static final int MSG_SEND_TEXT = 1;
    private static final int MSG_SEND_PHOTO = 2;
    private static final int MSG_SEND_FACE = 3;
    private static final int MSG_SEND_VOICE = 4;

    //发送状态
    private static final int MSG_STATE_SUCCESS = 1;
    private static final int MSG_STATE_FAIL = 2;
    private static final int MSG_STATE_LOADING = 3;

    private Long id;
    private int type;//1-text 2-photo 3-face 4-voice
    private int state;//1-success 2-fail 3-loading

    private String fromUserName;
    private String toUserName;
    private String fromUserAvater;
    private String toUserAvater;

    private String content;
    private Drawable phoot ;
    private Date time;

    private boolean isSend;
    private boolean sendSuccess;

    public Message(int type, int state, String fromUserName, String toUserName,
                   String fromUserAvater, String toUserAvater, String content,
                   Date time, boolean isSend, boolean sendSuccess) {
        this.type = type;
        this.state = state;
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.fromUserAvater = fromUserAvater;
        this.toUserAvater = toUserAvater;
        this.content = content;
        this.time = time;
        this.isSend = isSend;
        this.sendSuccess = sendSuccess;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserAvater() {
        return fromUserAvater;
    }

    public void setFromUserAvater(String fromUserAvater) {
        this.fromUserAvater = fromUserAvater;
    }

    public String getToUserAvater() {
        return toUserAvater;
    }

    public void setToUserAvater(String toUserAvater) {
        this.toUserAvater = toUserAvater;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public boolean isSendSuccess() {
        return sendSuccess;
    }

    public void setSendSuccess(boolean sendSuccess) {
        this.sendSuccess = sendSuccess;
    }
}
