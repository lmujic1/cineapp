package ba.unsa.etf.cinebh.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
  private String firstName;
  private String lastName;
  private String phone;
  private String photo;
  private String email;
  private Long city;
}
