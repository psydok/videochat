package ru.csu.videochat.model.entries;

public class Chat {
    private String uid1Token, uid2Token, category;

    public Chat() {
    }

    public Chat(String uid1Token, String uid2Token, String categoryChat) {
        this.uid1Token = uid1Token;
        this.uid2Token = uid2Token;
        this.category = categoryChat;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
