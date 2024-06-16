package edu.ap.softwareproject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailObjectMother {
  public static String sender() {
    return System.getenv("MAIL_ADDRESS");
  }

  public static String password() {
    return System.getenv("MAIL_PWD");
  }

  public static Properties getProps() {
    Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.office365.com");
    props.put("mail.smtp.port", 587);
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.ssl.protocols", "TLSv1.2");
    props.put("mail.smtp.ssl.ciphers", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
    props.put("mail.smtp.auth", "true");
    props.put("mail.debug", "false");
    return props;
  }

  public static Message createMimeMessage(Session session, String sender, String senderName, String receiver,
      String subject, String htmlBody) throws MessagingException, UnsupportedEncodingException {

    Message msg = new MimeMessage(session);
    msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
    msg.addHeader("format", "flowed");
    msg.addHeader("Content-Transfer-Encoding", "8bit");
    msg.setReplyTo(InternetAddress.parse(receiver, false));
    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver, false));
    msg.setFrom(new InternetAddress(sender, senderName));
    msg.setSubject(subject);
    // msg.setText(body);
    msg.setContent(htmlBody, "text/html");

    return msg;
  }

  public static Session session(String sender, String password) {
    return Session.getInstance(
        MailObjectMother.getProps(),
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(sender, password);
          }
        });
  }

  public static Map<String, String> mailDetails() {
    Map<String, String> map = new HashMap<>();
    map.put("senderName", "Test");
    map.put("receiver", "s141662@ap.be");
    map.put("subject", "Test");
    map.put("htmlBody", "Test");
    return map;
  }

  public static boolean mailRegexCheck(String receiver) {
    String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    return Pattern.compile(regexPattern)
        .matcher(receiver)
        .matches();
  }
}