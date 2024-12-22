package ba.unsa.etf.cinebh.repository.movie;

import ba.unsa.etf.cinebh.model.movie.Movie;
import ba.unsa.etf.cinebh.model.movie.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

  @Query("SELECT mov FROM Movie mov WHERE mov.projectionStart <= CURRENT_DATE() AND mov.projectionEnd >= CURRENT_DATE() AND mov.status = 'PUBLISHED'")
  Page<Movie> findCurrentlyShowing(final Pageable pageable);

  @Query("SELECT mov FROM Movie mov WHERE mov.projectionStart >= DATEADD(DAY, 10, CURRENT_DATE()) AND mov.status = 'PUBLISHED'")
  Page<Movie> findUpcoming(final Pageable pageable);

  @Query("SELECT mov FROM Movie mov WHERE mov.projectionStart > CURRENT_DATE() AND mov.status = 'PUBLISHED'")
  Page<Movie> findAllUpcoming(final Pageable pageable);

  @Query("SELECT COUNT(mov) FROM Movie mov WHERE mov.projectionStart <= CURRENT_DATE() AND mov.projectionEnd >= CURRENT_DATE() AND mov.status = 'PUBLISHED'")
  long countCurrentlyShowing();

  @Query("SELECT COUNT(mov) FROM Movie mov WHERE mov.projectionStart >= CURRENT_DATE() AND mov.status = 'PUBLISHED'")
  long countUpcoming();

  @Query("SELECT COUNT(mov) FROM Movie mov WHERE mov.status = 'DRAFT'")
  long countDrafts();

  @Query("SELECT COUNT(mov) FROM Movie mov WHERE mov.status = 'ARCHIVED'")
  long countArchived();

  @Query("SELECT mov FROM Movie mov WHERE mov.projectionEnd <= CURRENT_DATE AND mov.status = 'PUBLISHED'")
  List<Movie> findMoviesToArchive();

  Page<Movie> findByStatus(final Status status, final Pageable pageable);
}
