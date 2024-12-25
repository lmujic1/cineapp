package ba.unsa.etf.cineapp.dto.request.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusRequest {
  private Set<Long> movieIds;
  private String status;
}
