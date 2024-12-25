package ba.unsa.etf.cineapp.config;

import ba.unsa.etf.cineapp.model.auth.Token;
import ba.unsa.etf.cineapp.repository.auth.TokenRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

  private final TokenRepository tokenRepository;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    final String authHeader = request.getHeader("Authorization");
    if (StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer ")) {
      return;
    }
    String token = authHeader.substring(7);
    Token storedToken = tokenRepository.findByToken(token).orElse(null);
    if(storedToken!=null) {
      storedToken.setLoggedOut(true);
      tokenRepository.save(storedToken);
    }
  }
}
