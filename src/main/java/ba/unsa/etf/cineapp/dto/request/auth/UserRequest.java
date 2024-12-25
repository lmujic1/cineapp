package ba.unsa.etf.cineapp.dto.request.auth;

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
  private String image;
  private String email;
  private Long city;
}
