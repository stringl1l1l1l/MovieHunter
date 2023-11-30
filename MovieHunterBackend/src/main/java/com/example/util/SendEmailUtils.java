package com.example.util;

import org.apache.commons.mail.HtmlEmail;

/**
 * 发送邮箱验证码工具类
 */
public class SendEmailUtils {

    private static final String HOST_NAME = "smtp.163.com";
    private static final String SMTP_EMAIL = "liwener200207@163.com";
    private static final String SMTP_PASSWORD_APP = "FZNDJMWHLYJRDWNY";
    private static final String SENDER_NAME = "MovieHunter";
    public static final int LIFETIME_MIN = 5;
    /**
     * 发送验证码
     * @param email  接收邮箱
     * @return  void
     */
    public static String sendAuthCodeEmail(String email) throws Exception{

        HtmlEmail mail = new HtmlEmail();

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
        mail.setSubject("MovieHunter验证码");
        /*设置邮件的内容*/
        Integer code = ValidateCodeUtils.generateNumValidateCode(4);
        mail.setMsg("【MovieHunter】验证码 " + code + "，" + LIFETIME_MIN + "分钟内有效");
        //发送
        mail.send();

        return code.toString();
    }

    public static void main(String[] args) throws Exception {
        Integer code = ValidateCodeUtils.generateNumValidateCode(4);
        System.out.println(code);
        sendAuthCodeEmail("1097410948@qq.com");
    }
}


