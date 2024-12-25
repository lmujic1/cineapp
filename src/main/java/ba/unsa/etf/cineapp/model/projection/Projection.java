package ba.unsa.etf.cineapp.model.projection;

import ba.unsa.etf.cineapp.model.movie.Movie;
import ba.unsa.etf.cineapp.model.venue.Venue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Entity
@Table(name = "projections")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Projection {
  @Id
  @Column(name = "projectionId", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long projectionId;
  private Time time;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "movie_id", nullable = false)
  private Movie movie;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "venue_id", nullable = false)
  private Venue venue;

  public Projection(final Time time, final Movie movie, final Venue venue) {
    this.time = time;
    this.movie = movie;
    this.venue = venue;
  }
}
