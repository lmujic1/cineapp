package ba.unsa.etf.cineapp.dto.request.auth;

import lombok.Data;

@Data
public class SignUpRequest {
  private String firstName;
  private String lastName;
  private String email;
  private String password;
}

