package ru.csu.videochat.presents;

import ru.csu.videochat.activities.chat.VideoChatActivity;
import ru.csu.videochat.model.SearchChatModel;

public class VideoChatPresent {
    private VideoChatActivity view;
    private SearchChatModel model;

    public VideoChatPresent(VideoChatActivity view, SearchChatModel model) {
        this.view = view;
        this.model = model;
    }

    public void detachView() {
        view = null;
    }
}
