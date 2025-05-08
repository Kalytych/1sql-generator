package com.example.service;

import com.example.infrastructure.MailService;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import jakarta.mail.MessagingException;

public class VerificationService {
  private final MailService mailService;
  private final Map<String, String> verificationCodes = new HashMap<>();

  public VerificationService(MailService mailService) {
    this.mailService = mailService;
  }

  public void sendCode(String email) throws MessagingException {
    String code = String.valueOf(new Random().nextInt(900_000) + 100_000); // 6 цифр
    verificationCodes.put(email, code);
    mailService.sendVerificationCode(email, code);
  }

  public boolean verify(String email, String inputCode) {
    return verificationCodes.containsKey(email) && verificationCodes.get(email).equals(inputCode);
  }
}
