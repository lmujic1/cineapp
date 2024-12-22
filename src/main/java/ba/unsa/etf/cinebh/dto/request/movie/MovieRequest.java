package ba.unsa.etf.cinebh.dto.request.movie;

import ba.unsa.etf.cinebh.model.movie.Status;
import ba.unsa.etf.cinebh.model.movie.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequest {
  private String name;
  private String language;
  private Date projectionStart;
  private Date projectionEnd;
  private String director;
  private String synopsis;
  private String rating;
  private Integer duration;
  private String trailer;
  private Status status;
  private Step step;
  private Set<Long> genres;
}

