package ru.csu.videochat.model.entries;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageAuth {
    @JsonProperty(value = "status")
    private int status;
    @JsonProperty(value = "message")
    private String message;
    @JsonProperty(value = "token")
    private String token;
    @JsonProperty(value = "user")
    private ServerUser[] user;

    public MessageAuth(int status, String message, String token, ServerUser[] user) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.user = user;
    }

    public MessageAuth() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ServerUser[] getUser() {
        return user;
    }

    public void setUser(ServerUser[] user) {
        this.user = user;
    }
}
