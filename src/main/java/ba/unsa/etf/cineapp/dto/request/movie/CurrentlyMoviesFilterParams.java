package ba.unsa.etf.cineapp.dto.request.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrentlyMoviesFilterParams extends MovieFilterParams {
  private String time;
}
