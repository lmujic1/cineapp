package ba.unsa.etf.cinebh.dto.request.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectionRequest {
  private Long id;
  private Time time;
  private Long city;
  private Long venue;
}

