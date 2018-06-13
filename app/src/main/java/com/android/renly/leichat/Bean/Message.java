package com.android.renly.leichat.Bean;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class Message {
    //发送类型
    public static final int MSG_SEND_TEXT = 1;
    public static final int MSG_SEND_PHOTO = 2;
    public static final int MSG_SEND_FACE = 3;
    public static final int MSG_SEND_VOICE = 4;

    //发送状态
    public static final int MSG_STATE_SUCCESS = 1;
    public static final int MSG_STATE_FAIL = 2;
    public static final int MSG_STATE_LOADING = 3;

    private Long id;
    private int type;//1-text 2-photo 3-face 4-voice
    private int state;//1-success 2-fail 3-loading

    private String userName;
    private String userAvater;

    private String content;
    private Date time;

    private boolean isSend;
    private boolean sendSuccess;

    private String fromUserId;
    private String toUserId;
    private String form;//判断是否空包 空包为type

    public Message(){
        super();
    }

    public Message(String userName, String userAvater, String content, boolean isSend, int state) {
        this.userName = userName;
        this.userAvater = userAvater;
        this.content = content;
        this.isSend = isSend;
        this.state = state;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
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

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}
