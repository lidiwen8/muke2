package com.muke.pojo;

/**
 * Created by ASUS on 2018/8/9.
 */
public class EmailAccout {
    // 邮箱用户
    private String username="cn_edu_njust@163.com";

    // 邮箱密码
    private String password="lty123456789";

    // 邮箱服务器
    private String place="smtp.163.com";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
