package ba.unsa.etf.cineapp.dto.request.venue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VenueRequest {
  private String name;
  private String street;
  private Integer number;
  private String telephone;
  private Long city;
}
