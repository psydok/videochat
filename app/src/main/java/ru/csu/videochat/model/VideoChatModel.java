package ru.csu.videochat.model;

import android.content.Context;

public class VideoChatModel {
    private Context context;

    public VideoChatModel(Context context) {
        this.context = context;
    }

    public static VideoChatModel getInstance(Context context) {
        return new VideoChatModel(context);
    }

    public void sendInvitationResponse(String receiverToken) {
    }



}
