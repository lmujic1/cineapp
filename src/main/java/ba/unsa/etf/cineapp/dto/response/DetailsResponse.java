package ba.unsa.etf.cineapp.dto.response;

import ba.unsa.etf.cineapp.model.movie.Professional;
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
