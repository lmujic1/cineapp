package ba.unsa.etf.cinebh.dto.request.auth;

import lombok.Data;

@Data
public class RefreshTokenRequest {
  private String refreshToken;
  private String token;
}
