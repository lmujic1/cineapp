package ba.unsa.etf.cineapp.service.other;

import ba.unsa.etf.cineapp.dto.request.other.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  @Autowired
  private JavaMailSender javaMailSender;

  public String sendEmail(final EmailRequest emailRequest) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(emailRequest.getTo());
      message.setSubject(emailRequest.getSubject());
      message.setText(emailRequest.getText());
      javaMailSender.send(message);
      return "Email sent.";
    } catch (Exception e) {
      e.printStackTrace();
      return "Error, sending email failed.";
    }
  }
}
