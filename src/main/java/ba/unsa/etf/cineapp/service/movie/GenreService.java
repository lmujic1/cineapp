package ba.unsa.etf.cineapp.service.movie;

import ba.unsa.etf.cineapp.exception.ResourceNotFoundException;
import ba.unsa.etf.cineapp.model.movie.Genre;
import ba.unsa.etf.cineapp.repository.movie.GenreRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreService {
  @Autowired
  private GenreRepository genreRepository;

  public Iterable<Genre> getAll() {
    return genreRepository.findAll();
  }

  public Genre findById(Long id) {
    Optional<Genre> genre = genreRepository.findById(id);
    if (genre.isPresent()) {
      return genre.get();
    } else {
      throw new ResourceNotFoundException("Genre with provided id not found!");
    }
  }

  public Genre save(Genre genre) {
    return genreRepository.save(genre);
  }

  public void remove(Long id) throws JsonProcessingException {
    if (!genreRepository.existsById(id)) {
      throw new ResourceNotFoundException("Genre with id= " + id+ " does not exist");
    }
    genreRepository.deleteById(id);
  }
}
