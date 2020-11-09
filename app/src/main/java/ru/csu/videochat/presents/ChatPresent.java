package ru.csu.videochat.presents;

import androidx.annotation.NonNull;

import ru.csu.videochat.activities.chat.ChatActivity;
import ru.csu.videochat.interfaces.IUsersListener;
import ru.csu.videochat.model.ModelDB;

public class ChatPresent {
    private final ModelDB model;
    private ChatActivity view;

    public ChatPresent(@NonNull ModelDB model, @NonNull ChatActivity view) {
        this.model = model;
        this.view = view;
    }

    void detachView() {
        view = null;
    }

    public void getChats(String category, IUsersListener usersListener) {
        model.loadChat(category, user -> {
//            view.setConnectVideoUser(chats);
            usersListener.initiativeVideoMeeting(user);
        });
    }
}
