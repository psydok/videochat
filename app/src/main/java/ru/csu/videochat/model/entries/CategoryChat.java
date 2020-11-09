package ru.csu.videochat.model.entries;

import java.util.List;

public class CategoryChat {
    public String name;
    public List<Chats> chats;

    public CategoryChat() {
    }

    public CategoryChat(String name, List<Chats> chats) {
        this.name = name;
        this.chats = chats;
    }

    public String getName() {
        return name;
    }

    public List<Chats> getChats() {
        return chats;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChats(List<Chats> chats) {
        this.chats = chats;
    }
}
