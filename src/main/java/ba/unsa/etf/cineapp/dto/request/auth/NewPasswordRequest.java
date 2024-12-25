package ba.unsa.etf.cineapp.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordRequest {
  private String email;
  private String newPassword;
  private String token;
}
