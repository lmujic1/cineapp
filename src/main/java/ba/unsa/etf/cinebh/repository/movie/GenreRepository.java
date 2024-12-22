package ba.unsa.etf.cinebh.repository.movie;

import ba.unsa.etf.cinebh.model.movie.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
