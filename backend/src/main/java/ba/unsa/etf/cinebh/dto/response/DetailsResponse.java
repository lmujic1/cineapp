package ba.unsa.etf.cinebh.dto.response;

import ba.unsa.etf.cinebh.model.movie.Professional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailsResponse {
  private Set<Professional> actors;
  private Set<Professional> writers;
}
