package ba.unsa.etf.cineapp.repository.projection;

import ba.unsa.etf.cineapp.model.movie.Movie;
import ba.unsa.etf.cineapp.model.projection.Projection;
import ba.unsa.etf.cineapp.model.venue.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;

public interface ProjectionRepository extends JpaRepository<Projection, Long>, JpaSpecificationExecutor<Projection> {
  @Query("SELECT DISTINCT time FROM Projection ORDER BY time ASC ")
  List<Time> getTimes();

  @Query("SELECT proj FROM Projection proj WHERE proj.movie.movieId=?1 AND proj.venue.venueId=?2 ORDER BY proj.time")
  List<Projection> getProjectionsForMovieAndVenue(final Long movieId, final Long venueId);

  @Query("SELECT proj FROM Projection proj WHERE proj.movie=?1 ORDER BY proj.time")
  List<Projection> getProjectionsForMovie(final Movie movie);

  @Transactional
  void deleteByMovie(final Movie movie);
}
