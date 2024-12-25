package ba.unsa.etf.cineapp.dto.response;

import ba.unsa.etf.cineapp.model.auth.Role;
import lombok.Data;

@Data
public class JWTAuthenticationResponse {
  private String token;
  private String refreshToken;
  private String firstName;
  private String lastName;
  private Role role;
  private Boolean addedBySuperAdmin;
  private Boolean passwordChanged;
}
