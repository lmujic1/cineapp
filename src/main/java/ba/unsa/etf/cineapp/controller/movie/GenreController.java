package ba.unsa.etf.cineapp.controller.movie;

import ba.unsa.etf.cineapp.model.movie.Genre;
import ba.unsa.etf.cineapp.service.movie.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {

  private GenreService genreService;

  @GetMapping
  public ResponseEntity<Iterable<Genre>> getAll() {
    return ResponseEntity.ok(genreService.getAll());
  }
}
