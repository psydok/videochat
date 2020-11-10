package ru.csu.videochat.presents;

import androidx.annotation.NonNull;

import ru.csu.videochat.activities.chat.SearchChatActivity;
import ru.csu.videochat.interfaces.IUsersListener;
import ru.csu.videochat.model.SearchChatModel;

public class SearchChatPresent {
    private final SearchChatModel model;
    private SearchChatActivity view;

    public SearchChatPresent(@NonNull SearchChatModel model, @NonNull SearchChatActivity view) {
        this.model = model;
        this.view = view;
    }

    public void detachView() {
        exitFromChat();
        view = null;
    }

    public void getChats(String category, IUsersListener usersListener) {
        model.loadChat(category, user -> {
            usersListener.initiativeVideoMeeting(user);
        });
    }

    public void exitFromChat() {
        model.deleteChat();
    }


    public void connectVideo() {
        model.connectVideo();
    }
}
