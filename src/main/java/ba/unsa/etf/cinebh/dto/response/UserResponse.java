package ba.unsa.etf.cinebh.dto.response;

import ba.unsa.etf.cinebh.model.address.City;
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
  private String photo;
  private String email;
  private City city;
}