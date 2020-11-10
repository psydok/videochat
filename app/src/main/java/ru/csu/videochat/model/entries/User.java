package ru.csu.videochat.model.entries;

public class User {
    private String uidToken;

    public User() {
    }

    public User(String uidToken) {
        this.uidToken = uidToken;
    }

    public String getUidToken() {
        return uidToken;
    }

    public void setUidToken(String uidToken) {
        this.uidToken = uidToken;
    }
}
