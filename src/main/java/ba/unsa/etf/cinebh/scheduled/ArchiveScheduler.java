package ba.unsa.etf.cinebh.scheduled;

import ba.unsa.etf.cinebh.model.movie.Movie;
import ba.unsa.etf.cinebh.model.movie.Status;
import ba.unsa.etf.cinebh.repository.movie.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ArchiveScheduler {

  private final MovieRepository movieRepository;

  @Autowired
  public ArchiveScheduler(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  @Scheduled(cron = "0 0 0 * * *")
  public void archiveMovies() {
    log.info("ArchiveScheduler started at {}", java.time.LocalTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES));

    List<Movie> moviesToArchive = movieRepository.findMoviesToArchive();

    for (Movie movie : moviesToArchive) {
      movie.setStatus(Status.ARCHIVED);
      movieRepository.save(movie);
      log.info("Archived movie with id={}", movie.getMovieId());
    }

    log.info("ArchiveScheduler completed at {}", java.time.LocalTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES));
  }
}
