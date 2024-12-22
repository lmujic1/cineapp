package ba.unsa.etf.cinebh.dto.request.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieFilterParams {
  private String contains;
  private Long genre = 0L;
  private Long venue = 0L;
  private Long city = 0L;
  private Date startDate;
}
