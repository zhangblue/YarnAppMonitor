package com.yarn.util;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * @author zhaogj
 * @version 1.1 20161104
 */
public class EmailUtil {
    private String strMailSmtpHost = "mail.qq.com";//发件人服务器
    private String strUserName = "xxxx@qq.com";//发件人邮箱地址
    private String strPassword = "123456";//发件人邮箱密码

    /**
     * 发送简单邮件
     *
     * @param astrRecipient 收件人列表
     * @param strSubject    标题
     * @param strText       正文
     */
    public boolean sendSimpleTextEmail(String[] astrRecipient, String[] aCCRecipient, String strSubject, String strText) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.host", strMailSmtpHost);
        // props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(strUserName, strPassword);
            }
        });
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(strUserName));
            /***
             * 收件人
             */
            for (String strRecipient : astrRecipient) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strRecipient));
            }

            /***
             * 抄送人
             */
            for (String strCCRecipient : aCCRecipient) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(strCCRecipient));
            }


            msg.setSubject(strSubject);
            msg.setSentDate(new Date());
            msg.setText(strText);
            Transport.send(msg);
        } catch (Exception e) {
            Log4jFactory.logger_system.error(e);
            return false;
        }
        return true;
    }

    public boolean sendSimpleTextEmail(Iterable<String> itRecipient, Iterable<String> itCCRecipient, String strSubject, String strText) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.host", strMailSmtpHost);
        // props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(strUserName, strPassword);
            }
        });
        // session.setDebug(true);
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(strUserName));
            for (String strRecipient : itRecipient) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strRecipient));
            }
            for (String strCCRecipient : itCCRecipient) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(strCCRecipient));
            }
            msg.setSubject(strSubject);
            msg.setSentDate(new Date());
            msg.setText(strText);
            Transport.send(msg);
        } catch (Exception e) {
            Log4jFactory.logger_system.error(e);
            return false;
        }
        return true;
    }

    /**
     * 发送带附件的邮件
     *
     * @param astrRecipient     收件人列表
     * @param strSubject        标题
     * @param strText           正文
     * @param strAttachmentPath 附件绝对路径
     */
    public void sendEmailWithAttachment(String[] astrRecipient, String strSubject, String strText,
                                        String strAttachmentPath) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.put("mail.smtp.host", strMailSmtpHost);
        // props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(strUserName, strPassword);
            }
        });
        // session.setDebug(true);
        try {
            // create a message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(strUserName));
            // InternetAddress[] address = { new
            // InternetAddress("22655080@qq.com") };
            // msg.setRecipients(Message.RecipientType.TO, address);
            for (String strRecipient : astrRecipient) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strRecipient));
            }
            msg.setSubject(strSubject);

            // Create a multipar message
            Multipart mp = new MimeMultipart();

            // Create the message part
            MimeBodyPart mbp = new MimeBodyPart();

            // Now set the actual message
            mbp.setText(strText);

            // Set text message part
            mp.addBodyPart(mbp);

            // Part two is attachment
            mbp = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(strAttachmentPath) {
                public String getContentType() {
                    return "application/octet-stream";
                }
            };
            mbp.setDataHandler(new DataHandler(fds));
            mbp.setFileName(fds.getName());
            mp.addBodyPart(mbp);

            // Send the complete message parts
            msg.setContent(mp);

            msg.setSentDate(new Date());
            // Send message
            Transport.send(msg);
        } catch (Exception e) {
            Log4jFactory.logger_system.error(e);
        }
    }
}
