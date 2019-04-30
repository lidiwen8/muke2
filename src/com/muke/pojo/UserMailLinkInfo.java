package com.muke.pojo;

public class UserMailLinkInfo {
    private String username;
    private String active_key;
    private String active_time;
    private String new_pass;
    public String getActive_key() {
        return active_key;
    }

    public void setActive_key(String active_key) {
        this.active_key = active_key;
    }

    public String getActive_time() {
        return active_time;
    }

    public void setActive_time(String active_time) {
        this.active_time = active_time;
    }

    public String getNew_pass() {
        return new_pass;
    }

    public void setNew_pass(String new_pass) {
        this.new_pass = new_pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
