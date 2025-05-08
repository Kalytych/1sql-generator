package com.example.infrastructure;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MailService {

  private final String username = "c.kalytych.vasyl@student.uzhnu.edu.ua";
  private final String password = "smlo vace xgmf hzeg";

  public void send(String to, String subject, String text) {
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(
          Message.RecipientType.TO,
          InternetAddress.parse(to)
      );
      message.setSubject(subject);
      message.setText(text);

      Transport.send(message);
      System.out.println("Лист успішно відправлено!");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public void sendVerificationCode(String toEmail, String code) throws MessagingException {
    send(toEmail, "Ваш код підтвердження", "Код для підтвердження реєстрації: " + code);
  }
}
