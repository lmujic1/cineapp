package ba.unsa.etf.cineapp.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
  private String oldPassword;
  private String newPassword;
  private Boolean passwordChanged;
}

