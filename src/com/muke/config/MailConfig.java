package com.muke.config;

public class MailConfig {
    private static final String mail_smtp_host = "smtp.qq.com";
    private static final String mail_password = "ghxxwuyinxxqcajf";
    private static final String mail_user = "1632029393@qq.com";
    private static final String smtp_port = "465";

    public static final String getMail_smtp_host() {
        return mail_smtp_host;
    }

    public static final String getMail_password() {
        return mail_password;
    }

    public static final String getMail_user() {
        return mail_user;
    }

    public static final String getSmtp_port() {
        return smtp_port;
    }


}
