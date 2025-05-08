package com.example;

import com.example.infrastructure.MailService;

public class MailTest {
  public static void main(String[] args) {
    MailService mailService = new MailService();
    mailService.send("c.kalytych.vasyl@student.uzhnu.edu.ua", "Тест", "Привіт, це тестовий лист!");
  }
}
