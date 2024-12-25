package ba.unsa.etf.cineapp.config;

import ba.unsa.etf.cineapp.model.auth.User;
import ba.unsa.etf.cineapp.repository.auth.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomLogInHandler implements AuthenticationSuccessHandler {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    User user = (User) authentication.getPrincipal();
    if (user.getAddedBySuperAdmin()) {
      response.sendRedirect("/user-profile/password-change"); // Redirect to password change page
    } else {
      response.sendRedirect("/"); // Redirect to main dashboard
    }
  }
}
