package ba.unsa.etf.cineapp.dto.request.auth;

import lombok.Data;

@Data
public class SignInRequest {
  private String email;
  private String password;
}

