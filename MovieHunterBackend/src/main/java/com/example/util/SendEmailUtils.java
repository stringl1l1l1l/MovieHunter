package com.example.util;

import org.apache.commons.mail.HtmlEmail;

/**
 * 发送邮箱验证码工具类
 */
public class SendEmailUtils {

    private static final String HOST_NAME = "smtp.qq.com";
    private static final String SMTP_EMAIL = "2682855462@qq.com";
    private static final String SMTP_PASSWORD_APP = "kundyovyhazkdjbi";
    private static final String SENDER_NAME = "MovieHunter";
    /**
     * 发送验证码
     * @param email  接收邮箱
     * @param code   验证码
     * @return  void
     */
    public static void sendAuthCodeEmail(String email, String code) {
        try {
            HtmlEmail mail = new HtmlEmail();
            /*发送邮件的服务器 126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com*/
            mail.setHostName(HOST_NAME);
            /*不设置发送的消息有可能是乱码*/
            mail.setCharset("UTF-8");
            /*IMAP/SMTP服务的密码 username为你开启发送验证码功能的邮箱号 password为你在qq邮箱获取到的一串字符串*/
            mail.setAuthentication(SMTP_EMAIL, SMTP_PASSWORD_APP);
            /*发送邮件的邮箱和发件人*/
            mail.setFrom(SMTP_EMAIL, SENDER_NAME);
            /*使用安全链接*/
            mail.setSSLOnConnect(true);
            /*接收的邮箱*/
            mail.addTo(email);
            /*设置邮件的主题*/
            mail.setSubject("MovieHunter登录验证码");
            /*设置邮件的内容*/
            mail.setMsg("尊敬的用户你好! MovieHunter登录验证码为: " + code + " (有效期为一分钟)");
            //发送
            mail.send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Integer code = ValidateCodeUtils.generateNumValidateCode(4);
        System.out.println(code);
        sendAuthCodeEmail("1097410948@qq.com", code.toString());
    }
}


