package ba.unsa.etf.cineapp.dto.response;

import ba.unsa.etf.cineapp.model.address.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
  private Long userId;
  private String firstName;
  private String lastName;
  private String phone;
  private String image;
  private String email;
  private City city;
}
