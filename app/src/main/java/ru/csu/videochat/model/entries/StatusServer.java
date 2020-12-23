package ru.csu.videochat.model.entries;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusServer {
    @JsonProperty(value = "status")
    private int status;

    public StatusServer(int status) {
        this.status = status;
    }

    public StatusServer() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
