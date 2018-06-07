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

    private String userName;
    private String userAvater;

    private String content;
    private Drawable phoot ;
    private Date time;

    private boolean isSend;
    private boolean sendSuccess;

    public Message(String userName, String userAvater, String content, boolean isSend) {
        this.userName = userName;
        this.userAvater = userAvater;
        this.content = content;
        this.isSend = isSend;
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

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getUserAvater(){
        return this.userAvater;
    }

    public void setUserAvater(String userAvater){
        this.userAvater = userAvater;
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
