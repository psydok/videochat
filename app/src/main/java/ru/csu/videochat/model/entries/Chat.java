package ru.csu.videochat.model.entries;

public class Chat {
    private String uid1Token, uid2Token;

    public Chat() {
    }

    public Chat(String uid1_token, String uid2_token) {
        this.uid1Token = uid1_token;
        this.uid2Token = uid2_token;
    }

    public String getUid1Token() {
        return uid1Token;
    }

    public void setUid1Token(String uid1Token) {
        this.uid1Token = uid1Token;
    }

    public String getUid2Token() {
        return uid2Token;
    }

    public void setUid2Token(String uid2Token) {
        this.uid2Token = uid2Token;
    }
}
