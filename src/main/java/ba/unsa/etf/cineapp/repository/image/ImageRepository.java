package ba.unsa.etf.cineapp.repository.image;

import ba.unsa.etf.cineapp.model.image.Image;
import ba.unsa.etf.cineapp.model.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ImageRepository extends JpaRepository<Image, Long> {

  @Query("SELECT img FROM Image img WHERE img.movie=?1")
  Set<Image> getImagesByMovie(final Movie movie);

  @Query("SELECT img FROM Image img WHERE img.movie=?1 AND img.cover=TRUE")
  Image getMovieCover(final Movie movie);

  Set<Image> findByCoverTrue();
}
