package ba.unsa.etf.cineapp.repository.movie;

import ba.unsa.etf.cineapp.model.movie.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
