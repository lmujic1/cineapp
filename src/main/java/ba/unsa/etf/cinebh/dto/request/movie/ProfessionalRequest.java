package ba.unsa.etf.cinebh.dto.request.movie;

import ba.unsa.etf.cinebh.model.movie.Profession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalRequest {
  private String firstName;
  private String lastName;
}
