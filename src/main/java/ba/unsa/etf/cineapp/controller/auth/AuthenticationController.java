package ba.unsa.etf.cineapp.controller.auth;

import ba.unsa.etf.cineapp.config.CustomLogoutHandler;
import ba.unsa.etf.cineapp.dto.request.auth.RefreshTokenRequest;
import ba.unsa.etf.cineapp.dto.request.auth.SignInRequest;
import ba.unsa.etf.cineapp.dto.request.auth.SignUpRequest;
import ba.unsa.etf.cineapp.dto.response.JWTAuthenticationResponse;
import ba.unsa.etf.cineapp.model.auth.User;
import ba.unsa.etf.cineapp.service.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  private final CustomLogoutHandler logoutHandler; // Dodano

  @PostMapping("/sign-up")
  public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest) {
    return ResponseEntity.ok(authenticationService.signup(signUpRequest));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<JWTAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest) {
    JWTAuthenticationResponse response = null;
    try {
      response = authenticationService.signIn(signInRequest);
    }
    catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    try {
      logoutHandler.logout(request, response, authentication);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }


  @PostMapping("/refresh")
  public ResponseEntity<JWTAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
  }
}
