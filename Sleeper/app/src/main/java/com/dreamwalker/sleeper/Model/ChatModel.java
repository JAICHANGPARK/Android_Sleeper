package com.dreamwalker.sleeper.Model;

/**
 * Created by 2E313JCP on 2017-10-14.
 */

public class ChatModel {

    public String chatMessage;
    public String chatTime;
    public boolean isSend;

    public ChatModel(String chatMessage, boolean isSend) {
        this.chatMessage = chatMessage;
        this.isSend = isSend;
    }

    public ChatModel(String chatMessage, String chatTime, boolean isSend) {
        this.chatMessage = chatMessage;
        this.chatTime = chatTime;
        this.isSend = isSend;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }
}
