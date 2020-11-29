package ru.csu.videochat.activities.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.csu.videochat.R;

public class VideoChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat);
//        SearchChatModel model = SearchChatModel.getInstance(this);
//        VideoChatPresent present = new VideoChatPresent(this, model);
//
//        present.connectVideo();
    }


}