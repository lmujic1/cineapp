package ba.unsa.etf.cinebh.dto.response;

import ba.unsa.etf.cinebh.model.auth.Role;
import lombok.Data;

@Data
public class JWTAuthenticationResponse {
  private String token;
  private String refreshToken;
  private String firstName;
  private String lastName;
  private Role role;
}
