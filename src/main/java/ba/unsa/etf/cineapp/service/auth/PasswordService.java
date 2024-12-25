package ba.unsa.etf.cineapp.service.auth;

import org.apache.commons.lang3.RandomStringUtils;

public class PasswordService {
  public static String generateRandomPassword(int length) {
    boolean useLetters = true;
    boolean useNumbers = true;
    boolean useSpecial = true;
    String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&*()_+-=[]?";

    return RandomStringUtils.random(length, 0, allowedChars.length(), useLetters, useNumbers, allowedChars.toCharArray());
  }
}
