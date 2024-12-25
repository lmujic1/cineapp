package ba.unsa.etf.cineapp.service.auth;

import ba.unsa.etf.cineapp.dto.request.auth.RefreshTokenRequest;
import ba.unsa.etf.cineapp.dto.request.auth.SignInRequest;
import ba.unsa.etf.cineapp.dto.request.auth.SignUpRequest;
import ba.unsa.etf.cineapp.dto.response.JWTAuthenticationResponse;
import ba.unsa.etf.cineapp.model.auth.Role;
import ba.unsa.etf.cineapp.model.auth.Token;
import ba.unsa.etf.cineapp.model.auth.User;
import ba.unsa.etf.cineapp.repository.auth.TokenRepository;
import ba.unsa.etf.cineapp.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JWTService jwtService;
  private final TokenRepository tokenRepository;

  public User signup(final SignUpRequest signUpRequest) {
    User user = new User();
    user.setEmail(signUpRequest.getEmail());
    user.setFirstName(signUpRequest.getFirstName());
    user.setLastName(signUpRequest.getLastName());
    user.setRole(Role.USER);
    user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
    return userRepository.save(user);
  }

  public JWTAuthenticationResponse signIn(final SignInRequest signInRequest) throws IllegalAccessException {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
    } catch (AuthenticationException e) {
      throw new IllegalArgumentException("Invalid email or password.");
    }

    var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
    if(user.getSoftDelete()) throw new IllegalAccessException("Account deactivated.");
    var jwt = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

    revokeAllTokensForUser(user);
    saveUserToken(jwt, refreshToken, user);

    JWTAuthenticationResponse authenticationResponse = new JWTAuthenticationResponse();
    authenticationResponse.setToken(jwt);
    authenticationResponse.setRefreshToken(refreshToken);
    authenticationResponse.setFirstName(user.getFirstName());
    authenticationResponse.setLastName(user.getLastName());
    authenticationResponse.setRole(user.getRole());
    authenticationResponse.setAddedBySuperAdmin(user.getAddedBySuperAdmin());
    authenticationResponse.setPasswordChanged(user.getPasswordChanged());
    return authenticationResponse;
  }

  private void revokeAllTokensForUser(final User user) {
    List<Token> validTokensOfUser = tokenRepository.findAllTokensByUser(user);
    if(!validTokensOfUser.isEmpty()) {
      validTokensOfUser.forEach(t->t.setLoggedOut(true));
    }
    tokenRepository.saveAll(validTokensOfUser);
  }

  private void saveUserToken(final String jwt, final String refreshToken, final User user) {
    Token token = new Token();
    token.setToken(jwt);
    token.setRefreshToken(refreshToken);
    token.setLoggedOut(false);
    token.setUser(user);
    tokenRepository.save(token);
  }

  public JWTAuthenticationResponse refreshToken(final RefreshTokenRequest request) {
    String userEmail = jwtService.getUsernameFromToken(request.getRefreshToken());
    User user = userRepository.findByEmail(userEmail).orElseThrow();
    if (jwtService.isTokenValid(request.getRefreshToken(), user)) {
      Token token = tokenRepository.findByToken(request.getToken()).orElseThrow(() -> new IllegalArgumentException("User with provided token not found"));
      token.setLoggedOut(true);
      var jwt = jwtService.generateToken(user);
      saveUserToken(jwt, request.getRefreshToken(), user);
      JWTAuthenticationResponse authenticationResponse = new JWTAuthenticationResponse();
      authenticationResponse.setToken(jwt);
      authenticationResponse.setRefreshToken(request.getRefreshToken());
      authenticationResponse.setFirstName(user.getFirstName());
      authenticationResponse.setLastName(user.getLastName());
      authenticationResponse.setRole(user.getRole());
      return authenticationResponse;
    }
    return null;
  }
}
