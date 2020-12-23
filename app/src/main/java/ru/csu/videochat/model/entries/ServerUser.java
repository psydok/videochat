package ru.csu.videochat.model.entries;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerUser {

    @JsonProperty(value = "login")
    private String login;
    @JsonProperty(value = "image")
    private String image;
    @JsonProperty(value = "link_id")
    private String link_id;

    public ServerUser(String login, String image, String link_id) {
        this.login = login;
        this.image = image;
        this.link_id = link_id;
    }

    public ServerUser() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }
}
