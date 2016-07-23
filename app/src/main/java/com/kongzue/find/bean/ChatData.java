package com.kongzue.find.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by chao on 2016/3/15.
 */
public class ChatData extends BmobObject {
    private Integer messageType;
    private String message;
    private User sender;
    private String chatId;
    private String favWord;

    public String getFavWord() {
        return favWord;
    }

    public void setFavWord(String favWord) {
        this.favWord = favWord;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
