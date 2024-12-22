package ba.unsa.etf.cinebh.dto.request.auth;

import lombok.Data;

@Data
public class SignInRequest {
  private String email;
  private String password;
}

