package ba.unsa.etf.cinebh.service.auth;

import ba.unsa.etf.cinebh.dto.request.other.EmailRequest;
import ba.unsa.etf.cinebh.exception.ResourceNotFoundException;
import ba.unsa.etf.cinebh.model.auth.PasswordReset;
import ba.unsa.etf.cinebh.model.auth.User;
import ba.unsa.etf.cinebh.repository.auth.PasswordResetRepository;
import ba.unsa.etf.cinebh.repository.auth.UserRepository;
import ba.unsa.etf.cinebh.service.other.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class PasswordResetService {

  @Autowired
  private PasswordResetRepository passwordResetRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailService emailService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public String forgotPassword(String email){
    User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    return createPasswordResetRequest(user);
  }

  public String checkCode(String email, String code){
    User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    return checkCode(user, code);
  }

  public String setNewPassword(String email, String password, String token){
    User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    PasswordReset passwordReset;
    if(token!=null)passwordReset = passwordResetRepository.findByEmailAndToken(email, token).orElseThrow(() -> new ResourceNotFoundException("Invalid token!"));
    else throw new ResourceNotFoundException("Please provide token!");
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
    passwordReset.setToken(null);
    passwordResetRepository.save(passwordReset);
    return "Users password changed successfully!";
  }

  private String createPasswordResetRequest(User user) {
    PasswordReset passwordReset = new PasswordReset();
    passwordReset.setEmail(user.getEmail());
    passwordReset.setResetCode(generateResetCode());
    passwordReset.setExpiryTime(LocalDateTime.now().plusMinutes(5));
    passwordReset.setUser(user);
    passwordReset.setValid(true);
    revokeAllPasswordResetsForUser(user);
    passwordResetRepository.save(passwordReset);
    return emailService.sendEmail(
        new EmailRequest(
            user.getEmail(),
            "Cinebh reset password code",
            "Hi "+user.getFirstName()+" "+user.getLastName()+", \n\nYour reset code is: "+passwordReset.getResetCode()+". \nYour code will expire after 5 minutes.\n\n If you don't want to change your password, just ignore this message.\n -The Cinebh Team"));
  }

  private String generateResetCode() {
    int min = 1000;
    int max = 9999;
    int randomCode = ThreadLocalRandom.current().nextInt(min, max + 1);
    return String.valueOf(randomCode);
  }

  private void revokeAllPasswordResetsForUser(User user) {
    List<PasswordReset> validPasswordResets = passwordResetRepository.findAllPasswordResetsForUser(user);
    if(!validPasswordResets.isEmpty()) {
      validPasswordResets.forEach(pr->pr.setValid(false));
    }
    passwordResetRepository.saveAll(validPasswordResets);
  }

  private String checkCode(User user, String code) {
    List<PasswordReset> passwordResets = passwordResetRepository.findAllPasswordResetsForUser(user);
    if(passwordResets==null) throw new ResourceNotFoundException("There is no code generated for specified user");
    PasswordReset passwordReset = getPasswordResetByCode(passwordResets, code).orElseThrow(() -> new ResourceNotFoundException("Invalid code!"));
    if(isCodeExpired(passwordReset.getExpiryTime())) throw new IllegalArgumentException("Code expired!");
    else {
      String token = generateToken();
      passwordReset.setToken(token);
      passwordResetRepository.save(passwordReset);
      return token;
    }
  }

  private boolean isCodeExpired(LocalDateTime codeExpiration) {
    return codeExpiration.isBefore(LocalDateTime.now());
  }

  private static String generateToken() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString().replace("-", "");
  }

  private Optional<PasswordReset> getPasswordResetByCode(List<PasswordReset> passwordResets, String code) {
    return passwordResets.stream()
        .filter(pr -> pr.getResetCode().equals(code))
        .findFirst();
  }
}
