package ba.unsa.etf.cinebh.controller.auth;

import ba.unsa.etf.cinebh.dto.request.auth.RefreshTokenRequest;
import ba.unsa.etf.cinebh.dto.request.auth.SignInRequest;
import ba.unsa.etf.cinebh.dto.request.auth.SignUpRequest;
import ba.unsa.etf.cinebh.dto.response.JWTAuthenticationResponse;
import ba.unsa.etf.cinebh.model.auth.User;
import ba.unsa.etf.cinebh.service.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

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

  @PostMapping("/refresh")
  public ResponseEntity<JWTAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
  }
}
