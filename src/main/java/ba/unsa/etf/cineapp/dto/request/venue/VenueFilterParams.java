package ba.unsa.etf.cineapp.dto.request.venue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VenueFilterParams {
  private String contains;
  private Long city = 0L;
}

